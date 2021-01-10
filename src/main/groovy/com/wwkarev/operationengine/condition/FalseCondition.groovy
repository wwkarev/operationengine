package com.wwkarev.operationengine.condition

/**
 * FALSE condition class
 * @author Vitalii Karev (wwkarev)
 */
class FalseCondition extends Condition {
    FalseCondition(String id) {
        super(id)
    }

    @Override
    Boolean isValid() {
        return false
    }
}
