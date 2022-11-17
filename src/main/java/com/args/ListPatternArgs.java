package com.args;

import com.common.Annotation;
import java.io.Serializable;

public class ListPatternArgs implements Serializable {
    @Annotation(value = "input file(s), mhap.gz format,generated by mHapTools and indexed")
    public String mhapPath = "";
    @Annotation("genomic CpG file, gz format and Indexed")
    public String cpgPath = "";
    @Annotation("the first region, in the format of chr:start-end")
    public String FPrimer = "";
    @Annotation("the second region, in the format of chr:start-end")
    public String RPrimer = "";
    @Annotation("a bed file, in the format of \"FPrimer\\tRPrimer\"")
    public String bedPath = "";
    @Annotation("output directory, created in advance")
    public String outputDir = "";
    @Annotation("prefix of the output file(s)")
    public String tag = "listPattern.out";

    public String getMhapPath() {
        return mhapPath;
    }

    public void setMhapPath(String mhapPath) {
        this.mhapPath = mhapPath;
    }

    public String getCpgPath() {
        return cpgPath;
    }

    public void setCpgPath(String cpgPath) {
        this.cpgPath = cpgPath;
    }

    public String getFPrimer() {
        return FPrimer;
    }

    public void setFPrimer(String FPrimer) {
        this.FPrimer = FPrimer;
    }

    public String getRPrimer() {
        return RPrimer;
    }

    public void setRPrimer(String RPrimer) {
        this.RPrimer = RPrimer;
    }

    public String getBedPath() {
        return bedPath;
    }

    public void setBedPath(String bedPath) {
        this.bedPath = bedPath;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}
