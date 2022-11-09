package com.File;

import com.bean.MHapInfo;
import com.bean.Region;
import htsjdk.tribble.readers.TabixReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MHapFile {
    public static final Logger log = LoggerFactory.getLogger(MHapFile.class);

    TabixReader tabixReader;

    public MHapFile(String mHapPath) throws IOException {
        tabixReader = new TabixReader(mHapPath);
    }

    public List<MHapInfo> parseByRegion(Region region, String strand, Boolean isMerge) throws IOException {
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
}
