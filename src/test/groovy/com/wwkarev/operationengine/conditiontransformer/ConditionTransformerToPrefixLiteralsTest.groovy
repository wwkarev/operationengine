package com.wwkarev.operationengine.conditiontransformer


import com.wwkarev.operationengine.condition.Condition
import com.wwkarev.operationengine.conditionconstructor.ConditionConstructorFromPrefixLiterals
import com.wwkarev.operationengine.literal.Literal
import com.wwkarev.operationengine.literal.LiteralBuilder
import com.wwkarev.operationengine.literal.LiteralNotationTransformer
import com.wwkarev.operationengine.literal.LiteralType
import com.wwkarev.operationengine.literal.LiteralUtil
import com.wwkarev.operationengine.literal.representation.ClassicOperatorRepresentation
import spock.lang.Shared
import spock.lang.Specification

class ConditionTransformerToPrefixLiteralsTest extends Specification {
    @Shared
    Condition condition1 = new Condition() {
        @Override
        String getId() {
            return 'condition1'
        }

        @Override
        Boolean isValid() {
            return true
        }
    }

    @Shared
    Condition condition2 = new Condition() {
        @Override
        String getId() {
            return 'condition2'
        }

        @Override
        Boolean isValid() {
            return false
        }
    }

    @Shared
    Condition condition3 = new Condition() {
        @Override
        String getId() {
            return 'condition3'
        }

        @Override
        Boolean isValid() {
            return true
        }
    }

    def "test ConditionTransformer"() {
        when:
        String logicalExpression = "${condition1.getId()} and not (${condition2.getId()} or ${condition3.getId()})"

        Map<String, Condition> conditions = [(condition1.getId()): condition1, (condition2.getId()): condition2, (condition3.getId()): condition3]

        LiteralBuilder literalBuilder = new LiteralBuilder(new ClassicOperatorRepresentation())
        LiteralNotationTransformer notationTransformer = new LiteralNotationTransformer()
        List<Literal> prefixLiterals = notationTransformer.infixToPrefix(
                literalBuilder.buildByLogicalExpression(logicalExpression)
        )
        Condition resultCondition = new ConditionConstructorFromPrefixLiterals(conditions, prefixLiterals).construct()

        ConditionTransformerToPrefixLiterals conditionTransformer = new ConditionTransformerToPrefixLiterals(resultCondition)
        List<Literal> transformedLiteralList = conditionTransformer.transform()


        def destResultList = [
                [LiteralType.AND],
                [LiteralType.PARAM, 'not_Hello_not'],
                [LiteralType.NOT],
                [LiteralType.OPEN_BRACKET],
                [LiteralType.OR],
                [LiteralType.CLOSE_BRACKET],

                [LiteralType.AND],
                [LiteralType.OPEN_BRACKET],
                [LiteralType.PARAM, 'WO_-RLD'],
                [LiteralType.CLOSE_BRACKET],
                [LiteralType.CLOSE_BRACKET]
        ]
        then:
        assert LiteralUtil.isLiteralListsEqual(transformedLiteralList, prefixLiterals)
    }
}
