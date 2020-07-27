package com.yp.fastpayment.util;

import android.text.TextUtils;

import com.yp.fastpayment.api.response.MeshOrderItemVO;
import com.yp.fastpayment.constant.Constants;
import com.yp.fastpayment.dao.OrderInfoDao;
import com.yp.fastpayment.model.OrderInfo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ESCUtil {

    public static final byte ESC = 27;// 换码
    public static final byte FS = 28;// 文本分隔符
    public static final byte GS = 29;// 组分隔符
    public static final byte DLE = 16;// 数据连接换码
    public static final byte EOT = 4;// 传输结束
    public static final byte ENQ = 5;// 询问字符
    public static final byte SP = 32;// 空格
    public static final byte HT = 9;// 横向列表
    public static final byte LF = 10;// 打印并换行（水平定位）
    public static final byte CR = 13;// 归位键
    public static final byte FF = 12;// 走纸控制（打印并回到标准模式（在页模式下） ）
    public static final byte CAN = 24;// 作废（页模式下取消打印数据 ）

    // ------------------------打印机初始化-----------------------------

    /**
     * 打印机初始化
     *
     * @return
     */
    public static byte[] init_printer() {
        byte[] result = new byte[2];
        result[0] = ESC;
        result[1] = 64;
        return result;
    }

    // ------------------------换行-----------------------------

    /**
     * 换行
     * 要换几行
     *
     * @param lineNum
     * @return
     */
    public static byte[] nextLine(int lineNum) {
        byte[] result = new byte[lineNum];
        for (int i = 0; i < lineNum; i++) {
            result[i] = LF;
        }

        return result;
    }

    // ------------------------下划线-----------------------------

    /**
     * 绘制下划线（1点宽）
     *
     * @return
     */
    public static byte[] underlineWithOneDotWidthOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 1;
        return result;
    }

    /**
     * 绘制下划线（2点宽）
     *
     * @return
     */
    public static byte[] underlineWithTwoDotWidthOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 2;
        return result;
    }

    /**
     * 取消绘制下划线
     *
     * @return
     */
    public static byte[] underlineOff() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 0;
        return result;
    }

    // ------------------------加粗-----------------------------

    /**
     * 选择加粗模式
     *
     * @return
     */
    public static byte[] boldOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0xF;
        return result;
    }

    /**
     * 取消加粗模式
     *
     * @return
     */
    public static byte[] boldOff() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0;
        return result;
    }

    // ------------------------对齐-----------------------------

    /**
     * 左对齐
     *
     * @return
     */
    public static byte[] alignLeft() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 0;
        return result;
    }

    /**
     * 居中对齐
     *
     * @return
     */
    public static byte[] alignCenter() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 1;
        return result;
    }

    /**
     * 右对齐
     *
     * @return
     */
    public static byte[] alignRight() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 2;
        return result;
    }

    /**
     * 水平方向向右移动col列
     *
     * @param col
     * @return
     */
    public static byte[] set_HT_position(byte col) {
        byte[] result = new byte[4];
        result[0] = ESC;
        result[1] = 68;
        result[2] = col;
        result[3] = 0;
        return result;
    }
    // ------------------------字体变大-----------------------------

    /**
     * 字体变大为标准的n倍
     *
     * @param num
     * @return
     */
    public static byte[] fontSizeSetBig(int num) {
        byte realSize = 0;
        switch (num) {
            case 1:
                realSize = 0;
                break;
            case 2:
                realSize = 17;
                break;
            case 3:
                realSize = 34;
                break;
            case 4:
                realSize = 51;
                break;
            case 5:
                realSize = 68;
                break;
            case 6:
                realSize = 85;
                break;
            case 7:
                realSize = 102;
                break;
            case 8:
                realSize = 119;
                break;
        }
        byte[] result = new byte[3];
        result[0] = 29;
        result[1] = 33;
        result[2] = realSize;
        return result;
    }

    // ------------------------字体变小-----------------------------

    /**
     * 字体取消倍宽倍高
     *
     * @param num
     * @return
     */
    public static byte[] fontSizeSetSmall(int num) {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 33;

        return result;
    }

    // ------------------------切纸-----------------------------

    /**
     * 进纸并全部切割
     *
     * @return
     */
    public static byte[] feedPaperCutAll() {
        byte[] result = new byte[4];
        result[0] = GS;
        result[1] = 86;
        result[2] = 65;
        result[3] = 0;
        return result;
    }

    /**
     * 进纸并切割（左边留一点不切）
     *
     * @return
     */
    public static byte[] feedPaperCutPartial() {
        byte[] result = new byte[4];
        result[0] = GS;
        result[1] = 86;
        result[2] = 66;
        result[3] = 0;
        return result;
    }

    // ------------------------切纸-----------------------------
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    public static byte[] byteMerger(byte[][] byteList) {

        int length = 0;
        for (int i = 0; i < byteList.length; i++) {
            length += byteList[i].length;
        }
        byte[] result = new byte[length];

        int index = 0;
        for (int i = 0; i < byteList.length; i++) {
            byte[] nowByte = byteList[i];
            for (int k = 0; k < byteList[i].length; k++) {
                result[index] = nowByte[k];
                index++;
            }
        }
        for (int i = 0; i < index; i++) {
            // CommonUtils.LogWuwei("", "result[" + i + "] is " + result[i]);
        }
        return result;
    }

    // --------------------
    public static byte[] generateMockData(OrderInfo orderInfo, byte[] code) {
        try {
            List<MeshOrderItemVO> orderItemVOList = orderInfo.getOrderItemList();
            byte[] next2Line = ESCUtil.nextLine(2);
            byte[] title = Constants.shopName.getBytes("gb2312");
            byte[] branchName = Constants.branchName.getBytes("gb2312");
//			byte[] title = "这是门店名  **中心店".getBytes("gb2312");

            byte[] boldOn = ESCUtil.boldOn();
            byte[] fontSize2Big = ESCUtil.fontSizeSetBig(2);
            byte[] center = ESCUtil.alignCenter();

            ////支付类型 0：二维码支付，1：人脸支付，2：实体卡支付，3：其他支付,  4:商户扫码支付

            byte[] Focus = ("取餐号：" + orderInfo.getSerial()).getBytes("gb2312");
            byte[] mealCode = ("取餐码：" + orderInfo.getMealCode()).getBytes("gb2312");
            byte[] boldOff = ESCUtil.boldOff();
            byte[] fontSize2Small = ESCUtil.fontSizeSetSmall(2);

            byte[] Note = ("备注：" + orderInfo.getNote()).getBytes("gb2312");
            boldOff = ESCUtil.boldOff();
            byte[] fontSize1SmallNote = ESCUtil.fontSizeSetBig(2);

            byte[] left = ESCUtil.alignLeft();
            byte[] orderSerinum = ("订单编号：" + orderInfo.getOrderNo()).getBytes("gb2312");
            boldOn = ESCUtil.boldOn();
            byte[] fontSize1Big = ESCUtil.fontSizeSetBig(1);
            byte[] fontSize3Big = ESCUtil.fontSizeSetBig(2);
            byte[] FocusOrderContent = ("手机号:" + orderInfo.getCustomerPhone()).getBytes("gb2312");
            boldOff = ESCUtil.boldOff();
            byte[] fontSize1Small = ESCUtil.fontSizeSetSmall(2);

            next2Line = ESCUtil.nextLine(2);

            List<byte[]> orderDetailList = new ArrayList<>();
            for (MeshOrderItemVO meshOrderItemVO : orderInfo.getOrderItemList()) {
                byte[] temp = (meshOrderItemVO.getProductName() + "     X" + meshOrderItemVO.getQuantity()).getBytes("gb2312");

                orderDetailList.add(temp);
            }
            byte[] priceInfo = ("应收:" + orderInfo.getTotalfee() + "元 优惠："
                    + orderInfo.getRealfee() + "元 ").getBytes("gb2312");
            byte[] nextLine = ESCUtil.nextLine(1);

            byte[] priceShouldPay = ("实收:" + orderInfo.getRealfee() + "元").getBytes("gb2312");
            nextLine = ESCUtil.nextLine(1);

            byte[] takeTime = "".getBytes("gb2312");
//			byte[] takeTime = "取餐时间:2015-02-13 12:51:59".getBytes("gb2312");
            nextLine = ESCUtil.nextLine(1);
            byte[] setOrderTime = ("下单时间：" + OrderInfoDao.simpleDateFormat.format(orderInfo.getPaytime()))
                    .getBytes("gb2312");

            byte[] tips_1 = "请关注'云澎智能'小程序".getBytes("gb2312");
            nextLine = ESCUtil.nextLine(1);
            byte[] tips_2 = "".getBytes("gb2312");
            byte[] next4Line = ESCUtil.nextLine(4);

            byte[] breakPartial = ESCUtil.feedPaperCutPartial();

            //备注为null不打印
            //取餐号为空不打印

//            byte[][] cmdBytes = new byte[][]{title, nextLine, branchName, next2Line, center, boldOn, fontSize2Big,
//                    TextUtils.isEmpty(orderInfo.getSerial()) ? ESCUtil.nextLine(0) : Focus, boldOff, fontSize3Big,
//                    next2Line,
//                    TextUtils.isEmpty(orderInfo.getNote()) ? ESCUtil.nextLine(0) : Note, boldOff,
//                    next2Line, left, fontSize1Small, orderSerinum, fontSize1SmallNote, nextLine, center, boldOn,
//                    fontSize1Big, FocusOrderContent, boldOff,
//                    fontSize1Big, nextLine, left, next2Line, center, boldOn, fontSize1Big};
            System.out.println("备注："+orderInfo.getNote());
            byte [] separator = "                                                                                ".getBytes("gb2312");
            byte[][] cmdBytes = new byte[][]{
                    separator,
                    nextLine,center, fontSize1Big,title,
                    nextLine,center, fontSize2Big,branchName,
                    nextLine,center, fontSize2Big,TextUtils.isEmpty(orderInfo.getSerial()) ? ESCUtil.nextLine(0) : Focus,
                    nextLine,center, fontSize1Big,TextUtils.isEmpty(orderInfo.getNote()) ? ESCUtil.nextLine(0) : Note,
                    nextLine,center, fontSize1Big,orderSerinum,
                    nextLine,center, fontSize1Big,
                    nextLine};

//            byte[][] test = {
//                    code ==  null ? ESCUtil.nextLine(0) : code,
//                    TextUtils.isEmpty(orderInfo.getSerial()) ? ESCUtil.nextLine(0) : next2Line,
//                    boldOff, next2Line, fontSize1Small, takeTime,
//                    nextLine, setOrderTime, next2Line, center,
//                    tips_1, nextLine, center, tips_2, next4Line,m
//                    breakPartial};

            byte[][] test = {
                    nextLine,code ==  null ? ESCUtil.nextLine(0) : code,
                    nextLine,center, fontSize2Big,TextUtils.isEmpty(orderInfo.getMealCode()) ? ESCUtil.nextLine(0) : mealCode,
                    nextLine,fontSize1Big,TextUtils.isEmpty(orderInfo.getSerial()) ? nextLine : next2Line,center, setOrderTime,
                    next2Line,fontSize1Big,center, tips_1,
                    nextLine,separator,
                    nextLine,separator,
                    breakPartial};

            byte[] result = ESCUtil.byteMerger(cmdBytes);
            byte[] result2 = ESCUtil.byteMerger(test);
            for (byte[] temp : orderDetailList) {
                byte[][] tempArray = {temp, nextLine};
                byte[] tempMerge = ESCUtil.byteMerger(tempArray);
                result = ESCUtil.byteMerger(result, tempMerge);
            }

            return ESCUtil.byteMerger(result, result2);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}