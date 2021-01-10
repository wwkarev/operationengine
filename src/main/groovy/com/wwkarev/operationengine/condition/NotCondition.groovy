package com.wwkarev.operationengine.condition

/**
 * NOT condition class
 * @author Vitalii Karev (wwkarev)
 */
class NotCondition extends Condition {
    private Condition condition

    NotCondition(String id, Condition condition) {
        super(id)
        this.condition = condition
    }

    @Override
    Boolean isValid() {
        return !condition.isValid()
    }

    Condition getCondition() {
        return condition
    }
}
