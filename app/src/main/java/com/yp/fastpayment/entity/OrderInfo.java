package com.yp.fastpayment.entity;

/**
 * @author cp
 * @date 2019-10-10
 * description：
 */
public class OrderInfo {
    String serialNum;
    double price;
    String orderNum;
    String date;
    int printState;

    public OrderInfo(String serialNum, double price, String orderNum, String date, int printState) {
        this.serialNum = serialNum;
        this.price = price;
        this.orderNum = orderNum;
        this.date = date;
        this.printState = printState;
    }


    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPrintState() {
        return printState;
    }

    public void setPrintState(int printState) {
        this.printState = printState;
    }
}
