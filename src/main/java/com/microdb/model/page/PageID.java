package com.microdb.model.page;

/**
 * pageID用于封装页编号、引用tableId，便于在page中使用tableDesc
 *
 */
public interface PageID {

    int getTableId();

    int getPageNo();
}
