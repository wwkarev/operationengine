package com.wwkarev.operationengine.condition

import spock.lang.Specification

class NotConditionTest extends Specification {
    def "test NotCondition"() {
        expect:
        assert condition.isValid() == result
        where:
        condition | result
        new NotCondition(UUID.randomUUID().toString(), new TrueCondition(UUID.randomUUID().toString())) | false
        new NotCondition(UUID.randomUUID().toString(), new FalseCondition(UUID.randomUUID().toString())) | true
    }
}
