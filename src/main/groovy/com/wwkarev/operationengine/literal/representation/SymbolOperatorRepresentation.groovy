package com.wwkarev.operationengine.literal.representation

/**
 * Mapping of boolean operator to string representation. AND - &&, OR - ||, NOT - !
 * @author Vitalii Karev (wwkarev)
 */
class SymbolOperatorRepresentation implements OperatorRepresentation {
    @Override
    String getAND() {
        return "&&"
    }

    @Override
    String getOR() {
        return "||"
    }

    @Override
    String getNOT() {
        return "!"
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
