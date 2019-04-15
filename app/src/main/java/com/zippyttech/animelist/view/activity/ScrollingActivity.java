package com.zippyttech.animelist.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.zippyttech.animelist.R;
import com.zippyttech.animelist.common.utility.Utils;
import com.zippyttech.animelist.common.utility.UtilsImage;
import com.zippyttech.animelist.common.utility.setup;
import com.zippyttech.animelist.data.AnimesDB;
import com.zippyttech.animelist.model.Animes;
import com.zippyttech.animelist.view.fragment.ListFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG="ScrollingActivity";
    private EditText etName;
    private EditText etCapitule;
//    private EditText etStatus;
    private EditText etImage;
    private EditText etDate;
    private EditText etDateUpdate;
    private View vColor;
    private TextView tvColo;
    private Spinner spnStatus;

    //CONSTANT FIELD
    private List<Animes> list;
    private Boolean enabled=false;
    private AnimesDB animesDB;
    private Animes animes;
    private ArrayAdapter<String> dataAdapter;

    //PARAMETER DEFAULT LIST
    private int TYPE;
    private int id;
    private int id_anime;
    private String name;
    private String sub_name;
    private int cap;
    private String status;
    private String image;
    private String date;
    private String date_update;
    private String color;
    private ArrayList<String> listStatus;
    private String FILENAME="myFile.txt";
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    private Context context;
    final String[] ListColor =
            new String[]{"sinColor","rojo","azul","amarillo","blanco","negro","verde","violeta","naranja"};
    private int cont;
    private FloatingActionButton fab;
    private CollapsingToolbarLayout toolbarLayout;

    private int mREQUEST_CODE_CAMERA=0;
    private int mREQUEST_CODE_GALERY=10;

    private static String APP_DIRECTORY = "MyPictureApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";

    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;
    private final int REQUEST_ACCESS_FINE = 0;
    private ImageView ivPhot;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
            initComponent();
    }

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initComponent() {
        cont=0;
        context = ScrollingActivity.this;
        enabled=false;
        animesDB = new AnimesDB(getBaseContext());
        listStatus = new ArrayList<>();
        String[] srtStatus = new String[] {"En emisión","Finalizado","Olvidado"};
        list = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Bundle param = this.getIntent().getExtras();

        etName = (EditText) findViewById(R.id.etName);
        etCapitule = (EditText) findViewById(R.id.etCapitule);
//        etStatus = (EditText) findViewById(R.id.etStatus);
        etImage = (EditText) findViewById(R.id.etImage);
        etDate = (EditText) findViewById(R.id.etDate);
        etDateUpdate = (EditText) findViewById(R.id.etDateUpdate);
        vColor = (View) findViewById(R.id.vColor);
        tvColo = (TextView) findViewById(R.id.tvColor);
        TextView tvIm = (TextView) findViewById(R.id.tvIm);
        ivPhot = (ImageView) findViewById(R.id.ivPhot);
        tvIm.setOnClickListener(this);
        vColor.setOnClickListener(this);
        tvColo.setOnClickListener(this);

        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        spnStatus  = (Spinner) this.findViewById(R.id.spinnerStatus);

        dataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_spinner, srtStatus);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_status);
        spnStatus.setAdapter(dataAdapter);

        if (param!=null){
            TYPE = param.getInt(setup.list.TYPE,0);
            enabled = param.getBoolean(setup.list.STAT,Boolean.valueOf(setup.list.STAT));
             animes = new Animes();
             id = param.getInt(setup.list.ID,1);
             id_anime = param.getInt(setup.list.ID_ANIME,animesDB.getSizeDB()+1);
             name = param.getString(setup.list.NAME,"");
             sub_name = param.getString(setup.list.SUB_NAME,"NoName");
             cap = param.getInt(setup.list.CAPITULE,0);
             status = param.getString(setup.list.STATUS,"NoStatus");
             image = param.getString(setup.list.IMAGE,"No Image");
             date = param.getString(setup.list.DATE_CREATED,Utils.dateFormatAll(setup.date.FORMAT));
             date_update = param.getString(setup.list.DATE_UPDATE,"null");
             color = param.getString(setup.list.COLOR,"amarillo");




            animes.setId(id);
            animes.setIdAnime(id_anime+1);
            animes.setName(name);
            animes.setSubName(sub_name);
            animes.setCapitule(cap);
            animes.setStatus(status);
            animes.setImage(image);
            animes.setDateCreated(date);
            animes.setDateUpdate(date_update);
            animes.setColor(color);

            etName.setText(name);
            etCapitule.setText(""+cap);
//            etStatus.setText(status);
            etImage.setText(image);
            etDate.setText(date);
            mColor(color);
            tvColo.setText(color);
            tvColo.setOnClickListener(this);
            etDateUpdate.setText(date_update);
            getSupportActionBar().setTitle(Utils.completeList(id,name));


        }

        spnStatus.setOnItemSelectedListener(this);
        etName.setEnabled(enabled);
        etCapitule.setEnabled(enabled);
//        etStatus.setEnabled(enabled);
        spnStatus.setEnabled(enabled);
        etImage.setEnabled(enabled);

         fab = (FloatingActionButton) findViewById(R.id.fab);
            if (animes.getStatus().equals(getResources().getString(R.string.status_progress))){
                spnStatus.setSelection(0);
            }else if (animes.getStatus().equals(getResources().getString(R.string.status_finish))){
                    spnStatus.setSelection(1);
            }else {
                spnStatus.setSelection(2);
            }

        toolbarLayout.setBackgroundResource(R.drawable.background_main);

        fab.setOnClickListener(this);

        etImage.setText("");


        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.w(TAG,""+s);
                if (s.toString().length()!=0){
                    getSupportActionBar().setTitle(s.toString());
                }else {
                    getSupportActionBar().setTitle("");
                }
            }
        });

    }

    private void sendTextFromEditText(final EditText editText,final TextView tuTextView){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.w(TAG,"resp: "+s);
                if(editText.getText().length() != 0) {
                    tuTextView.setText(editText.getText().toString());
                }else{
                    tuTextView.setText("");
                }
            }
        });
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data!=null) {
            if ((resultCode == RESULT_OK) && (requestCode==mREQUEST_CODE_GALERY)) {
                Uri path = data.getData();
                ivPhot.setImageURI(path);
                String strDir = "file/"+path.toString().substring(8)+".png";
                try {
                    Log.w(TAG,"pollo: "+strDir);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
                    String strB64 = UtilsImage.BitmapToBase64(bitmap);
                    String json = Utils.JsonCustomerImage(strB64);
                    Log.w(TAG,"JSON EDITAR galeria: "+json);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else if ((resultCode == RESULT_OK) && (requestCode==mREQUEST_CODE_CAMERA)) {
                try {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    String strB64 = UtilsImage.BitmapToBase64(bitmap);
//                    getSupportActionBar().setIcon(UtilsImage.);
                    ivPhot.setImageBitmap(bitmap);
                    if (!strB64.contains("data:image/png;base64,")){
                        strB64 = getResources().getString(R.string.image_complement) + strB64;
                        Log.w(TAG,"IMAGEN CAMERA: "+strB64);
                    }

                    String json = Utils.JsonCustomerImage(strB64);
                    Log.w(TAG,"JSON EDITAR camara: "+json);

                }catch (NullPointerException e){
                    Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.fab:
             String   msg="";


               enabled = !enabled;
                etName.setEnabled(enabled);
                etCapitule.setEnabled(enabled);
                spnStatus.setEnabled(enabled);
                etImage.setEnabled(enabled);
//                etStatus.setEnabled(enabled);

                if (enabled){
                    fab.setImageResource(R.drawable.ic_edit);
                }else {
                    fab.setImageResource(R.drawable.ic_save);
                }

                if (etName.getText().toString().equals(name))
                    etName.setTextColor(etName.getContext().getResources().getColorStateList(R.color.hintGrey));
                else
                    etName.setTextColor(etName.getContext().getResources().getColorStateList(R.color.hintBlue));

                if (etCapitule.getText().toString().equals(""+cap))
                    etCapitule.setTextColor(etCapitule.getContext().getResources().getColorStateList(R.color.hintGrey));
                else
                    etCapitule.setTextColor(etCapitule.getContext().getResources().getColorStateList(R.color.hintBlue));

                animes.setStatus(spnStatus.getSelectedItem().toString());

                if (etDate.getText().toString().equals("null")){
                    etDate.setText(animes.getDateUpdate());
                    animes.setDateCreated(animes.getDateUpdate());
                }

                if (!enabled){
                    getSupportActionBar().setTitle(animes.getName());
                    if (etDate.getText().toString().equals("null")){
                        etDate.setText(animes.getDateUpdate());
                        animes.setDateCreated(animes.getDateUpdate());
                    }
                    animes.setName(etName.getText().toString());
                    animes.setSubName("subName "+animes.getId());
                    animes.setCapitule(Integer.parseInt(etCapitule.getText().toString()));
                    animes.setDateCreated(etDate.getText().toString());
                    animes.setDateUpdate(Utils.dateFormatAll(setup.date.FORMAT));
                    etDateUpdate.setText(animes.getDateUpdate());
                    animes.setCapitule(Integer.parseInt(etCapitule.getText().toString()));

                    if (etDateUpdate.getText().toString().equals(date_update)) {
                        etDateUpdate.setTextColor(etDateUpdate.getContext().getResources().getColorStateList(R.color.hintGrey));
                    }else {
                        etDateUpdate.setTextColor(etDateUpdate.getContext().getResources().getColorStateList(R.color.hintBlue));

                    }

                    list.add(animes);
                    if(TYPE==1) {
//                        Toast.makeText(this, "funcion de crear nuevo item", Toast.LENGTH_SHORT).show();

                             animesDB.setAnimes(list);

                        sendData();
                        finish();

                    }else {
                            animesDB.updateData(animes.getId(),list);

//                        Toast.makeText(this, "funcion de editar info", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    etName.setTextColor(etName.getContext().getResources().getColorStateList(R.color.hintBlack));
                    etCapitule.setTextColor(etCapitule.getContext().getResources().getColorStateList(R.color.hintBlack));
//                    etStatus.setTextColor(etStatus.getContext().getResources().getColorStateList(R.color.hintBlack));
//                    etStatus.setTextColor(etStatus.getContext().getResources().getColorStateList(R.color.hintGrey));
                }

                msg = enabled ? "Editando información..." : "información editada." ;

                Snackbar.make(v, msg , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                break;
            case R.id.tvColor:
                cont++;
              if (cont>8) cont=0;
                animes.setColor(ListColor[cont]);
               mColor(ListColor[cont]);
                break;
            case R.id.tvIm:
                LoadImage1(this);
                break;
        }
    }

    private void sendData() {
        Intent intent = new Intent();
        intent.putExtra(setup.list.ID,animes.getIdAnime());
        intent.putExtra(setup.list.NAME,animes.getName());
        intent.putExtra(setup.list.SUB_NAME,animes.getSubName());
        intent.putExtra(setup.list.STATUS, animes.getStatus());
        intent.putExtra(setup.list.CAPITULE,animes.getCapitule());
        intent.putExtra(setup.list.COLOR,animes.getColor());
        intent.putExtra(setup.list.DATE_CREATED,animes.getDateCreated());
        intent.putExtra(setup.list.DATE_UPDATE,animes.getDateUpdate());
        intent.putExtra(setup.list.IMAGE,animes.getImage());
        setResult(RESULT_OK,intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        cont=0;
    }

    private void saveData(){
        Bundle bundle = new Bundle();
        bundle.putString("edttext", "From Activity");
// set Fragmentclass Arguments
        ListFragment fragobj = new ListFragment();
        fragobj.setArguments(bundle);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Fragment frg = null;
        saveData();
        frg = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void readFile(View v){
        if (Utils.isExternalStorage()){
            StringBuilder sb = new StringBuilder();
            try {
                File textFile = new File(Environment.getExternalStorageState(),FILENAME);
                FileInputStream fis = new FileInputStream(textFile);

                if (fis != null){
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader buff = new BufferedReader(isr);

                    String line =null;
                    while (buff.readLine() != null){
                        sb.append(line + "\n");
                    }
                    fis.close();
                }
                etImage.setText(sb);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(this, "No se puede leer del directorio externo", Toast.LENGTH_SHORT).show();
        }
    }


    public List<Animes> setList(){
        List<Animes> listAux = new ArrayList<>();
        Animes a = new Animes();
        a.setId(animesDB.getSizeDB());
        a.setName(animes.getName());
        return listAux;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public  void mColor(String color){
        tvColo.setText(color);
        switch (color){

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            Toast.makeText(this, ""+spnStatus.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void oldData(String date) {
        String FORMATE = Utils.dateFormatAll(setup.date.FORMAT);
        try {
            Integer oldDate = Utils.Epoch(date, FORMATE);
            Integer today = Utils.Epoch(FORMATE, FORMATE);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed(){
//        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()){
            super.onBackPressed();
//            return;
//        }
//        else {
//            Toast.makeText(getBaseContext(), "Doble click en ATRAS para salir...", Toast.LENGTH_SHORT).show(); }
//
//        mBackPressed = System.currentTimeMillis();
    }

    private void myMenuColor(final int position, final Animes animes) {
        final Dialog dialog;

        dialog = new Dialog(context, R.style.AnimateDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.item_menu_color);
        ImageButton btnColor1   = (ImageButton) dialog.findViewById(R.id.ivbtnColor1);
        ImageButton btnColor2  = (ImageButton) dialog.findViewById(R.id.ivbtnColor2);
        ImageButton btnColor3 = (ImageButton) dialog.findViewById(R.id.ivbtnColor3);
        ImageButton btnColor4   = (ImageButton) dialog.findViewById(R.id.ivbtnColor4);
        ImageButton btnColor5   = (ImageButton) dialog.findViewById(R.id.ivbtnColor5);
        ImageButton btnColor6  = (ImageButton) dialog.findViewById(R.id.ivbtnColor6);
        ImageButton btnColor7 = (ImageButton) dialog.findViewById(R.id.ivbtnColor7);
        ImageButton btnColor8   = (ImageButton) dialog.findViewById(R.id.ivbtnColor8);

        btnColor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                List<Animes> list = new ArrayList<>();
                animes.setColor(ListColor[1]);
                list.set(position,animes);
//                changeDataItem(list);
                Toast.makeText(context, animes.getName()+", color: "+animes.getColor(), Toast.LENGTH_SHORT).show();
                Log.w(TAG,"color");
                list.add(animes);
//                animesDB.updateData(animes.getId(),list);
                dialog.cancel();
            }
        });
        btnColor2.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view)
            {
                animes.setColor(ListColor[2]);
                list.set(position,animes);
//                changeDataItem(list);
                Toast.makeText(context, animes.getName()+", color: "+animes.getColor(), Toast.LENGTH_SHORT).show();
                Log.w(TAG,"color");
                dialog.cancel();
            }
        });
        btnColor3.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view)
            {
                animes.setColor(ListColor[3]);
                list.set(position,animes);
//                changeDataItem(list);
                Toast.makeText(context, animes.getName()+", color: "+animes.getColor(), Toast.LENGTH_SHORT).show();
                Log.w(TAG,"color");
                dialog.cancel();
            }
        });
        btnColor4.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view)
            {
                animes.setColor(ListColor[4]);
                list.set(position,animes);
//                changeDataItem(list);
                Toast.makeText(context, animes.getName()+", color: "+animes.getColor(), Toast.LENGTH_SHORT).show();
                Log.w(TAG,"color");
                dialog.cancel();
            }
        });
        btnColor5.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view)
            {
                animes.setColor(ListColor[5]);
                list.set(position,animes);
//                changeDataItem(list);
                Toast.makeText(context, animes.getName()+", color: "+animes.getColor(), Toast.LENGTH_SHORT).show();
                Log.w(TAG,"color");
                dialog.cancel();
            }
        });
        btnColor6.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view)
            {
                animes.setColor(ListColor[6]);
                list.set(position,animes);
//                changeDataItem(list);
                Toast.makeText(context, animes.getName()+", color: "+animes.getColor(), Toast.LENGTH_SHORT).show();
                Log.w(TAG,"color");
                dialog.cancel();
            }
        });
        btnColor7.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view)
            {
                animes.setColor(ListColor[7]);
                list.set(position,animes);
//                changeDataItem(list);
                Toast.makeText(context, animes.getName()+", color: "+animes.getColor(), Toast.LENGTH_SHORT).show();
                Log.w(TAG,"color");
                dialog.cancel();
            }
        });
        btnColor8.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view)
            {
                animes.setColor(ListColor[8]);
                list.set(position,animes);
//                changeDataItem(list);
                Toast.makeText(context, animes.getName()+", color: "+animes.getColor(), Toast.LENGTH_SHORT).show();
                Log.w(TAG,"color");
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
