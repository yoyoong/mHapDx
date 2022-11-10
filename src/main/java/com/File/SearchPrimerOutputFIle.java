package com.File;

import java.io.IOException;
import java.lang.reflect.Field;

public class SearchPrimerOutputFIle extends OutputFile {
    public String Fpos = "";
    public String Rpos = "";
    public String Fpattern = "";
    public String Rpattern = "";
    public Integer T_RC = 0;
    public Integer T_PRC = 0;
    public Float T = 0.0f;
    public Integer N_RC = 0;
    public Integer N_PRC = 0;
    public Float N = 0.0f;
    public Float FC = 0.0f;

    public SearchPrimerOutputFIle(String directory, String fileName) throws IOException {
        super(directory, fileName);
    }

}
