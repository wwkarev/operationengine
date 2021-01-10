package com.wwkarev.operationengine.literal

import groovy.transform.InheritConstructors;

/**
 * Transforms literal list to literal list in another notation
 * @param <T>
 * @author Vitalii Karev (wwkarev)
 */
final class LiteralNotationTransformer {
    List<Literal> infixToPostfix(List<Literal> infixLiterals)
    {
        infixLiterals = [new Literal(LiteralType.OPEN_BRACKET)] + infixLiterals + [new Literal(LiteralType.CLOSE_BRACKET)]

        List<String> serviceList = []
        List<Literal> postfixLiterals = []

        infixLiterals.each{literal ->
            switch (literal.getType()) {
                case LiteralType.PARAM:
                    postfixLiterals.add(literal)
                    break
                case LiteralType.OPEN_BRACKET:
                    serviceList.add(new Literal(LiteralType.OPEN_BRACKET))
                    break
                case LiteralType.CLOSE_BRACKET:
                    while(((Literal)serviceList.last()).getType() != LiteralType.OPEN_BRACKET) {
                        postfixLiterals.add(serviceList.removeLast())
                    }
                    serviceList.removeLast()
                    break
                default:
                    while (getPriority(literal.getType())
                            <= getPriority(((Literal)serviceList.last()).getType())
                    ) {
                        postfixLiterals.add(serviceList.removeLast())
                    }
                    serviceList.add(literal);
            }
        }
        return postfixLiterals;
    }

    List<Literal> infixToPrefix(List<Literal> infixLiterals)
    {
        infixLiterals = infixLiterals.reverse()

        infixLiterals = infixLiterals.collect {Literal literal ->
            if (literal.getType() == LiteralType.OPEN_BRACKET) {
                literal = new Literal(LiteralType.CLOSE_BRACKET)
            } else if (literal.getType() == LiteralType.CLOSE_BRACKET) {
                literal = new Literal(LiteralType.OPEN_BRACKET)
            }
            return literal
        }

        return infixToPostfix(infixLiterals).reverse()
    }

    List<Literal> prefixToInfix(List<Literal> prefixLiterals) {
        List<Literal> postfixLiterals = prefixLiterals.reverse()
        return postfixToInfix(postfixLiterals)
    }

    List<Literal> postfixToInfix(List<Literal> postfixLiterals) {
        try {
            List<LiteralContainer> containers = []
            List<Literal> infixLiterals = _postfixToInfix(postfixLiterals, containers)
            if (containers.size() > 0) {
                throw new LiteralNotationTransformer.TransformException()
            }
            return infixLiterals
        } catch (NoSuchElementException e) {
            throw new LiteralNotationTransformer.TransformException(e)
        }
    }

    private Integer getPriority(LiteralType type)
    {
        Integer priority = 0
        switch (type) {
            case LiteralType.OR:
                priority = 1
                break
            case LiteralType.AND:
                priority = 2
                break
            case LiteralType.NOT:
                priority = 3
                break
        }
        return priority
    }

    private Integer getBracketPriority(LiteralType type)
    {
        Integer priority = 0
        switch (type) {
            case LiteralType.NOT:
                priority = 1
                break
            case LiteralType.AND:
                priority = 2
                break
            case LiteralType.OR:
                priority = 3
                break
        }
        return priority
    }

    private List<Literal> _postfixToInfix(List<Literal> postfixLiterals, List<LiteralContainer> containers) {
        postfixLiterals.each{literal ->
            switch (literal.getType()) {
                case LiteralType.AND:
                    LiteralContainer leftContainer = containers.removeLast()
                    LiteralContainer rightContainer = containers.removeLast()

                    List<Literal> literals = []
                    if (getBracketPriority(leftContainer.literalType) > getBracketPriority(literal.getType())) {
                        literals += (
                                [new Literal(LiteralType.OPEN_BRACKET)] +
                                leftContainer.literals + [new Literal(LiteralType.CLOSE_BRACKET)]
                        )
                    } else {
                        literals += leftContainer.literals
                    }

                    literals += [new Literal(literal.getType())]

                    if (getBracketPriority(rightContainer.literalType) > getBracketPriority(literal.getType())) {
                        literals += (
                                [new Literal(LiteralType.OPEN_BRACKET)] +
                                        rightContainer.literals + [new Literal(LiteralType.CLOSE_BRACKET)]
                        )
                    } else {
                        literals += rightContainer.literals
                    }

                    containers.add(new LiteralContainer(literalType: literal.getType(), literals: literals))
                    break
                case LiteralType.OR:
                    LiteralContainer leftContainer = containers.removeLast()
                    LiteralContainer rightContainer = containers.removeLast()

                    List<Literal> literals = leftContainer.literals + [new Literal(literal.getType())] + rightContainer.literals
                    containers.add(new LiteralContainer(literalType: literal.getType(), literals: literals))
                    break
                case LiteralType.NOT:
                    LiteralContainer notContainer = containers.removeLast()
                    List<Literal> literals = [new Literal(literal.getType())] + notContainer.literals
                    if (getBracketPriority(notContainer.literalType) > getBracketPriority(literal.getType())) {
                        literals = [new Literal(literal.getType())] + [new Literal(LiteralType.OPEN_BRACKET)] +
                                notContainer.literals + [new Literal(LiteralType.CLOSE_BRACKET)]
                    }
                    containers.add(new LiteralContainer(literalType: literal.getType(), literals: literals))
                    break
                case LiteralType.PARAM:
                    containers.add(new LiteralContainer(literalType: literal.getType(), literals: [literal]))
                    break
            }
        }
        return containers.removeLast().literals
    }

    private static class LiteralContainer {
        LiteralType literalType
        List<Literal> literals
    }

    @InheritConstructors
    static class TransformException extends Exception {}
}
