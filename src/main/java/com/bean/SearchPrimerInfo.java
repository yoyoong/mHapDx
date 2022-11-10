package com.bean;

public class SearchPrimerInfo {
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

    public String getFpos() {
        return Fpos;
    }

    public void setFpos(String fpos) {
        Fpos = fpos;
    }

    public String getRpos() {
        return Rpos;
    }

    public void setRpos(String rpos) {
        Rpos = rpos;
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

    public Integer getT_RC() {
        return T_RC;
    }

    public void setT_RC(Integer t_RC) {
        T_RC = t_RC;
    }

    public Integer getT_PRC() {
        return T_PRC;
    }

    public void setT_PRC(Integer t_PRC) {
        T_PRC = t_PRC;
    }

    public Float getT() {
        return T;
    }

    public void setT(Float t) {
        T = t;
    }

    public Integer getN_RC() {
        return N_RC;
    }

    public void setN_RC(Integer n_RC) {
        N_RC = n_RC;
    }

    public Integer getN_PRC() {
        return N_PRC;
    }

    public void setN_PRC(Integer n_PRC) {
        N_PRC = n_PRC;
    }

    public Float getN() {
        return N;
    }

    public void setN(Float n) {
        N = n;
    }

    public Float getFC() {
        return FC;
    }

    public void setFC(Float FC) {
        this.FC = FC;
    }
}
