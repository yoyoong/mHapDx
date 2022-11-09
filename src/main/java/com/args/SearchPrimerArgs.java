package com.args;

import java.io.Serializable;

public class SearchPrimerArgs implements Serializable {
    public String mhapPathT = ""; // input tumor file,mhap.gz format,generated by mHapTools and indexed
    public String mhapPathN = ""; // input normal file,mhap.gz format,generated by mHapTools and indexed
    public String cpgPath = ""; // genomic CpG file, gz format and Indexed
    public String region = ""; // one region, in the format of chr:start-end
    public String bedPath = ""; // input BED file
    public String outputDir = ""; // output directory, created in advance
    public String tag = "searchPrimer_out"; // prefix of the output file(s)
    public Integer fLength = 25; //
    public Integer rLength = 25; //
    public Double minT = 0.1; //
    public Double maxN = 0.05; //
    public Double minFC = 2.0; //
    public Integer minInsertSize = 40; //
    public Integer maxInsertSize = 160; //
    public Integer minCov = 4; //

    public String getMhapPathT() {
        return mhapPathT;
    }

    public void setMhapPathT(String mhapPathT) {
        this.mhapPathT = mhapPathT;
    }

    public String getMhapPathN() {
        return mhapPathN;
    }

    public void setMhapPathN(String mhapPathN) {
        this.mhapPathN = mhapPathN;
    }

    public String getCpgPath() {
        return cpgPath;
    }

    public void setCpgPath(String cpgPath) {
        this.cpgPath = cpgPath;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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

    public Integer getfLength() {
        return fLength;
    }

    public void setfLength(Integer fLength) {
        this.fLength = fLength;
    }

    public Integer getrLength() {
        return rLength;
    }

    public void setrLength(Integer rLength) {
        this.rLength = rLength;
    }

    public Double getMinT() {
        return minT;
    }

    public void setMinT(Double minT) {
        this.minT = minT;
    }

    public Double getMaxN() {
        return maxN;
    }

    public void setMaxN(Double maxN) {
        this.maxN = maxN;
    }

    public Double getMinFC() {
        return minFC;
    }

    public void setMinFC(Double minFC) {
        this.minFC = minFC;
    }

    public Integer getMinInsertSize() {
        return minInsertSize;
    }

    public void setMinInsertSize(Integer minInsertSize) {
        this.minInsertSize = minInsertSize;
    }

    public Integer getMaxInsertSize() {
        return maxInsertSize;
    }

    public void setMaxInsertSize(Integer maxInsertSize) {
        this.maxInsertSize = maxInsertSize;
    }

    public Integer getMinCov() {
        return minCov;
    }

    public void setMinCov(Integer minCov) {
        this.minCov = minCov;
    }
}
