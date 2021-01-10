package com.wwkarev.operationengine.conditionconstructor


import com.wwkarev.operationengine.condition.Condition
import com.wwkarev.operationengine.literal.Literal
import com.wwkarev.operationengine.literal.LiteralNotationTransformer

/**
 * Constructs condition object by literal list in infix notation
 * @param <T>
 * @author Vitalii Karev (wwkarev)
 */
class ConditionConstructorFromInfixLiterals extends ConditionConstructorFromPrefixLiterals {
    ConditionConstructorFromInfixLiterals(Map<String, Condition> conditions, List<Literal> infixNotationLiterals) {
        super(conditions, new LiteralNotationTransformer().infixToPrefix(infixNotationLiterals))
    }
}
