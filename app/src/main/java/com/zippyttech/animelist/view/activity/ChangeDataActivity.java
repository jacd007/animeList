package com.zippyttech.animelist.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFormatException;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zippyttech.animelist.R;
import com.zippyttech.animelist.common.utility.UtilsDate;
import com.zippyttech.animelist.common.utility.UtilsImage;
import com.zippyttech.animelist.common.utility.setup;
import com.zippyttech.animelist.data.AnimesDB;
import com.zippyttech.animelist.model.Animes;
import com.zippyttech.animelist.view.dialog.DialogUser;
import com.zippyttech.animelist.view.dialog.DialogView;

import java.util.ArrayList;
import java.util.List;

public class ChangeDataActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "ChangeDataActivity";
    private static final String SHARED_KEY = "shared_key";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    //Field
    private EditText etTitle,etCapitule;
    private ImageView ivPhoto;
    private ImageButton ivAdd,ivSust;
    private TextView tvDateC,tvDateUp;
    private Spinner spnColor,spnDay,spnStatus;
    private Button btnSave,btnCancel;
    private View vColor;

    //DB
    private AnimesDB animesDB;
    private Animes a;
    private List<Animes> listc;

    //Otros
    private int posicion,idAnime,capituleAnime,idAnimeDB;
    private String nameAnime,imageAnime,statusAnime,colorAnime,dayAnime,dateCreatedAnime,dateUpdateAnime;
    private static  String[] srtCapitule;
    private static final String[] srtStatus = {"Estado","En emisión","Finalizado","Estreno","Olvidado"};
    private static final String[] srtDays = {"Dia","Domingo","Lunes","Martes","Miercoles","Jueves","Viernes","Sabado"};
    private static final String[] srtColor = {"sinColor","rojo","azul","amarillo","blanco","negro","verde","violeta","naranja"};

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
        setContentView(R.layout.activity_change_data);

        initComponent();


    }

    private void initComponent() {
        settings = getSharedPreferences(SHARED_KEY,0);
        editor = settings.edit();
        listc = new ArrayList<>();
        srtCapitule = new String[10000];
//        srtCapitule = new ArrayList();
        animesDB = new AnimesDB(this);
        goCapituleArray();

        etTitle = (EditText) findViewById(R.id.etItemTitle);
        ivPhoto = (ImageView) findViewById(R.id.ivItemImage);
        etCapitule = (EditText) findViewById(R.id.etItemCapitule);
        ivAdd = (ImageButton) findViewById(R.id.ivAdd1);
        ivSust = (ImageButton) findViewById(R.id.ivSust1);
//        spnCapitule = (Spinner) findViewById(R.id.spnItemCap);
        spnStatus = (Spinner) findViewById(R.id.spnItemStatus);
        spnDay = (Spinner) findViewById(R.id.spnItemNextDay);
        spnColor = (Spinner) findViewById(R.id.spnItemColor);
        tvDateC = (TextView) findViewById(R.id.tvDateItemCr);
        tvDateUp = (TextView) findViewById(R.id.tvDateItemUp);
        vColor = (View) findViewById(R.id.vItemColor);

        btnSave = (Button) findViewById(R.id.btnItemSave);
        btnCancel = (Button) findViewById(R.id.btnItemCancel);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);
//        spnCapitule.setOnItemSelectedListener(this);
        spnStatus.setOnItemSelectedListener(this);
        spnColor.setOnItemSelectedListener(this);
        spnDay.setOnItemSelectedListener(this);
        ivAdd.setOnClickListener(this);
        ivSust.setOnClickListener(this);

        resetData();

        getDataBundle();

    }

    private void goCapituleArray() {

        for(int i=0;i<setup.constants.TOTAL_CAPITULES;i++){
            srtCapitule[i]=""+i;
        }
    }

    private void resetData() {
        posicion = 0;
        idAnime = 0;
        nameAnime = "";
        imageAnime = "";
        capituleAnime = 0;
        statusAnime = "";
        dayAnime = "";
        colorAnime = "";
        dateCreatedAnime = "";
        dateUpdateAnime = "";
        idAnimeDB = 0;
    }

    private void getDataBundle() {
        try{
            listc = new ArrayList<>();
            imageAnime = ""+ settings.getString(setup.list.IMAGE,"null");
//            Log.w(TAG,"IMAGE: "+imageAnime);
            Bundle parametros = this.getIntent().getExtras();
            if(parametros !=null){
                posicion = parametros.getInt(setup.list.POSITION);
                idAnime = parametros.getInt(setup.list.ID_ANIME);
                nameAnime = parametros.getString(setup.list.NAME);
//                imageAnime = parametros.getString(setup.list.IMAGE);
                capituleAnime = parametros.getInt(setup.list.CAPITULE);
                statusAnime = parametros.getString(setup.list.STATUS);
                dayAnime = parametros.getString(setup.list.DAY);
                colorAnime = parametros.getString(setup.list.COLOR);
                dateCreatedAnime = parametros.getString(setup.list.DATE_CREATED);
                dateUpdateAnime = parametros.getString(setup.list.DATE_UPDATE);
                idAnimeDB = parametros.getInt(setup.list.ID);
                Log.w(TAG,"IdAnime: "+idAnime);
                ArrayAdapter<String> dAdapter;

//                dAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_spinner, srtCapitule);
//                dAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_status);
//                spnDay.setAdapter(dAdapter);

                dAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_spinner, srtDays);
                dAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_status);
                spnDay.setAdapter(dAdapter);

                dAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_spinner, srtStatus);
                dAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_status);
                spnStatus.setAdapter(dAdapter);

                dAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_spinner, srtColor);
                dAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_status);
                spnColor.setAdapter(dAdapter);



                a = new Animes();
                a.setIdAnime(idAnime);
                a.setName(nameAnime);
                a.setImage(imageAnime);
                a.setCapitule(capituleAnime);
                a.setStatus(statusAnime);
                a.setDay(dayAnime);
                a.setColor(colorAnime);
                a.setDateCreated(dateCreatedAnime);
                a.setDateUpdate(dateUpdateAnime);
                a.setId(idAnimeDB);
            }else {
                Log.w(TAG,"parametros nulos");
            }
        }catch (NullPointerException e) {e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String auxDate = UtilsDate.changeData(dateUpdateAnime,setup.date.FORMAT,setup.date.FORMAT_SHORT);
        String today = UtilsDate.dateFormatAll(setup.date.FORMAT_SHORT);
        int dias = UtilsDate.differencesDate(auxDate,today);

        etTitle.setText(nameAnime);
        etCapitule.setText(""+capituleAnime);
        setImage(this,imageAnime,ivPhoto);
        tvDateC.setText(dateCreatedAnime);
        tvDateUp.setText( UtilsDate.textDate(dias,dateUpdateAnime) );
        mColor(colorAnime);
        spnColor.setSelection(getPositionItem(spnColor, colorAnime));
        spnDay.setSelection(getPositionItem(spnDay, dayAnime));
        spnStatus.setSelection(getPositionItem(spnStatus, statusAnime));
    }

    public static int getPositionItem(Spinner spinner, String date) {
        //Creamos la variable posicion y lo inicializamos en 0
        int posicion = 0;
        //Recorre el spinner en busca del ítem que coincida con el parametro `String fruta`
        //que lo pasaremos posteriormente
        for (int i = 0; i < spinner.getCount(); i++) {
            //Almacena la posición del ítem que coincida con la búsqueda
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(date)) {
                posicion = i;
            }
        }
        //Devuelve un valor entero (si encontro una coincidencia devuelve la
        // posición 0 o N, de lo contrario devuelve 0 = posición inicial)
        return posicion;
    }

    private void editDB(int id, List<Animes> list){
        animesDB.updateData(id, list);
        Intent i = new Intent();
        i.putExtra(setup.tools.RECHARGE_LIST,true);
        setResult(RESULT_OK,i);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        try {
            capituleAnime = Integer.parseInt(etCapitule.getText().toString());
        }catch (ParcelFormatException e){
            e.printStackTrace();
        }

        switch (id){
            case R.id.btnItemSave:
                a.setName(etTitle.getText().toString());
                a.setCapitule(Integer.parseInt(etCapitule.getText().toString()));
                a.setDateUpdate(UtilsDate.dateFormatAll(setup.date.FORMAT));
                listc.add(a);
//                Log.w(TAG,"Nombre: "+a.getName()+" Estado: "+a.getStatus()+" Color: "+a.getColor()+" Proximo: "+a.getDay());
                editDB(a.getIdAnime(),listc);
                break;
            case R.id.btnItemCancel:
                finish();
                break;
            case R.id.ivItemImage:
                LoadImage1(this);
                break;
            case R.id.ivAdd1:
                if (capituleAnime<9999)
                    capituleAnime++;
                else
                    Toast.makeText(this, "No se puede SUBIR el numero de capitulo", Toast.LENGTH_SHORT).show();

//                Log.w(TAG,"CAP+: "+capituleAnime);
                etCapitule.setText("");
                etCapitule.setText(""+capituleAnime);
                break;
            case R.id.ivSust1:
                if(capituleAnime>0)
                    capituleAnime--;
                else
                    Toast.makeText(this, "No se puede BAJAR el numero de capitulo", Toast.LENGTH_SHORT).show();

//                Log.w(TAG,"CAP-: "+capituleAnime);
                etCapitule.setText("");
                etCapitule.setText(""+capituleAnime);
                break;
            default:
                break;
        }
    }

    private void LoadImage1(final Context context){

        final CharSequence[] opciones={"Vista Previa","Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(context);
        alertOpciones.setTitle("Seleccione una opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(opciones[i].equals("Vista Previa")){
                    Toast.makeText(context, "No Disponible", Toast.LENGTH_SHORT).show();
//                    Preview();
                }
                else if(opciones[i].equals("Tomar Foto")){
                        try {
                            CameraE();
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                }
                else if(opciones[i].equals("Cargar Imagen")){
                    try {
                        loadImageEst();
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                        dialogInterface.dismiss();
                }

            }
        });
        alertOpciones.show();
    }

    private void Preview() {
      DialogView dv =  new DialogView(this);
      dv.preView(a.getImage());

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap;
        if (data!=null) {
            if ((resultCode == RESULT_OK) && (requestCode==mREQUEST_CODE_GALERY)) {
                Uri path = data.getData();
                Glide.with(this)
                        .load(path)
                        .placeholder(R.drawable.ic_broken_image)
                        .error(R.drawable.ic_no_image)
                        .into(ivPhoto);
//                ivPhoto.setImageURI(path);
                String strDir = "file/"+path.toString().substring(8)+".png";
                try {
                    Log.w(TAG,"pollo: "+strDir);
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
                    strB64 = UtilsImage.BitmapToBase64(bitmap);
                    a.setImage(strB64);
                    Log.w(TAG,"JSON EDITAR galeria: "+strB64);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else if ((resultCode == RESULT_OK) && (requestCode==mREQUEST_CODE_CAMERA)) {
                try {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    strB64 = UtilsImage.BitmapToBase64(bitmap);
//                    getSupportActionBar().setIcon(UtilsImage.);
                    ivPhoto.setImageBitmap(bitmap);
                    if (!strB64.contains("data:image/png;base64,")){
                        strB64 = getResources().getString(R.string.image_complement) + strB64;
                        Log.w(TAG,"IMAGEN CAMERA: "+strB64);
                    }
                    a.setImage(strB64);
                }catch (NullPointerException e){
                    Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
//            case R.id.spnItemCap:
//                    if (position>0){
//                        a.setCapitule(Integer.parseInt(spnCapitule.getSelectedItem().toString()));
//                    }else a.setCapitule(capituleAnime);
//                break;
            case R.id.spnItemStatus:
                if (position>0){
                    a.setStatus(spnStatus.getSelectedItem().toString());
                }else a.setStatus(statusAnime);
                break;
            case R.id.spnItemNextDay:
                if (position>0){
                    a.setDay(spnDay.getSelectedItem().toString());
                }else a.setDay(dayAnime);
                break;
            case R.id.spnItemColor:
                if (position>0){
                    mColor(spnColor.getSelectedItem().toString());
                    a.setColor(spnColor.getSelectedItem().toString());
                }else a.setColor(colorAnime);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        switch (parent.getId()){
////            case R.id.spnItemCap:
////                    a.setCapitule(capituleAnime);
////                break;
//            case R.id.spnItemStatus:
//                    a.setStatus(statusAnime);
//                break;
//            case R.id.spnItemNextDay:
//                    a.setDay(dayAnime);
//                break;
//            case R.id.spnItemColor:
//                    a.setColor(colorAnime);
//                break;

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
                    ivView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 500, 500, false));
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


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public  void mColor(String color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            switch (color) {

                case "sinColor"://sin color
                    vColor.setBackgroundTintList(vColor.getContext().getResources().
                            getColorStateList(R.color.semiTrans2));
                    break;
                case "rojo"://rojo
                    vColor.setBackgroundTintList(vColor.getContext().getResources().
                            getColorStateList(R.color.cvRed));
                    break;
                case "azul"://azul
                    vColor.setBackgroundTintList(vColor.getContext().getResources().
                            getColorStateList(R.color.cvBlue));
                    break;
                case "amarillo"://amarillo
                    vColor.setBackgroundTintList(vColor.getContext().getResources().
                            getColorStateList(R.color.cvYellow));
                    break;
                case "blanco"://blanco
                    vColor.setBackgroundTintList(vColor.getContext().getResources().
                            getColorStateList(R.color.cvWhite));
                    break;
                case "negro"://negro
                    vColor.setBackgroundTintList(vColor.getContext().getResources().
                            getColorStateList(R.color.cvBlack));
                    break;
                case "verde"://verde
                    vColor.setBackgroundTintList(vColor.getContext().getResources().
                            getColorStateList(R.color.cvGreen));
                    break;
                case "violeta"://violeta
                    vColor.setBackgroundTintList(vColor.getContext().getResources().
                            getColorStateList(R.color.cvViolet));
                    break;
                case "naranja"://naranja
                    vColor.setBackgroundTintList(vColor.getContext().getResources().
                            getColorStateList(R.color.cvOrange));
                    break;
                default:
                    vColor.setBackgroundTintList(vColor.getContext().getResources().
                            getColorStateList(R.color.cvYellow));
                    break;
            }
        }
    }

}
