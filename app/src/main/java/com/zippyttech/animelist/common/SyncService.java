package com.zippyttech.animelist.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.zippyttech.animelist.R;
import com.zippyttech.animelist.common.utility.UtilsDate;
import com.zippyttech.animelist.common.utility.setup;
import com.zippyttech.animelist.data.AnimesDB;
import com.zippyttech.animelist.model.Animes;
import com.zippyttech.animelist.view.activity.NavigationActivity;

import java.util.List;

import static android.content.ContentValues.TAG;
import static java.lang.Thread.sleep;

public class SyncService extends Service {
//    private final int TIME_SYNC = 8 * 60 * 60 * 1000;
    private final int TIME_SYNC = 15000;

    private String url_weather="http://api.openweathermap.org/data/2.5/forecast?id=524901&units=metric&APPID=44d8a60f7707ec918da8c1123c521ab1";
    private String url_news="https://lanacionweb.com/wp-json/wp/v2/posts";
    public int V;
    public String DAY;
    private ProgressDialog dialog;

    public static final String SHARED_KEY ="shared_key";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    private List<Animes> list;
    private AnimesDB animesDB;
    private Animes animes;
    private hilo hil;

    public SyncService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Creando servicio...");
        settings = getSharedPreferences(SHARED_KEY, 0);
        editor = settings.edit();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Servicio iniciado...");
//        Toast.makeText(SyncService.this, "Sync's Service start!", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Notificaciones Activadas!", Toast.LENGTH_SHORT).show();
        animesDB = new AnimesDB(this);

        try{

             hil = new hilo(this);
            hil.execute();

        }catch (Exception e){
            e.printStackTrace();
        }


        return START_NOT_STICKY;
      //  return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
//        Toast.makeText(this, "Sync's Service done!", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Notificaciones Desactivadas!", Toast.LENGTH_SHORT).show();
        hil.cancel(true);
    }
    public class hilo extends AsyncTask<String,String,String>{
        Context context ;
   public hilo(Context context){
       this.context = context;

   }

        @Override
        protected String doInBackground(String... strings) {
                try {

                    do{
                        DAY = UtilsDate.dateFormatAll(setup.date.FORMAT_DAY);
                        Log.w(TAG,"Ejecutando servicio...");
                        for (Animes a: animesDB.getAnimes()){
                            String auxDay = a.getDay().toLowerCase();
                            Log.w(TAG,"ANIME: "+a.getName()+" -> "+auxDay+" = "+DAY+ " STATUS: "+a.getStatus());

                            if (!a.getStatus().equals(context.getResources().getString(R.string.status_finish)) &&
                                    !a.getStatus().equals(context.getResources().getString(R.string.status_premiere))){
                                        if (DAY.equals(auxDay)){
                                            throudNotificacion(a.getId(),a.getName(),a.getDay());
                                        }
                            }

//                            if (DAY.equals(auxDay)){
//                                throudNotificacion(a.getId(),a.getName(),a.getDay());
//                            }
                        }

                        sleep(TIME_SYNC);
                    }while (true);

//                    for (int i = 0; i < 2; i++) {
//                      //  publishProgress(strings);
//
//                        V=i;
//                        sleep(5000);
//                    }
                }
                catch (Exception e){

                }
            return null;
        }

        private void sendBroadCast(Context context) {
            Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("es.androcode.android.mybroadcast");
                broadcastIntent.putExtra("parameter", "Nueva notificacion.");
                context.sendBroadcast(broadcastIntent);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

          //  new Utils.GetData(context,url_news);

          //  sendBroadCast(context);
           // throudNotificacion(V);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            sendBroadCast(context);
           // throudNotificacion(V);
        }
    }





    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void throudNotificacion(int cont, String contentText, String ticker){

        Intent intent = new Intent(this, SyncNotification.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, NavigationActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(contentText).setSmallIcon(R.drawable.ic_wallpaper_black)
                .setContentIntent(contentIntent)
                .setTicker(ticker)
                .setContentInfo(""+1)
                .setSound(sonido)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(cont, noti);
    }

}
