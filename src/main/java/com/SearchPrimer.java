package com;

import com.File.BedFile;
import com.File.CpgFile;
import com.File.MHapFile;
import com.File.SearchPrimerOutputFIle;
import com.args.SearchPrimerArgs;
import com.bean.MHapInfo;
import com.bean.Region;
import com.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SearchPrimer {
    public static final Logger log = LoggerFactory.getLogger(SearchPrimer.class);

    SearchPrimerArgs args = new SearchPrimerArgs();
    Util util = new Util();

    public void searchPrimer(SearchPrimerArgs searchPrimerArgs) throws Exception {
        log.info("command.searchPrimer start!");
        args = searchPrimerArgs;

        CpgFile cpgFile = new CpgFile(args.getCpgPath());
        MHapFile mHapFileN = new MHapFile(args.getMhapPathN());
        MHapFile mHapFileT = new MHapFile(args.getMhapPathT());

        // check the command
        boolean checkResult = checkArgs();
        if (!checkResult) {
            log.error("Checkargs fail, please check the command.");
            return;
        }

        // get regionList, from region or bedfile
        List<Region> regionList = new ArrayList<>();
        if (args.getRegion() != null && !args.getRegion().equals("")) {
            Region region = new Region(args.getRegion());
            regionList.add(region);
        } else if (args.getBedPath() != null && !args.getBedPath().equals("")) {
            BedFile bedFile = new BedFile(args.getBedPath());
            regionList = bedFile.parseWholeFile();
        }

        for (Region region : regionList) {
            log.info("searchPrimer read" + region.toHeadString() + " start! ");
            // parse the cpg file
            List<Integer> cpgPosList = cpgFile.parseByRegionWithShift(region, 2000);
            if (cpgPosList.size() < 1) {
                continue;
            }

            // parse the mhap file
            List<MHapInfo> tumorMHapList = mHapFileT.parseByRegion(region);
            if (tumorMHapList.size() < 1) {
                continue;
            }
            List<MHapInfo> normalMHapList = mHapFileN.parseByRegion(region);
            if (normalMHapList.size() < 1) {
                continue;
            }

            String outputFIleName = args.getTag() + "_" + region.toFileString() + ".searchPrimer.txt";
            SearchPrimerOutputFIle outputFIle = new SearchPrimerOutputFIle(args.getOutputDir(), outputFIleName);
            outputFIle.writeHead();

            Region fWindow = new Region(region.getChrom(), 0, 0); // forward window region
            Integer fWindowStart = region.getStart(); // forward window start position
            Integer totalPosCnt = region.getEnd() - region.getStart();
            Integer readPosCnt = 0;
            for (; fWindowStart < region.getEnd() - args.getMinInsertSize() - args.getrLength() + 1 && fWindow.getEnd() <= region.getEnd(); fWindowStart++) {
                fWindow.setStart(fWindowStart); // forward window start position
                fWindow.setEnd(fWindowStart + args.getfLength() - 1); // forward window end position
                readPosCnt++;
                if (readPosCnt % (totalPosCnt / 10) == 0) {
                    int percent = (int) Math.round(Double.valueOf(readPosCnt) * 100 / totalPosCnt);
                    log.info("Read complete " + percent + "%.");
                }

                // get the cpg position list in forward window
                List<Integer> cpgPosListInFWindow = cpgFile.parseByRegion(fWindow);
                if (cpgPosListInFWindow.size() < 1) {
                    continue;
                }

                Region rWindow = new Region(region.getChrom(), 0, 0); // reverse window region
                Integer rWindowStart = fWindow.getEnd() + args.getMinInsertSize() + 1; // reverse window start position
                for (; rWindowStart < fWindow.getEnd() + args.getMaxInsertSize() + 1 && rWindow.getEnd() <= region.getEnd(); rWindowStart++) {
                    rWindow.setStart(rWindowStart); // reverse window start position
                    rWindow.setEnd(rWindowStart + args.getrLength() - 1); // reverse window end position
                    //log.info("Reverse window: " + rWindow.toHeadString() + " read start!");

                    // get the region include forward and reverse window
                    Region f2rWindow = new Region(region.getChrom(), fWindow.getStart(), rWindow.getEnd());

                    // get the cpg position list in both forward and reverse window
                    List<Integer> cpgPosListInWindow = cpgFile.parseByRegion(f2rWindow);
                    if (cpgPosListInWindow.size() < 1) {
                        continue;
                    }
                    List<Integer> cpgPosListInRWindow = cpgFile.parseByRegion(rWindow);
                    if (cpgPosListInRWindow.size() < 1) {
                        continue;
                    }
                    Integer fWindowCpgStartIndex = util.indexOfList(cpgPosListInWindow, cpgPosListInFWindow.get(0));
                    Integer fWindowCpgEndIndex = util.indexOfList(cpgPosListInWindow, cpgPosListInFWindow.get(cpgPosListInFWindow.size() - 1));
                    Integer rWindowCpgStartIndex = util.indexOfList(cpgPosListInWindow, cpgPosListInRWindow.get(0));
                    Integer rWindowCpgEndIndex = util.indexOfList(cpgPosListInWindow, cpgPosListInRWindow.get(cpgPosListInRWindow.size() - 1));                    Integer cpgStart = cpgPosListInWindow.get(fWindowCpgStartIndex);
                    Integer cpgEnd = cpgPosListInWindow.get(rWindowCpgEndIndex);

                    // get the tumor pattern in window
                    Map<String, Integer> tumorPatternMap = util.getPatternInWindow(tumorMHapList, cpgPosList, cpgStart, cpgEnd);
                    Map<String, Integer> newTumorPatternMap = new HashMap<>();
                    Iterator<String> tumorPatternMapIterator = tumorPatternMap.keySet().iterator();
                    while (tumorPatternMapIterator.hasNext()) {
                        String key = tumorPatternMapIterator.next();
                        String newKey = key.substring(fWindowCpgStartIndex, fWindowCpgEndIndex + 1) + key.substring(rWindowCpgStartIndex);
                        if (newTumorPatternMap.containsKey(newKey)) {
                            newTumorPatternMap.put(newKey, newTumorPatternMap.get(newKey) + tumorPatternMap.get(key));
                        } else {
                            newTumorPatternMap.put(newKey, tumorPatternMap.get(key));
                        }
                    }

                    // get the normal pattern in window
                    Map<String, Integer> normalPatternMap = util.getPatternInWindow(normalMHapList, cpgPosList, cpgStart, cpgEnd);
                    Map<String, Integer> newNormalPatternMap = new HashMap<>();
                    Iterator<String> normalPatternMapIterator = normalPatternMap.keySet().iterator();
                    while (normalPatternMapIterator.hasNext()) {
                        String key = normalPatternMapIterator.next();
                        String newKey = key.substring(fWindowCpgStartIndex, fWindowCpgEndIndex + 1) + key.substring(rWindowCpgStartIndex);
                        if (newNormalPatternMap.containsKey(newKey)) {
                            newNormalPatternMap.put(newKey, newNormalPatternMap.get(newKey) + normalPatternMap.get(key));
                        } else {
                            newNormalPatternMap.put(newKey, normalPatternMap.get(key));
                        }
                    }

                    // get the tumor and normal total pattern count
                    Integer tumarTotalPatternCount = newTumorPatternMap.entrySet().stream().mapToInt(t->t.getValue()).sum();
                    Integer normalTotalPatternCount = newNormalPatternMap.entrySet().stream().mapToInt(t->t.getValue()).sum();

                    tumorPatternMapIterator = newTumorPatternMap.keySet().iterator();
                    while (tumorPatternMapIterator.hasNext()) {
                        String key = tumorPatternMapIterator.next();
                        Integer tumarPatternCount = newTumorPatternMap.get(key);
                        Double tumorRate = newTumorPatternMap.get(key).doubleValue() / tumarTotalPatternCount.doubleValue();
                        Integer normalPatternCount = 0;
                        Double normalRate = 0.0;
                        if (newNormalPatternMap.containsKey(key)) {
                            normalPatternCount = newNormalPatternMap.get(key);
                            normalRate = newNormalPatternMap.get(key).doubleValue() / normalTotalPatternCount.doubleValue();
                        }
                        Double foldChange = tumorRate / normalRate;
                        if (tumorRate >= args.getMinT() && normalRate <= args.getMaxN() && foldChange >= args.getMinFC()) {
                            outputFIle.Fpos = fWindow.toHeadString();
                            outputFIle.Rpos = rWindow.toHeadString();
                            outputFIle.Fpattern = key.substring(fWindowCpgStartIndex, fWindowCpgEndIndex + 1);
                            outputFIle.Rpattern = key.substring(fWindowCpgEndIndex + 1);
                            outputFIle.T_RC = tumarTotalPatternCount;
                            outputFIle.T_PRC = tumarPatternCount;
                            outputFIle.T = tumorRate.floatValue();
                            outputFIle.N_RC = normalTotalPatternCount;
                            outputFIle.N_PRC = normalPatternCount;
                            outputFIle.N = normalRate.floatValue();
                            outputFIle.FC = foldChange.floatValue();
                            outputFIle.writeLine();
                        }
                    }

                    normalPatternMapIterator = newNormalPatternMap.keySet().iterator();
                    while (normalPatternMapIterator.hasNext()) {
                        String key = normalPatternMapIterator.next();
                        Integer tumarPatternCount = 0;
                        Double tumorRate = 0.0;
                        if (!newTumorPatternMap.containsKey(key)) {
                            Integer normalPatternCount = newNormalPatternMap.get(key);
                            Double normalRate = newNormalPatternMap.get(key).doubleValue() / normalTotalPatternCount.doubleValue();
                            Double foldChange = tumorRate / normalRate;
                            if (tumorRate >= args.getMinT() && normalRate <= args.getMaxN() && foldChange >= args.getMinFC()) {
                                outputFIle.Fpos = fWindow.toHeadString();
                                outputFIle.Rpos = rWindow.toHeadString();
                                outputFIle.Fpattern = key.substring(fWindowCpgStartIndex, fWindowCpgEndIndex + 1);
                                outputFIle.Rpattern = key.substring(fWindowCpgEndIndex + 1);
                                outputFIle.T_RC = tumarTotalPatternCount;
                                outputFIle.T_PRC = tumarPatternCount;
                                outputFIle.T = tumorRate.floatValue();
                                outputFIle.N_RC = normalTotalPatternCount;
                                outputFIle.N_PRC = normalPatternCount;
                                outputFIle.N = normalRate.floatValue();
                                outputFIle.FC = foldChange.floatValue();
                                outputFIle.writeLine();
                            }
                        }

                    }
                    //log.info("Reverse window: " + rWindow.toHeadString() + " read end!");
                }
                //log.info("Forward window: " + fWindow.toHeadString() + " read end!");
            }

            log.info("searchPrimer read" + region.toHeadString() + " end! ");
            outputFIle.close();
        }
        cpgFile.close();
        mHapFileN.close();
        mHapFileT.close();

        log.info("command.searchPrimer end!");
    }

    private boolean checkArgs() {
        if (args.getMhapPathN().equals("") || args.getMhapPathT().equals("")) {
            log.error("mhapPath can not be null.");
            return false;
        }
        if (args.getCpgPath().equals("")) {
            log.error("cpgPath can not be null.");
            return false;
        }
        if (!args.getRegion().equals("") && !args.getBedPath().equals("")) {
            log.error("Can not input region and bedPath at the same time.");
            return false;
        }

        return true;
    }

}
