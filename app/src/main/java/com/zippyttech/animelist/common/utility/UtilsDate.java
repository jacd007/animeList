package com.zippyttech.animelist.common.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;

import com.zippyttech.animelist.model.Animes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zippyttech on 27/10/17.
 */

public class UtilsDate {

    private static final String OK_TAG = "UtilsDate";
    private static final String ERROR_TAG = "ERROR";
    public static final String SHARED_PREFERENCES_KEY = "SharedPreferences_data";
    private SharedPreferences settings;
    private static SharedPreferences.Editor editor;

    public static boolean checkPermission(Context context, String permission){
        int check = ContextCompat.checkSelfPermission(context,permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Encode image Bitmap to Base64
     *
     * @param bm
     * @return
     */
    public static String encodeImage(Bitmap bm) {
        Log.w("encodeImage","image bounds: "+ bm.getWidth()+", "+bm.getHeight());

        if (bm.getHeight() <= 400 && bm.getWidth() <= 400) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            return (Base64.encodeToString(b, Base64.DEFAULT)).replaceAll("\\n", "");
        }
        int mHeight=400;
        int mWidth=400;

        if(bm.getHeight()>bm.getWidth()){
          float div=(float)bm.getWidth()/((float) bm.getHeight());
           float auxW=div*480;
            mHeight=480;
            mWidth=Math.round(auxW);
            Log.w("encodeImage","new high: "+mHeight+" width: "+mWidth);
        }
        else{
            float div= ((float) bm.getHeight())/(float)bm.getWidth();
            float auxH=div*480;
            mWidth=480;
            mHeight=360;
          mHeight=Math.round(auxH);
            Log.w("encodeImage","new high: "+mHeight+" width: "+mWidth);
        }

        bm = Bitmap.createScaledBitmap(bm, mWidth, mHeight, false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return (Base64.encodeToString(b, Base64.DEFAULT)).replaceAll("\\n", "");
    }

    public static String dateFormatAll(String formate) {
        //// TODO: formato de fecha dd-MM-yyyy HH:mm:ss      31-12-2017 17:59:59
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat(formate);
        return formatter.format(date);
    }

    public static String dateFormat(String data, String formate) {
        //// TODO: formato de fecha dd-MM-yyyy HH:mm:ss      31-12-2017 17:59:59
        Date date = StringToDate(data, formate);
        SimpleDateFormat formatter = new SimpleDateFormat(formate);
        return formatter.format(date);
    }

    public static Integer Epoch(String timestamp,String formate){
        if(timestamp == null) return null;
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(formate);
            Date dt = sdf.parse(timestamp);
            long epoch = dt.getTime();
            return (int)(epoch/1000);
        } catch(ParseException e) {
            return null;
        }
    }


    public static String reformateDate( String Date, String formate, String formate2) {
//        dateOld =>  YYYY-MM-DD
        Date date = null;
        String resp = null;

        @SuppressLint("SimpleDateFormat") SimpleDateFormat parseador = new SimpleDateFormat(formate);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formateador = new SimpleDateFormat(formate2);

        try {
            date = parseador.parse(Date);
            resp = formateador.format(date);
            return resp;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String DateToString(Date date, String formate){
        String string="";

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(formate);
        string = sdf.format(new Date());

        return string;
    }

    public static Date StringToDate(String string, String formate){
        DateFormat format = new SimpleDateFormat(formate, Locale.ENGLISH);
        try {
           return format.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String DateVariable(String fecha, int dias, String formate) {//TODO: Suma y/o Resta a FECHAS
        //tomo string fecha sumo o resto "dias" y devuelvo el string modificado de fecha
        Date date=null;
        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat(formate);
        try {
            date = format.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, dias);
        Date dcalenda = calendar.getTime();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(formate);
        String s = sdf.format(dcalenda);
        return s;
    }

    public static String filterTextView( String Date, String formate, String formate2) {
//        dateOld =>  YYYY-MM-DD
        Date date = null;
        String resp = null;

        @SuppressLint("SimpleDateFormat") SimpleDateFormat parseador = new SimpleDateFormat(formate);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formateador = new SimpleDateFormat(formate2);

        try {
            date = parseador.parse(Date);
            resp = formateador.format(date);
            return resp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resp;
    }
    public static String filterTextViewMonth( String Date, String formate, String formate2) {
//        dateOld =>  YYYY-MM-DD
        Date date = null;
        String resp = null;

        @SuppressLint("SimpleDateFormat") SimpleDateFormat parseador = new SimpleDateFormat(formate);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formateador = new SimpleDateFormat(formate2);

        try {
            date = parseador.parse(Date);
            resp = formateador.format(date);
            return resp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resp;
    }

    public static String getTimeZone() {
        Calendar cal = Calendar.getInstance();
        long milliDiff = cal.get(Calendar.ZONE_OFFSET);
// Got local offset, now loop through available timezone id(s).
        String[] ids = TimeZone.getAvailableIDs();
        String name = null;
        for (String id : ids) {
            TimeZone tz = TimeZone.getTimeZone(id);
            int hours = tz.getDSTSavings();
            int raw = tz.getRawOffset();
            int s = tz.getOffset(milliDiff);
            if (tz.getRawOffset() == milliDiff) {
                // Found a match.
                name = id;
                break;
            }
        }

        return name;
    }

    public static float getHoursTimeZone() {
        Calendar calendar = Calendar.getInstance();
        long milliDiff = calendar.get(Calendar.ZONE_OFFSET);
        float hours = (milliDiff / 1000) / 3600;
        return hours;
    }

    public static String dateTodayGMTFormat(String value_format) {
        Date date = Calendar.getInstance().getTime();
        String timezone = getTimeZone();
        SimpleDateFormat formatter = new SimpleDateFormat(value_format);
        String today = formatter.format(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // Configuramos la fecha que se recibe
        int hours_add = (int) (getHoursTimeZone() * (-1));
        calendar.add(Calendar.HOUR, hours_add);  // numero de horas a añadir, o restar en caso de horas<0
        calendar.add(Calendar.MINUTE, -5); // revisar 5 minutos antes de la hora del server
        Date newDate = calendar.getTime();
        today = formatter.format(newDate);
        return today;
    }

    public static String dateTodayFormat(String valueFormat) {
        Date date = Calendar.getInstance().getTime();
        String timezone = getTimeZone();
        SimpleDateFormat formatter = new SimpleDateFormat(valueFormat);
        String today = formatter.format(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // Configuramos la fecha que se recibe
        int hours_add = (int) (getHoursTimeZone() * (-1));
        //calendar.add(Calendar.HOUR, hours_add);  // numero de horas a añadir, o restar en caso de horas<0
        // calendar.add(Calendar.MINUTE,-30); // revisar 5 minutos antes de la hora del server
        Date newDate = calendar.getTime();
        today = formatter.format(newDate);
        return today;
    }

    public static int differencesDate(String dateVisite, String dateCompare) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date fechaInicial= null;
        Date fechaFinal= null;
        try {
            fechaInicial = dateFormat.parse(dateVisite);
            fechaFinal=dateFormat.parse(dateCompare);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int dias=(int) ((fechaFinal.getTime()-fechaInicial.getTime())/86400000);

        if(dias<0)return dias *= (-1);
        else return dias;
    }

    public static String changeData(String dateOld){
//     dateOld =>  YYYY-MM-DD
        Date date = null;
        String resp = null;

        SimpleDateFormat parseador = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");

        try {
            date = parseador.parse(dateOld);
            resp = formateador.format(date);
            return resp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resp;
    }


}
