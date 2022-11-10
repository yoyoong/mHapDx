package com.File;

import com.bean.ListPatternInfo;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class ListPatternOutputFIle extends OutputFile {
    public ListPatternInfo listPatternInfo = new ListPatternInfo();
    public String[] mHapPathList;

    public ListPatternOutputFIle(String directory, String fileName) throws IOException {
        super(directory, fileName);
    }

    @Override
    public void writeHead() throws IOException, IllegalAccessException {
        Field[] fields = ListPatternInfo.class.getDeclaredFields();
        String head = fields[0].getName();
        for (int i = 1; i < fields.length; i++) { // joint the fields generate line string
            Field field = fields[i];
            if (!(field.getType() == int[].class)) {
                head += "\t" + field.getName().split("/")[field.getName().split("/").length - 1];;
            }
        }
        for (String mHapPath : mHapPathList) {
            head += "\t" + mHapPath;
        }
        head += "\n";
        bufferedWriter.write(head);
    }

    @Override
    public void writeLine() throws IOException, IllegalAccessException {
        Field[] fields = ListPatternInfo.class.getDeclaredFields();// get the field name list
        String line = (String) fields[0].get(listPatternInfo);
        for (int i = 1; i < fields.length; i++) { // joint the fields generate line string
            Field field = fields[i];
            if (field.getType() == int[].class) {
                int[] patternCntList = (int[]) field.get(listPatternInfo);
                for (int cnt : patternCntList) {
                    line += "\t" + cnt;
                }
            } else {
                line += "\t" + field.get(listPatternInfo);
            }
        }
        line += "\n";
        bufferedWriter.write(line);
    }
}
