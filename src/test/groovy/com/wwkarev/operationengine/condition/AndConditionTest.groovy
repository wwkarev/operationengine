package com.wwkarev.operationengine.condition

import spock.lang.Specification

class AndConditionTest extends Specification {
    def "test AndCondition"() {
        expect:
        assert condition.isValid() == result
        where:
        condition | result
        new AndCondition(UUID.randomUUID().toString(), new TrueCondition(UUID.randomUUID().toString()), new TrueCondition(UUID.randomUUID().toString())) | true
        new AndCondition(UUID.randomUUID().toString(), new TrueCondition(UUID.randomUUID().toString()), new FalseCondition(UUID.randomUUID().toString())) | false
        new AndCondition(UUID.randomUUID().toString(), new FalseCondition(UUID.randomUUID().toString()), new TrueCondition(UUID.randomUUID().toString())) | false
        new AndCondition(UUID.randomUUID().toString(), new FalseCondition(UUID.randomUUID().toString()), new FalseCondition(UUID.randomUUID().toString())) | false
    }
}
