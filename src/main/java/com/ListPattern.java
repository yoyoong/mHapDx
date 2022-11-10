package com;

import com.File.*;
import com.args.ListPatternArgs;
import com.bean.ListPatternInfo;
import com.bean.MHapInfo;
import com.bean.Region;
import com.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ListPattern {
    public static final Logger log = LoggerFactory.getLogger(ListPattern.class);

    ListPatternArgs args = new ListPatternArgs();
    Util util = new Util();

    public void listPattern(ListPatternArgs listPatternArgs) throws Exception {
        log.info("command.listPattern start!");
        args = listPatternArgs;

        CpgFile cpgFile = new CpgFile(args.getCpgPath());

        // check the command
        boolean checkResult = checkArgs();
        if (!checkResult) {
            log.error("Checkargs fail, please check the command.");
            return;
        }

        // get a pair of regions
        Region fPrimer = new Region(args.getFPrimer());
        Region rPrimer = new Region(args.getRPrimer());

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

        String[] mHapPathList = args.getMhapPath().split(" ");
        List<ListPatternInfo> listPatternInfoList = new ArrayList<>();
        List<String> patternList = new ArrayList<>();
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

            // get the pattern list
            Iterator<String> patternMapCuttedIterator = patternMapCutted.keySet().iterator();
            while (patternMapCuttedIterator.hasNext()) {
                String key = patternMapCuttedIterator.next();
                if (patternList.contains(key)) { // exist, get the listPatternInfo of pattern and add to the patternCntList
                    ListPatternInfo listPatternInfo = listPatternInfoList.get(patternList.indexOf(key));
                    int[] patternCntList = listPatternInfo.getPatternCntList();
                    patternCntList[i] = patternMapCutted.get(key);
                    listPatternInfo.setPatternCntList(patternCntList);
                } else { // not exist, add a new listPatternInfo
                    patternList.add(key);
                    ListPatternInfo listPatternInfo = new ListPatternInfo();
                    listPatternInfo.setF_Primer(fPrimer.toHeadString());
                    listPatternInfo.setR_Primer(rPrimer.toHeadString());
                    listPatternInfo.setFpattern(key.substring(fPrimerCpgStartIndex, fPrimerCpgEndIndex + 1));
                    listPatternInfo.setRpattern(key.substring(fPrimerCpgEndIndex + 1));
                    int[] patternCntList = new int[mHapPathList.length];
                    patternCntList[i] = patternMapCutted.get(key);
                    listPatternInfo.setPatternCntList(patternCntList);
                    listPatternInfoList.add(listPatternInfo);
                }
            }

            mHapFile.close();
        }

        // create the output file and write
        String outputFileName = args.getTag() + ".searchPrimer.txt";
        ListPatternOutputFIle outputFile = new ListPatternOutputFIle(args.getOutputDir(), outputFileName);
        outputFile.mHapPathList = mHapPathList;
        outputFile.writeHead();
        for (int i = 0; i < listPatternInfoList.size(); i++) {
            outputFile.listPatternInfo = listPatternInfoList.get(i);
            outputFile.writeLine();
        }

        outputFile.close();
        cpgFile.close();
        log.info("command.listPattern end!");
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
        if (args.getFPrimer().equals("") || args.getRPrimer().equals("")) {
            log.error("Region1 and region2 cannot be null.");
            return false;
        }

        return true;
    }
}
