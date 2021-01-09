package com.yp.fastpayment.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yp.fastpayment.api.response.MeshOrderItemVO;
import com.yp.fastpayment.api.response.OrderVO;
import com.yp.fastpayment.model.OrderInfo;
import com.yp.fastpayment.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class OrderInfoDao {

    private DBHelper mHelper;
    private SQLiteDatabase mDatabase;

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public OrderInfoDao(Context context) {
        mHelper = new DBHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    /**
     * // 建表 订单表
     *      String orderInfoSql = "create table mesh_order" +
     *                 "(id integer primary key autoincrement, " +
     *                 "shopId integer, " +
     *                 "customerId integer, " +
     *                 "branchId integer, " +
     *                 "serial varchar, " +
     *                 "orderNo varchar, " +
     *                 "customerName varchar, " +
     *                 "customerPhone varchar, " +
     *                 "realfee integer, " +
     *                 "paytime varchar, " +
     *                 "printState integer," +
     *                 "totalfee integer," +
     *                 "itemCount integer," +
     *                 "note varchar," +
     *                 "mealCode varchar," +
     *                 "mealHourConfigId integer,"+
     *                 "mealHourConfigName varchar,"+
     *                 "reserveStatus integer,"+
     *                 "levelName varchar"+
     *                 ")";
     */
    public void insertData(OrderVO orderVO, Integer shopId, Integer branchId) {
        if (orderVO.getOrderItemList() == null) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put("shopId", shopId);
        values.put("customerId", orderVO.getCustomerId());
        values.put("branchId", branchId);
        values.put("serial", orderVO.getSerial());
        values.put("mealCode", orderVO.getMealCode() != null ? orderVO.getMealCode() : "");
        values.put("orderNo", orderVO.getOrderNo());
        values.put("customerName", orderVO.getCustomerName());
        values.put("customerPhone", orderVO.getCustomerPhone());
        values.put("realfee", orderVO.getRealfee());
        values.put("paytime", orderVO.getPaytime());
        values.put("printState", 0);
        values.put("reserveStatus",orderVO.getReserveStatus());
        values.put("levelName",orderVO.getLevelName());
        values.put("totalfee", orderVO.getTotalfee());
        values.put("itemCount", orderVO.getItemCount());
        values.put("mealHourConfigId",orderVO.getMealHourConfigId());
        values.put("mealHourConfigName",orderVO.getMealHourConfigName());
        values.put("note", orderVO.getNote());
        mDatabase.insert("mesh_order", null, values);

        for (MeshOrderItemVO itemVO : orderVO.getOrderItemList()) {

            ContentValues orderItem = new ContentValues();

            orderItem.put("orderNo", orderVO.getOrderNo());
            orderItem.put("productId", itemVO.getProductId());
            orderItem.put("productName", itemVO.getProductName());
            orderItem.put("price", itemVO.getPrice());
            orderItem.put("quantity", itemVO.getQuantity());

            mDatabase.insert("order_item", null, orderItem);
        }
    }


    public void updatePrintState(int state, String orderNo) {
        ContentValues values = new ContentValues();
        values.put("printState", state);
        mDatabase.update("mesh_order", values, " orderNo = '" + orderNo + "'", null);
    }
    public void updateState(int state, String orderNo) {
        ContentValues values = new ContentValues();
        values.put("reserveStatus", state);
        mDatabase.update("mesh_order", values, " orderNo = '" + orderNo + "'", null);
    }

    public OrderInfo queryOrderByOrderNo(String orderNo) {
        String sql = "select * from mesh_order where orderNo = ?";
        Cursor cursor = mDatabase.rawQuery(sql, new String[]{orderNo});

        if (cursor == null) {
            return null;
        }

        while (cursor.moveToNext()) {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setCustomerId(cursor.getInt(2));
            orderInfo.setSerial(cursor.getString(4));
            orderInfo.setOrderNo(cursor.getString(5));
            orderInfo.setCustomerName(cursor.getString(6));
            orderInfo.setCustomerPhone(cursor.getString(7));
            orderInfo.setRealfee(cursor.getLong(8));
            orderInfo.setPaytime(new Date(cursor.getLong(9)));
            orderInfo.setPrintState(cursor.getInt(10));
            orderInfo.setTotalfee(cursor.getLong(11));
            orderInfo.setItemCount(cursor.getInt(12));
            orderInfo.setMealCode(cursor.getString(14));
            orderInfo.setMealHourConfigId(cursor.getInt(15));
            orderInfo.setMealHourConfigName(cursor.getString(16));
            orderInfo.setReserveStatus(cursor.getInt(17));
            orderInfo.setLevelName(cursor.getString(18));
            try {

                orderInfo.setNote(cursor.getString(13));
            } catch (Exception e) {
                orderInfo.setNote("");
            }

            return orderInfo;
        }

        return null;
    }

    public List<OrderInfo> query(Integer shopId, Integer branchId) {
        String sql = "select * from mesh_order where shopId = ? and branchId = ? order by id desc";
        Cursor cursor = mDatabase.rawQuery(sql, new String[]{shopId.toString(), branchId.toString()});

        if (cursor == null) {
            return null;
        }

        List<OrderInfo> orderInfoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            OrderInfo orin = new OrderInfo();
            orin.setCustomerId(cursor.getInt(2));
            orin.setSerial(cursor.getString(4));
            orin.setOrderNo(cursor.getString(5));
            orin.setCustomerName(cursor.getString(6));
            orin.setCustomerPhone(cursor.getString(7));
            orin.setRealfee(cursor.getLong(8));
            orin.setPaytime(new Date(cursor.getLong(9)));
            orin.setPrintState(cursor.getInt(10));
            orin.setTotalfee(cursor.getLong(11));
            orin.setItemCount(cursor.getInt(12));
            orin.setMealCode(cursor.getString(14));
            orin.setMealHourConfigId(cursor.getInt(15));
            orin.setMealHourConfigName(cursor.getString(16));
            orin.setReserveStatus(cursor.getInt(17));
            orin.setLevelName(cursor.getString(18));
            try {
                orin.setNote(cursor.getString(13));
            } catch (Exception e) {
                orin.setNote("");
            }
            List<MeshOrderItemVO> orderItemVOList = queryByOrderNo(orin.getOrderNo());
            orin.setOrderItemList(orderItemVOList);
            orderInfoList.add(orin);
        }
        return orderInfoList;
    }

    public List<MeshOrderItemVO> queryByOrderNo(String orderNo) {
        String sql = "select * from order_item where orderNo = '" + orderNo + "'";
        Cursor cursor = mDatabase.rawQuery(sql, null);

        if (cursor == null) {
            return null;
        }
        List<MeshOrderItemVO> orderItemVOList = new ArrayList<>();
        while (cursor.moveToNext()) {

            MeshOrderItemVO orderItemVO = new MeshOrderItemVO();

            orderItemVO.setProductId(cursor.getInt(2));
            orderItemVO.setProductName(cursor.getString(3));
            orderItemVO.setPrice(Long.valueOf(cursor.getInt(4)));
            orderItemVO.setQuantity(cursor.getInt(5));
            orderItemVOList.add(orderItemVO);

        }
        return orderItemVOList;
    }

    public List<OrderInfo> querymeal(String name){
        String sql = "select * from mesh_order where mealHourConfigName = ?  order by id desc";
        Cursor cursor = mDatabase.rawQuery(sql,  new String[]{name});

        if (cursor == null) {
            return null;
        }
        List<OrderInfo> orderInfoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            OrderInfo orin = new OrderInfo();
            orin.setCustomerId(cursor.getInt(2));
            orin.setSerial(cursor.getString(4));
            orin.setOrderNo(cursor.getString(5));
            orin.setCustomerName(cursor.getString(6));
            orin.setCustomerPhone(cursor.getString(7));
            orin.setRealfee(cursor.getLong(8));
            orin.setPaytime(new Date(cursor.getLong(9)));
            orin.setPrintState(cursor.getInt(10));
            orin.setTotalfee(cursor.getLong(11));
            orin.setItemCount(cursor.getInt(12));
            orin.setMealCode(cursor.getString(14));
            orin.setMealHourConfigId(cursor.getInt(15));
            orin.setMealHourConfigName(cursor.getString(16));
            orin.setReserveStatus(cursor.getInt(17));
            orin.setLevelName(cursor.getString(18));
            try {
                orin.setNote(cursor.getString(13));
            } catch (Exception e) {
                orin.setNote("");
            }
            List<MeshOrderItemVO> orderItemVOList = queryByOrderNo(orin.getOrderNo());
            orin.setOrderItemList(orderItemVOList);
            orderInfoList.add(orin);
        }
        return orderInfoList;
    }

    public List<OrderInfo> queryrNo(String name){
        String sql = "select * from mesh_order where orderNo = ?  order by id desc";
        Cursor cursor = mDatabase.rawQuery(sql,  new String[]{name});

        if (cursor == null) {
            return null;
        }
        List<OrderInfo> orderInfoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            OrderInfo orin = new OrderInfo();
            orin.setCustomerId(cursor.getInt(2));
            orin.setSerial(cursor.getString(4));
            orin.setOrderNo(cursor.getString(5));
            orin.setCustomerName(cursor.getString(6));
            orin.setCustomerPhone(cursor.getString(7));
            orin.setRealfee(cursor.getLong(8));
            orin.setPaytime(new Date(cursor.getLong(9)));
            orin.setPrintState(cursor.getInt(10));
            orin.setTotalfee(cursor.getLong(11));
            orin.setItemCount(cursor.getInt(12));
            orin.setMealCode(cursor.getString(14));
            orin.setMealHourConfigId(cursor.getInt(15));
            orin.setMealHourConfigName(cursor.getString(16));
            orin.setReserveStatus(cursor.getInt(17));
            orin.setLevelName(cursor.getString(18));
            try {
                orin.setNote(cursor.getString(13));
            } catch (Exception e) {
                orin.setNote("");
            }
            List<MeshOrderItemVO> orderItemVOList = queryByOrderNo(orin.getOrderNo());
            orin.setOrderItemList(orderItemVOList);
            orderInfoList.add(orin);
        }
        return orderInfoList;
    }

    public void delete() {
        mDatabase.delete("mesh_order", "id > 0", new String[]{});
        mDatabase.delete("order_item", "id > 0", new String[]{});
    }

    public void deleteByOrderno(String orderno){
        mDatabase.delete("mesh_order","orderNo = ?",new String[]{orderno});
    }
}
