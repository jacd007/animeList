package com.zippyttech.animelist.data;

import android.provider.BaseColumns;

/**
 * Created by zippyttech on 21/08/17.
 */

public class AnimesSchedule {


    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    /**
     * Create tables of data base
     */
    public static final String SQL_CREATE_PRODUCT =
            "CREATE TABLE " +  Anime.TABLE_NAME + " (" +
                    Anime._ID + " INTEGER PRIMARY KEY," +
                    Anime.COLUMN_NAME_ID + INTEGER_TYPE  +  COMMA_SEP +
                    Anime.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    Anime.COLUMN_NAME_SUBNAME + TEXT_TYPE + COMMA_SEP +
                    Anime.COLUMN_NAME_CAPITULE + INTEGER_TYPE + COMMA_SEP +
                    Anime.COLUMN_NAME_STATUS + TEXT_TYPE + COMMA_SEP +
                    Anime.COLUMN_NAME_DATA + TEXT_TYPE + COMMA_SEP +
                    Anime.COLUMN_NAME_DATA_UPDATE + TEXT_TYPE + COMMA_SEP +
                    Anime.COLUMN_NAME_COLOR + TEXT_TYPE + COMMA_SEP +
                    Anime.COLUMN_NAME_IMAGE+ TEXT_TYPE + COMMA_SEP +
                    Anime.COLUMN_NAME_DAY+ TEXT_TYPE + COMMA_SEP +
                    Anime.COLUMN_NAME_DELETED+ TEXT_TYPE + COMMA_SEP +
                    Anime.COLUMN_NAME_ENABLED + TEXT_TYPE +
                    ")";

    public static final String SQL_CREATE_IMAGES=
            "CREATE TABLE " +  Images.TABLE_NAME + " (" +
                    Images._ID + " INTEGER PRIMARY KEY," +
                    Images.COLUMN_NAME_ID_ITEM + INTEGER_TYPE  +  COMMA_SEP +
                    Images.COLUMN_NAME_IMAGE + TEXT_TYPE  + COMMA_SEP +
                    Images.COLUMN_NAME_TITLE + TEXT_TYPE  +
                    ")";

//    public static final String ALTER_TABLES_COMPANY_ADD_DEBT = "" +
//            "ALTER TABLE "+Product.TABLE_NAME+" ADD COLUMN "+Product.COLUMN_NAME_DEBT+INTEGER_TYPE+" DEFAULT 0";
//
//    public static final String ALTER_TABLES_COMPANY_ADD_BALANCE = "" +
//            "ALTER TABLE "+Product.TABLE_NAME+" ADD COLUMN "+Product.COLUMN_NAME_BALANCE+INTEGER_TYPE+" DEFAULT 0";
    public static abstract class Anime implements BaseColumns {
        public static final String TABLE_NAME = "Product";
        public static final String COLUMN_NAME_ID= "idAnime";
        public static final String COLUMN_NAME = "nameAnime";
        public static final String COLUMN_NAME_SUBNAME = "subName";
        public static final String COLUMN_NAME_CAPITULE = "capitule";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_DATA = "dateCreated";
        public static final String COLUMN_NAME_DATA_UPDATE = "dateUpdate";
        public static final String COLUMN_NAME_COLOR = "color";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_DELETED= "deleted";
        public static final String COLUMN_NAME_ENABLED= "enabled";
        public static final String COLUMN_NAME_DAY = "day";

    }

    public static abstract class Images implements BaseColumns {
        public static final String TABLE_NAME = "images";
        public static final String COLUMN_NAME_ID_ITEM= "idItem";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_TITLE = "title";



    }



}
