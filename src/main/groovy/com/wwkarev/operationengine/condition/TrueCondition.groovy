package com.wwkarev.operationengine.condition

/**
 * TRUE condition class
 * @author Vitalii Karev (wwkarev)
 */
class TrueCondition implements Condition {
    private String id

    TrueCondition(String id) {
        this.id = id
    }

    @Override
    String getId() {
        return id
    }

    @Override
    Boolean isValid() {
        return true
    }
}
