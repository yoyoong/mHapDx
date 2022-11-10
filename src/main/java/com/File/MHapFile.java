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

public class MHapFile implements InputFile {
    public static final Logger log = LoggerFactory.getLogger(MHapFile.class);

    TabixReader tabixReader;

    public MHapFile(String mHapPath) throws IOException {
        tabixReader = new TabixReader(mHapPath);
    }

    @Override
    public List<MHapInfo> parseByRegion(Region region) throws IOException {
        TabixReader.Iterator mhapIterator = tabixReader.query(region.getChrom(), region.getStart() - 1, region.getEnd());
        List<MHapInfo> mHapInfoList = new ArrayList<>();
        String mHapLine = "";
        Integer lineCnt = 0;
        while((mHapLine = mhapIterator.next()) != null) {
            lineCnt++;
            if (lineCnt % 1000000 == 0) {
                log.info("Read " + region.getChrom() + " mhap " + lineCnt + " lines.");
            }
            MHapInfo mHapInfo = new MHapInfo();
            mHapInfo.setChrom(mHapLine.split("\t")[0]);
            mHapInfo.setStart(Integer.valueOf(mHapLine.split("\t")[1]));
            mHapInfo.setEnd(Integer.valueOf(mHapLine.split("\t")[2]));
            mHapInfo.setCpg(mHapLine.split("\t")[3]);
            mHapInfo.setCnt(Integer.valueOf(mHapLine.split("\t")[4]));
            mHapInfo.setStrand(mHapLine.split("\t")[5]);
            mHapInfoList.add(mHapInfo);
        }

        return mHapInfoList;
    }

    @Override
    public List<?> parseWholeFile() throws Exception {
        return null;
    }

    @Override
    public Map<?, ?> parseWholeFileGroupByChr() throws Exception {
        return null;
    }

    @Override
    public void close() {
        tabixReader.close();
    }
}
