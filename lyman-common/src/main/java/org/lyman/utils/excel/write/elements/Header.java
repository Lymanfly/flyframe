package org.lyman.utils.excel.write.elements;

import lombok.Data;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

@Data
public class Header {

    private String[] titles;

    private float height = -1;

    private short fontHeight = 12;

    private String fontName;

    private boolean bold = true;

    private boolean italic;

    private boolean textWrap;

    private HorizontalAlignment horizontalAlignment = HorizontalAlignment.CENTER;

    private VerticalAlignment verticalAlignment = VerticalAlignment.CENTER;

    private short fontColor = IndexedColors.AUTOMATIC.getIndex();

    private short background = IndexedColors.AUTOMATIC.getIndex();

    private Border leftBorder = Border.defaultBorder();

    private Border rightBorder = Border.defaultBorder();

    private Border topBorder = Border.defaultBorder();

    private Border bottomBorder = Border.defaultBorder();

    private Header() {}

    public static HeaderBuilder builder() {
        return new HeaderBuilder();
    }

    public static class HeaderBuilder {

        private Header header;

        private HeaderBuilder() {
            this.header = new Header();
        }

        public Header build() {
            return this.header;
        }

        public HeaderBuilder titles(String... titles) {
            this.header.setTitles(titles);
            return this;
        }

        public HeaderBuilder height(short height) {
            this.header.setHeight(height);
            return this;
        }

        public HeaderBuilder fontHeight(short fontHeight) {
            this.header.setFontHeight(fontHeight);
            return this;
        }

        public HeaderBuilder fontName(String fontName) {
            this.header.setFontName(fontName);
            return this;
        }

        public HeaderBuilder bold(boolean bold) {
            this.header.setBold(bold);
            return this;
        }

        public HeaderBuilder italic(boolean italic) {
            this.header.setItalic(italic);
            return this;
        }

        public HeaderBuilder textWrap(boolean textWrap) {
            this.header.setTextWrap(textWrap);
            return this;
        }

        public HeaderBuilder horizontalAlignment(HorizontalAlignment horizontalAlignment) {
            this.header.setHorizontalAlignment(horizontalAlignment);
            return this;
        }

        public HeaderBuilder verticalAlignment(VerticalAlignment verticalAlignment) {
            this.header.setVerticalAlignment(verticalAlignment);
            return this;
        }

        public HeaderBuilder fontColor(short fontColor) {
            this.header.setFontColor(fontColor);
            return this;
        }

        public HeaderBuilder background(short background) {
            this.header.setBackground(background);
            return this;
        }

        public HeaderBuilder leftBorder(Border leftBorder) {
            this.header.setLeftBorder(leftBorder);
            return this;
        }

        public HeaderBuilder rightBorder(Border rightBorder) {
            this.header.setRightBorder(rightBorder);
            return this;
        }

        public HeaderBuilder topBorder(Border topBorder) {
            this.header.setTopBorder(topBorder);
            return this;
        }

        public HeaderBuilder bottomBorder(Border bottomBorder) {
            this.header.setBottomBorder(bottomBorder);
            return this;
        }

    }

}
