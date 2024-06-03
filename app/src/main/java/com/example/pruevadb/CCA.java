package com.example.pruevadb;

import java.util.ArrayList;

public class CCA {
    private String code;
    private String label;
    private String parent_code;


    public CCA(){}

    public CCA(String code, String label, String parent_code) {
        this.code = code;
        this.label = label;
        this.parent_code = parent_code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getParent_code() {
        return parent_code;
    }

    public void setParent_code(String parent_code) {
        this.parent_code = parent_code;
    }
}
