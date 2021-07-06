package org.lyman.utils.excel.write.elements;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Border {

    private BorderStyle style;

    private short color;

    private Border() {
        style = BorderStyle.THIN;
        color = IndexedColors.AUTOMATIC.getIndex();
    }

    public static Border defaultBorder() {
        return new Border();
    }

    public static Border of(BorderStyle borderStyle, short color) {
        return new Border(borderStyle, color);
    }

}
