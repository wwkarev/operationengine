package com.wwkarev.operationengine.condition


import spock.lang.Specification

class MemorizingConditionTest extends Specification {
    static class CounterCondition extends Condition {
        Integer count = 0
        CounterCondition(String id) {
            super(id)
        }

        @Override
        Boolean isValid() {
            count++
            return true
        }
    }

    def "test MemorizingConditionTest"() {
        when:
        Condition memorizingCondition = new MemorizingCondition(new CounterCondition(UUID.randomUUID().toString()))
        memorizingCondition.isValid()
        memorizingCondition.isValid()

        then:
        assert ((CounterCondition)memorizingCondition.getCondition()).count == 1
    }

    def "test not MemorizingConditionTest"() {
        when:
        Condition condition = new CounterCondition(UUID.randomUUID().toString())
        condition.isValid()
        condition.isValid()

        then:
        assert ((CounterCondition)condition).count == 2
    }
}
