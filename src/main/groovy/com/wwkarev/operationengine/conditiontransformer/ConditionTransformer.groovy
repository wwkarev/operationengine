package com.wwkarev.operationengine.conditiontransformer

import com.wwkarev.operationengine.literal.Literal

/**
 * Transforms condition to literal list
 * @param <T>
 * @author Vitalii Karev (wwkarev)
 */
interface ConditionTransformer {
    abstract List<Literal> transform()
}
