package org.lyman.utils.excel.write;

import com.google.common.collect.RangeMap;
import lombok.Data;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.lyman.utils.excel.write.elements.Column;
import org.lyman.utils.excel.write.elements.Header;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Data
public class SheetProperties {

    private String name;

    private List<Column> columns;

    private List<Header> headers;

    private List<CellRangeAddress> mergedRegions;

    private int frozenLeft;

    private int frozenTop;

    private Iterator<?> data;

    private ConditionalFormatter conditionalFormatter;

    private Map<String, RangeMap<Integer, Short>> indentionMap;

    public interface ConditionalFormatter {

        void addConditionalFormatter(Sheet sheet);

    }

}
