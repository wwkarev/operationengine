package com.wwkarev.operationengine.literal.representation

/**
 * Mapping of boolean operator to string representation. AND - and, OR - or, NOT - not
 * @author Vitalii Karev (wwkarev)
 */
class ClassicOperatorRepresentation implements OperatorRepresentation {
    @Override
    String getAND() {
        return "AND"
    }

    @Override
    String getOR() {
        return "OR"
    }

    @Override
    String getNOT() {
        return "NOT "
    }

    @Override
    String getOpenBracket() {
        return "("
    }

    @Override
    String getCloseBracket() {
        return ")"
    }
}
