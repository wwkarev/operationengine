package com.wwkarev.operationengine.conditionconstructor

import com.wwkarev.operationengine.condition.AndCondition
import com.wwkarev.operationengine.condition.Condition
import com.wwkarev.operationengine.condition.NotCondition
import com.wwkarev.operationengine.condition.OrCondition
import com.wwkarev.operationengine.literal.Literal
import com.wwkarev.operationengine.literal.LiteralType
/**
 * Constructs condition object by literal list in prefix notation
 * @param <T>
 * @author Vitalii Karev (wwkarev)
 */
class ConditionConstructorFromPrefixLiterals implements ConditionConstructor {
    private Map<String, Condition> conditions
    private List<Literal> prefixNotationLiterals

    ConditionConstructorFromPrefixLiterals(Map<String, Condition> conditions, List<Literal> prefixNotationLiterals) {
        this.conditions = conditions
        this.prefixNotationLiterals = prefixNotationLiterals
    }

    @Override
    Condition construct() {
        List<Literal> literals = prefixNotationLiterals.clone()

        try {
            Condition condition = recursiveConstruct(literals)
            if (literals.size() > 0) {
                throw new ConstructionException()
            }
            return condition
        } catch (NoSuchElementException e) {
            throw new ConstructionException(e)
        } catch (NullPointerException e) {
            throw new ConstructionException(e)
        }
    }

    private Condition recursiveConstruct(List<Literal> literals) {
        Condition condition
        Literal literal = literals[0]
        switch (literal.getType()) {
            case LiteralType.AND:
                condition = constructAndCondition(literals)
                break
            case LiteralType.OR:
                condition = constructOrCondition(literals)
                break
            case LiteralType.NOT:
                condition = constructNotCondition(literals)
                break
            case LiteralType.PARAM:
                condition = constructParamCondition(literals)
                break
        }
        return condition
    }

    private Condition constructAndCondition(List<Literal> literals) {
        Literal literal = literals.pop()
        Condition leftCondition = recursiveConstruct(literals)
        Condition rightCondition = recursiveConstruct(literals)
        return new AndCondition(literal.getId(), leftCondition, rightCondition)
    }

    private Condition constructOrCondition(List<Literal> literals) {
        Literal literal = literals.pop()
        Condition leftCondition = recursiveConstruct(literals)
        Condition rightCondition = recursiveConstruct(literals)
        return new OrCondition(literal.getId(), leftCondition, rightCondition)
    }

    private Condition constructNotCondition(List<Literal> literals) {
        Literal literal = literals.pop()
        Condition condition = recursiveConstruct(literals)
        return new NotCondition(literal.getId(), condition)
    }

    private Condition constructParamCondition(List<Literal> literals) {
        Literal literal = literals.pop()
        Condition condition = this.conditions.get(literal.getId())
        if (condition == null) {
            throw new ConditionNotFoundException()
        }
        return condition
    }
}
