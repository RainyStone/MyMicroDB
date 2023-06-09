package com.microdb.operator;

import com.microdb.exception.DbException;
import com.microdb.model.DataBase;
import com.microdb.model.row.Row;
import com.microdb.model.table.TableDesc;
import com.microdb.model.table.tablefile.ITableFileIterator;
import com.microdb.model.table.tablefile.TableFile;

/**
 * 表顺序扫描 sequence scan，基于迭代器
 *
 */
public class SeqScan extends Operator {

    private final TableFile tableFile;

    /**
     * 表中行数据的迭代
     */
    private final ITableFileIterator tableFileIterator;

    public SeqScan(int tableId) {
        this.tableFile = DataBase.getInstance().getDbTableById(tableId).getTableFile();
        this.tableFileIterator = this.tableFile.getIterator();
    }

    @Override
    public void open() throws DbException {
        tableFileIterator.open();
        super.open();
    }

    @Override
    public void close() {
        super.close();
        tableFileIterator.close();
    }

    @Override
    public TableDesc getTableDesc() {
        return tableFile.getTableDesc();
    }

    @Override
    protected Row fetchNextMatched() {
        if (tableFileIterator != null && tableFileIterator.hasNext()) {
            return tableFileIterator.next();
        }
        return null;
    }
}
