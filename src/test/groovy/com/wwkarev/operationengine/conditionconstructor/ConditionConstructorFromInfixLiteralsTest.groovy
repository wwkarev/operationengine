package com.wwkarev.operationengine.conditionconstructor

import com.wwkarev.operationengine.condition.*
import com.wwkarev.operationengine.literal.Literal
import com.wwkarev.operationengine.literal.LiteralBuilder
import com.wwkarev.operationengine.literal.LiteralNotationTransformer
import com.wwkarev.operationengine.literal.representation.ClassicOperatorRepresentation
import spock.lang.Shared
import spock.lang.Specification

class ConditionConstructorFromInfixLiteralsTest extends Specification {
    @Shared
    Condition condition1 = new TrueCondition('condition1')

    @Shared
    Condition condition2 = new FalseCondition('condition2')

    @Shared
    Condition condition3 = new TrueCondition('condition3')

    def "test ConditionConstructor. Error. Condition not found"() {
        when:
        String logicalExpression = "${condition1.getId()} and not (${condition2.getId()} or ${condition3.getId()})"
        Map<String, Condition> conditions = [(condition1.getId()): condition1, (condition2.getId()): condition2]

        LiteralBuilder literalBuilder = new LiteralBuilder(new ClassicOperatorRepresentation())
        List<Literal> literalList = literalBuilder.buildByLogicalExpression(logicalExpression)
        new ConditionConstructorFromInfixLiterals(conditions, literalList).construct()
        then:
        thrown ConditionConstructor.ConditionNotFoundException
    }

    def "test ConditionConstructor. Error. Illegal logical expression"() {
        when:
        Map<String, Condition> conditions = [(condition1.getId()): condition1, (condition2.getId()): condition2, (condition3.getId()): condition3]

        LiteralBuilder literalBuilder = new LiteralBuilder(new ClassicOperatorRepresentation())
        List<Literal> literalList = literalBuilder.buildByLogicalExpression(logicalExpression)
        new ConditionConstructorFromInfixLiterals(conditions, literalList).construct()
        then:
        thrown ConditionConstructor.ConstructionException
        where:
        logicalExpression << [
                "${condition1.getId()} and not (${condition2.getId()} or ${condition3.getId()}) AND",
                "${condition1.getId()} and not (${condition2.getId()} or ${condition3.getId()} ${condition3.getId()})"
        ]
    }


    def "test ConditionConstructor. Legal logical expression"() {
        when:
        Map<String, Condition> conditions = [(condition1.getId()): condition1, (condition2.getId()): condition2, (condition3.getId()): condition3]

        LiteralBuilder literalBuilder = new LiteralBuilder(new ClassicOperatorRepresentation())
        List<Literal> literalList = literalBuilder.buildByLogicalExpression(logicalExpression)
        Condition resultCondition = new ConditionConstructorFromInfixLiterals(conditions, literalList).construct()
        resultFunc(resultCondition)
        then:
        notThrown IllegalArgumentException
        where:
        logicalExpression | resultFunc
        "${condition1.getId()} and not (${condition2.getId()} or ${condition3.getId()})" | {Condition _resultCondition -> checkLogicalExpression1(_resultCondition)}
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
