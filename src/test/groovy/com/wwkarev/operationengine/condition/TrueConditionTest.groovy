package com.wwkarev.operationengine.condition

import spock.lang.Specification

class TrueConditionTest extends Specification {
    def "test TrueCondition"() {
        expect:
        assert condition.isValid() == result
        where:
        condition | result
        new TrueCondition(UUID.randomUUID().toString()) | true
    }
}
