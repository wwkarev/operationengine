package com.wwkarev.operationengine.filter

import com.wwkarev.operationengine.Operation
import groovy.transform.InheritConstructors

/**
 * OperationFilter filters list of operations.
 * @param <T>
 * @author Vitalii Karev (wwkarev)
 */
interface OperationFilter<T> {
    abstract List<Operation<T>> filter()

    @InheritConstructors
    static class OperationNotFound extends Exception {}
}
