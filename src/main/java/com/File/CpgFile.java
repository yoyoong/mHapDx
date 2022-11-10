package com.File;

import com.bean.Region;
import htsjdk.tribble.readers.TabixReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CpgFile implements InputFile {
    public static final Logger log = LoggerFactory.getLogger(CpgFile.class);

    TabixReader tabixReader;

    public CpgFile(String cpgPath) throws IOException {
        tabixReader = new TabixReader(cpgPath);
    }

    @Override
    public List<Integer> parseByRegion(Region region) throws Exception {
        List<Integer> cpgPosList = new ArrayList<>();
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

    @Override
    public List<?> parseWholeFile() throws Exception {
        return null;
    }

    @Override
    public Map<String, List<Integer>> parseWholeFileGroupByChr() throws Exception {
        TreeMap<String, List<Integer>> cpgPosListMap = new TreeMap<>();

        List<Integer> cpgPosList = new ArrayList<>();
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

    public List<Integer> parseByRegionWithShift(Region region, Integer shift) throws Exception {
        List<Integer> cpgPosList = new ArrayList<>();
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

        return cpgPosList;
    }

    @Override
    public void close() {
        tabixReader.close();
    }
}
