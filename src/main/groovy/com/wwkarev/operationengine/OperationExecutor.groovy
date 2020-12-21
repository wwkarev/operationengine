package com.wwkarev.operationengine

import com.wwkarev.operationengine.filter.OperationFilter
import com.wwkarev.operationengine.reducer.Reducer

/**
 * Filters operation list and pass it to reducer.
 * @param <T>
 * @author Vitalii Karev (wwkarev)
 */
final class OperationExecutor<T> {
    private Reducer<T> reducer
    private OperationFilter<T> operationFilter

    OperationExecutor(Reducer<T> reducer, OperationFilter<T> operationFilter) {
        this.reducer = reducer
        this.operationFilter = operationFilter
    }

    void execute() {
        List<Operation<T>> operations = operationFilter.filter()
        reducer.reduce(operations)
    }
}
