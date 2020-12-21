package com.wwkarev.operationengine.literal

import com.wwkarev.operationengine.literal.representation.ClassicOperatorRepresentation
import com.wwkarev.operationengine.literal.representation.OperatorRepresentation
import spock.lang.Specification

class LiteralBuilderTest extends Specification {
    def "test LiteralBuilder."() {
        when:
        String str = 'NOT not_Hello_not and (WO_-RLD or not 1 )'

        OperatorRepresentation literalRepresentation = new ClassicOperatorRepresentation()

        def destResultList = [
                [LiteralType.NOT],
                [LiteralType.PARAM, 'not_Hello_not'],
                [LiteralType.AND],
                [LiteralType.OPEN_BRACKET],
                [LiteralType.PARAM, 'WO_-RLD'],
                [LiteralType.OR],
                [LiteralType.NOT],
                [LiteralType.PARAM, '1'],
                [LiteralType.CLOSE_BRACKET]
        ]

        LiteralBuilder literalBuilder = new LiteralBuilder(literalRepresentation)
        List<Literal> srcLiteralList = literalBuilder.buildByLogicalExpression(str)
        then:
        destResultList.eachWithIndex{destResult, index ->
            assert srcLiteralList[index].type == destResult[0]
            if (destResult[0] == LiteralType.PARAM) {
                assert srcLiteralList[index].getId() == destResult[1]
            }
        }
    }
}
