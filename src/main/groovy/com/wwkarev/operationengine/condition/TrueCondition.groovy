package com.wwkarev.operationengine.condition

/**
 * TRUE condition class
 * @author Vitalii Karev (wwkarev)
 */
class TrueCondition extends Condition {
    TrueCondition(String id) {
        super(id)
    }

    @Override
    Boolean isValid() {
        return true
    }
}
