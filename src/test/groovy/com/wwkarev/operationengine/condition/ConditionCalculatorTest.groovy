package com.wwkarev.operationengine.condition

import com.wwkarev.operationengine.condition.*
import spock.lang.Specification

class ConditionCalculatorTest extends Specification {

    def "test ConditionCalculator"() {
        when:
        String conditionUUID_1 = UUID.randomUUID().toString()
        String conditionUUID_2 = UUID.randomUUID().toString()
        String conditionUUID_3 = UUID.randomUUID().toString()

        Condition condition1 = new TrueCondition(conditionUUID_1)
        Condition condition2 = new TrueCondition(conditionUUID_2)
        Condition condition3 = new FalseCondition(conditionUUID_3)

        Condition resultCondition = condition1 & ~(condition2 | condition3)

        ConditionCalculator conditionCalculator = new ConditionCalculator(resultCondition)
        Map<String, Boolean> results = conditionCalculator.calculate()
        then:

        Condition andCondition = resultCondition
        assert andCondition instanceof AndCondition
        assert results[andCondition.getId()] == false

        Condition resultCondition1 = andCondition.getLeftCondition()
        assert resultCondition1.getId() == condition1.getId()
        assert results[resultCondition1.getId()] == true


        Condition notCondition = andCondition.getRightCondition()
        assert notCondition instanceof NotCondition
        assert results[notCondition.getId()] == false

        Condition orCondition = notCondition.getCondition()
        assert orCondition instanceof OrCondition
        assert results[orCondition.getId()] == true

        Condition resultCondition2 = orCondition.getLeftCondition()
        assert resultCondition2.getId() == condition2.getId()
        assert results[resultCondition2.getId()] == true

        Condition resultCondition3 = orCondition.getRightCondition()
        assert resultCondition3.getId() == condition3.getId()
        assert results[resultCondition3.getId()] == false
    }
}
