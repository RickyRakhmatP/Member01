package com.skybiz.member01.m_Database.m_Local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 7 on 30/10/2017.
 */


public class DBHelper extends SQLiteOpenHelper {
    private static final long BYTES_IN_A_MEGABYTE = 1048576;
    /**
     * Maximum size of the database in bytes
     */
    private final long mMaxSize;

    public DBHelper(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        mMaxSize = BYTES_IN_A_MEGABYTE * 8;
    }
    //TABLE CREATION
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.setMaximumSize(mMaxSize);
            db.execSQL(Constants.CREATE_TB);
            db.execSQL(Constants.CREATE_STK_MASTER);
            db.execSQL(Constants.CREATE_STK_ITEMLOCATION);
            db.execSQL(Constants.CREATE_OSTK_STKCHECK_DT);
            db.execSQL(Constants.CREATE_DUMLIST);
            db.execSQL(Constants.CREATE_COMPANYSETUP);
            db.execSQL(Constants.CREATE_STK_GROUP);
            db.execSQL(Constants.CREATE_STK_TAX);
            db.execSQL(Constants.CREATE_SYS_RUNNO_DT);
            db.execSQL(Constants.CREATE_CLOUD);
            db.execSQL(Constants.CREATE_STK_INVENTORY_DT);
            db.execSQL(Constants.CREATE_STK_INVENTORY_HD);
            db.execSQL(Constants.CREATE_CUSTOMER);
            db.execSQL(Constants.CREATE_TBMEMBER);
            db.execSQL(Constants.CREATE_SYS_GENERAL_SETUP2);
            db.execSQL(Constants.CREATE_STK_DETAIL_TRN_OUT);
            db.execSQL(Constants.CREATE_STK_GRNDO_DT);
            db.execSQL(Constants.CREATE_STK_GRNDO_HD);
            db.execSQL(Constants.CREATE_STK_DETAIL_TRN_IN);
            db.execSQL(Constants.CREATE_TBLOC);
            db.execSQL(Constants.INSERT_PREFIX1);
            db.execSQL(Constants.INSERT_PREFIX2);
            db.execSQL(Constants.INSERT_PREFIX3);
            db.execSQL(Constants.INSERT_PREFIX4);
            db.execSQL(Constants.INSERT_SETTINGDB);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //TABLE UPGRADE
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("UPGRADE DB","Old Version "+ String.valueOf(oldVersion));
        if(newVersion>oldVersion){

        }
        onCreate(db);
    }

}
