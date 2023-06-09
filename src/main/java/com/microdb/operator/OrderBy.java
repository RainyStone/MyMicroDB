package com.microdb.operator;

import com.microdb.exception.DbException;
import com.microdb.model.field.Field;
import com.microdb.model.row.Row;
import com.microdb.model.table.TableDesc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * orderBy 操作符
 *
 */
public class OrderBy extends Operator {

    /**
     * 原始行数据的迭代器
     */
    private final IOperatorIterator tableIterator;

    /**
     * 排序的字段
     */
    private final int orderByFieldIndex;

    /**
     * 排序方式
     */
    private final boolean asc;

    /**
     * 排序后的行数据
     */
    private final List<Row> sortedRows = new ArrayList<>();

    /**
     * 排序后的行数据迭代器
     */
    private Iterator<Row> sortedRowIterator;

    public OrderBy(IOperatorIterator tableIterator, int orderByFieldIndex, boolean asc) {
        this.tableIterator = tableIterator;
        this.orderByFieldIndex = orderByFieldIndex;
        this.asc = asc;
    }


    @Override
    public void open() throws DbException {
        tableIterator.open();
        while (tableIterator.hasNext()) {
            sortedRows.add(tableIterator.next());
        }

        sortedRows.sort(new RowComparator(orderByFieldIndex, asc));
        this.sortedRowIterator = sortedRows.iterator();
        super.open();
    }

    @Override
    public void close() {
        super.close();
        sortedRowIterator = null;
    }

    @Override
    public TableDesc getTableDesc() {
        return tableIterator.getTableDesc();
    }


    @Override
    public Row fetchNextMatched() {
        if (sortedRowIterator != null && sortedRowIterator.hasNext()) {
            return sortedRowIterator.next();
        } else {
            return null;
        }
    }

    // ============================ 比较器 ==================================
    public static class RowComparator implements Comparator<Row> {
        int fieldIndex;
        boolean asc;

        public RowComparator(int fieldIndex, boolean asc) {
            this.fieldIndex = fieldIndex;
            this.asc = asc;
        }

        public int compare(Row o1, Row o2) {
            Field t1 = (o1).getField(fieldIndex);
            Field t2 = (o2).getField(fieldIndex);
            if (t1.compare(PredicateEnum.EQUALS, t2))
                return 0;
            if (t1.compare(PredicateEnum.GREATER_THAN, t2))
                return asc ? 1 : -1;
            else
                return asc ? -1 : 1;
        }

    }
}
