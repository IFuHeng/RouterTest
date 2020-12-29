package com.changhong.telnettool.webinterface.been;

import com.alibaba.fastjson.JSONObject;

public class RequestBeen<T> {
    private String jsonrpc = "2.0";
    private int id = 1;
    private String method;

    private T params;

    public RequestBeen(T params) {
        this.params = params;
    }

    public RequestBeen(String method, T params) {
        this.method = method;
        this.params = params;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public int getId() {
        return id;
    }

    public String getMethod() {
        return method;
    }

    public T getParams() {
        return params;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setParams(T params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "RequestBeen{" +
                "jsonrpc='" + jsonrpc + '\'' +
                ", id=" + id +
                ", method='" + method + '\'' +
                ", params=" + params +
                '}';
    }

    public String toJsonString() {
        return JSONObject.toJSONString(this);
    }
}
