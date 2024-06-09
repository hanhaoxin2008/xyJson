# xyJson
一个超级简单的json解析器。本项目纯属造轮子娱乐，功能并不完善。
基本实现了json的解析。
# 使用
```java
String jsonstr;
gat g=new gat();
jsonObject jobject=g.getJsoonObject(jsonstr);

jsonObject jo1=(jsonObject)jobject.get("root");
or
jsonArray ja1=(jsonArray)jo1.get("root");
```
