package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class jsonObject
{
    private Map<String, Object> map=new HashMap<String, Object>();
    public void put(String key, Object value){
        map.put(key, value);
    }
    public Object get(String key){
        return map.get(key);
    }
    public List<String> keys(){
        List<String> list=new ArrayList<String>();
        for(String key:map.keySet()){
            list.add(key);
        }
        return list;
    }
    public jsonObject getJsonObject(String key){
        if (!map.containsKey(key)) {
            //检查是否存在该键
            throw new IllegalArgumentException("Invalid key");
        }
        Object value=map.get(key);
        if (!(value instanceof jsonObject)) {
            //检查该键对应的值是否为JsonObject
            throw new IllegalArgumentException("Value is not a JsonObject");
        }
        return (jsonObject)value;
    }
    public jsonArray getJsonArray(String key){
        if (!map.containsKey(key)) {
            throw new IllegalArgumentException("Invalid key");
        }
        Object value=map.get(key);
        if (!(value instanceof jsonArray)) {

            throw new IllegalArgumentException("Value is not a JsonArray");
        }
        return (jsonArray)value;
    }




}
