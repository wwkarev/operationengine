package com.wwkarev.operationengine.condition


import com.wwkarev.operationengine.conditionconstructor.ConditionConstructorFromPrefixLiterals
import com.wwkarev.operationengine.literal.*
import com.wwkarev.operationengine.literal.representation.ClassicOperatorRepresentation
import spock.lang.Shared
import spock.lang.Specification

class MemorizingConditionTransformerTest extends Specification {
    static class TestCondition1 implements Condition {
        @Override
        String getId() {
            return 'condition1'
        }

        @Override
        Boolean isValid() {
            return true
        }
    }
    static class TestCondition2 implements Condition {
        @Override
        String getId() {
            return 'condition2'
        }

        @Override
        Boolean isValid() {
            return true
        }
    }
    static class TestCondition3 implements Condition {
        @Override
        String getId() {
            return 'condition3'
        }

        @Override
        Boolean isValid() {
            return true
        }
    }

    @Shared
    Condition condition1 = new TestCondition1()
    @Shared
    Condition condition2 = new TestCondition2()
    @Shared
    Condition condition3 = new TestCondition3()

    def "test MemorizingConditionConstructor"() {
        when:
        Map<String, Condition> conditions = [(condition1.getId()): condition1, (condition2.getId()): condition2, (condition3.getId()): condition3]

        LiteralBuilder literalBuilder = new LiteralBuilder(new ClassicOperatorRepresentation())
        LiteralNotationTransformer notationTransformer = new LiteralNotationTransformer()
        List<Literal> prefixLiterals = notationTransformer.infixToPrefix(
                literalBuilder.buildByLogicalExpression(logicalExpression)
        )
        Condition resultCondition = new ConditionConstructorFromPrefixLiterals(conditions, prefixLiterals).construct()

        MemorizingConditionTransformer conditionTransformer = new MemorizingConditionTransformer(resultCondition)
        Condition memorizingCondition = conditionTransformer.transform()
        resultFunc(memorizingCondition)
        then:
        notThrown Exception
        where:
        logicalExpression | resultFunc
        "${condition1.getId()} and not (${condition2.getId()} or ${condition3.getId()})" | {Condition _memorizingCondition -> checkLogicalExpression1(_memorizingCondition)}
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
