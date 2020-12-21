package com.wwkarev.operationengine.condition

import spock.lang.Specification

class OrConditionTest extends Specification {
    def "test OrCondition"() {
        expect:
        assert condition.isValid() == result
        where:
        condition | result
        new OrCondition(UUID.randomUUID().toString(), new TrueCondition(UUID.randomUUID().toString()), new TrueCondition(UUID.randomUUID().toString())) | true
        new OrCondition(UUID.randomUUID().toString(), new TrueCondition(UUID.randomUUID().toString()), new FalseCondition(UUID.randomUUID().toString())) | true
        new OrCondition(UUID.randomUUID().toString(), new FalseCondition(UUID.randomUUID().toString()), new TrueCondition(UUID.randomUUID().toString())) | true
        new OrCondition(UUID.randomUUID().toString(), new FalseCondition(UUID.randomUUID().toString()), new FalseCondition(UUID.randomUUID().toString())) | false
    }
}
