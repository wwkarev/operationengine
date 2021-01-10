package com.wwkarev.operationengine.literal

import com.wwkarev.operationengine.literal.representation.ClassicOperatorRepresentation
import com.wwkarev.operationengine.literal.representation.OperatorRepresentation
import groovy.json.JsonBuilder
import spock.lang.Specification

class LiteralNotationTransformerTest extends Specification {
    def "test NotationTransformer. InfixToPostfix"() {
        when:
        String str = 'NOT not_Hello_not and (WO_-RLD or not 1 )'

        OperatorRepresentation literalRepresentation = new ClassicOperatorRepresentation()

        LiteralBuilder literalBuilder = new LiteralBuilder(literalRepresentation)
        List<Literal> srcLiteralList = literalBuilder.buildByLogicalExpression(str)
        LiteralNotationTransformer notationTransformer = new LiteralNotationTransformer()
        List<Literal> transformedLiteralList = notationTransformer.infixToPostfix(srcLiteralList)

        def destResultList = [
                new Literal('not_Hello_not', LiteralType.PARAM),
                new Literal(LiteralType.NOT),
                new Literal('WO_-RLD', LiteralType.PARAM),
                new Literal('1', LiteralType.PARAM),
                new Literal(LiteralType.NOT),
                new Literal(LiteralType.OR),
                new Literal(LiteralType.AND)
        ]
        then:
        assert LiteralUtil.isLiteralListsEqual(transformedLiteralList, destResultList) == true
    }

    def "test NotationTransformerTest. InfixToPrefix"() {
        when:
        String str = 'NOT not_Hello_not and (WO_-RLD or not 1 )'

        OperatorRepresentation literalRepresentation = new ClassicOperatorRepresentation()

        LiteralBuilder literalBuilder = new LiteralBuilder(literalRepresentation)
        List<Literal> srcLiteralList = literalBuilder.buildByLogicalExpression(str)
        LiteralNotationTransformer notationTransformer = new LiteralNotationTransformer()
        List<Literal> transformedLiteralList = notationTransformer.infixToPrefix(srcLiteralList)

        def destResultList = [
                new Literal(LiteralType.AND),
                new Literal(LiteralType.NOT),
                new Literal('not_Hello_not', LiteralType.PARAM),
                new Literal(LiteralType.OR),
                new Literal('WO_-RLD', LiteralType.PARAM),
                new Literal(LiteralType.NOT),
                new Literal('1', LiteralType.PARAM)
        ]
        then:
        assert LiteralUtil.isLiteralListsEqual(transformedLiteralList, destResultList) == true
    }

    def "test NotationTransformerTest. PrefixToInfix"() {
        when:
        OperatorRepresentation literalRepresentation = new ClassicOperatorRepresentation()

        LiteralBuilder literalBuilder = new LiteralBuilder(literalRepresentation)
        List<Literal> srcLiteralList = literalBuilder.buildByLogicalExpression(str)
        LiteralNotationTransformer notationTransformer = new LiteralNotationTransformer()
        List<Literal> transformedLiteralList = notationTransformer.prefixToInfix(notationTransformer.infixToPrefix(srcLiteralList))

        then:
        assert LiteralUtil.isLiteralListsEqual(transformedLiteralList, destResultList) == true
        where:
        str|destResultList
        'not_Hello_not and not WO_-RLD'|[
                new Literal('not_Hello_not', LiteralType.PARAM),
                new Literal(LiteralType.AND),
                new Literal(LiteralType.NOT),
                new Literal('WO_-RLD', LiteralType.PARAM)
        ]
        '(xxx or yyy) and not (zzz and xxx)'|[
                new Literal(LiteralType.OPEN_BRACKET),
                new Literal('xxx', LiteralType.PARAM),
                new Literal(LiteralType.OR),
                new Literal('yyy', LiteralType.PARAM),
                new Literal(LiteralType.CLOSE_BRACKET),
                new Literal(LiteralType.AND),
                new Literal(LiteralType.NOT),
                new Literal(LiteralType.OPEN_BRACKET),
                new Literal('zzz', LiteralType.PARAM),
                new Literal(LiteralType.AND),
                new Literal('xxx', LiteralType.PARAM),
                new Literal(LiteralType.CLOSE_BRACKET)
        ]
    }

    def "test NotationTransformerTest. PrefixToInfix. Illegal prefix literals"() {
        when:
        String str = 'not_Hello_not and not WO_-RLD'

        OperatorRepresentation literalRepresentation = new ClassicOperatorRepresentation()

        LiteralBuilder literalBuilder = new LiteralBuilder(literalRepresentation)
        List<Literal> srcLiteralList = literalBuilder.buildByLogicalExpression(str)
        LiteralNotationTransformer notationTransformer = new LiteralNotationTransformer()
        List<Literal> prefixLiterals = notationTransformer.infixToPrefix(srcLiteralList)
        List<Literal> illegalPrefixLiterals = [new Literal(LiteralType.AND)] + prefixLiterals
        notationTransformer.prefixToInfix(illegalPrefixLiterals)
        then:
        thrown LiteralNotationTransformer.TransformException
    }

    def "test NotationTransformerTest. InfixToPrefix Equality"() {
        when:
        String str_1 = 'NOT not_Hello_not and (WO_-RLD or not 1 )'

        OperatorRepresentation literalRepresentation = new ClassicOperatorRepresentation()

        LiteralBuilder literalBuilder = new LiteralBuilder(literalRepresentation)
        List<Literal> srcLiteralList_1 = literalBuilder.buildByLogicalExpression(str_1)
        LiteralNotationTransformer notationTransformer = new LiteralNotationTransformer()
        List<Literal> transformedLiteralList_1 = notationTransformer.infixToPrefix(srcLiteralList_1)

        String str_2 = '((NOT not_Hello_not and ((WO_-RLD or not 1 ))))'
        List<Literal> srcLiteralList_2 = literalBuilder.buildByLogicalExpression(str_2)
        List<Literal> transformedLiteralList_2 = notationTransformer.infixToPrefix(srcLiteralList_2)

        then:
        assert LiteralUtil.isLiteralListsEqual(transformedLiteralList_1, transformedLiteralList_2) == true
    }
}
