package com.lvb.baseApi.common.result;


/**
 * Created by 邓小顺 on 2019/4/25.
 */
public class ResultPage extends ReturnT {


    /// <summary>
    /// 返回的结果,大多数情况下是分页数据
    /// </summary>
    /// <value>
    /// The data.
    /// </value>
    Object data;


    public ResultPage(int code, String msg) {
        super(code, msg);
    }

    public ResultPage(int code, String msg,Object Data) {
        super(code, msg);
        data =  Data;
    }

    @Override
    public Object getData() {
        return data;
    }

    public void setData(ResultPageData data) {
        this.data = data;
    }
}
