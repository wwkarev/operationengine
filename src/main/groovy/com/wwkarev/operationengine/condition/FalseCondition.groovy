package com.wwkarev.operationengine.condition

/**
 * FALSE condition class
 * @author Vitalii Karev (wwkarev)
 */
class FalseCondition implements Condition {
    private String id

    FalseCondition(String id) {
        this.id = id
    }

    @Override
    String getId() {
        return id
    }

    @Override
    Boolean isValid() {
        return false
    }
}
