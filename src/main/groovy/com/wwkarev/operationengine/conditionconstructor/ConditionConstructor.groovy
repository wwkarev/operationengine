package com.wwkarev.operationengine.conditionconstructor

import com.wwkarev.operationengine.condition.Condition
import groovy.transform.InheritConstructors

/**
 * Constructs condition object
 * @param <T>
 * @author Vitalii Karev (wwkarev)
 */
interface ConditionConstructor {
    abstract Condition construct()

    @InheritConstructors
    static class ConstructionException extends Exception {
    }

    @InheritConstructors
    static class ConditionNotFoundException extends Exception {
    }
}
