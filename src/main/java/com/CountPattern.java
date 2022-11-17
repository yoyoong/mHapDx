package com;

import com.File.*;
import com.args.CountPatternArgs;
import com.bean.CountPatternInfo;
import com.bean.ListPatternInfo;
import com.bean.MHapInfo;
import com.bean.Region;
import com.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CountPattern {
    public static final Logger log = LoggerFactory.getLogger(CountPattern.class);

    CountPatternArgs args = new CountPatternArgs();
    Util util = new Util();

    public void countPattern(CountPatternArgs countPatternArgs) throws Exception {
        log.info("command.countPattern start!");
        args = countPatternArgs;

        CpgFile cpgFile = new CpgFile(args.getCpgPath());

        // check the command
        boolean checkResult = checkArgs();
        if (!checkResult) {
            log.error("Checkargs fail, please check the command.");
            return;
        }

        List<Region> fPrimerList = new ArrayList<>();
        List<Region> rPrimerList = new ArrayList<>();
        List<String> fPatternList = new ArrayList<>();
        List<String> rPatternList = new ArrayList<>();
        // get primer list from region or bed file
        if (!args.getBedPath().equals("")) {
            BedFile bedFile = new BedFile(args.getBedPath());
            List<String> lineList = bedFile.getLineList();
            for (String line : lineList) {
                Region fPrimer = new Region(line.split("\t")[0]);
                Region rPrimer = new Region(line.split("\t")[1]);
                String fPattern = line.split("\t")[2];
                String rPattern = line.split("\t")[3];
                fPrimerList.add(fPrimer);
                rPrimerList.add(rPrimer);
                fPatternList.add(fPattern);
                rPatternList.add(rPattern);
            }
        } else {
            Region fPrimer = new Region(args.getFPrimer());
            Region rPrimer = new Region(args.getRPrimer());
            String fPattern = args.getFPattern();
            String rPattern = args.getRPattern();
            fPrimerList.add(fPrimer);
            rPrimerList.add(rPrimer);
            fPatternList.add(fPattern);
            rPatternList.add(rPattern);
        }

        String[] mHapPathList = args.getMhapPath().split(" ");
        // create the output file and write
        String outputFileName = args.getTag() + ".countPattern.txt";
        CountPatternOutputFile outputFile = new CountPatternOutputFile(args.getOutputDir(), outputFileName);
        outputFile.mHapPathList = mHapPathList;
        outputFile.writeHead();


        for (int n = 0; n < fPrimerList.size(); n++) {
            Region fPrimer = fPrimerList.get(n);
            Region rPrimer = rPrimerList.get(n);
            String fPattern = fPatternList.get(n);
            String rPattern = rPatternList.get(n);

            // get the cpg position list in FPrimer and RPrimer
            List<Integer> cpgPosListInFPrimer = cpgFile.parseByRegion(fPrimer);
            if (cpgPosListInFPrimer.size() < 1) {
                return;
            }
            List<Integer> cpgPosListInRPrimer = cpgFile.parseByRegion(rPrimer);
            if (cpgPosListInRPrimer.size() < 1) {
                return;
            }

            // get the cpg position list range from FPrimer to RPrimer
            Region region = new Region(fPrimer.getChrom(), cpgPosListInFPrimer.get(0), cpgPosListInRPrimer.get(cpgPosListInRPrimer.size() - 1));
            List<Integer> cpgPosListInWindow = cpgFile.parseByRegion(region);
            if (cpgPosListInWindow.size() < 1) {
                return;
            }

            // get the FPrimer/RPrimer start/end cpg position index in region
            Integer fPrimerCpgStartIndex = util.indexOfList(cpgPosListInWindow, cpgPosListInFPrimer.get(0));
            Integer fPrimerCpgEndIndex = util.indexOfList(cpgPosListInWindow, cpgPosListInFPrimer.get(cpgPosListInFPrimer.size() - 1));
            Integer rPrimerCpgStartIndex = util.indexOfList(cpgPosListInWindow, cpgPosListInRPrimer.get(0));
            Integer rPrimerCpgEndIndex = util.indexOfList(cpgPosListInWindow, cpgPosListInRPrimer.get(cpgPosListInRPrimer.size() - 1));


            int[] patternCntList = new int[mHapPathList.length];
            int[] totalCntList = new int[mHapPathList.length];
            for (int i = 0; i < mHapPathList.length; i++) {
                MHapFile mHapFile = new MHapFile(mHapPathList[i]);
                List<MHapInfo> mHapListInWindow = mHapFile.parseByRegion(region);
                if (mHapListInWindow.size() < 1) {
                    continue;
                }

                // get the pattern in window, and cut the pattern neither in FPrimer nor in RPrimer
                Map<String, Integer> patternMap = util.getPatternInWindow(mHapListInWindow, cpgPosListInWindow, region.getStart(), region.getEnd());
                Map<String, Integer> patternMapCutted = new HashMap<>();
                Iterator<String> patternMapIterator = patternMap.keySet().iterator();
                while (patternMapIterator.hasNext()) {
                    String key = patternMapIterator.next();
                    String newKey = key.substring(fPrimerCpgStartIndex, fPrimerCpgEndIndex + 1) + key.substring(rPrimerCpgStartIndex);
                    if (patternMapCutted.containsKey(newKey)) {
                        patternMapCutted.put(newKey, patternMapCutted.get(newKey) + patternMap.get(key));
                    } else {
                        patternMapCutted.put(newKey, patternMap.get(key));
                    }
                }
                totalCntList[i] = patternMapCutted.entrySet().stream().mapToInt(t->t.getValue()).sum();

                // get the pattern list
                Iterator<String> patternMapCuttedIterator = patternMapCutted.keySet().iterator();
                while (patternMapCuttedIterator.hasNext()) {
                    String pattern = patternMapCuttedIterator.next();
                    if (pattern.equals(fPattern + rPattern)) {
                        patternCntList[i] = patternMapCutted.get(pattern);
                    }
                }

                mHapFile.close();
            }

            CountPatternInfo countPatternInfo = new CountPatternInfo();
            countPatternInfo.setF_Primer(fPrimer.toHeadString());
            countPatternInfo.setR_Primer(rPrimer.toHeadString());
            countPatternInfo.setFpattern(fPattern);
            countPatternInfo.setRpattern(rPattern);
            countPatternInfo.patternCntList = patternCntList;
            countPatternInfo.totalCntList = totalCntList;
            outputFile.countPatternInfo = countPatternInfo;
            outputFile.writeLine();
        }



        outputFile.close();
        cpgFile.close();
        log.info("command.countPattern end!");
    }

    private boolean checkArgs() {
        if (args.getMhapPath().equals("")) {
            log.error("MHapPath can not be null.");
            return false;
        }
        if (args.getCpgPath().equals("")) {
            log.error("CpgPath can not be null.");
            return false;
        }

        return true;
    }
}
