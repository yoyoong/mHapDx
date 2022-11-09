package com.common;

import com.bean.MHapInfo;
import com.bean.Region;
import htsjdk.tribble.readers.TabixReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Util {
    public static final Logger log = LoggerFactory.getLogger(Util.class);

    public Region parseRegion(String regionStr) {
        Region region = new Region();
        region.setChrom(regionStr.split(":")[0]);
        region.setStart(Integer.valueOf(regionStr.split(":")[1].split("-")[0]));
        region.setEnd(Integer.valueOf(regionStr.split(":")[1].split("-")[1]));
        return region;
    }

    public List<Integer> parseCpgFile(String cpgPath, Region region) throws Exception {
        List<Integer> cpgPosList = new ArrayList<>();
        TabixReader tabixReader = new TabixReader(cpgPath);
        TabixReader.Iterator cpgIterator = tabixReader.query(region.getChrom(), region.getStart(), region.getEnd());
        String cpgLine = "";
        while((cpgLine = cpgIterator.next()) != null) {
            if (cpgLine.split("\t").length < 3) {
                continue;
            } else {
                cpgPosList.add(Integer.valueOf(cpgLine.split("\t")[1]));
            }
        }

        tabixReader.close();
        return cpgPosList;
    }

    public Map<String, List<Integer>> parseWholeCpgFile(String cpgPath) throws Exception {
        TreeMap<String, List<Integer>> cpgPosListMap = new TreeMap<>();

        List<Integer> cpgPosList = new ArrayList<>();
        TabixReader tabixReader = new TabixReader(cpgPath);
        String cpgLine = tabixReader.readLine();
        String lastChr = cpgLine.split("\t")[0];
        while(cpgLine != null && !cpgLine.equals("")) {
            if (cpgLine.split("\t").length < 3) {
                continue;
            } else {
                if (lastChr.equals(cpgLine.split("\t")[0])) {
                    cpgPosList.add(Integer.valueOf(cpgLine.split("\t")[1]));
                } else {
                    cpgPosListMap.put(lastChr, cpgPosList);
                    lastChr = cpgLine.split("\t")[0];
                    cpgPosList = new ArrayList<>();
                    cpgPosList.add(Integer.valueOf(cpgLine.split("\t")[1]));
                }
                cpgLine = tabixReader.readLine();
            }
        }
        cpgPosListMap.put(lastChr, cpgPosList);
        log.info("Read cpg file success.");

        tabixReader.close();
        return cpgPosListMap;
    }

    public List<Integer> parseCpgFileWithShift(String cpgPath, Region region, Integer shift) throws Exception {
        List<Integer> cpgPosList = new ArrayList<>();
        TabixReader tabixReader = new TabixReader(cpgPath);
        Integer start = region.getStart() - shift > 1 ? region.getStart() - shift : 1;
        TabixReader.Iterator cpgIterator = tabixReader.query(region.getChrom(), start, region.getEnd() + shift);
        String cpgLine = "";
        while((cpgLine = cpgIterator.next()) != null) {
            if (cpgLine.split("\t").length < 3) {
                continue;
            } else {
                cpgPosList.add(Integer.valueOf(cpgLine.split("\t")[1]));
            }
        }

        tabixReader.close();
        return cpgPosList;
    }

    public List<Region> getBedRegionList(String bedFile) throws Exception {
        List<Region> regionList = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(bedFile)));
        String bedLine = "";
        while ((bedLine = bufferedReader.readLine()) != null && !bedLine.equals("")) {
            Region region = new Region();
            if (bedLine.split("\t").length < 3) {
                log.error("Interval not in correct format.");
                break;
            }
            region.setChrom(bedLine.split("\t")[0]);
            region.setStart(Integer.valueOf(bedLine.split("\t")[1]) + 1);
            region.setEnd(Integer.valueOf(bedLine.split("\t")[2]));
            regionList.add(region);
        }
        return regionList;
    }

    public BufferedWriter createOutputFile(String directory, String fileName) throws IOException {
        String filePath = "";
        if (directory != null && !directory.equals("")) {
            // create the output directory
            File outputDir = new File(directory);
            if (!outputDir.exists()){
                if (!outputDir.mkdirs()){
                    log.error("create" + outputDir.getAbsolutePath() + "fail");
                    return null;
                }
            }
            filePath = directory + "/" + fileName;
        } else {
            filePath = fileName;
        }

        // create the output file
        File file = new File(filePath);
        if (!file.exists()) {
            if (!file.createNewFile()) {
                log.error("create" + file.getAbsolutePath() + "fail");
                return null;
            }
        } else {
            FileWriter fileWriter =new FileWriter(file.getAbsoluteFile());
            fileWriter.write("");  //写入空
            fileWriter.flush();
            fileWriter.close();
        }
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        return bufferedWriter;
    }

    public List<MHapInfo> parseMhapFile(String mhapPath, Region region, String strand, Boolean isMerge) throws IOException, InterruptedException {
        TabixReader tabixReader = new TabixReader(mhapPath);
        TabixReader.Iterator mhapIterator = tabixReader.query(region.getChrom(), region.getStart() - 1, region.getEnd());
        List<MHapInfo> mHapInfoList = new ArrayList<>();
        String mHapLine = "";
        Integer lineCnt = 0;
        while((mHapLine = mhapIterator.next()) != null) {
            lineCnt++;
            if (lineCnt % 1000000 == 0) {
                log.info("Read " + region.getChrom() + " mhap " + lineCnt + " lines.");
            }
            if ((strand.equals("plus") && mHapLine.split("\t")[5].equals("-")) ||
                    (strand.equals("minus") && mHapLine.split("\t")[5].equals("+"))) {
                continue;
            }
            MHapInfo mHapInfo = new MHapInfo(mHapLine.split("\t")[0], Integer.valueOf(mHapLine.split("\t")[1]),
                    Integer.valueOf(mHapLine.split("\t")[2]), mHapLine.split("\t")[3],
                    Integer.valueOf(mHapLine.split("\t")[4]), mHapLine.split("\t")[5]);
            if (isMerge) {
                mHapInfoList.add(mHapInfo);
            } else {
                Integer cnt = mHapInfo.getCnt();
                if (cnt > 1) {
                    for (int i = 0; i < cnt; i++) {
                        mHapInfo.setCnt(1);
                        mHapInfoList.add(mHapInfo);
                    }
                } else {
                    mHapInfoList.add(mHapInfo);
                }
            }
        }

        tabixReader.close();
        return mHapInfoList;
    }

    public Integer indexOfList(List<Integer> list, Integer start, Integer end, Integer findValue) {
        if(start <= end){
            Integer middle = (start + end) / 2;
            Integer middleValue = list.get(middle);//中间值
            if (findValue.equals(middleValue)) {
                //查找值等于中间值直接返回
                return  middle;
            } else if (findValue < middleValue) {
                //小于中间值，在中间值之前的数据中查找
                return indexOfList(list, start, middle - 1, findValue);
            } else {
                //大于中间值，在中间值之后的数据中查找
                return indexOfList(list, middle + 1, end, findValue);
            }
        }
        return -1;
    }
}
