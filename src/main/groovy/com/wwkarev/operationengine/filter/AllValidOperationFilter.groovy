package com.wwkarev.operationengine.filter

import com.wwkarev.operationengine.Operation
import com.wwkarev.operationengine.action.Action

/**
 * OperationFilter filters list of operations. Return all Operations with valid Condition.
 * @param <T>
 * @author Vitalii Karev (wwkarev)
 */
final class AllValidOperationFilter<T> implements OperationFilter<T> {
    private List<Operation<T>> operations

    AllValidOperationFilter(List<Operation<T>> operations) {
        this.operations = operations
    }

    @Override
    List<Operation<T>> filter() {
        List<Operation<T>> foundOperations = operations.findAll{ operation -> operation.isValid()}
        if (foundOperations.size() == 0) {
            throw new OperationFilter.OperationNotFound()
        }
        return foundOperations
    }
}
