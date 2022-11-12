package com.bean;

public class CountPatternInfo {
    public String F_Primer = "";
    public String R_Primer = "";
    public String Fpattern = "";
    public String Rpattern = "";
    public int[] patternCntList = new int[0];
    public int[] totalCntList = new int[0];

    public String getF_Primer() {
        return F_Primer;
    }

    public void setF_Primer(String f_Primer) {
        F_Primer = f_Primer;
    }

    public String getR_Primer() {
        return R_Primer;
    }

    public void setR_Primer(String r_Primer) {
        R_Primer = r_Primer;
    }

    public String getFpattern() {
        return Fpattern;
    }

    public void setFpattern(String fpattern) {
        Fpattern = fpattern;
    }

    public String getRpattern() {
        return Rpattern;
    }

    public void setRpattern(String rpattern) {
        Rpattern = rpattern;
    }

    public int[] getPatternCntList() {
        return patternCntList;
    }

    public void setPatternCntList(int[] patternCntList) {
        this.patternCntList = patternCntList;
    }

    public int[] getTotalCntList() {
        return totalCntList;
    }

    public void setTotalCntList(int[] totalCntList) {
        this.totalCntList = totalCntList;
    }
}
