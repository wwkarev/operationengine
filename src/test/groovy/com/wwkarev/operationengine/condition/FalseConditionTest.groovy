package com.wwkarev.operationengine.condition

import spock.lang.Specification

class FalseConditionTest extends Specification {
    def "test FalseCondition"() {
        expect:
        assert condition.isValid() == result
        where:
        condition | result
        new FalseCondition(UUID.randomUUID().toString()) | false
    }
}
