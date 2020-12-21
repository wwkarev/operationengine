package com.wwkarev.operationengine.reducer

import com.wwkarev.operationengine.Operation

/**
 * Reducer receives list of Operations and performs target task.
 * @param <T>
 * @author Vitalii Karev (wwkarev)
 */
interface Reducer<T> {
    abstract void reduce(List<Operation<T>> operations)
}
