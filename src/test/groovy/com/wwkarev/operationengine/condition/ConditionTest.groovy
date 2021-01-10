package com.wwkarev.operationengine.condition

import com.wwkarev.operationengine.literal.Literal
import com.wwkarev.operationengine.literal.LiteralBuilder
import com.wwkarev.operationengine.literal.LiteralNotationTransformer
import com.wwkarev.operationengine.literal.representation.ClassicOperatorRepresentation
import spock.lang.Shared
import spock.lang.Specification

class ConditionTest extends Specification {
    @Shared
    Condition condition1 = new TrueCondition('condition1')
    @Shared
    Condition condition2 = new FalseCondition('condition2')
    @Shared
    Condition condition3 = new TrueCondition('condition3')

    def "test Condition. Legal logical expression"() {
        when:
        resultFunc(resultCondition)
        then:
        notThrown IllegalArgumentException
        where:
        resultCondition | resultFunc
        condition1 & ~(condition2 | condition3) | {Condition _resultCondition -> checkLogicalExpression1(_resultCondition)}
    }

    void checkLogicalExpression1(Condition resultCondition) {
        assert resultCondition instanceof AndCondition
        Condition resultCondition1 = ((AndCondition)resultCondition).getLeftCondition()
        assert resultCondition1.hashCode() == condition1.hashCode()
        Condition notCondition = ((AndCondition)resultCondition).getRightCondition()
        assert notCondition instanceof NotCondition
        Condition orCondition = ((NotCondition)notCondition).getCondition()
        assert orCondition instanceof OrCondition
        Condition resultCondition2 = ((OrCondition)orCondition).getLeftCondition()
        assert resultCondition2.hashCode() == condition2.hashCode()
        Condition resultCondition3 = ((OrCondition)orCondition).getRightCondition()
        assert resultCondition3.hashCode() == condition3.hashCode()
    }
}
