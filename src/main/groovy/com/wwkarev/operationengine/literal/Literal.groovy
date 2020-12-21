package com.wwkarev.operationengine.literal

/**
 * Describes literal in boolean logical expression
 * @author Vitalii Karev (wwkarev)
 */
class Literal {
    private String id
    private LiteralType type

    Literal(LiteralType type) {
        this.id = UUID.randomUUID().toString()
        this.type = type
    }

    Literal(String id, LiteralType type) {
        this.id = id
        this.type = type
    }

    String getId() {
        return id
    }

    LiteralType getType() {
        return type
    }
}
