package org.lyman.enums;

public enum WhiteSpaceType {

    EMPTY(""), BAR("-"), BAR_DUAL("--"), NULL(null);

    private String value;

    WhiteSpaceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public WhiteSpaceType get(String value) {
        if (value == null)
            return NULL;
        switch (value.trim()) {
            case "":
                return EMPTY;
            case "-":
                return BAR;
            case "--":
                return BAR_DUAL;
            default:
                return NULL;
        }
    }

}
