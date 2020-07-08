package com.yp.fastpayment.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // 数据库文件名
    public static final String DB_NAME = "shangmi92.db";

    // 数据库表名
    // 数据库版本号
    public static final int DB_VERSION = 8;


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    // 当数据库文件创建时，执行初始化操作，并且只执行一次
    @Override
    public void onCreate(SQLiteDatabase db) {

        // 建表 商户配置表
        String sql3 = "create table shopConfig" +
                "(id integer primary key autoincrement, " +
                "shopId integer, " +
                "branchId integer, " +
                "cashierDeskId integer ," +
                "shopName varchar ," +
                "username varchar, " +
                "autoLogin integer, " +
                "autoPrint integer, " +
                "password varchar "
                + ")";

        db.execSQL(sql3);


        // 建表 订单表
        String orderInfoSql = "create table mesh_order" +
                "(id integer primary key autoincrement, " +
                "shopId integer, " +
                "customerId integer, " +
                "branchId integer, " +
                "serial varchar, " +
                "orderNo varchar, " +
                "customerName varchar, " +
                "customerPhone varchar, " +
                "realfee integer, " +
                "paytime varchar, " +
                "printState integer," +
                "totalfee integer," +
                "itemCount integer," +
                "note varchar," +
                "mealCode varchar" +
                ")";

        db.execSQL(orderInfoSql);

        // 建表 订单明细表
        String orderItemSql = "create table order_item" +
                "(id integer primary key autoincrement, " +
                "orderNo varchar, " +
                "productId integer, " +
                "productName varchar, " +
                "price integer, " +
                "quantity integer" +
                ")";

        db.execSQL(orderItemSql);

    }

    // 当数据库版本更新执行该方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("oldVersion : " + oldVersion);
    }

}
