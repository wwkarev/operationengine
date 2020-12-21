package com.wwkarev.operationengine.condition

import com.wwkarev.operationengine.condition.AndCondition
import com.wwkarev.operationengine.condition.Condition
import com.wwkarev.operationengine.condition.MemorizingCondition
import com.wwkarev.operationengine.condition.NotCondition
import com.wwkarev.operationengine.condition.OrCondition

/**
 * Class for transformation condition to memorizing condition
 * @author Vitalii Karev (wwkarev)
 */
class MemorizingConditionTransformer {
    private Condition condition

    MemorizingConditionTransformer(Condition condition) {
        this.condition = condition
    }

    /**
     * Recursively transforms every condition
     * @return Memorizing condition
     */
    Condition transform() {
        return recursiveTransform(condition)
    }

    private recursiveTransform(Condition condition) {
        if (condition instanceof AndCondition) {
            return getConditionByAndCondition(condition)
        } else if (condition instanceof OrCondition) {
            return getConditionByOrCondition(condition)
        } else if (condition instanceof NotCondition) {
            return getConditionByNotCondition(condition)
        } else {
            return getConditionByParamCondition(condition)
        }
    }

    private Condition getConditionByAndCondition(Condition condition) {
        Condition leftCondition = ((AndCondition)condition).getLeftCondition()
        Condition rightCondition = ((AndCondition)condition).getRightCondition()
        return new MemorizingCondition(
                new AndCondition(
                        condition.getId(), recursiveTransform(leftCondition), recursiveTransform(rightCondition)
                )
        )
    }

    private Condition getConditionByOrCondition(Condition condition) {
        Condition leftCondition = ((OrCondition)condition).getLeftCondition()
        Condition rightCondition = ((OrCondition)condition).getRightCondition()
        return new MemorizingCondition(
                new OrCondition(
                        condition.getId(), recursiveTransform(leftCondition), recursiveTransform(rightCondition)
                )
        )
    }

    private Condition getConditionByNotCondition(Condition condition) {
        Condition notCondition = ((NotCondition)condition).getCondition()
        return new MemorizingCondition(
                new NotCondition(
                        condition.getId(), recursiveTransform(notCondition)
                )
        )
    }

    private Condition getConditionByParamCondition(Condition condition) {
        return new MemorizingCondition(condition)
    }
}
