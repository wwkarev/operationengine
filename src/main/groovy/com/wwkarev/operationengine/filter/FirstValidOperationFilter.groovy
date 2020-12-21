package com.wwkarev.operationengine.filter

import com.wwkarev.operationengine.Operation
import com.wwkarev.operationengine.action.Action

/**
 * OperationFilter filters list of operations. Return first Operations with valid Condition.
 * @param <T>
 * @author Vitalii Karev (wwkarev)
 */
final class FirstValidOperationFilter<T> implements OperationFilter<T> {
    private List<Operation<T>> operations

    FirstValidOperationFilter(List<Operation<T>> operations) {
        this.operations = operations
    }

    @Override
    List<Operation<T>> filter() {
        Operation<T> foundOperation = operations.find{operation -> operation.isValid()}
        if (foundOperation == null) {
            throw new OperationFilter.OperationNotFound()
        }
        return [foundOperation]
    }
}
