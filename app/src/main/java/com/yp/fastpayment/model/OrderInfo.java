package com.yp.fastpayment.model;

import com.yp.fastpayment.api.response.MeshOrderItemVO;

import java.util.Date;
import java.util.List;

public class OrderInfo {
    private String orderNo;
    private String serial;
    private Integer customerId;
    private Integer itemCount;
    private Integer printState;
    private Integer reserveStatus;
    private String levelName;
    private String customerName;
    private String customerPhone;
    private String note;
    private String mealCode;
    private List<MeshOrderItemVO> orderItemList;
    private Long realfee;
    private Long totalfee;
    private int mealHourConfigId;
    private String mealHourConfigName;
    private Date paytime;

    public Integer getItemCount() {
        return itemCount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Long getTotalfee() {
        return totalfee;
    }

    public void setTotalfee(Long totalfee) {
        this.totalfee = totalfee;
    }


    public Integer getPrintState() {
        return printState;
    }

    public void setPrintState(Integer printState) {
        this.printState = printState;
    }

    public Integer getReserveStatus() {
        return reserveStatus;
    }

    public void setReserveStatus(Integer reserveStatus) {
        this.reserveStatus = reserveStatus;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public List<MeshOrderItemVO> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<MeshOrderItemVO> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public Long getRealfee() {
        return realfee;
    }

    public void setRealfee(Long realfee) {
        this.realfee = realfee;
    }

    public Date getPaytime() {
        return paytime;
    }

    public void setPaytime(Date paytime) {
        this.paytime = paytime;
    }

    public String getMealCode() {
        return mealCode;
    }

    public int getMealHourConfigId() {
        return mealHourConfigId;
    }

    public void setMealHourConfigId(int mealHourConfigId) {
        this.mealHourConfigId = mealHourConfigId;
    }

    public String getMealHourConfigName() {
        return mealHourConfigName;
    }

    public void setMealHourConfigName(String mealHourConfigName) {
        this.mealHourConfigName = mealHourConfigName;
    }

    public void setMealCode(String mealCode) {
        this.mealCode = mealCode;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "orderNo='" + orderNo + '\'' +
                ", serial='" + serial + '\'' +
                ", customerId=" + customerId +
                ", itemCount=" + itemCount +
                ", printState=" + printState +
                ", reserveStatus=" + reserveStatus +
                ", levelName='" + levelName + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", note='" + note + '\'' +
                ", mealCode='" + mealCode + '\'' +
                ", orderItemList=" + orderItemList +
                ", realfee=" + realfee +
                ", totalfee=" + totalfee +
                ", mealHourConfigId=" + mealHourConfigId +
                ", mealHourConfigName='" + mealHourConfigName + '\'' +
                ", paytime=" + paytime +
                '}';
    }
}
