package com.wwkarev.operationengine.literal.representation

/**
 * Mapping of boolean operator to string representation
 * @author Vitalii Karev (wwkarev)
 */
interface OperatorRepresentation {
    abstract String getAND()
    abstract String getOR()
    abstract String getNOT()
    abstract String getOpenBracket()
    abstract String getCloseBracket()
}
