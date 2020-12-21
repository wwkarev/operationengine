package com.wwkarev.operationengine.condition

/**
 * Condition class with memorizing result. Сondition is evaluated only once.
 * @author Vitalii Karev (wwkarev)
 */
class MemorizingCondition implements Condition {
    private Condition condition
    private Boolean isValidResult

    MemorizingCondition(Condition condition) {
        this.condition = condition
    }

    @Override
    String getId() {
        return condition.getId()
    }

    @Override
    Boolean isValid() {
        if (isValidResult == null) {
            isValidResult = condition.isValid()
        }
        return isValidResult
    }

    Condition getCondition() {
        return condition
    }
}
