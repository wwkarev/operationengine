package com.wwkarev.operationengine

import com.wwkarev.operationengine.action.Action
import com.wwkarev.operationengine.condition.Condition

/**
 * Operation contains Condition object and Action object.
 * @param <T>
 * @author Vitalii Karev (wwkarev)
 */
final class Operation<T> {
    private String id
    private Condition condition
    private Action<T> action

    Operation(String id, Condition condition, Action<T> action) {
        this.id = id
        this.condition = condition
        this.action = action
    }

    Operation(Condition condition, Action<T> action) {
        this.id = UUID.randomUUID().toString()
        this.condition = condition
        this.action = action
    }

    String getId() {
        return id
    }

    Condition getCondition() {
        return condition
    }

    Action<T> getAction() {
        return action
    }

    Boolean isValid() {
        return condition.isValid()
    }
}
