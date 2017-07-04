package org.tongwoo.util;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by tw on 2017/7/3.
 */
public class MyJsonObject {
    private JSONObject obj;

    public MyJsonObject(JSONObject jsonObject){
        this.obj = jsonObject;
    }

    public Long getLong(String key) {
        Long val = obj.getLong(key);
        if(val == 0) val = 19700101000000L;
        return val;
    }

    public String getString(String key) {
        String str =  obj.getString(key);
        if(str == null) str = "";
        else if("0".equals(str)) if(key.toLowerCase().indexOf("time")>-1) str = "19700101000000";
        return str;
    }

    public int getIntValue(String key) {
        return obj.getIntValue(key);
    }

    public Integer getInteger(String key) {
        return obj.getInteger(key);
    }

    public double getDoubleValue(String key) {
        return obj.getDoubleValue(key);
    }

    public long getLongValue(String key) {
        return obj.getLongValue(key);
    }

    public boolean containsKey(String key) {
        return obj.containsKey(key);
    }

    public String toJSONString() {
        return obj.toJSONString();
    }
}
