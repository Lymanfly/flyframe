package org.lyman.utils.excel.write;

public enum CustomCellStyle {

    GENERIAL("General"),
    NO_DECIMALS("0"),
    TWO_DECIMALS("0.00"),
    DATE("yyyy-MM-dd"),
    DATE_TIME("yyyy-MM-dd hh:mm:ss"),
    DATE_EN("MM/dd/yyyy"),
    DATE_EN_TIME("MM/dd/yyyy hh:mm:ss"),
    MONEY("#,##0.00"),
    PERCENT("0.00%");

    private String format;

    CustomCellStyle(String format) {
        this.format = format;
    }

    public String getFormat() {
        return this.format;
    }

    public static CustomCellStyle get(String format) {
        if (format == null)
            return GENERIAL;
        switch (format) {
            case "0":
                return NO_DECIMALS;
            case "0.00":
                return TWO_DECIMALS;
            case "yyyy-MM-dd":
                return DATE;
            case "yyyy-MM-dd hh:mm:ss":
                return DATE_TIME;
            case "MM/dd/yyyy":
                return DATE_EN;
            case "MM/dd/yyyy hh:mm:ss":
                return DATE_EN_TIME;
            case "#,##0.00":
                return MONEY;
            case "0.00%":
                return PERCENT;
            default:
                return GENERIAL;
        }
    }

}
