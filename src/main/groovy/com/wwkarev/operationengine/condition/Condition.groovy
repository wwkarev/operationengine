package com.wwkarev.operationengine.condition

/**
 * Condition class
 * @author Vitalii Karev (wwkarev)
 */
abstract class Condition {
    protected String id

    Condition(String id) {
        this.id = id
    }

    abstract Boolean isValid()

    String getId() {
        return id
    }

    Condition and(Condition condition) {
        return new AndCondition(UUID.randomUUID().toString(), this, condition)
    }

    Condition or(Condition condition) {
        return new OrCondition(UUID.randomUUID().toString(), this, condition)
    }

    Condition bitwiseNegate() {
        return new NotCondition(UUID.randomUUID().toString(), this)
    }
}
