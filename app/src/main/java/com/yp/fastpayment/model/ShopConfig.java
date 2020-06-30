package com.yp.fastpayment.model;

public class ShopConfig {
    private Integer shopId;
    private Integer branchId;
    private Integer cashierDeskId;
    private String shopName;
    private String password;
    private String username;
    private Integer autoLogin;
    private Integer autoPrint;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getAutoPrint() {
        return autoPrint;
    }

    public void setAutoPrint(Integer autoPrint) {
        this.autoPrint = autoPrint;
    }

    public Integer getAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(Integer autoLogin) {
        this.autoLogin = autoLogin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getCashierDeskId() {
        return cashierDeskId;
    }

    public void setCashierDeskId(Integer cashierDeskId) {
        this.cashierDeskId = cashierDeskId;
    }
}
