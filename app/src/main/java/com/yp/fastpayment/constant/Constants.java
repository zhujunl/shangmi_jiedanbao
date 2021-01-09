package com.yp.fastpayment.constant;

import com.yp.fastpayment.api.response.BranchVO;
import com.yp.fastpayment.api.response.OrderVO;
import com.yp.fastpayment.model.OrderInfo;

import java.util.List;

public class Constants {

    public static List<OrderVO> orderVOList;
    public static List<BranchVO> branchVOList;
    public static Integer shopId;
    public static Integer branchId;
    public static Integer employeeId;
    public static String branchName;
    public static String shopName;
    public static OrderInfo curOrderInfo;
    public static Boolean isAutoPrint=false;
    public static boolean newOrder = false;
}
