package org.example;

import java.util.ArrayList;

public class lat  {
    private charReader reader;
    private ArrayList<token> tokens;
    public ArrayList<token> getTokens(String input)  throws JsonParseException {
        tokens = new ArrayList<>();
        reader = new charReader(input);
        tokenize();
        return tokens;
    }

    public void tokenize() throws JsonParseException {
        token t;
        do {
            t = getNextToken();
            if (t!= null) {
                tokens.add(t);
            }}
        while (t.type!= TokenType.END_DOCUMENT);

    }
    public token getNextToken() throws JsonParseException {
        char ch;
        for(;;) {
            if (!reader.hasNext()) {
                return new token(TokenType.END_DOCUMENT, null);
            }

            ch = reader.next();
            if (!isWhiteSpace(ch)) {
                break;
            }
        }
        switch (ch){
            case '{':
                return new token(TokenType.BEGIN_OBJECT, "{");
            case '}':
                return new token(TokenType.END_OBJECT, "}");
            case '[':
                return new token(TokenType.BEGIN_ARRAY, "[");
            case ']':
                return new token(TokenType.END_ARRAY, "]");
            case ':':
                return new token(TokenType.SEP_COLON, ":");
            case ',':
                return new token(TokenType.SEP_COMMA, ",");
            case 'n':
                return new token(TokenType.NULL, readNull());
            case '"':
                return new token(TokenType.STRING, readString());
            case 't':
                return new token(TokenType.BOOLEAN, readBoolean("true"));
            case 'f':
                return new token(TokenType.BOOLEAN, readBoolean("false"));
            case '-':
                return new token(TokenType.NUMBER, readNumber(ch));

        }
        if (isDigit(ch)) {
            return new token(TokenType.NUMBER, readNumber(ch));
        }
        throw new JsonParseException("Unexpected character '" + ch + "'");

    }
    private String readNull() throws JsonParseException {
        if (!(reader.next() == 'u' && reader.next() == 'l' && reader.next() == 'l')) {
            throw new JsonParseException("Invalid json string");
        }

        return "null";
    }
    private String readString() throws JsonParseException {
        StringBuilder sb = new StringBuilder();
        for (;;) {
            char ch = reader.next();
            // 处理转义字符
            if (ch == '\\') {
                if (!isEscape()) {
                    throw new JsonParseException("Invalid escape character");
                }
                sb.append('\\');
                ch = reader.peek();
                sb.append(ch);
                // 处理 Unicode 编码，形如 \u4e2d。且只支持 \u0000 ~ \uFFFF 范围内的编码
                if (ch == 'u') {
                    for (int i = 0; i < 4; i++) {
                        ch = reader.next();
                        if (isHex(ch)) {
                            sb.append(ch);
                        } else {
                            throw new JsonParseException("Invalid character");
                        }
                    }
                }
            } else if (ch == '"') { // 碰到另一个双引号，则认为字符串解析结束，返回 Token
                return sb.toString();
            } else if (ch == '\r' || ch == '\n') { // 传入的 JSON 字符串不允许换行
                throw new JsonParseException("Invalid character");
            } else {
                sb.append(ch);
            }
        }
    }
    private boolean isEscape() {
        //判断是否是转义字符
        char ch = reader.peek();
        return ch == '"' || ch == '\\' || ch == '/' || ch == 'b' || ch == 'f' || ch == 'n' || ch == 'r' || ch == 't';
    }
    private boolean isHex(char ch) {
        //判断是否是十六进制字符
        return (ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F');
    }
    private String readBoolean(String expected) throws JsonParseException {
        if (expected.equals("true")) {
            if (reader.next() == 'r' && reader.next() == 'u' && reader.next() == 'e') {
                return "true";
            } else {
                throw new JsonParseException("Invalid boolean value");
            }
        }else if (expected.equals("false")) {

            if (reader.next() == 'a' && reader.next() == 'l' && reader.next() == 's' && reader.next() == 'e') {
                return "false";
            }
        }
        throw new JsonParseException("Invalid boolean value");
    }
    private String readNumber(char ch) throws JsonParseException {
        StringBuilder sb = new StringBuilder();
        //char ch = reader.peek();
        if (ch == '-') {
            sb.append(ch);
            ch = reader.next();

        }
        if (ch == '0') {
            sb.append(ch);
            ch = reader.peek();
            if (ch == 'x' || ch == 'X') {
                // 十六进制数字
                sb.append(ch);
                ch = reader.next();
                while (isHex(ch)) {
                    // 十六进制数字
                    sb.append(ch);
                    ch = reader.next();
                }
            } else {
                while (isDigit(ch)) {
                    // 十进制数字
                    sb.append(ch);
                    ch = reader.next();
                }
            }
        } else {
            while (isDigit(ch)) {
                sb.append(ch);
                ch = reader.next();
            }
            if (ch == '.') {
                sb.append(ch);
                ch = reader.next();
                while (isDigit(ch)) {
                    sb.append(ch);
                    ch = reader.next();
                }
            }
            if (ch == 'e' || ch == 'E') {
                sb.append(ch);
                ch = reader.next();
                if (ch == '+' || ch == '-') {
                    sb.append(ch);
                    ch = reader.next();
                }
                while (isDigit(ch)) {
                    sb.append(ch);
                    ch = reader.next();
                }
            }
        }
        return sb.toString();
    }
    private boolean isDigit(char ch) {
        //判断是否是数字
        return ch >= '0' && ch <= '9';
    }





    private boolean isWhiteSpace(char ch) {
        //判断是否是空白字符
        return ch =='\0' || ch == '\t' || ch == '\n' || ch == '\r' || ch == ' ';
    }


}
