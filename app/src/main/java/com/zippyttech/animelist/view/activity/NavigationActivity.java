package com.zippyttech.animelist.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.zippyttech.animelist.R;
import com.zippyttech.animelist.common.utility.Utils;
import com.zippyttech.animelist.common.utility.UtilsDate;
import com.zippyttech.animelist.common.utility.UtilsImage;
import com.zippyttech.animelist.view.dialog.DialogUser;
import com.zippyttech.animelist.view.fragment.SettingFragment;
import com.zippyttech.animelist.common.utility.SendEmail;
import com.zippyttech.animelist.common.utility.setup;
import com.zippyttech.animelist.data.AnimesDB;
import com.zippyttech.animelist.model.Animes;
import com.zippyttech.animelist.view.fragment.ListFragment;
import com.zippyttech.animelist.view.fragment.MainFragment;
import com.zippyttech.animelist.view.fragment.PerfilFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SearchView.OnQueryTextListener {

    private static final String TAG = "NavigationActivity";
    private static final String SHARED_KEY = "shared_key";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    private TextView navUserName;
    private TextView navUserEmail;
    private ImageView navImageUser;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private FloatingActionButton fab;
    private Toolbar toolbar;

    //DB
    private AnimesDB animesDB;
    private Animes animes;
    private List<Animes> listA;
    private FABToolbarLayout morph;

    private static final int TIME_INTERVAL = 300; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    private String string;
    private SendEmail sendEmail;
    private int mREQUEST_CODE_CAMERA=0;
    private int mREQUEST_CODE_GALERY=10;

    private static String APP_DIRECTORY = "MyPictureApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";

    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;
    private final int REQUEST_ACCESS_FINE = 0;
    private String strB64;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_navigation);
        initComponent();
    }

    private void initComponent() {

        settings = getSharedPreferences(SHARED_KEY, 0);
        editor = settings.edit();
        sendEmail = new SendEmail(this);

        Bundle parametros = this.getIntent().getExtras();
        if (parametros!=null){
           string = parametros.getString(setup.list.TYPE);
        }

        animesDB = new AnimesDB(getApplicationContext());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


         fab = (FloatingActionButton) findViewById(R.id.fab);
        morph = (FABToolbarLayout) findViewById(R.id.fabtoolbar);

        View uno, dos, tres, cuatro;

        uno = findViewById(R.id.uno);
        dos = findViewById(R.id.dos);
        tres = findViewById(R.id.tres);
//        cuatro = findViewById(R.id.cuatro);


        fab.setOnClickListener(this);
        uno.setOnClickListener(this);
        dos.setOnClickListener(this);
        tres.setOnClickListener(this);
//        cuatro.setOnClickListener(this);

         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

         navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        navUserName = (TextView) headerView.findViewById(R.id.nav_user_name);
        navUserEmail = (TextView) headerView.findViewById(R.id.nav_email_user);
        navImageUser =  (ImageView) headerView.findViewById(R.id.imageView);
        navImageUser.setOnClickListener(this);
        navUserEmail.setOnClickListener(this);
        navImageUser.setOnClickListener(this);

        settings = getSharedPreferences(SHARED_KEY,0);
        editor = settings.edit();
        String ima = settings.getString(setup.general.image,"null");
        setImage(this,ima,navImageUser);
        setFragment(1);
        if (animesDB.getSizeDB()<1){
            ActionBar actionBar = getSupportActionBar();
            actionBar.setHomeAsUpIndicator(R.drawable.ic_delete_db);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }else {
            if (animesDB.getSizeDB()<1){
                ActionBar actionBar = getSupportActionBar();
                actionBar.setHomeAsUpIndicator(R.drawable.ic_delete_db);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }


    }

    private void setImage(Context context, String dateImage, ImageView ivView){
        try {
            if (!dateImage.equals("null")) {
                if (!dateImage.contains("http")) {
                    if (!dateImage.substring(0, 23).equals("data:image/jpeg;base64,")) {
                        dateImage = context.getResources().getString(R.string.image_complement) + dateImage;
                    }
                    Bitmap bitmap = UtilsImage.b64ToBitmap(dateImage);
                    ivView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 400, 400, false));
                } else {
                    Glide.with(context)
                            .load(dateImage)
                            .placeholder(R.drawable.ic_broken_image)
                            .error(R.drawable.ic_no_image)
                            .into(ivView);
                }
            }else {
                Glide.with(context)
                        .load("xxxx")
                        .error(R.drawable.ic_broken_image)
                        .into(ivView);
            }
        }catch (VerifyError e){
            ivView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_no_image));
            e.printStackTrace();
            Log.e("setImage",e.getMessage());
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            Glide.with(context)
                    .load(dateImage)
                    .placeholder(R.drawable.ic_broken_image)
                    .error(R.drawable.ic_no_image)
                    .into(ivView);
            Log.e("setImage",e.getMessage());
        }catch (NullPointerException e){
            Log.e("setImage",e.getMessage());
        }
    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    public void onBackPressed(){
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()){
        super.onBackPressed();
            return;
        }
        else {
            if (settings.getBoolean(setup.tools.ENABLED_BACK,false)){
//                sendEmail.Email("jacd0107@gmail.com",
//                        "Lista del "+ UtilsDate.dateFormatAll(setup.date.FORMAT_SIMPLE),
//                        "\n"+Utils.getListAn(animesDB.getAnimes()).replaceAll(",",",\n"));

                finish();
            }else {
                setFragment(1);
                Toast.makeText(getBaseContext(), "Doble click para salir...", Toast.LENGTH_SHORT).show();
            }
           }

        mBackPressed = System.currentTimeMillis();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.dashboard, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) NavigationActivity.this.getSystemService(Context.SEARCH_SERVICE);

        android.support.v7.widget.SearchView searchView = null;
        if (searchItem != null) {
            searchView = (android.support.v7.widget.SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(NavigationActivity.this.getComponentName()));
            searchView.setOnQueryTextListener(NavigationActivity.this);
            searchView.setInputType(InputType.TYPE_CLASS_TEXT);

        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_ord_name:
                ordenarList(0);
                break;
            case R.id.action_ord_status:
                ordenarList(1);
                break;
            case R.id.action_ord_date:
                ordenarList(2);
                break;
        }
//        if (id == R.id.act_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private void ordenarList(int i) {
            //TODO: ordenamiendo de la lista
            final String FORMAT = setup.date.FORMAT;

            switch (i){
                case 0:

                    break;
                case 1:

                    break;
                case 2:
                    //por fecha
                    final int today = Utils.Epoch( Utils.dateFormatAll(FORMAT) , FORMAT );
                    List<Animes> list = animesDB.getAnimes();
                    Collections.sort(list, new Comparator<Animes>() {
                        @Override
                        public int compare(Animes o1, Animes o2) {
                            int rsp,infinite;
                            infinite=today+today+today+today;
                            int va1=0;
                            int va2=0;
                            String aux1 = ""+o1.getDateUpdate();
                            String aux2 = ""+o2.getDateUpdate();
                            try{
                                va1 = aux1.equals("null")?(infinite):Utils.Epoch(o1.getDateUpdate(), FORMAT);
                                va2 = aux2.equals("null")?(infinite):Utils.Epoch(o2.getDateUpdate(),FORMAT);
                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }

                            if (va1 < va2) {
                                rsp = 1;
                            } else if (va1 > va2) {
                                rsp = -1;
                            } else {
                                rsp = 0;
                            }
                            return rsp;
                        }
                    });
                    if (settings.getBoolean(setup.tools.ITEM_TYPE,false)){
                        ListFragment.adapter2.changeDataItem(list);
                        ListFragment.adapter2.notifyDataSetChanged();
                    }else{
                        ListFragment.adapter.changeDataItem(list);
                        ListFragment.adapter.notifyDataSetChanged();
                    }
                    break;
            }



    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            item.setChecked(false);
            drawer.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle(getString(R.string.navPerfil));
            setFragment(-1);

        } else if (id == R.id.nav_list) {
            item.setChecked(true);
            drawer.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle(getString(R.string.navList));
            setFragment(1);

        } else if (id == R.id.nav_tools) {
            item.setChecked(true);
            drawer.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle(getString(R.string.navList));
            setFragment(2);
            //TODO: para crear imagenes
//            Drawable d = getResources().getDrawable(R.drawable.ic_color_1);
//            Bitmap bitmap = UtilsImage.drawableToBitmap(d);
//            UtilsImage.convertBitmapToFile(this,bitmap,"mImagen2");

            //TODO: intent de las herramientas
//            Intent intent =new Intent(this, ToolsActivity.class);
//            intent.putExtra(setup.tools.ENABLED,true);
//            startActivityForResult(intent,setup.key.keyToolsEnabled);

//            if (animesDB.getSizeDB()>0) {
//                String resp = Utils.sendEmailRespald(this, animesDB.getAnimes());
//                Log.w(TAG, "Lista de Animes para enviar a E-mail:\n"+ resp);
//                sendEmail.Email("jacd0107@gmail.com","Lista de animes del "+Utils.dateFormatAll(setup.date.FORMAT),resp);
//
//            }else
//                Log.w(TAG,"No hay nada");

        } else if (id == R.id.nav_delDB) {
            if (animesDB.getSizeDB()>0) {
                alertDialogWarning();
            }else
                Toast.makeText(NavigationActivity.this,
                        "No hay nada en la base de datos", Toast.LENGTH_SHORT).show();

//
        } else if (id == R.id.nav_xxx) {

        } else if (id == R.id.nav_exit) {
                finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void createFile(Context context, String title, String content,
                           String extention, String address, Bitmap bitmap){
        boolean isImage=false;
        try {
            File nuevaCarpeta = new File(Environment.getExternalStorageDirectory(), "AnimeListDB");
            if (!nuevaCarpeta.exists()) {
                nuevaCarpeta.mkdir();
            }
            if(extention.equals("png") || extention.equals("jpg") || extention.equals("jpeg")
                    || extention.equals("svg") || extention.equals("bmp") || extention.equals("gif")
                    || extention.equals("ico") || extention.equals("jpe") ){
                isImage=true;
            }
            try {
                File file = new File(nuevaCarpeta, title + "."+extention);
                file.createNewFile();

                if (isImage) {
                    OutputStreamWriter fout1 = null;
                    fout1 = new OutputStreamWriter(new FileOutputStream(file));
                    fout1.write(content);
                    fout1.close();
                }else {
                    File filesDir = context.getExternalFilesDir(address);
                    File imageFile = new File(filesDir, title + ".jpg");

                    OutputStream os;
                    try {
                        os = new FileOutputStream(imageFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        os.flush();
                        os.close();
                    } catch (Exception e) {
                        Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                    }
                }

            } catch (NullPointerException e){
                e.printStackTrace();
            } catch (Exception ex) {
                Log.e("Error", "ex: " + ex);
            }
        } catch (Exception e) {
            Log.e("Error", "e: " + e);
        }
    }
    @SuppressLint("RestrictedApi")
    public void setFragment(int position) {
        if (position==1){
            editor.putBoolean(setup.tools.ENABLED_BACK,true);
            editor.commit();
            fab.setVisibility(View.VISIBLE);
        }else {
            editor.putBoolean(setup.tools.ENABLED_BACK,false);
            editor.commit();
            fab.setVisibility(View.GONE);
        }
        android.support.v4.app.FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (position) {
            case -1:
                PerfilFragment perfilFragment = new PerfilFragment();
                fragmentTransaction.replace(R.id.content_frame, perfilFragment);
                fragmentTransaction.commit();
                break;

            case 0:
                MainFragment inboxFragment = new MainFragment();
                fragmentTransaction.replace(R.id.content_frame, inboxFragment);
                fragmentTransaction.commit();
                break;

            case 1:
                ListFragment starredFragment = new ListFragment();
                fragmentTransaction.replace(R.id.content_frame, starredFragment);
                fragmentTransaction.commit();
                break;
            case 2:
                SettingFragment fragment = new SettingFragment();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
                break;

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab:
                morph.show();
                break;
            case R.id.uno:
                int i = getIdAni();
            Intent intent = new Intent(getApplicationContext(),EditActivity.class);
                intent.putExtra(setup.list.STAT,true);
                intent.putExtra(setup.list.TYPE,1);
                intent.putExtra(setup.list.ID_ANIME,i);
                startActivityForResult(intent,setup.key.keyListAdd);
                morph.hide();

                break;
            case R.id.dos:

                morph.hide();
//                Snackbar.make(v, "No funcion...", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                break;
            case R.id.tres:
                if (settings.getBoolean(setup.tools.ENABLED_EMAIL,false)){
                    Toast.makeText(this, "Enviando email a "+settings.getString(setup.general.UserEmail,"jacd0107@gmail.com"), Toast.LENGTH_SHORT).show();
                    sendEmail.Email(settings.getString(setup.general.UserEmail,"jacd0107@gmail.com"),
                            "Lista del "+ UtilsDate.dateFormatAll(setup.date.FORMAT_SIMPLE),
                            "\n"+Utils.getListAn(animesDB.getAnimes()));
                    morph.hide();
                }else {
                    Snackbar.make(v, "Habilite ésta opción en \"Configuración\".", Snackbar.LENGTH_LONG)
                            .setAction("SendEmail", null).show();
                }

                break;
            case  R.id.imageView:
                    LoadImage1(this);
                break;
            case R.id.nav_email_user:
                EditText();
                break;
            case R.id.nav_user_name:
                EditText();
                break;
            default:
                morph.hide();
                break;
        }
    }

    private void EditText() {
        new DialogUser(this);
    }

    private void LoadImage1(final Context context){

        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(context);
        alertOpciones.setTitle("Seleccione una opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(opciones[i].equals("Tomar Foto")){
                    CameraE();
                }else{
                    if(opciones[i].equals("Cargar Imagen")){
                        try {
                            loadImageEst();
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();
    }

    private void loadImageEst() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        }else{
            try {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Seleccione la aplicación"),mREQUEST_CODE_GALERY);
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this, "Error al abrir la galeria...", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void CameraE(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},REQUEST_ACCESS_FINE);

        }else{
            try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, mREQUEST_CODE_CAMERA);
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this, "Error al abrir la camara...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_ACCESS_FINE){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                try {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, mREQUEST_CODE_CAMERA);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this, "Error al abrir la camara...", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Permiso de la camara denegado", Toast.LENGTH_SHORT).show();
            }

        }else if (requestCode == 1){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                try {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent,"Seleccione la aplicación"),mREQUEST_CODE_GALERY);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this, "Error en el permiso de almacenamiento...", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Permiso de lectura de almacenamiento denegado", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        recreate();
    }

    public int getIdAni(){
        int idx=1;boolean val = true;

        return idx;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == setup.key.keyToolsEnabled && resultCode == RESULT_OK) {
                if (data!=null) {
                    String dat = data.getStringExtra(setup.list.TYPE);
                    Toast.makeText(this, "Recibiendo cambios: "+dat, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "no hubo ningun cambio", Toast.LENGTH_SHORT).show();
                }
            }
            Bitmap bitmap;
            if ((resultCode == RESULT_OK) && (requestCode==mREQUEST_CODE_GALERY)) {
                Uri path = data.getData();
                navImageUser.setImageURI(path);
                String strDir = "file/"+path.toString().substring(8)+".png";
                try {
                    Log.w(TAG,"pollo: "+strDir);
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
                    strB64 = UtilsImage.BitmapToBase64(bitmap);
                    editor.putString(setup.general.image,strB64);
                    editor.commit();
                    editor.apply();
                    Log.w(TAG,"JSON EDITAR galeria: "+strB64);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else if ((resultCode == RESULT_OK) && (requestCode==mREQUEST_CODE_CAMERA)) {
                try {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    strB64 = UtilsImage.BitmapToBase64(bitmap);
//                    getSupportActionBar().setIcon(UtilsImage.);
                    navImageUser.setImageBitmap(bitmap);
                    if (!strB64.contains("data:image/png;base64,")){
                        strB64 = getResources().getString(R.string.image_complement) + strB64;
                        Log.w(TAG,"IMAGEN CAMERA: "+strB64);
                    }
                    editor.putString(setup.general.image,strB64);
                    editor.commit();
                    editor.apply();

                }catch (NullPointerException e){
                    Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }catch (NullPointerException e){
            e.printStackTrace();
            Toast.makeText(this, "No se pudo realizar esta operación.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        String ima = settings.getString(setup.general.image,"null");
        String email = settings.getString(setup.general.UserEmail,"null");
        String user = settings.getString(setup.general.UserName,"null");
        setImage(this,ima,navImageUser);
        navUserEmail.setText(email);
        navUserName.setText(user);
    }

    public void alertDialogWarning(){
        final boolean[] x = {false};
        AlertDialog alertDialog = new AlertDialog.Builder(NavigationActivity.this).create();
        alertDialog.setTitle("PRECAUCION");
        alertDialog.setCancelable(false);

        alertDialog.setMessage("¿Esta seguro que desea borrar todo?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                            animesDB.deleteAll();
                            editor.clear();
                            setFragment(1);
                            Toast.makeText(NavigationActivity.this,
                                    "La base de datos ha sido borrada...", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<Animes> list_aux = new ArrayList<>();
        List<Animes> list= animesDB.getAnimes();

        for (Animes aux:
                list ) {
            try {
                String name = "",cap="0",status="";

                name = aux.getName().toLowerCase();
                cap = String.valueOf(aux.getCapitule());
                status = aux.getStatus().toLowerCase();

                String text = newText.toLowerCase();


                if (name.contains(text))
                    list_aux.add(aux);
                else if (cap.contains(text))
                    list_aux.add(aux);
                else if (status.contains(text))
                    list_aux.add(aux);
                else
                    Log.w(TAG,"nada");

            }catch (Exception e){
                e.printStackTrace();
                continue;
            }
        }
        ListFragment.adapter.changeDataItem(list_aux);

        return false;
    }
}
