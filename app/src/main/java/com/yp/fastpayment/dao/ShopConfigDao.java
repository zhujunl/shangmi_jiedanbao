package com.yp.fastpayment.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yp.fastpayment.model.ShopConfig;
import com.yp.fastpayment.util.DBHelper;




public class ShopConfigDao {

    private DBHelper mHelper;
    private SQLiteDatabase mDatabase;

    public ShopConfigDao(Context context) {
        mHelper = new DBHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    public void insertData(Integer shopId, Integer cashierDeskId, Integer branchId,
                           String shopName, String username, String password) {
        delete();

        ContentValues values = new ContentValues();
        values.put("shopId", shopId);
        values.put("branchId", branchId);
        values.put("cashierDeskId", cashierDeskId);
        values.put("shopName", shopName);
        values.put("username", username);
        values.put("autoLogin", 1);
        values.put("autoPrint", 0);
        values.put("password", password);
        mDatabase.insert("shopConfig", null, values);
    }

    public void updateExit(){
        ContentValues values = new ContentValues();
        values.put("autoLogin", "0");
        mDatabase.update("shopConfig", values, " id > 0 ", null);
    }


    public void updatePrintState(int state){
        ContentValues values = new ContentValues();
        values.put("autoPrint", state);
        mDatabase.update("shopConfig", values, " id > 0 ", null);
    }

    public void delete() {
        mDatabase.delete("shopConfig", "id > 0", new String[]{});
    }

    /**
     * // 建表 商户配置表
     *         String sql3 = "create table shopConfig" +
     *                 "(id integer primary key autoincrement, " +
     *                 "shopId integer, " +
     *                 "branchId integer, " +
     *                 "cashierDeskId integer ," +
     *                 "shopName varchar ," +
     *                 "username varchar, " +
     *                 "autoLogin integer, " +
     *                 "autoPrint integer "
     *                 + ")";
     */
    public ShopConfig query() {
        String sql = "select * from shopConfig";
        Cursor cursor = mDatabase.rawQuery(sql, null);

        if (cursor == null) {
            return null;
        }

        while (cursor.moveToNext()) {
            ShopConfig shopConfig = new ShopConfig();
            shopConfig.setShopId(cursor.getInt(1));
            shopConfig.setBranchId(cursor.getInt(2));
            shopConfig.setCashierDeskId(cursor.getInt(3));
            shopConfig.setShopName(cursor.getString(4));
            shopConfig.setUsername(cursor.getString(5));
            shopConfig.setAutoLogin(cursor.getInt(6));
            shopConfig.setAutoPrint(cursor.getInt(7));
            shopConfig.setPassword(cursor.getString(8));
            return shopConfig;
        }

        return null;
    }


}
