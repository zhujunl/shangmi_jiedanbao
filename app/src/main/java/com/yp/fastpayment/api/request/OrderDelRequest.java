package com.yp.fastpayment.api.request;


import java.util.List;

//{
//        "page_num": 1,
//        "page_size": 30,
//        "params": {
//        "shopId": "23",
//        "branchId":"9"
//        },
//        "sql_id": "GET-CANCLE-RESERVER-ORDERNOS"
//        }
public class OrderDelRequest {
    @Override
    public String toString() {
        return "OrderDelRequest{" +
                "page_num=" + page_num +
                ", page_size=" + page_size +
                ", params=" + params +
                ", sql_id='" + sql_id + '\'' +
                '}';
    }

    private int page_num;
    private int page_size;
    private PeriodRequest params;
    private String sql_id;

    public int getPage_num() {
        return page_num;
    }

    public void setPage_num(int page_num) {
        this.page_num = page_num;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public PeriodRequest getParams() {
        return params;
    }

    public void setParams(PeriodRequest params) {
        this.params = params;
    }

    public String getSql_id() {
        return sql_id;
    }

    public void setSql_id(String sql_id) {
        this.sql_id = sql_id;
    }
}
