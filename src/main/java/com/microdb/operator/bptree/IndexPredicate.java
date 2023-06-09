package com.microdb.operator.bptree;

import com.microdb.model.field.Field;
import com.microdb.operator.PredicateEnum;

/**
 * 用于B+Tree 索引检索的条件
 *
 */
public class IndexPredicate {

    /**
     * 条件
     */
    private PredicateEnum predicateEnum;

    /**
     * 条件参数
     */
    private Field paramOperand;

    public IndexPredicate(PredicateEnum predicate, Field paramOperand) {
        this.predicateEnum = predicate;
        this.paramOperand = paramOperand;
    }

    public PredicateEnum getPredicate() {
        return predicateEnum;
    }

    public Field getParamOperand() {
        return paramOperand;
    }
}
