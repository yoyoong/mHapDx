package com.bean;

import java.io.Serializable;
import java.text.DecimalFormat;

public class MHapInfo implements Serializable {
    public String chrom;
    public Integer start;
    public Integer end;
    public String cpg;
    public Integer cnt;
    public String strand;

    public String getChrom() {
        return chrom;
    }

    public void setChrom(String chrom) {
        this.chrom = chrom;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public String getCpg() {
        return cpg;
    }

    public void setCpg(String cpg) {
        this.cpg = cpg;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public String getStrand() {
        return strand;
    }

    public void setStrand(String strand) {
        this.strand = strand;
    }

    public String indexByPos() {
        return this.chrom + this.start;
    }

    public String indexByRead() {
        return this.chrom + this.start+ this.end + this.cpg;
    }

    public String indexByReadAndStrand() {
        return this.chrom + this.start+ this.end + this.cpg + this.strand;
    }

    public String index() {
        return this.chrom + this.start+ this.end + this.cpg + this.strand;
    }
}
