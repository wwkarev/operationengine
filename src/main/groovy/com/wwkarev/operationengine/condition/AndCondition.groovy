package com.wwkarev.operationengine.condition

/**
 * AND condition class
 * @author Vitalii Karev (wwkarev)
 */
class AndCondition implements Condition {
    private String id
    private Condition leftCondition
    private Condition rightCondition

    AndCondition(String id, Condition leftCondition, Condition rightCondition) {
        this.id = id
        this.leftCondition = leftCondition
        this.rightCondition = rightCondition
    }

    @Override
    String getId() {
        return id
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
