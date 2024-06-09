package org.example;
import java.util.ArrayList;

public class gat
{
    public tokenReader tr;
    public jsonObject getJsoonObject(String jsonString) throws JsonParseException{
        lat l = new lat();
        ArrayList<token> tokens = l.getTokens(jsonString);
        tr = new tokenReader(tokens);
        jsonObject jsoon = parseJsonObject();
        return jsoon;
    }

    public jsonObject parseJsonObject() throws JsonParseException {

        int expectToken = TokenType.STRING.getValue() | TokenType.BEGIN_ARRAY.getValue() | TokenType.BEGIN_OBJECT.getValue();
        String key = "root";
        Object value = null;

        jsonObject jabt = new jsonObject();
        while (tr.hasNext()) {
            token t = tr.next();
            TokenType tt = t.getType();
            String v = t.getValue();
            switch (tt) {
                case BEGIN_OBJECT :
                    checkExpectToken(tt, expectToken);
                    jabt.put(key, parseJsonObject());
                    expectToken = TokenType.END_OBJECT.getValue() | TokenType.SEP_COMMA.getValue();
                    break;
                case STRING:
                    checkExpectToken(tt, expectToken);
                    token peek=tr.peekPrevious();
                    if (peek.getType()==TokenType.SEP_COLON){
                        value = v;
                        jabt.put(key, value);
                        expectToken = TokenType.END_OBJECT.getValue() | TokenType.SEP_COMMA.getValue();
                    }else{
                        key = v;
                        expectToken = TokenType.SEP_COLON.getValue();
                    }
                    break;
                case NULL:
                    checkExpectToken(tt, expectToken);
                    value = null;
                    jabt.put(key, value);
                    expectToken = TokenType.END_OBJECT.getValue() | TokenType.SEP_COMMA.getValue();
                    break;
                case NUMBER:
                    checkExpectToken(tt, expectToken);
                    //判断是否是浮点数
                    if(v.contains(".")&&!v.contains("e")&&!v.contains("E")){
                        value = Double.parseDouble(v);
                        jabt.put(key, value);
                        expectToken = TokenType.END_OBJECT.getValue() | TokenType.SEP_COMMA.getValue();
                    }else{
                        long longValue = Long.parseLong(v);
                        if(longValue>Integer.MAX_VALUE||longValue<Integer.MIN_VALUE){
                            //判断是否超出int范围
                            value = longValue;
                            jabt.put(key, value);
                            expectToken = TokenType.END_OBJECT.getValue() | TokenType.SEP_COMMA.getValue();
                        }else{

                            int intValue = (int)longValue;
                            value = intValue;
                            jabt.put(key, value);
                            expectToken = TokenType.END_OBJECT.getValue() | TokenType.SEP_COMMA.getValue();
                        }

                    }
                    break;

                case BEGIN_ARRAY:
                    checkExpectToken(tt, expectToken);
                    jabt.put(key, parseJsonArray());
                    expectToken = TokenType.END_OBJECT.getValue() | TokenType.SEP_COMMA.getValue();
                    break;

                case BOOLEAN:
                    checkExpectToken(tt, expectToken);
                    value = Boolean.parseBoolean(v);
                    jabt.put(key, value);
                    expectToken = TokenType.END_OBJECT.getValue() | TokenType.SEP_COMMA.getValue();
                    break;
                case SEP_COLON:
                    checkExpectToken(tt, expectToken);
                    expectToken = (TokenType.BOOLEAN.getValue()|TokenType.NULL.getValue()
                            |TokenType.NUMBER.getValue()|TokenType.STRING.getValue()|
                            TokenType.BEGIN_ARRAY.getValue()|TokenType.BEGIN_OBJECT.getValue())|
                            TokenType.BEGIN_ARRAY.getValue();
                    break;
                case SEP_COMMA:
                    checkExpectToken(tt, expectToken);
                    expectToken = TokenType.STRING.getValue() ;
                    break;
                case END_OBJECT:
                    checkExpectToken(tt, expectToken);
                    //expectToken = TokenType.END_DOCUMENT.getValue()|TokenType.END_OBJECT.getValue()|TokenType.END_ARRAY.getValue();
                    return jabt;
                case END_DOCUMENT:
                    //checkExpectToken(tt, expectToken);
                    return jabt;
            }

        }
        return jabt;
    }
    public jsonArray parseJsonArray() throws JsonParseException {
        int expectToken = TokenType.STRING.getValue() | TokenType.BEGIN_ARRAY.getValue()|TokenType.BEGIN_OBJECT.getValue()
                |TokenType.BOOLEAN.getValue()|TokenType.NULL.getValue()|TokenType.NUMBER.getValue()|TokenType.END_ARRAY.getValue();
        jsonArray jay = new jsonArray();
        while (tr.hasNext()) {
            token t = tr.next();
            TokenType tt = t.getType();
            String v = t.getValue();
            switch (tt) {
                case BEGIN_ARRAY:
                    checkExpectToken(tt, expectToken);
                    jay.add(parseJsonArray());
                    expectToken = TokenType.END_ARRAY.getValue() | TokenType.SEP_COMMA.getValue();
                    break;
                case BEGIN_OBJECT:
                    checkExpectToken(tt, expectToken);
                    jay.add(parseJsonObject());
                    expectToken = TokenType.SEP_COMMA.getValue()|TokenType.END_ARRAY.getValue();
                    break;
                case STRING:
                    checkExpectToken(tt, expectToken);
                    jay.add(v);
                    expectToken = TokenType.END_ARRAY.getValue() | TokenType.SEP_COMMA.getValue();
                    break;
                case NULL:
                    checkExpectToken(tt, expectToken);
                    jay.add(null);
                    expectToken = TokenType.END_ARRAY.getValue() | TokenType.SEP_COMMA.getValue();
                    break;
                case NUMBER:
                    checkExpectToken(tt, expectToken);
                    //判断是否是浮点数
                    if(v.contains(".")&&!v.contains("e")&&!v.contains("E")){
                        jay.add(Double.parseDouble(v));
                    }else{
                        long longValue = Long.parseLong(v);
                        if(longValue>Integer.MAX_VALUE||longValue<Integer.MIN_VALUE){
                            //判断是否超出int范围
                            jay.add(longValue);
                        }else{
                            int intValue = (int)longValue;
                            jay.add(intValue);
                        }
                    }
                    expectToken = TokenType.END_ARRAY.getValue() | TokenType.SEP_COMMA.getValue();
                    break;
                case BOOLEAN:
                    checkExpectToken(tt, expectToken);
                    jay.add(Boolean.parseBoolean(v));
                    expectToken = TokenType.END_ARRAY.getValue() | TokenType.SEP_COMMA.getValue();
                    break;
                case SEP_COMMA:
                    checkExpectToken(tt, expectToken);
                    expectToken = TokenType.STRING.getValue() | TokenType.BEGIN_ARRAY.getValue()|TokenType.BEGIN_OBJECT.getValue()
                            |TokenType.BOOLEAN.getValue()|TokenType.NULL.getValue()|TokenType.NUMBER.getValue()|TokenType.END_ARRAY.getValue();
                    break;
                case END_ARRAY:
                    checkExpectToken(tt, expectToken);
                    return jay;
            }
        }
        return jay;
    }


    private static void checkExpectToken(TokenType tokenType, int expectToken) throws JsonParseException {

        if ((tokenType.getValue() & expectToken) == 0) {
            throw new JsonParseException("Parse error, invalid Token.");
        }
    }
    public static void main(String[] args) {
        int expectToken = 7;
        try {
            checkExpectToken(TokenType.END_OBJECT, expectToken);

        }catch (JsonParseException e){
            System.out.println(e.getMessage());

        }
    }

}
