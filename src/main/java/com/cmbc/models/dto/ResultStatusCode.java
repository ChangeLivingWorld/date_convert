package com.cmbc.models.dto;

import lombok.Getter;

@Getter
public enum ResultStatusCode {
    OK(200, "OK"),
    HTTP_ERROR_100(100, "1XX错误"),
    HTTP_ERROR_300(300, "3XX错误"),
    HTTP_ERROR_400(400, "4XX错误"),
    HTTP_ERROR_500(500, "5XX错误"),
    YEAR_ERROR(10000, "交易日年份输入错误");
    public int getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }


    private int code;
    private String msg;

    ResultStatusCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
