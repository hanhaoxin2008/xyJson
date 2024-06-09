package org.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


public class jsonArray implements  Iterable
{
    private List Objects=new ArrayList();

    public void add(Object obj)
    {
        this.Objects.add(obj);
    }
    public Object get(int index){
        return this.Objects.get(index);
    }
    public int size(){
        return this.Objects.size();
    }
    public jsonObject getJsonObject(int index){
        if(this.Objects.get(index) instanceof jsonObject){
            return (jsonObject)this.Objects.get(index);
        }
        throw new ClassCastException("Object at index "+index+" is not a json object");
    }
    public jsonArray getJsonArray(int index){
        if(this.Objects.get(index) instanceof jsonArray){
            return (jsonArray)this.Objects.get(index);
        }
        throw new ClassCastException("Object at index "+index+" is not a json array");
    }




    @Override
    public Iterator iterator() {
        return this.Objects.iterator();
    }
}
