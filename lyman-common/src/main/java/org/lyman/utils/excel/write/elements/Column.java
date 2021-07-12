package org.lyman.utils.excel.write.elements;

import lombok.Data;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.lyman.enums.WhiteSpaceType;
import org.lyman.utils.StringUtils;
import org.lyman.utils.excel.write.CustomCellStyle;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Column {

    private String key;

    private int width = 20;

    private boolean isFormula;

    private int formulaRowNumOffset;

    private CustomCellStyle cellType = CustomCellStyle.GENERIAL;

    private WhiteSpaceType whiteSpaceType = WhiteSpaceType.BAR;

    private HorizontalAlignment horizontalAlignment = HorizontalAlignment.CENTER;

    private VerticalAlignment verticalAlignment = VerticalAlignment.CENTER;

    private String fontName = "Calibri";

    private short fontHeight = 10;

    private boolean bold;

    private boolean italic;

    private boolean textWrap = true;

    private short fontColor = IndexedColors.AUTOMATIC.getIndex();

    private short background = IndexedColors.AUTOMATIC.getIndex();

    private Border leftBorder;

    private Border rightBorder;

    private Border topBorder;

    private Border bottomBorder;

    private Column() {}

    public static ColumnBuilder builder() {
        return new ColumnBuilder();
    }

    public static class ColumnBuilder {

        private Column column;

        private ColumnBuilder() {
            this.column = new Column();
        }

        public Column build() {
            if (StringUtils.isEmpty(this.column.key))
                throw new RuntimeException("Column's Key can't be null.");
            return this.column;
        }

        public ColumnBuilder key(@NotNull String key) {
            this.column.setKey(key);
            return this;
        }

        public ColumnBuilder width(int width) {
            if (width > 0)
                this.column.setWidth(width);
            return this;
        }

        public ColumnBuilder cellType(@NotNull CustomCellStyle cellType) {
            this.column.setCellType(cellType);
            return this;
        }

        public ColumnBuilder isFormula(boolean isFormula) {
            this.column.setFormula(isFormula);
            return this;
        }

        public ColumnBuilder formulaRowNumOffset(int formulaRowNumOffset) {
            this.column.setFormulaRowNumOffset(formulaRowNumOffset);
            return this;
        }

        public ColumnBuilder whiteSpaceType(@NotNull WhiteSpaceType whiteSpaceType) {
            this.column.setWhiteSpaceType(whiteSpaceType);
            return this;
        }

        public ColumnBuilder horizontalAlignment(@NotNull HorizontalAlignment horizontalAlignment) {
            this.column.setHorizontalAlignment(horizontalAlignment);
            return this;
        }

        public ColumnBuilder verticalAlignment(@NotNull VerticalAlignment verticalAlignment) {
            this.column.setVerticalAlignment(verticalAlignment);
            return this;
        }

        public ColumnBuilder fontName(@NotEmpty String fontName) {
            this.column.setFontName(fontName);
            return this;
        }

        public ColumnBuilder fontHeight(short fontHeight) {
            if (fontHeight > 0)
                this.column.setFontHeight(fontHeight);
            return this;
        }

        public ColumnBuilder bold(boolean bold) {
            this.column.setBold(bold);
            return this;
        }

        public ColumnBuilder italic(boolean italic) {
            this.column.setItalic(italic);
            return this;
        }

        public ColumnBuilder textWrap(boolean textWrap) {
            this.column.setTextWrap(textWrap);
            return this;
        }

        public ColumnBuilder fontColor(short fontColor) {
            this.column.setFontColor(fontColor);
            return this;
        }

        public ColumnBuilder background(short background) {
            this.column.setBackground(background);
            return this;
        }

        public ColumnBuilder leftBorder(Border leftBorder) {
            this.column.setLeftBorder(leftBorder);
            return this;
        }

        public ColumnBuilder rightBorder(Border rightBorder) {
            this.column.setRightBorder(rightBorder);
            return this;
        }

        public ColumnBuilder topBorder(Border topBorder) {
            this.column.setTopBorder(topBorder);
            return this;
        }

        public ColumnBuilder bottomBorder(Border bottomBorder) {
            this.column.setBottomBorder(bottomBorder);
            return this;
        }

    }

}
