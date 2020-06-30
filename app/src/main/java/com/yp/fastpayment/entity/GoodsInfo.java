package com.yp.fastpayment.entity;

/**
 * @author cp
 * @date 2019-10-11
 * description：
 */
public class GoodsInfo {
    String goodsName;
    int goodsNum;
    String goodsPrice;

    public GoodsInfo(String goodsName, int goodsNum, String goodsPrice) {
        this.goodsName = goodsName;
        this.goodsNum = goodsNum;
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }
}
