package com.wwkarev.operationengine.action

/**
 * Action describes something that could happen in the application.
 * @param <T>
 * @author Vitalii Karev (wwkarev)
 */
interface Action<T> {
    abstract T invoke()
}
