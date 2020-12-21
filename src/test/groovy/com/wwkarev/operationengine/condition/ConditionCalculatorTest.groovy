package com.wwkarev.operationengine.condition

import com.wwkarev.operationengine.condition.*
import com.wwkarev.operationengine.conditionconstructor.ConditionConstructorFromPrefixLiterals
import com.wwkarev.operationengine.literal.Literal
import com.wwkarev.operationengine.literal.LiteralType
import spock.lang.Specification

class ConditionCalculatorTest extends Specification {

    static class TrueCondition implements Condition {
        private String id

        TrueCondition(String id) {
            this.id = id
        }

        @Override
        String getId() {
            return id
        }

        @Override
        Boolean isValid() {
            return true
        }
    }
    static class FalseCondition implements Condition {
        private String id

        FalseCondition(String id) {
            this.id = id
        }

        @Override
        String getId() {
            return id
        }

        @Override
        Boolean isValid() {
            return false
        }
    }

    def "test ConditionCalculator"() {
        when:
        String andUUID = UUID.randomUUID().toString()
        String orUUID = UUID.randomUUID().toString()
        String notUUID = UUID.randomUUID().toString()
        String conditionUUID_1 = UUID.randomUUID().toString()
        String conditionUUID_2 = UUID.randomUUID().toString()
        String conditionUUID_3 = UUID.randomUUID().toString()

        Condition condition1 = new TrueCondition(conditionUUID_1)
        Condition condition2 = new FalseCondition(conditionUUID_2)
        Condition condition3 = new TrueCondition(conditionUUID_3)

        List<Literal> prefixLiterals = [
                new Literal(andUUID, LiteralType.AND),
                new Literal(notUUID, LiteralType.NOT),
                new Literal(orUUID, LiteralType.OR),
                new Literal(conditionUUID_1, LiteralType.PARAM),
                new Literal(conditionUUID_2, LiteralType.PARAM),
                new Literal(conditionUUID_3, LiteralType.PARAM)
        ]

        Map<String, Condition> conditions = [(conditionUUID_1): condition1, (conditionUUID_2): condition2, (conditionUUID_3): condition3]

        Condition resultCondition = new ConditionConstructorFromPrefixLiterals(conditions, prefixLiterals).construct()

        ConditionCalculator conditionCalculator = new ConditionCalculator(resultCondition)
        Map<String, Boolean> results = conditionCalculator.calculate()
        then:
        assert results[andUUID] == false
        assert results[notUUID] == false
        assert results[orUUID] == true
        assert results[conditionUUID_1] == true
        assert results[conditionUUID_2] == false
        assert results[conditionUUID_3] == true
    }
}
