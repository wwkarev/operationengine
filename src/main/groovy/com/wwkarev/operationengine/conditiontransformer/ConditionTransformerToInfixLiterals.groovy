package com.wwkarev.operationengine.conditiontransformer

import com.wwkarev.operationengine.condition.AndCondition
import com.wwkarev.operationengine.condition.Condition
import com.wwkarev.operationengine.condition.NotCondition
import com.wwkarev.operationengine.condition.OrCondition
import com.wwkarev.operationengine.literal.Literal
import com.wwkarev.operationengine.literal.LiteralNotationTransformer
import com.wwkarev.operationengine.literal.LiteralType

/**
 * Transforms condition to literal list in infix notation
 * @param <T>
 * @author Vitalii Karev (wwkarev)
 */
class ConditionTransformerToInfixLiterals extends ConditionTransformerToPrefixLiterals {
    ConditionTransformerToInfixLiterals(Condition condition) {
        super(condition)
    }

    @Override
    List<Literal> transform() {
        return new LiteralNotationTransformer().prefixToInfix(super.transform())
    }
}
