package org.example;

public class charReader {

    private int pointer ;
    private String str;
    public charReader(String str) {
        this.str = str;
        this.pointer = 0;
    }
    public char next() {
        if (pointer < str.length()) {
            char c = str.charAt(pointer);
            pointer++;
            return c;
        }
        throw new RuntimeException("End of stream");
    }

    public char peek() {
        if (pointer < str.length()) {
            return str.charAt(pointer);
        }
        throw new RuntimeException("End of stream");
    }

    public boolean hasNext() {
        return pointer < str.length();
    }

}
