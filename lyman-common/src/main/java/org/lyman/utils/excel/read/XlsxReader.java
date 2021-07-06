package org.lyman.utils.excel.read;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.lyman.utils.excel.read.abs.SheetHandler;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.XMLReader;

import java.io.InputStream;
import java.util.Iterator;

public class XlsxReader {

    public static void processSheets(InputStream is, SheetHandler handler) throws Exception {
    }

    public static void processSheets(InputStream is, SheetHandler handler, int sheetNum) throws Exception {
        OPCPackage pkg = OPCPackage.open(is);
        XSSFReader reader = new XSSFReader(pkg);
        StylesTable st = reader.getStylesTable();
        handler.setStylesTable(st);
        SharedStringsTable sst = reader.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst, handler);
        Iterator<InputStream> sheets = reader.getSheetsData();
        int count = 0;
        while (sheets.hasNext()) {
            if (sheetNum > 0 && count >= sheetNum)
                break;
            InputStream sheet = sheets.next();
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
            count++;
        }
    }

    private static XMLReader fetchSheetParser(SharedStringsTable sst, SheetHandler handler) throws Exception {
        XMLReader parser = XMLReaderFactory.createXMLReader("com.sun.org.apache.xerces.internal.parsers.SAXParser");
        handler.setSharedStringTable(sst);
        parser.setContentHandler(handler);
        return parser;
    }

}
