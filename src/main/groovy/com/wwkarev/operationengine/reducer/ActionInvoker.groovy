package com.wwkarev.operationengine.reducer

import com.wwkarev.operationengine.Operation

/**
 * This reducer simply invokes all operation's actions.
 * @param <T>
 * @author Vitalii Karev (wwkarev)
 */
final class ActionInvoker<T> implements Reducer<T> {
    @Override
    void reduce(List<Operation<T>> operations) {
        operations.each{Operation<T> operation ->
            operation.getAction().invoke()
        }
    }
}
