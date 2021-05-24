package com.lvb.baseApi.common.result;



import java.util.List;


public class ResultPageData<T> {

    /// <summary>
    /// 总页码
    /// </summary>
    long totalPage;

    /// <summary>
    /// 返回的结果,大多数情况下是分页数据
    /// </summary>
    List<T> data;

    ///当前页码
    long currentPage;

    ///每页数量
    long totalSize ;



    public ResultPageData(long Current, long Total, long Pages, List<T> Data) {
        currentPage = Current;
        data =  Data;
        totalSize = Total;
        totalPage = Pages;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }
}
