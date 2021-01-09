package com.yp.fastpayment.api.response;

import java.util.List;

public class OrderDelResponse {
    private int code;
    private String message;
    private OrderDelVO data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OrderDelVO getData() {
        return data;
    }

    public void setData(OrderDelVO data) {
        this.data = data;
    }
}
