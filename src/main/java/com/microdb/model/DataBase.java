package com.microdb.model;

import com.microdb.annotation.VisibleForTest;
import com.microdb.bufferpool.BufferPool;
import com.microdb.config.DBConfig;
import com.microdb.config.PropertiesConfigParser;
import com.microdb.exception.DbException;
import com.microdb.logging.RedoLogFile;
import com.microdb.logging.UndoLogFile;
import com.microdb.model.table.DbTable;
import com.microdb.model.table.TableDesc;
import com.microdb.model.table.tablefile.TableFile;
import com.microdb.transaction.LockManager;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 数据库对象
 *
 */
public class DataBase {
    private static final AtomicReference<DataBase> singleton = new AtomicReference<>(new DataBase());

    /**
     * 配置
     */
    private final DBConfig dbConfig;

    /**
     * dbTableFile id to DbTable
     */
    private final HashMap<Integer, DbTable> tableId2Table;
    private final HashMap<String, DbTable> tableName2Table;

    /**
     * 缓冲池
     */
    private final BufferPool bufferPool;

    /**
     * 锁管理器
     */
    private final LockManager lockManager;

    /**
     * undo 日志
     */
    private final UndoLogFile undoLogFile;

    /**
     * redo 日志
     */
    private final RedoLogFile redoLogFile;

    private DataBase() {
        this.dbConfig = new PropertiesConfigParser().parse("config.properties");
        this.tableId2Table = new HashMap<>();
        this.tableName2Table = new HashMap<>();
        this.lockManager = new LockManager();
        this.bufferPool = new BufferPool(dbConfig);
        this.undoLogFile = new UndoLogFile(new File("undo"));
        this.redoLogFile = new RedoLogFile(new File("redo"));
    }

    public static DataBase getInstance() {
        return singleton.get();
    }

    public static DBConfig getDBConfig() {
        return singleton.get().dbConfig;
    }

    public static BufferPool getBufferPool() {
        return singleton.get().bufferPool;
    }

    public static LockManager getLockManager() {
        return singleton.get().lockManager;
    }

    public static UndoLogFile getUndoLogFile() {
        return singleton.get().undoLogFile;
    }

    public static RedoLogFile getRedoLogFile() {
        return singleton.get().redoLogFile;
    }

    /**
     * 添加表
     *
     * @param tableFile table file
     * @param tableName table name
     */
    @Deprecated
    public void addTable(TableFile tableFile, String tableName, TableDesc tableDesc) {
        DbTable dbTable = new DbTable(tableName, tableFile, tableDesc);
        tableId2Table.put(tableFile.getTableId(), dbTable);
        tableName2Table.put(tableName, dbTable);
    }

    /**
     * 添加表
     *
     * @param tableFile table file
     * @param tableName table name
     */
    public void addTable(TableFile tableFile, String tableName) {
        TableDesc tableDesc = tableFile.getTableDesc();
        if (tableDesc == null) {
            throw new DbException("table file's table desc must not be null");
        }

        DbTable dbTable = new DbTable(tableName, tableFile, tableDesc);
        tableId2Table.put(tableFile.getTableId(), dbTable);
        tableName2Table.put(tableName, dbTable);
    }

    public DbTable getDbTableById(int tableId) {
        DbTable dbTable = tableId2Table.get(tableId);
        if (dbTable == null) {
            throw new DbException("table not exist");
        }
        return dbTable;
    }

    public DbTable getDbTableByName(String name) {
        DbTable dbTable = tableName2Table.get(name);
        if (dbTable == null) {
            throw new DbException("table not exist");
        }
        return dbTable;
    }


    @VisibleForTest
    public static void resetWithFile(TableFile tableFile, String tableName) {
        DataBase dataBase = new DataBase();
        dataBase.addTable(tableFile, tableName);
        singleton.set(dataBase);
    }

    @VisibleForTest
    public static void reset() {
        DataBase dataBase = new DataBase();
        singleton.set(dataBase);
    }
}