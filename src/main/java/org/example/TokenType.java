package org.example;

public enum TokenType {
    BEGIN_OBJECT(1),
    END_OBJECT(2),
    BEGIN_ARRAY(4),
    END_ARRAY(8),
    NULL(16),
    NUMBER(32),
    STRING(64),
    BOOLEAN(128),
    SEP_COLON(256),
    SEP_COMMA(512),
    END_DOCUMENT(1024);
    private final int value;
    private TokenType(final int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
