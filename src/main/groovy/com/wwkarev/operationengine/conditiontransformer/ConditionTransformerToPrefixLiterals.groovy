package com.wwkarev.operationengine.conditiontransformer

import com.wwkarev.operationengine.condition.AndCondition
import com.wwkarev.operationengine.condition.Condition
import com.wwkarev.operationengine.condition.NotCondition
import com.wwkarev.operationengine.condition.OrCondition
import com.wwkarev.operationengine.literal.Literal
import com.wwkarev.operationengine.literal.LiteralType

/**
 * Transforms condition to literal list in prefix notation
 * @param <T>
 * @author Vitalii Karev (wwkarev)
 */
class ConditionTransformerToPrefixLiterals implements ConditionTransformer {
    protected Condition condition

    ConditionTransformerToPrefixLiterals(Condition condition) {
        this.condition = condition
    }

    @Override
    List<Literal> transform() {
        return recursiveTransform(condition)
    }

    private List<Literal> recursiveTransform(Condition condition) {
        if (condition instanceof AndCondition) {
            return getLiteralListByAndCondition(condition)
        } else if (condition instanceof OrCondition) {
            return getLiteralListByOrCondition(condition)
        } else if (condition instanceof NotCondition) {
            return getLiteralListByNotCondition(condition)
        } else {
            return getLiteralListByParamCondition(condition)
        }
    }

    private List<Literal> getLiteralListByAndCondition(Condition condition) {
        List<Literal> literals = []
        Condition leftCondition = ((AndCondition)condition).getLeftCondition()
        Condition rightCondition = ((AndCondition)condition).getRightCondition()

        literals.add(new Literal(condition.getId(), LiteralType.AND))
        literals += recursiveTransform(leftCondition)
        literals += recursiveTransform(rightCondition)
        return literals
    }

    private List<Literal> getLiteralListByOrCondition(Condition condition) {
        List<Literal> literals = []
        Condition leftCondition = ((OrCondition)condition).getLeftCondition()
        Condition rightCondition = ((OrCondition)condition).getRightCondition()

        literals.add(new Literal(condition.getId(), LiteralType.OR))
        literals += recursiveTransform(leftCondition)
        literals += recursiveTransform(rightCondition)
        return literals
    }

    private List<Literal> getLiteralListByNotCondition(Condition condition) {
        List<Literal> literals = []
        Condition notCondition = ((NotCondition)condition).getCondition()
        literals.add(new Literal(condition.getId(), LiteralType.NOT))
        literals += recursiveTransform(notCondition)
        return literals
    }

    private List<Literal> getLiteralListByParamCondition(Condition condition) {
        List<Literal> literals = []
        literals.add(new Literal(condition.getId(), LiteralType.PARAM))
        return literals
    }
}
