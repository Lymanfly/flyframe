package org.lyman.utils.excel.write;

import com.google.common.collect.RangeMap;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.lyman.enums.WhiteSpaceType;
import org.lyman.utils.ArrayUtils;
import org.lyman.utils.CollectionUtils;
import org.lyman.utils.StringUtils;
import org.lyman.utils.excel.write.converter.DefaultConverter;
import org.lyman.utils.excel.write.converter.abs.ObjectConverter;
import org.lyman.utils.excel.write.elements.Border;
import org.lyman.utils.excel.write.elements.Column;
import org.lyman.utils.excel.write.elements.Header;

import java.io.IOException;
import java.io.OutputStream;

import java.math.BigDecimal;
import java.util.*;

public class XlsxWriter {

    private static final int MAX_ROW = 1024 * 1024 - 1;

    private static final int MAX_COLUMN = 128 * 128 - 1;

    public static final String FORMULA_SEQ_PH = "@SEQ@";

    private static final ThreadLocal<Map<Column, CellStyle>> BODY_STYLE_CACHE = new ThreadLocal<>();

    private static final ThreadLocal<Map<String, Integer>> SHEET_NAMES = new ThreadLocal<>();

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

    @SuppressWarnings({"unchecked", "UnstableApiUsage"})
    private static void write(OutputStream os, ObjectConverter converter, boolean inOneSheet, SheetProperties... sheetProperties) {
        checkAndPrepare(os, converter, sheetProperties);
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        DataFormat dataFormat = workbook.createDataFormat();
        Sheet sheet = null;
        for (int i = 0, len = sheetProperties.length; i < len; i++) {
            SheetProperties properties = sheetProperties[i];
            List<Column> columns = properties.getColumns();
            if (columns.size() > MAX_COLUMN)
                throw new RuntimeException("Data amount overflow! (Excel's max column number in one sheet is 16384)");
            if (i == 0 || !inOneSheet)
                sheet = createSheet(workbook, properties);
            Iterator<?> data = properties.getData();
            if (data == null)
                continue;
            if (properties.getConditionalFormatter() != null)
                properties.getConditionalFormatter().addConditionalFormatter(sheet);
            Map<String, RangeMap<Integer, Short>> indentionMap = properties.getIndentionMap();
            boolean hasIndention = CollectionUtils.isNotEmpty(indentionMap);
            int count = 0;
            while (data.hasNext()) {
                Object obj = data.next();
                if (obj == null)
                    continue;
                Map<String, Object> item = obj instanceof Map
                        ? (Map<String, Object>) obj : converter.convert(obj);
                if (CollectionUtils.isEmpty(item))
                    continue;
                int rn = sheet.getLastRowNum();
                if (rn >= MAX_ROW)
                    throw new RuntimeException("Data amount overflow! (Excel's max capacity in one sheet is 1048576)");
                Row row = sheet.createRow(rn + 1);
                for (int j = 0, width = columns.size(); j < width; j++) {
                    Column column = columns.get(j);
                    String key = column.getKey();
                    Object value = item.get(key);
                    Cell cell = row.createCell(j);
                    if (value instanceof String) {
                        if (column.isFormula()) {
                            String formula = ((String) value).replaceAll(FORMULA_SEQ_PH,
                                    String.valueOf(count + column.getFormulaRowNumOffset()));
                            cell.setCellFormula(formula);
                        } else
                            cell.setCellValue((String) value);
                    } else if (value instanceof Double)
                        cell.setCellValue((Double) value);
                    else if (value instanceof Integer)
                        cell.setCellValue((Integer) value);
                    else if (value instanceof Long)
                        cell.setCellValue((Long) value);
                    else if (value instanceof BigDecimal)
                        cell.setCellValue(((BigDecimal) value).doubleValue());
                    else if (value instanceof Boolean)
                        cell.setCellValue((Boolean) value);
                    else if (value == null) {
                        WhiteSpaceType wst = column.getWhiteSpaceType();
                        cell.setCellValue(wst.getValue());
                    }
                    RangeMap<Integer, Short> indentionMapInner;
                    Short indetnion = hasIndention && (indentionMapInner = indentionMap.get(key)) != null
                            ? indentionMapInner.get(count) : null;
                    cell.setCellStyle(getBodyStyle(workbook, column, indetnion));
                }
                count++;
            }
        }
        afterCompletion();
        try {
            workbook.write(os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            workbook.dispose();
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Sheet createSheet(Workbook workbook, SheetProperties properties) {
        Sheet sheet;
        String sheetName = properties.getName();
        if (StringUtils.isNotEmpty(sheetName)) {
            Map<String, Integer> snMap = SHEET_NAMES.get();
            int duplicateNum = snMap.getOrDefault(sheetName, 0);
            sheet = workbook.createSheet(duplicateNum > 0
                    ? sheetName.concat(String.valueOf(duplicateNum))
                    : sheetName);
            snMap.put(sheetName, ++duplicateNum);
        } else
            sheet = workbook.createSheet();
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

    private static CellStyle getBodyStyle(Workbook workbook, Column column, Short indention) {
        Map<Column, CellStyle> cacheMap = BODY_STYLE_CACHE.get();
        CellStyle cs = cacheMap.get(column);
        if (indention == null || indention < 0)
            indention = 0;
        if (cs == null) {
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
        }
        cs.setIndention(indention);
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
        SHEET_NAMES.set(new HashMap<>());
    }

    private static void afterCompletion() {
        BODY_STYLE_CACHE.remove();
        SHEET_NAMES.remove();
    }

}
