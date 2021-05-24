package com.lvb.baseApi.common.result;


import com.lvb.baseApi.common.error.IErrorCode;

public class ResultVo {
    private Integer status;
    private Object data;
    private String message;

    public static ResultVo ok(Object data) {
        ResultVo resultDTO = new ResultVo();
        resultDTO.setStatus(200);
        resultDTO.setData(data);
        resultDTO.setMessage("请求成功");
        return resultDTO;
    }

    public static ResultVo fail(String message) {
        ResultVo resultDTO = new ResultVo();
        resultDTO.setStatus(400);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultVo fail(IErrorCode errorCode) {
        ResultVo resultDTO = new ResultVo();
        resultDTO.setStatus(errorCode.getCode());
        resultDTO.setMessage(errorCode.getMessage());
        return resultDTO;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
