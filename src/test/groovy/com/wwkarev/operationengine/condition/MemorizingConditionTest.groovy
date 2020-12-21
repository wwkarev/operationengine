package com.wwkarev.operationengine.condition


import com.wwkarev.operationengine.condition.Condition
import spock.lang.Shared
import spock.lang.Specification

class MemorizingConditionTest extends Specification {

    def "test MemorizingConditionTest"() {
        when:
        Integer count1 = 0
        Condition memorizingCondition = new MemorizingCondition(new Condition() {
            @Override
            String getId() {
                return 'condition1'
            }

            @Override
            Boolean isValid() {
                count1++
                return true
            }
        })
        memorizingCondition.isValid()
        memorizingCondition.isValid()

        then:
        assert count1 == 1
    }

    def "test not MemorizingConditionTest"() {
        when:
        Integer count1 = 0
        Condition memorizingCondition = new Condition() {
            @Override
            String getId() {
                return 'condition1'
            }

            @Override
            Boolean isValid() {
                count1++
                return true
            }
        }
        memorizingCondition.isValid()
        memorizingCondition.isValid()

        then:
        assert count1 == 2
    }
}
