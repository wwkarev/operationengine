package com.wwkarev.operationengine.literal

import groovy.transform.InheritConstructors;

/**
 * Transforms literal list to literal list in another notation
 * @param <T>
 * @author Vitalii Karev (wwkarev)
 */
final class LiteralNotationTransformer {
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
            List<List<Literal>> serviceList = []
            List<Literal> infixLiterals = _postfixToInfix(postfixLiterals, serviceList)
            if (serviceList.size() > 0) {
                throw new LiteralNotationTransformer.TransformException()
            }
            return infixLiterals
        } catch (NoSuchElementException e) {
            throw new LiteralNotationTransformer.TransformException(e)
        }
    }

    private List<Literal> _postfixToInfix(List<Literal> postfixLiterals, List<List<Literal>> serviceList) {
        postfixLiterals.each{literal ->
            switch (literal.getType()) {
                case LiteralType.AND:
                    List<Literal> leftLiterals = serviceList.removeLast()
                    List<Literal> rightLiterals = serviceList.removeLast()
                    serviceList.add(
                            [new Literal(LiteralType.OPEN_BRACKET)] + leftLiterals +
                                    [new Literal(LiteralType.AND)] +
                                    rightLiterals + [new Literal(LiteralType.CLOSE_BRACKET)]
                    )
                    break
                case LiteralType.OR:
                    List<Literal> leftLiterals = serviceList.removeLast()
                    List<Literal> rightLiterals = serviceList.removeLast()
                    serviceList.add(
                            [new Literal(LiteralType.OPEN_BRACKET)] + leftLiterals +
                                    [new Literal(LiteralType.OR)] +
                                    rightLiterals + [new Literal(LiteralType.CLOSE_BRACKET)]
                    )
                    break
                case LiteralType.NOT:
                    List<Literal> notLiterals = serviceList.removeLast()
                    serviceList.add(
                            [new Literal(LiteralType.NOT)] +[new Literal(LiteralType.OPEN_BRACKET)] +
                                    notLiterals + [new Literal(LiteralType.CLOSE_BRACKET)]
                    )
                    break
                case LiteralType.PARAM:
                    serviceList.add([literal])
                    break
            }
        }

        return serviceList.removeLast()
    }

    @InheritConstructors
    static class TransformException extends Exception {}
}
