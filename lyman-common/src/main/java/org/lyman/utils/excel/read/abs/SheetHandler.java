package org.lyman.utils.excel.read.abs;

import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

import java.util.List;

public interface SheetHandler extends EntityResolver, DTDHandler, ContentHandler, ErrorHandler {

    void setSharedStringTable(SharedStringsTable sst);

    void setStylesTable(StylesTable st);

    void convertEntity(List<String> rowStrs);

    void setSkippedRows(Object skippedRows);

    String getMessage();

    int getCount();

}
