package org.lyman.utils.excel.read.abs;

import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.*;

public abstract class BasicSheetHandler extends DefaultSheetHandler {

    private static final String ATTR_C = "c";
    private static final String ATTR_R = "r";
    private static final String ATTR_T = "t";
    private static final String ATTR_S = "s";
    private static final String ATTR_V = "v";
    private static final String ATTR_ROW = "row";

    private SharedStringsTable sst;

    private StylesTable st;

    private String lastContents;

    private boolean nextIsString;

    private List<String> rowList;

    private int curRow;

    private int curCol;

    private Set<Integer> skippedRows;

    private String preRef;

    private String ref;

    private String maxRef;

    private CellDataType nextDataType;

    private final DataFormatter formatter = new DataFormatter();

    private short formatIndex;

    private String formatString;

    private boolean curColHasV;

    private boolean formatNumbers;

    public BasicSheetHandler() {
        this.rowList = new ArrayList<>();
        this.curCol = 0;
        this.curRow = 0;
        this.skippedRows = new HashSet<>();
        this.preRef = null;
        this.ref = null;
        this.maxRef = null;
        this.nextDataType = CellDataType.SSTINDEX;
        this.curColHasV = false;
        this.formatNumbers = false;
    }

    enum CellDataType {
        BOOL, ERROR, FORMULA, INLINSTER, SSTINDEX, NUMBER, DATE, NULL
    }

    @Override
    public void setSharedStringTable(SharedStringsTable sst) {
        this.sst = sst;
    }

    @Override
    public void setStylesTable(StylesTable st) {
        this.st = st;
    }

    @Override
    public void setSkippedRows(Object skippedRowNums) {
        if (skippedRowNums == null)
            return;
        if (skippedRowNums instanceof Integer) {
            this.skippedRows.add(((Integer) skippedRowNums) - 1);
        } else if (skippedRowNums instanceof Integer[]) {
            Arrays.stream((Integer[]) skippedRowNums).forEach(this::setSkippedRows);
        } else if (skippedRowNums instanceof Collection) {
            ((Collection<?>) skippedRowNums).forEach(this::setSkippedRows);
        } else if (skippedRowNums instanceof String) {
            String[] groups = ((String) skippedRowNums).split(",");
            for (String item : groups) {
                item = item.trim();
                if (item.contains("-")) {
                    String[] minmax = item.split("-");
                    if (minmax.length == 2) {
                        try {
                            Integer min = Integer.valueOf(minmax[0].trim());
                            Integer max = Integer.valueOf(minmax[1].trim());
                            for (;min <= max;)
                                this.skippedRows.add(min++ - 1);
                        } catch (NumberFormatException ignored) {}
                    }
                } else {
                    try {
                        Integer rowNum = Integer.valueOf(item);
                        this.skippedRows.add(rowNum - 1);
                    } catch (NumberFormatException ignored) {}
                }
            }
        }
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes atts) throws SAXException {
        if (ATTR_C.equals(name)) {
            if (preRef == null)
                preRef = atts.getValue(ATTR_R);
            else
                preRef = ref;
            ref = atts.getValue(ATTR_R);
            this.setNextDataType(atts);
            String cellType = atts.getValue(ATTR_T);
            nextIsString = cellType != null && cellType.equals(ATTR_S);
            curColHasV = false;
        }
        lastContents = "";
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        if (this.sst == null)
            throw new RuntimeException("SharedStringsTable not found for org.lyman.utils.excel.read.abs.BasicSheetHandler");
        if (nextIsString) {
            int index = Integer.parseInt(lastContents);
            lastContents = sst.getItemAt(index).toString();
            nextIsString = false;
        }
        switch (name) {
            case ATTR_C:
                if (!curColHasV)
                    addRowStr(null);
                break;
            case ATTR_V:
                String value = this.getDataValue(lastContents.trim());
                addRowStr(value);
                curColHasV = true;
                break;
            case ATTR_ROW:
                if (curRow == 0)
                    maxRef = ref;
                if (maxRef != null && ref != null) {
                    int len = countNullCells(maxRef, ref);
                    for (int i = 0; i <= len; i++) {
                        rowList.add(curCol, "");
                        curCol++;
                    }
                }
                if (!skippedRows.contains(curRow))
                    convertEntity(rowList);
                curRow++;
                rowList.clear();
                curCol = 0;
                preRef = null;
                ref = null;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        lastContents += new String(ch, start, length);
    }

    private void setNextDataType(Attributes attributes) {
        this.nextDataType = CellDataType.NUMBER;
        this.formatIndex = -1;
        formatString = null;
        String cellType = attributes.getValue(ATTR_T);
        String cellStyle = attributes.getValue(ATTR_S);
        if (cellType != null) {
            switch (cellType) {
                case "b":
                    nextDataType = CellDataType.BOOL;
                    break;
                case "e":
                    nextDataType = CellDataType.ERROR;
                    break;
                case "s":
                    nextDataType = CellDataType.SSTINDEX;
                    break;
                case "str":
                    nextDataType = CellDataType.FORMULA;
                    break;
                case "inlineStr":
                    nextDataType = CellDataType.INLINSTER;
                    break;
                default:
                    break;
            }
        }
        if (st != null && cellStyle != null) {
            int styleIndex = Integer.parseInt(cellStyle);
            XSSFCellStyle style = st.getStyleAt(styleIndex);
            formatIndex = style.getDataFormat();
            formatString = style.getDataFormatString();
            if ("m/d/yy".equals(formatString)) {
                nextDataType = CellDataType.DATE;
                formatString = "yyyy-MM-dd";
            }
            if (formatString == null) {
                nextDataType = CellDataType.NULL;
                formatString = BuiltinFormats.getBuiltinFormat(formatIndex);
            }
        }
    }

    private String getDataValue(String value) {
        String str;
        switch (nextDataType) {
            case BOOL:
                char first = value.charAt(0);
                str = first == '0' ? "FALSE" : "TRUE";
                break;
            case ERROR:
                str = "\"ERROR:" + value + "\"";
                break;
            case FORMULA:
                str = '"' + value + '"';
                break;
            case INLINSTER:
                XSSFRichTextString rts = new XSSFRichTextString(value);
                str = rts.toString();
                break;
            case SSTINDEX:
                str = value;
                break;
            case NUMBER:
                if (formatString != null && formatNumbers)
                    try {
                        str = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString);
                    } catch (NumberFormatException e) {
                        str = value;
                    }
                else
                    str = value;
                str = str.replace("_", "").trim();
                break;
            case DATE:
                if (formatString != null && formatNumbers)
                    try {
                        str = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString);
                    } catch (NumberFormatException e) {
                        str = value;
                    }
                else
                    str = value;
                str = str.replace(" ", "").trim();
                break;
            default:
                str = "";
                break;
        }
        return str;
    }

    private static int countNullCells(String ref, String preRef) {
        //Excel's maximum row is 1048576, maximum column is 16384, last column names XFD
        String xfd = ref.replaceAll("\\d+", "");
        String xfd_1 = preRef.replaceAll("\\d+", "");
        xfd = fillChar(xfd, 3, '@', true);
        xfd_1 = fillChar(xfd_1, 3, '@', true);
        char[] letter = xfd.toCharArray();
        char[] letter_1 = xfd_1.toCharArray();
        int res = (letter[0] - letter_1[0]) * 26 * 26 + (letter[1] - letter_1[1]) * 26 + (letter[2] - letter_1[2]);
        return res - 1;
    }

    private static String fillChar(String str, int len, int let, boolean isPre) {
        int len_1 = str.length();
        if (len_1 < len) {
            StringBuilder builder = new StringBuilder(str);
            for (int i = 0, diff = len - len_1; i < diff; i++) {
                if (isPre)
                    builder.insert(0, let);
                else
                    builder.append(let);
            }
            str = builder.toString();
        }
        return str;
    }

    private void addRowStr(String value) {
        if (!ref.equals(preRef)) {
            int len = countNullCells(ref, preRef);
            for (int i = 0; i < len; i++) {
                rowList.add(curCol, null);
                curCol++;
            }
        }
        rowList.add(curCol++, value);
    }

    protected int getCurRow() {
        return this.curRow;
    }

    protected void setFormatNumbers(boolean formatNumbers) {
        this.formatNumbers = formatNumbers;
    }

}
