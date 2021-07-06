package org.lyman.utils.excel.read.abs;

import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.IOException;

public abstract class DefaultSheetHandler implements SheetHandler {


    @Override
    public void setDocumentLocator(Locator locator) {

    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {

    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {

    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {

    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {

    }

    @Override
    public void skippedEntity(String name) throws SAXException {

    }

    @Override
    public void notationDecl(String name, String publicId, String systemId) throws SAXException {

    }

    @Override
    public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {

    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        return null;
    }

    @Override
    public void warning(SAXParseException exception) throws SAXException {

    }

    @Override
    public void error(SAXParseException exception) throws SAXException {

    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {

    }
}
