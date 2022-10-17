package com.jm.crm.commons.pojo;

public class ReturnObject {
    private String code;//标记 1-成功 0-失败
    private String message;//提示信息
    private Object other;//其他数据

    @Override
    public String toString() {
        return "ReturnObject{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", other=" + other +
                '}';
    }

    public ReturnObject(String code, String message, Object other) {
        this.code = code;
        this.message = message;
        this.other = other;
    }

    public ReturnObject() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getOther() {
        return other;
    }

    public void setOther(Object other) {
        this.other = other;
    }
}
