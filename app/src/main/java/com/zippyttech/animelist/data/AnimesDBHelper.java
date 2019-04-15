package com.zippyttech.animelist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zippyttech on 21/08/17.
 */

public class AnimesDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 9;
    public static final String DATABASE_NAME = "Animes.db";
    public Context context;


    public AnimesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(AnimesSchedule.SQL_CREATE_PRODUCT);
        sqLiteDatabase.execSQL(AnimesSchedule.SQL_CREATE_IMAGES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old, int i1) {
//       if(old<9){
//           sqLiteDatabase.execSQL(AnimesSchedule.ALTER_TABLES_COMPANY_ADD_BALANCE);
//           sqLiteDatabase.execSQL(AnimesSchedule.ALTER_TABLES_COMPANY_ADD_DEBT);
//
//       }

    }
}
