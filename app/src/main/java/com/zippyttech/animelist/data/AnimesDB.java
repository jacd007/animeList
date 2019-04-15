package com.zippyttech.animelist.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.zippyttech.animelist.common.utility.Utils;
import com.zippyttech.animelist.model.ImageGallery;
import com.zippyttech.animelist.model.Pendings;
import com.zippyttech.animelist.model.Animes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zippyttech on 21/08/17.
 */

public class AnimesDB {

    private static SQLiteDatabase db;
    private static AnimesDBHelper mDbHelper;
    public static final String SHARED_PREFERENCES_KEY = "SharedPreferences_data";
    private Context context;
    private static final String TAG = "AnimesDB";

    public AnimesDB(Context context) {

        mDbHelper = new AnimesDBHelper(context);
        this.context = context;
    }

    /**
     * @param tableName  name of table of dataBase to select registers
     * @param projection that specifies which columns from the table of database
     * @param where      Filter results WHERE "qr" = 'My qr'
     * @param args       arguments to where = {"My Caegory"}
     * @return
     */

    public Cursor selectRows(String tableName, String[] projection, String where, String[] args) {

        db = mDbHelper.getReadableDatabase();
/*
        Cursor c = db.query(
                tableName,                                  // The table to query
                projection,                                 // The columns to return
                where,                                      // The columns for the WHERE clause
                args,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                sortOrder                                   // The sort order
        );
*/
        String proj = Utils.StringJoiner(", ", projection);
        String argWhere = "";

        if (args.length > 1)
            argWhere = Utils.StringJoiner(", " + args);
        else if (args.length == 1)
            argWhere = args[0];

        String query = "SELECT " + proj + " FROM " + tableName + " " + where + " " + argWhere;

        Cursor c = db.rawQuery(query, null);

        return c;

    }

    /**
     * where and args separated by comma
     *
     * @param tableName
     * @param projection
     * @param where
     * @param args
     * @return
     */

    public Cursor selectRows(String tableName, String projection, String where, String args) {

        db = mDbHelper.getReadableDatabase();
        String query = "SELECT " + projection + " FROM " + tableName + " " + where + " " + args + ";";
        Cursor c = db.rawQuery(query, null);
        return c;

    }

    /**
     * @param tableName name of table of dataBase
     * @param values    values to new register
     */
    public long insertRow(String tableName, ContentValues values) {

        db = mDbHelper.getWritableDatabase();
        long insert = db.insert(tableName, null, values);
        db.close();
        Log.i("DB insert", tableName + ": " + String.valueOf(insert));
        return insert;

    }

    /**
     * Update Tables: where format => id= "01"
     * @param tableName
     * @param value
     * @param where
     * @return
     */

    public long updateRow(String tableName, ContentValues value, String where){

        db = mDbHelper.getWritableDatabase();
        long update= db.update(tableName, value, where, null);
        db.close();
        Log.i("DB update", tableName + ": " + String.valueOf(update));
        return update;
    }

    /**
     * Select images by id item
     * @param iditem
     * @return
     */

    public List<ImageGallery> GetImagesByIditem(String iditem) {
        try {
            List<ImageGallery> imageGalleryList = new ArrayList<ImageGallery>();

            String column_id = AnimesSchedule.Images._ID;
            String column_iditem = AnimesSchedule.Images.COLUMN_NAME_ID_ITEM;
            String column_image = AnimesSchedule.Images.COLUMN_NAME_IMAGE;
            String column_title = AnimesSchedule.Images.COLUMN_NAME_TITLE;

            String argument = AnimesSchedule.Images.COLUMN_NAME_ID_ITEM + "=" + iditem;
            Cursor c = selectRows(AnimesSchedule.Images.TABLE_NAME, "*", "WHERE", argument);

            if (c.moveToFirst()) {
                ImageGallery imageGallery;
                do {
                    imageGallery = new ImageGallery();
                    imageGallery.setId(c.getInt(c.getColumnIndex(column_id)));
                    imageGallery.setIdItem(c.getInt(c.getColumnIndex(column_iditem)));
                    imageGallery.setImage64(c.getString(c.getColumnIndex(column_image)));
                    imageGallery.setTitle(c.getString(c.getColumnIndex(column_title)));

                    imageGalleryList.add(imageGallery);

                } while (c.moveToNext());
            }
            return imageGalleryList;
        } catch (Exception e) {
            Log.e(TAG, "error obteniendo images por product");
            e.printStackTrace();
        }
        return null;
    }

    public void deleteAll(){
        db = mDbHelper.getWritableDatabase();
        String deleteQuery="DELETE FROM "+ AnimesSchedule.Anime.TABLE_NAME;
        db.execSQL(deleteQuery);
        deleteQuery="DELETE FROM "+ AnimesSchedule.Images.TABLE_NAME;
        db.execSQL(deleteQuery);


    }


    /**
     * Insert images by id item
     * @param iditem
     * @param imageGalleriesList
     */

    public void SetImagesByIditem(String iditem, List<ImageGallery> imageGalleriesList) {
        try {
            String table_name = AnimesSchedule.Images.TABLE_NAME;
            String column_id = AnimesSchedule.Images._ID;
            String column_iditem = AnimesSchedule.Images.COLUMN_NAME_ID_ITEM;
            String column_image = AnimesSchedule.Images.COLUMN_NAME_IMAGE;
            String column_title = AnimesSchedule.Images.COLUMN_NAME_TITLE;


            for (ImageGallery imagegallery :
                    imageGalleriesList) {

                ContentValues content = new ContentValues();
              //  content.put(column_id, imagegallery.getId());
                content.put(column_iditem, iditem);
                content.put(column_image, imagegallery.getImage64());
                content.put(column_title, imagegallery.getTitle());
                insertRow(table_name, content);

            }

        } catch (Exception e) {
            Log.e(TAG, "ocurrio un error insertando registros de images en db");
            e.printStackTrace();
        }


    }

        public void deleteItemProductById(String id){
            db = mDbHelper.getWritableDatabase();
//        String deleteQuery="DELETE FROM "+ ProductSchedule.Product.TABLE_NAME;
//        db.execSQL(deleteQuery);

            db.delete(AnimesSchedule.Anime.TABLE_NAME,
                    AnimesSchedule.Anime.COLUMN_NAME_ID + " = ?", new String[]{id});
            db.close();

        }


    public void eraseAllImagesById(String id){

        db = mDbHelper.getWritableDatabase();
    //    Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        String deleteQuery="DELETE FROM "+ AnimesSchedule.Images.TABLE_NAME+
                            " WHERE "+ AnimesSchedule.Images.COLUMN_NAME_ID_ITEM+"="+id;
        db.execSQL(deleteQuery);



    }

    public Animes getitemById(String id){
        db = mDbHelper.getWritableDatabase();
        Animes item=null;
        String table_item= AnimesSchedule.Anime.TABLE_NAME;
        String column_id= AnimesSchedule.Anime.COLUMN_NAME_ID;


        Cursor c = db.rawQuery("SELECT * FROM " +table_item +" WHERE "+column_id+"="+id, null);
        c.moveToFirst();

        if (c.moveToFirst()) {

            do {

                item = new Animes();
                item.setId(c.getInt(c.getColumnIndex(AnimesSchedule.Anime.COLUMN_NAME_ID)));
                item.setName(c.getString(c.getColumnIndex(AnimesSchedule.Anime.COLUMN_NAME)));
                item.setSubName(c.getString(c.getColumnIndex(AnimesSchedule.Anime.COLUMN_NAME_SUBNAME)));
                item.setStatus(c.getString(c.getColumnIndex(AnimesSchedule.Anime.COLUMN_NAME_STATUS)));
                item.setDateCreated(c.getString(c.getColumnIndex(AnimesSchedule.Anime.COLUMN_NAME_DATA)));
                item.setDateUpdate(c.getString(c.getColumnIndex(AnimesSchedule.Anime.COLUMN_NAME_DATA_UPDATE)));
                item.setColor(c.getString(c.getColumnIndex(AnimesSchedule.Anime.COLUMN_NAME_COLOR)));
                item.setImage(c.getString(c.getColumnIndex(AnimesSchedule.Anime.COLUMN_NAME_IMAGE)));
                item.setDeleted(Boolean.parseBoolean(c.getString(c.getColumnIndex(AnimesSchedule.Anime.COLUMN_NAME_DELETED))));
                item.setEnabled(Boolean.parseBoolean(c.getString(c.getColumnIndex(AnimesSchedule.Anime.COLUMN_NAME_ENABLED))));
                Log.i(TAG, "getDB " + item.getName());
//                item.setAddress(c.getString(c.getColumnIndex(AnimesSchedule.Product.COLUMN_NAME_ADDRESS)));
//                item.setName(c.getString(c.getColumnIndex(AnimesSchedule.Product.COLUMN_NAME_NAME)));
//                item.setPhone(c.getString(c.getColumnIndex(AnimesSchedule.Product.COLUMN_NAME_PHONE)));
//                item.setLocation(c.getString(c.getColumnIndex(AnimesSchedule.Product.COLUMN_NAME_LOCATION)));
//                item.setImage(c.getString(c.getColumnIndex(AnimesSchedule.Product.COLUMN_NAME_IMAGE)));
//                item.setDeleted(Boolean.parseBoolean(c.getString(c.getColumnIndex(AnimesSchedule.Product.COLUMN_NAME_DELETED))));
//                item.setitemTypeIcon(c.getString(c.getColumnIndex(AnimesSchedule.Product.COLUMN_NAME_SUBNAME_ICON)));
//                item.setEnabled(Boolean.parseBoolean(c.getString(c.getColumnIndex(AnimesSchedule.Product.COLUMN_NAME_ENABLED))));
//                item.setRuc(c.getString(c.getColumnIndex(AnimesSchedule.Product.COLUMN_NAME_RUC)));
//                item.setResponsiblePerson(c.getString(c.getColumnIndex(AnimesSchedule.Product.COLUMN_NAME_RESPONSIBLE)));
//                item.setStatus(c.getString(c.getColumnIndex(AnimesSchedule.Product.COLUMN_NAME_STATUS)));
//
//               if(AnimesDBHelper.DATABASE_VERSION>=9){
//                   item.setBalance(c.getColumnIndex(AnimesSchedule.Product.COLUMN_NAME_BALANCE));
//                   item.setDebt(c.getColumnIndex(AnimesSchedule.Product.COLUMN_NAME_DEBT));
//               }
//                Log.i(TAG, "getDB " + item.getName());

            } while (c.moveToNext());
        }

        return item;
    }

    public boolean isExistitem(String id){
        db = mDbHelper.getWritableDatabase();
        Animes item=null;
        String table_item= AnimesSchedule.Anime.TABLE_NAME;
        String column_id= AnimesSchedule.Anime.COLUMN_NAME_ID;


        Cursor c = db.rawQuery("SELECT * FROM " +table_item +" WHERE "+column_id+"="+id+";", null);
        c.moveToFirst();
        boolean exist=false;

        if (c.moveToFirst()) {
            exist=true;
            do {
               exist=true;

            } while (c.moveToNext());
        }

       return exist;
    }


    public void updateData(int id, List<Animes> list){

       try {
           if (isExistitem(""+id)){
               deleteItemProductById(""+id);
               setAnimes(list);
           }else Log.e(TAG, "No Existe este anime");
       }catch (NullPointerException e){
           e.printStackTrace();
       }

    }

    public List<Animes> getAnimes() {

        String dato = "";
        try {
            db = mDbHelper.getWritableDatabase();
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        List<Animes> Product = new ArrayList<>();

       if(AnimesDBHelper.DATABASE_VERSION<9) {
           String[] projection = {
                   AnimesSchedule.Anime.COLUMN_NAME_ID,
                   AnimesSchedule.Anime.COLUMN_NAME,
                   AnimesSchedule.Anime.COLUMN_NAME_SUBNAME,
                   AnimesSchedule.Anime.COLUMN_NAME_STATUS,
                   AnimesSchedule.Anime.COLUMN_NAME_DATA,
                   AnimesSchedule.Anime.COLUMN_NAME_COLOR,
                   AnimesSchedule.Anime.COLUMN_NAME_IMAGE,
                   AnimesSchedule.Anime.COLUMN_NAME_DELETED,
                   AnimesSchedule.Anime.COLUMN_NAME_ENABLED,
           };
       }

        //TODO: HACER UN "WHERE" PARA ENABLED

        String sortOrder = "";
        String arg[] = {"false"};
        Cursor c = db.rawQuery(
                "SELECT * FROM "
                        + AnimesSchedule.Anime.TABLE_NAME
                        +" ORDER BY  LOWER("+ AnimesSchedule.Anime.COLUMN_NAME+")", null);
        c.moveToFirst();
        int count = c.getCount();

        if (c.moveToFirst()) {
            Animes animes;
            do {

                animes = new Animes();
                animes.setId(c.getInt(c.getColumnIndex(AnimesSchedule.Anime.COLUMN_NAME_ID)));
                animes.setName(c.getString(c.getColumnIndex(AnimesSchedule.Anime.COLUMN_NAME)));
                animes.setSubName(c.getString(c.getColumnIndex(AnimesSchedule.Anime.COLUMN_NAME_SUBNAME)));
                animes.setCapitule(c.getInt(c.getColumnIndex(AnimesSchedule.Anime.COLUMN_NAME_CAPITULE)));
                animes.setStatus(c.getString(c.getColumnIndex(AnimesSchedule.Anime.COLUMN_NAME_STATUS)));
                animes.setDateCreated(c.getString(c.getColumnIndex(AnimesSchedule.Anime.COLUMN_NAME_DATA)));
                animes.setDateUpdate(c.getString(c.getColumnIndex(AnimesSchedule.Anime.COLUMN_NAME_DATA_UPDATE)));
                animes.setColor(c.getString(c.getColumnIndex(AnimesSchedule.Anime.COLUMN_NAME_COLOR)));
                animes.setImage(c.getString(c.getColumnIndex(AnimesSchedule.Anime.COLUMN_NAME_IMAGE)));
                animes.setDeleted(Boolean.parseBoolean(c.getString(c.getColumnIndex(AnimesSchedule.Anime.COLUMN_NAME_DELETED))));
                animes.setEnabled(Boolean.parseBoolean(c.getString(c.getColumnIndex(AnimesSchedule.Anime.COLUMN_NAME_ENABLED))));
                Log.i(TAG, "getDB " + animes.getName());
                Product.add(animes);

            } while (c.moveToNext());
        }

        return Product;

    }

    public void setAnimes(List<Animes> ListAnime) {
        try {
            String table_name = AnimesSchedule.Anime.TABLE_NAME;
            String column_id = AnimesSchedule.Anime.COLUMN_NAME_ID;
            String column_name = AnimesSchedule.Anime.COLUMN_NAME;
            String column_type = AnimesSchedule.Anime.COLUMN_NAME_SUBNAME;
            String column_capitule = AnimesSchedule.Anime.COLUMN_NAME_CAPITULE;
            String column_status = AnimesSchedule.Anime.COLUMN_NAME_STATUS;
            String column_data = AnimesSchedule.Anime.COLUMN_NAME_DATA;
            String column_data_update = AnimesSchedule.Anime.COLUMN_NAME_DATA_UPDATE;
            String column_color = AnimesSchedule.Anime.COLUMN_NAME_COLOR;
            String column_image = AnimesSchedule.Anime.COLUMN_NAME_IMAGE;
            String column_deleted = AnimesSchedule.Anime.COLUMN_NAME_DELETED;
            String column_enabled = AnimesSchedule.Anime.COLUMN_NAME_ENABLED;
            for (Animes anime
                    :
                    ListAnime) {
                String isdeleted = anime.isDeleted() ? "true" : "false";
                String isenabled = anime.isEnabled() ? "true" : "false";
                ContentValues register = new ContentValues();
                register.put(column_id, anime.getId());
                register.put(column_name, anime.getName());
                register.put(column_capitule, anime.getCapitule());
                register.put(column_type, anime.getSubName());
                register.put(column_status, anime.getStatus());
                register.put(column_data, anime.getDateCreated());
                register.put(column_data_update, anime.getDateUpdate());
                register.put(column_color, anime.getColor());
                register.put(column_image, anime.getImage());
                register.put(column_deleted, isdeleted);
                register.put(column_enabled, isenabled);
                insertRow(table_name, register);
            }
        } catch (Exception e) {
            Log.e(TAG, "ocurrio un error insertando registros en db");
            e.printStackTrace();
        }
    }

    public void insertProduct(Animes anime) {
        try {
            String table_name = AnimesSchedule.Anime.TABLE_NAME;
            String column_id = AnimesSchedule.Anime.COLUMN_NAME_ID;
            String column_name = AnimesSchedule.Anime.COLUMN_NAME;
            String column_type = AnimesSchedule.Anime.COLUMN_NAME_SUBNAME;
            String column_status = AnimesSchedule.Anime.COLUMN_NAME_STATUS;
            String column_data = AnimesSchedule.Anime.COLUMN_NAME_DATA;
            String column_data_update = AnimesSchedule.Anime.COLUMN_NAME_DATA_UPDATE;
            String column_color = AnimesSchedule.Anime.COLUMN_NAME_COLOR;
            String column_image = AnimesSchedule.Anime.COLUMN_NAME_IMAGE;
            String column_deleted = AnimesSchedule.Anime.COLUMN_NAME_DELETED;
            String column_enabled = AnimesSchedule.Anime.COLUMN_NAME_ENABLED;

                String isdeleted = anime.isDeleted() ? "true" : "false";
                String isenabled = anime.isEnabled() ? "true" : "false";
                ContentValues register = new ContentValues();
                register.put(column_id, anime.getId());
                register.put(column_name, anime.getName());
                register.put(column_type, anime.getSubName());
                register.put(column_status, anime.getStatus());
                register.put(column_data, anime.getDateCreated());
                register.put(column_data_update, anime.getDateUpdate());
                register.put(column_color, anime.getColor());
                register.put(column_image, anime.getImage());
                register.put(column_deleted, isdeleted);
                register.put(column_enabled, isenabled);
                insertRow(table_name, register);

        } catch (Exception e) {
            Log.e(TAG, "ocurrio un error insertando registros en db");
            e.printStackTrace();
        }
    }

    public int getSizeDB(){
        return getAnimes().size();
    }

}
