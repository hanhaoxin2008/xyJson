package org.example;

public class token
{
    public TokenType type;
    public String value;

    public token(TokenType type, String value)
    {
        this.type = type;
        this.value = value;
    }
    public TokenType getType()
    {
        return type;
    }
    public String getValue()

    {
        return value;
    }
}
