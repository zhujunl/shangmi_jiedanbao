package com.yp.fastpayment.api.request;

public class PeriodRequest {
    @Override
    public String toString() {
        return "PeriodRequest{" +
                "shopId=" + shopId +
                ", branchId=" + branchId +
                '}';
    }

    private int shopId;
    private int branchId;

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }
}
