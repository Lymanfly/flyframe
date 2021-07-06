package org.lyman.utils.excel.write;

import com.google.common.collect.RangeMap;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.lyman.utils.ArrayUtils;
import org.lyman.utils.CollectionUtils;
import org.lyman.utils.StringUtils;
import org.lyman.utils.excel.write.converter.DefaultConverter;
import org.lyman.utils.excel.write.converter.abs.ObjectConverter;
import org.lyman.utils.excel.write.elements.Border;
import org.lyman.utils.excel.write.elements.Column;
import org.lyman.utils.excel.write.elements.Header;

import java.io.OutputStream;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XlsxWriter {

    private static final ThreadLocal<Map<Column, CellStyle>> BODY_STYLE_CACHE = new ThreadLocal<>();

    public static void writeInOneSheet(OutputStream os, SheetProperties... sheetProperties) {
        write(os, new DefaultConverter(), true, sheetProperties);
    }

    public static void writeInOneSheet(OutputStream os, ObjectConverter converter, SheetProperties... sheetProperties) {
        write(os, converter, true, sheetProperties);
    }

    public static void write(OutputStream os, SheetProperties... sheetProperties) {
        write(os, new DefaultConverter(), false, sheetProperties);
    }

    public static void write(OutputStream os, ObjectConverter converter, SheetProperties... sheetProperties) {
        write(os, converter, false, sheetProperties);
    }

    private static void write(OutputStream os, ObjectConverter converter, boolean inOneSheet, SheetProperties... sheetProperties) {
        checkAndPrepare(os, converter, sheetProperties);
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        DataFormat dataFormat = workbook.createDataFormat();
        Sheet sheet = null;
        for (int i = 0, len = sheetProperties.length; i < len; i++) {
            SheetProperties properties = sheetProperties[i];
            if (i == 0 || !inOneSheet)
                sheet = createSheet(workbook, properties);
            if (properties.getConditionalFormatter() != null)
                properties.getConditionalFormatter().addConditionalFormatter(sheet);
            Map<String, RangeMap<Integer, Short>> indentionMap = properties.getIndentionMap();
        }
        afterCompletion();
    }

    private static Sheet createSheet(Workbook workbook, SheetProperties properties) {
        return createSheet(workbook, null, properties, 0);
    }

    private static Sheet createSheet(Workbook workbook, Sheet sheet, SheetProperties properties) {
        return createSheet(workbook, sheet, properties, 0);
    }

    private static Sheet createSheet(Workbook workbook, SheetProperties properties, int duplicateNum) {
        return createSheet(workbook, null, properties, duplicateNum);
    }

    private static Sheet createSheet(Workbook workbook, Sheet sheet, SheetProperties properties, int duplicateNum) {
        if (sheet == null) {
            String sheetName = properties.getName();
            sheet = StringUtils.isEmpty(sheetName)
                    ? workbook.createSheet()
                    : workbook.createSheet(duplicateNum > 0
                    ? sheetName.concat(String.valueOf(duplicateNum))
                    : sheetName);
        }
        int count = sheet.getLastRowNum(), frozenLeft = properties.getFrozenLeft(), frozenTop = properties.getFrozenTop();
        sheet.setDefaultColumnWidth(20);
        sheet.createFreezePane(frozenLeft, frozenTop);
        List<CellRangeAddress> mergedRegions = properties.getMergedRegions();
        if (CollectionUtils.isNotEmpty(mergedRegions))
            mergedRegions.forEach(sheet::addMergedRegion);
        List<Header> headers = properties.getHeaders();
        if (CollectionUtils.isNotEmpty(headers)) {
            for (Header header : headers) {
                Row row = sheet.createRow(++count);
                row.setRowStyle(getHeaderStyle(workbook, header));
                row.setHeightInPoints(header.getHeight());
                String[] titles = header.getTitles();
                if (ArrayUtils.isNotEmpty(titles)) {
                    for (int i = 0, len = titles.length; i < len; i++) {
                        Cell cell = row.createCell(i);
                        cell.setCellValue(titles[i]);
                    }
                }
            }
        }
        return sheet;
    }

    private static CellStyle getHeaderStyle(Workbook workbook, Header header) {
        CellStyle cs = workbook.createCellStyle();
        Font f = workbook.createFont();
        f.setFontName(header.getFontName());
        f.setBold(header.isBold());
        f.setItalic(header.isItalic());
        f.setColor(header.getFontColor());
        f.setFontHeight(header.getFontHeight());
        cs.setFont(f);
        cs.setAlignment(header.getHorizontalAlignment());
        cs.setVerticalAlignment(header.getVerticalAlignment());
        cs.setWrapText(header.isTextWrap());
        cs.setFillBackgroundColor(header.getBackground());
        Border b = header.getLeftBorder();
        if (b != null) {
            cs.setBorderLeft(b.getStyle());
            cs.setLeftBorderColor(b.getColor());
        }
        b = header.getTopBorder();
        if (b != null) {
            cs.setBorderTop(b.getStyle());
            cs.setTopBorderColor(b.getColor());
        }
        b = header.getRightBorder();
        if (b != null) {
            cs.setBorderRight(b.getStyle());
            cs.setRightBorderColor(b.getColor());
        }
        b = header.getBottomBorder();
        if (b != null) {
            cs.setBorderBottom(b.getStyle());
            cs.setBottomBorderColor(b.getColor());
        }
        return cs;
    }

    private static CellStyle getBodyStyle(Workbook workbook, Column column) {
        Map<Column, CellStyle> cacheMap = BODY_STYLE_CACHE.get();
        CellStyle cs = cacheMap.get(column);
        if (cs != null)
            return cs;
        cs = workbook.createCellStyle();
        Font f = workbook.createFont();
        f.setFontName(column.getFontName());
        f.setFontHeightInPoints(column.getFontHeight());
        f.setColor(column.getFontColor());
        f.setBold(column.isBold());
        f.setItalic(column.isItalic());
        cs.setFont(f);
        cs.setAlignment(column.getHorizontalAlignment());
        cs.setVerticalAlignment(column.getVerticalAlignment());
        cs.setWrapText(column.isTextWrap());
        cs.setFillBackgroundColor(column.getBackground());
        Border b = column.getLeftBorder();
        if (b != null) {
            cs.setBorderLeft(b.getStyle());
            cs.setLeftBorderColor(b.getColor());
        }
        b = column.getTopBorder();
        if (b != null) {
            cs.setBorderTop(b.getStyle());
            cs.setTopBorderColor(b.getColor());
        }
        b = column.getRightBorder();
        if (b != null) {
            cs.setBorderRight(b.getStyle());
            cs.setRightBorderColor(b.getColor());
        }
        b = column.getBottomBorder();
        if (b != null) {
            cs.setBorderBottom(b.getStyle());
            cs.setBottomBorderColor(b.getColor());
        }
        cacheMap.put(column, cs);
        return cs;
    }

    private static void checkAndPrepare(OutputStream os, ObjectConverter converter, SheetProperties... properties) {
        if (properties == null)
            throw new RuntimeException("SheetProperties is necessary for excel writer!");
        if (os == null)
            throw new RuntimeException("OutputStream is necessary for excel writer!");
        if (converter == null)
            throw new RuntimeException("ObjectConverter is necessary for excel writer!");
        for (SheetProperties p : properties)
            if (CollectionUtils.isEmpty(p.getColumns()))
                throw new RuntimeException("SheetProperties must has Columns!");
        BODY_STYLE_CACHE.set(new HashMap<>());
    }

    public static void check(OutputStream os, ObjectConverter converter, SheetProperties... properties) {
        if (properties == null)
            throw new RuntimeException("SheetProperties is necessary for excel writer!");
        if (os == null)
            throw new RuntimeException("OutputStream is necessary for excel writer!");
        if (converter == null)
            throw new RuntimeException("ObjectConverter is necessary for excel writer!");
        for (SheetProperties p : properties)
            if (CollectionUtils.isEmpty(p.getColumns()))
                throw new RuntimeException("SheetProperties must has Columns!");
    }

    private static void afterCompletion() {
        BODY_STYLE_CACHE.remove();
    }

}
