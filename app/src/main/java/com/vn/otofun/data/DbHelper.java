package com.vn.otofun.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.internal.LinkedTreeMap;
import com.vn.otofun.utils.VLog;
import com.vn.otofun.utils.Configs;
import com.vn.otofun.utils.MapHelper;

import java.util.ArrayList;
import java.util.HashMap;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


public class DbHelper extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "vsm.db";
	private static final int DATABASE_VERSION = 1;

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}



    public String getLastChangeTimeCompany() {
		VLog.d("");
        String maxTime = null;
        SQLiteDatabase db = getReadableDatabase();


		Cursor c = db.rawQuery("SELECT MAX(`change_time`) FROM `company`", null);
        if (c.moveToFirst()) {
            maxTime = c.getString(0);
        }
        c.close();
        db.close();
		VLog.d("last change time company: " + maxTime);
        return maxTime;
    }


//	public void addOrUpdateStock(String mack, String tenCty, boolean enabled, String maNganh, int maSan, long time) {
//		VLog.d("");
//		SQLiteDatabase db = getReadableDatabase();
//        Cursor c = db.query("company", new String[]{ "COUNT(`symbol`)" }, "symbol=?", new String[] { mack }, null, null, null);
//        boolean existed = c.moveToFirst() && c.getInt(0) > 0;
//        c.close();
//
//        ContentValues vals = new ContentValues();
//        vals.put("tencongty", tenCty);
//        vals.put("manganh", maNganh);
//        vals.put("masan", maSan);
//        vals.put("enable", enabled ? 1 : 0);
//        vals.put("time", String.valueOf(time));
//
//        if (existed) {
//            VLog.d("DbHelper", "Updating stock: " + mack + "; nganh="+maNganh+"; san=" + maSan + "; enable="+enabled + "; time="+time);
//            db.update("congty", vals, "mack = ?", new String[] { mack });
//        } else {
////            VLog.d("DbHelper", "Adding stock: " + mack + "; nganh="+maNganh+"; san=" + maSan + "; enable="+enabled + "; time="+time);
//			VLog.d("DbHelper", "INSERT INTO `congty` VALUES('" + mack + ",'"+tenCty+"',"+maNganh+"," + maSan + ",0,1,'"+time+"',0);");
//            vals.put("mack", mack);
//            db.insert("congty", null, vals);
//        }
//
//        db.close();
//    }





}
