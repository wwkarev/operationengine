package com.wwkarev.operationengine.condition

/**
 * AND condition class
 * @author Vitalii Karev (wwkarev)
 */
class AndCondition extends Condition {
    private Condition leftCondition
    private Condition rightCondition

    AndCondition(String id, Condition leftCondition, Condition rightCondition) {
        super(id)
        this.leftCondition = leftCondition
        this.rightCondition = rightCondition
    }

    @Override
    Boolean isValid() {
        return leftCondition.isValid() && rightCondition.isValid()
    }

    def getLeftCondition() {
        return leftCondition
    }

    def getRightCondition() {
        return rightCondition
    }
}
