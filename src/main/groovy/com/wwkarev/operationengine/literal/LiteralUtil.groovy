package com.wwkarev.operationengine.literal


/**
 * Util class for working with literals
 * @author Vitalii Karev (wwkarev)
 */
class LiteralUtil {
    static Boolean isLiteralsEqual(Literal first, Literal second) {
        Boolean isEqual = first.getType() == second.getType()
        if (first.getType() == LiteralType.PARAM) {
            isEqual &= (first.getId() == second.getId())
        }
        return isEqual
    }

    static Boolean isLiteralListsEqual(List<Literal> first, List<Literal> second) {
        Boolean isEqual = first.size() == second.size()
        if (isEqual) {
            first.eachWithIndex{ Literal firstEntry, int i ->
                Literal secondEntry = second[i]
                isEqual &= isLiteralsEqual(firstEntry, secondEntry)
            }
        }
        return isEqual
    }
}
