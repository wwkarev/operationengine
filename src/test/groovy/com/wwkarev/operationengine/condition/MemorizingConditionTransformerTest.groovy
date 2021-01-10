package com.wwkarev.operationengine.condition

import com.wwkarev.operationengine.literal.*
import spock.lang.Shared
import spock.lang.Specification

class MemorizingConditionTransformerTest extends Specification {
    @Shared
    Condition condition1 = new TrueCondition('condition1')
    @Shared
    Condition condition2 = new TrueCondition('condition2')
    @Shared
    Condition condition3 = new TrueCondition('condition3')

    def "test MemorizingConditionConstructor"() {
        when:
        MemorizingConditionTransformer conditionTransformer = new MemorizingConditionTransformer(resultCondition)
        Condition memorizingCondition = conditionTransformer.transform()
        resultFunc(memorizingCondition)
        then:
        notThrown Exception
        where:
        resultCondition | resultFunc
        condition1 & ~(condition2 | condition3) | {Condition _memorizingCondition -> checkLogicalExpression1(_memorizingCondition)}
    }

    void checkLogicalExpression1(Condition memorizingCondition) {
        Condition condition = ((MemorizingCondition)memorizingCondition).getCondition()
        assert condition instanceof AndCondition
        Condition resultCondition1 = ((MemorizingCondition)((AndCondition)condition).getLeftCondition()).getCondition()
        assert resultCondition1.getId() == condition1.getId()
        Condition notCondition = ((MemorizingCondition)((AndCondition)condition).getRightCondition()).getCondition()
        assert notCondition instanceof NotCondition
        Condition orCondition = ((MemorizingCondition)((NotCondition)notCondition).getCondition()).getCondition()
        assert orCondition instanceof OrCondition
        Condition resultCondition2 = ((MemorizingCondition)((OrCondition)orCondition).getLeftCondition()).getCondition()
        assert resultCondition2.getId() == condition2.getId()
        Condition resultCondition3 = ((MemorizingCondition)((OrCondition)orCondition).getRightCondition()).getCondition()
        assert resultCondition3.getId() == condition3.getId()
    }
}
