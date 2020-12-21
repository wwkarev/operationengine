package com.wwkarev.operationengine.literal

import com.wwkarev.operationengine.literal.representation.OperatorRepresentation

/**
 * Create list of literals from logical expression
 * @author Vitalii Karev (wwkarev)
 */
final class LiteralBuilder {
    private OperatorRepresentation literalRepresentation

    LiteralBuilder(OperatorRepresentation literalRepresentation) {
        this.literalRepresentation = literalRepresentation
    }

    List<Literal> buildByLogicalExpression(String logicalExpression) {
        String andRegex = "\\Q${literalRepresentation.getAND()}\\E"
        String orRegex = "\\Q${literalRepresentation.getOR()}\\E"
        String notRegex = "\\Q${literalRepresentation.getNOT()}\\E"
        String openBracketRegex = "\\Q${literalRepresentation.getOpenBracket()}\\E"
        String closeBracketRegex = "\\Q${literalRepresentation.getCloseBracket()}\\E"
        String paramRegex = "[a-z0-9_-]+"
        String regex = "(?i)($andRegex)|($orRegex)|($notRegex)|($openBracketRegex)|($closeBracketRegex)|($paramRegex)"
        return logicalExpression.findAll(regex).collect{String it ->
            return build(it)
        }
    }

    private Literal build(String representation) {
        Literal literal
        switch (representation.toLowerCase()) {
            case literalRepresentation.getAND().toLowerCase():
                literal = new Literal(LiteralType.AND)
                break
            case literalRepresentation.getOR().toLowerCase():
                literal = new Literal(LiteralType.OR)
                break
            case literalRepresentation.getNOT().toLowerCase():
                literal = new Literal(LiteralType.NOT)
                break
            case literalRepresentation.getOpenBracket().toLowerCase():
                literal = new Literal(LiteralType.OPEN_BRACKET)
                break
            case literalRepresentation.getCloseBracket().toLowerCase():
                literal = new Literal(LiteralType.CLOSE_BRACKET)
                break
            default:
                literal = new Literal(representation.toString(), LiteralType.PARAM)
        }
        return literal
    }
}
