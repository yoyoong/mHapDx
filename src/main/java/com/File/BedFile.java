package com.File;

import com.bean.MHapInfo;
import com.bean.Region;
import htsjdk.tribble.readers.TabixReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BedFile {
    public static final Logger log = LoggerFactory.getLogger(BedFile.class);

    File bedFile;

    public BedFile(String bedPath) {
        bedFile = new File(bedPath);
    }

    public List<Region> parseToRegionList() throws Exception {
        List<Region> regionList = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(bedFile));
        String bedLine = "";
        while ((bedLine = bufferedReader.readLine()) != null && !bedLine.equals("")) {
            if (bedLine.split("\t").length < 3) {
                log.error("Interval not in correct format.");
                break;
            }
            Region region = new Region(bedLine.split("\t")[0], Integer.valueOf(bedLine.split("\t")[1]) + 1,
                    Integer.valueOf(bedLine.split("\t")[2]));
            regionList.add(region);
        }
        return regionList;
    }

}
