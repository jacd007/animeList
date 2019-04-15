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
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zippyttech.animelist.R;
import com.zippyttech.animelist.common.utility.Utils;
import com.zippyttech.animelist.common.utility.UtilsImage;
import com.zippyttech.animelist.common.utility.setup;
import com.zippyttech.animelist.data.AnimesDB;
import com.zippyttech.animelist.model.Animes;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG="EditActivity";
    private static final String SHARED_KEY = "shared_key";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    private EditText etName;
    private EditText etCapitule;
    private EditText etImage;
    private EditText etDate;
    private EditText etDateUpdate;
    private View vColor;
    private ImageView ivImage;
    private TextView tvColo;
    private Spinner spnStatus;
    private Button btnEdit;
    private Button btnSave;

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
    private TextView tvtitle;
    private String strB64;
    private Bitmap bitmap;
    private int cccont;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initComponent();
    }

    private void initComponent() {
        settings = getSharedPreferences(SHARED_KEY, 0);
        editor = settings.edit();
        cccont = settings.getInt(setup.general.idCount,1);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        cont=0;
        context = this;
        enabled=false;
        animesDB = new AnimesDB(getBaseContext());
        listStatus = new ArrayList<>();
        String[] srtStatus = new String[] {"En emisión","Finalizado","Olvidado"};
        list = new ArrayList<>();

        Bundle param = this.getIntent().getExtras();

        etName = (EditText) findViewById(R.id.etName);
        etCapitule = (EditText) findViewById(R.id.etCapitule);
        etImage = (EditText) findViewById(R.id.etImage);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        etDate = (EditText) findViewById(R.id.etDate);
        etDateUpdate = (EditText) findViewById(R.id.etDateUpdate);
        vColor = (View) findViewById(R.id.vColor);
        tvColo = (TextView) findViewById(R.id.tvColor);
        ivPhot = (ImageView) findViewById(R.id.ivPhot);
        btnEdit = (Button) findViewById(R.id.fab);
        btnSave = (Button) findViewById(R.id.fab2);
        tvtitle = (TextView) findViewById(R.id.tvEtitle);

        vColor.setOnClickListener(this);
        tvColo.setOnClickListener(this);
        ivImage.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnSave.setOnClickListener(this);

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
            image = param.getString(setup.list.IMAGE,"null");
            date = param.getString(setup.list.DATE_CREATED, Utils.dateFormatAll(setup.date.FORMAT));
            date_update = param.getString(setup.list.DATE_UPDATE,"null");
            color = param.getString(setup.list.COLOR,"amarillo");




            animes.setId(cccont);
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
            ivPhot.setImageBitmap(UtilsImage.b64ToBitmap(image));
            etDate.setText(date);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mColor(color);
            }
            tvColo.setText(color);
            tvColo.setOnClickListener(this);
            etDateUpdate.setText(date_update);

            Log.w(TAG, "\nid: "+animes.getId()+" name: "+animes.getName()+
                    " capitule: "+animes.getCapitule()+" status: "+animes.getStatus()+
                    " created: "+animes.getDateCreated()+" update: "+animes.getDateUpdate()+
                    " color: "+animes.getColor()+"\n\nImage: "+animes.getImage()+ " = "
                    +animes.getBtpImage()+"\n\n" );
        }

        spnStatus.setOnItemSelectedListener(this);
        etName.setEnabled(enabled);
        etCapitule.setEnabled(enabled);
        spnStatus.setEnabled(enabled);
        ivImage.setEnabled(enabled);

        if (animes.getStatus().equals(getResources().getString(R.string.status_progress))){
            spnStatus.setSelection(0);
        }else if (animes.getStatus().equals(getResources().getString(R.string.status_finish))){
            spnStatus.setSelection(1);
        }else {
            spnStatus.setSelection(2);
        }

        etImage.setText("");

        sendTextFromEditText(etName,tvtitle);

            enabledEdit(TYPE==1);


    }

   private void enabledEdit(boolean b){

        if (b){
            btnEdit.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);
        }else {
            btnEdit.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.GONE);
        }


       etName.setEnabled(b);
       etCapitule.setEnabled(b);
       spnStatus.setEnabled(b);
       ivImage.setEnabled(b);
       tvColo.setEnabled(b);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.fab:
                btnEdit.setVisibility(View.GONE);
                btnSave.setVisibility(View.VISIBLE);
                enabledEdit(true);

                break;
            case R.id.fab2:

                btnSave.setVisibility(View.GONE);
                btnEdit.setVisibility(View.VISIBLE);
                enabledEdit(false);

                //extraer datos de los componentes
                animes.setImage(strB64);
                animes.setBtpImage(bitmap);
                animes.setName(etName.getText().toString());
                animes.setCapitule(Integer.parseInt(etCapitule.getText().toString()));
                animes.setStatus(spnStatus.getSelectedItem().toString());
                animes.setDateCreated(etDate.getText().toString());
                animes.setDateUpdate(etDateUpdate.getText().toString());
                animes.setColor(tvColo.getText().toString());
//                animes.setSubName(etName.getText().toString());

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
                    if (animes.getDateUpdate().equals("null")){
                        animes.setDateCreated(animes.getDateUpdate());
                        etDate.setText(animes.getDateUpdate());
                    }else {
                        animes.setDateCreated(animes.getDateUpdate());
                        etDate.setText(animes.getDateUpdate());
                    }

                }

                if (etDateUpdate.getText().toString().equals(date_update)) {
                    etDateUpdate.setTextColor(etDateUpdate.getContext().getResources().getColorStateList(R.color.hintGrey));
                }else {
                    etDateUpdate.setTextColor(etDateUpdate.getContext().getResources().getColorStateList(R.color.hintBlue));

                }
                animes.setDateUpdate(animes.getDateCreated());
                list.add(animes);

                Log.w(TAG, "---------\nid: "+animes.getId()+"\nname: "+animes.getName()+
                        "\ncapitule: "+animes.getCapitule()+"\nstatus: "+animes.getStatus()+
                        "\ncreated: "+animes.getDateCreated()+"\nupdate: "+animes.getDateUpdate()+
                        "\ncolor: "+animes.getColor()+"\n\nImage: "+animes.getBtpImage()+ " = "
                        +animes.getImage()+"\n\n---------" );

                saveData(list);

                Snackbar.make(v, "información editada." , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                goNextScreen();
                break;
            case R.id.tvColor:
                cont++;
                if (cont>8) cont=0;
                animes.setColor(ListColor[cont]);
                mColor(ListColor[cont]);
                break;
            case R.id.ivImage:
                LoadImage1(this);
                break;
        }
    }

    private void goNextScreen() {
       editor.putInt(setup.general.idCount,cccont+1);
       editor.commit();
        Intent intent = new Intent(this,NavigationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void saveData(List<Animes> lista){

            Log.w(TAG,"GUARDANDO...");
            animesDB.setAnimes(lista);
            sendData();
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
                    tuTextView.setText(s.toString());
                }else{
                    tuTextView.setText("<Sin titulo>");
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
                etImage.setText(strDir);
                try {
                    Log.w(TAG,"pollo: "+strDir);
                     bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
                     strB64 = UtilsImage.BitmapToBase64(bitmap);
                    Log.w(TAG,"JSON EDITAR galeria: "+strB64);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else if ((resultCode == RESULT_OK) && (requestCode==mREQUEST_CODE_CAMERA)) {
                try {
                     bitmap = (Bitmap) data.getExtras().get("data");
                     strB64 = UtilsImage.BitmapToBase64(bitmap);
//                    getSupportActionBar().setIcon(UtilsImage.);
                    ivPhot.setImageBitmap(bitmap);
                    if (!strB64.contains("data:image/png;base64,")){
                        strB64 = getResources().getString(R.string.image_complement) + strB64;
                        Log.w(TAG,"IMAGEN CAMERA: "+strB64);
                    }

                }catch (NullPointerException e){
                    Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }

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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


//        Toast.makeText(this, ""+spnStatus.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

}
