package org.example;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class tokenReader
{
    private ArrayList<token> tokens=new ArrayList<token>();
    private int pos=0;

    public tokenReader(ArrayList<token> tokens){
        this.tokens=tokens;
        pos=0;
    }

    public token next(){
        if(pos<=tokens.size()){
            token t=tokens.get(pos);
            pos++;
            return t;
        }else{
            throw new RuntimeException("End of stream");
        }
    }
    public token peek(){
        if(pos<tokens.size()){
            return tokens.get(pos);
        }else{
            throw new RuntimeException("End of stream");
        }
    }
    public token peekPrevious(){
        if(pos>0){
            return tokens.get(pos-2);
        }else{
            throw new NoSuchElementException("No previous token");
        }
    }
    public void addToken(token t){
        tokens.add(t);
    }
    public boolean hasNext(){
        return pos<tokens.size();
    }
}
