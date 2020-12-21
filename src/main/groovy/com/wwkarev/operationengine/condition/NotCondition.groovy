package com.wwkarev.operationengine.condition

/**
 * NOT condition class
 * @author Vitalii Karev (wwkarev)
 */
class NotCondition implements Condition{
    private String id
    private Condition condition

    NotCondition(String id, Condition condition) {
        this.id = id
        this.condition = condition
    }

    @Override
    String getId() {
        return id
    }

    @Override
    Boolean isValid() {
        return !condition.isValid()
    }

    Condition getCondition() {
        return condition
    }
}
