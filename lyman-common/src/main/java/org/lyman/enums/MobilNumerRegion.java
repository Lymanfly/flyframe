package org.lyman.enums;

public enum MobilNumerRegion {

    CHINA("^1[3-9]\\d{9}$"), OTHERS("[0-9]+");

    private String regex;

    MobilNumerRegion(String regex) {
        this.regex = regex;
    }

    public String regex() {
        return this.regex;
    }

}
