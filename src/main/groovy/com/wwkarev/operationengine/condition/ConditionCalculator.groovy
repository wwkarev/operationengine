package com.wwkarev.operationengine.condition

/**
 * Class for calculation every condition in logical expression
 * @author Vitalii Karev (wwkarev)
 */
class ConditionCalculator {
    private Condition condition

    ConditionCalculator(Condition condition) {
        this.condition = condition
    }

    /**
     * Rcursively calculates every condition
     * @return Map - key: id of condition, value: result of condition
     * @author Vitalii Karev (wwkarev)
     */
    Map<String, Boolean> calculate() {
        condition.isValid()
        Map<String, Boolean> result = [:]
        recursiveCalculate(condition, result)
        return result
    }

    private void recursiveCalculate(Condition condition, Map<String, Boolean> result) {
        if (condition instanceof AndCondition) {
            calculateByAndCondition(condition, result)
        } else if (condition instanceof OrCondition) {
            calculateByOrCondition(condition, result)
        } else if (condition instanceof NotCondition) {
            calculateByNotCondition(condition, result)
        } else {
            calculateByParamCondition(condition, result)
        }
    }

    private void calculateByAndCondition(Condition condition, Map<String, Boolean> result) {
        result[condition.getId()] = condition.isValid()
        Condition leftCondition = ((AndCondition)condition).getLeftCondition()
        Condition rightCondition = ((AndCondition)condition).getRightCondition()
        recursiveCalculate(leftCondition, result)
        recursiveCalculate(rightCondition, result)
    }

    private void calculateByOrCondition(Condition condition, Map<String, Boolean> result) {
        result[condition.getId()] = condition.isValid()
        Condition leftCondition = ((OrCondition)condition).getLeftCondition()
        Condition rightCondition = ((OrCondition)condition).getRightCondition()
        recursiveCalculate(leftCondition, result)
        recursiveCalculate(rightCondition, result)
    }

    private void calculateByNotCondition(Condition condition, Map<String, Boolean> result) {
        result[condition.getId()] = condition.isValid()
        Condition notCondition = ((NotCondition)condition).getCondition()
        recursiveCalculate(notCondition, result)
    }

    private void calculateByParamCondition(Condition condition, Map<String, Boolean> result) {
        result[condition.getId()] = condition.isValid()
    }
}
