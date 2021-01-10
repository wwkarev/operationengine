package com.wwkarev.operationengine.conditiontransformer


import com.wwkarev.operationengine.condition.Condition
import com.wwkarev.operationengine.condition.FalseCondition
import com.wwkarev.operationengine.condition.TrueCondition
import com.wwkarev.operationengine.literal.Literal
import com.wwkarev.operationengine.literal.LiteralType
import com.wwkarev.operationengine.literal.LiteralUtil
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification

class ConditionTransformerToInfixLiteralsTest extends Specification {
    @Shared
    Condition condition1 = new TrueCondition('condition1')

    @Shared
    Condition condition2 = new FalseCondition('condition2')

    @Shared
    Condition condition3 = new TrueCondition('condition3')

    def "test ConditionTransformer"() {
        when:
        Condition resultCondition = condition1 & ~(condition2 | condition3)

        ConditionTransformerToPrefixLiterals conditionTransformer = new ConditionTransformerToInfixLiterals(resultCondition)
        List<Literal> transformedLiteralList = conditionTransformer.transform()


        def destResultList = [
                new Literal('condition1', LiteralType.PARAM),
                new Literal(LiteralType.AND),
                new Literal(LiteralType.NOT),
                new Literal(LiteralType.OPEN_BRACKET),
                new Literal('condition2', LiteralType.PARAM),
                new Literal(LiteralType.OR),
                new Literal('condition3', LiteralType.PARAM),
                new Literal(LiteralType.CLOSE_BRACKET)
        ]
        then:
        assert LiteralUtil.isLiteralListsEqual(transformedLiteralList, destResultList)
    }
}
