package com.cmbc.models.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "返回说明")
public class JsonResult<T> implements Serializable {

    private static final long serialVersionUID = 8256295583856967961L;
    /**
     * 返回的代码，200表示成功，其他表示失败
     */
    @ApiModelProperty(value = "返回的代码\n" +
            "200:成功\n" +
            "100, 1XX错误\n" +
            "300, 3XX错误,\n" +
            "400, 4XX错误,\n" +
            "500, 5XX错误,\n" +
            "10000, 交易日年份错误)")
    private int code;
    /**
     * 成功或失败时返回的信息
     */
    @ApiModelProperty(value = "描述信息")
    private String msg;
    /**
     * 成功时返回的数据信息
     */
    @ApiModelProperty(value = "数据")
    private T data;

    public JsonResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 推荐使用此种方法返回
     *
     * @param resultStatusCode 枚举信息
     * @param data             返回数据
     */
    public JsonResult(ResultStatusCode resultStatusCode, T data) {
        this(resultStatusCode.getCode(), resultStatusCode.getMsg(), data);
    }

    public JsonResult(int code, String msg) {
        this(code, msg, null);
    }

    public JsonResult(ResultStatusCode resultStatusCode) {
        this(resultStatusCode, null);
    }
    public static JsonResult StatusCode(ResultStatusCode resultStatusCode){
        return new JsonResult(resultStatusCode, null);
    }
    public static <T> JsonResult success(T data){
        return new JsonResult(ResultStatusCode.OK,data);
    }
    public static JsonResult faild(){
        return new JsonResult(ResultStatusCode.HTTP_ERROR_500);
    }
    public static <T> JsonResult faild(T data){
        return new JsonResult(ResultStatusCode.HTTP_ERROR_500,data);
    }
    public static JsonResult success(){
        return new JsonResult(ResultStatusCode.OK);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
