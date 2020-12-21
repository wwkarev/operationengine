package com.wwkarev.operationengine.condition

/**
 * Condition class
 * @author Vitalii Karev (wwkarev)
 */
interface Condition {
    abstract String getId()
    abstract Boolean isValid()
}
