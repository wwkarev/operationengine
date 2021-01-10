package com.wwkarev.operationengine.condition

/**
 * OR condition class
 * @author Vitalii Karev (wwkarev)
 */
class OrCondition extends Condition {
    private Condition leftCondition
    private Condition rightCondition

    OrCondition(String id, Condition leftCondition, Condition rightCondition) {
        super(id)
        this.leftCondition = leftCondition
        this.rightCondition = rightCondition
    }

    @Override
    Boolean isValid() {
        return leftCondition.isValid() || rightCondition.isValid()
    }

    def getLeftCondition() {
        return leftCondition
    }

    def getRightCondition() {
        return rightCondition
    }
}
