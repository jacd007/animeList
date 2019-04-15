package com.zippyttech.animelist.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.TransactionTooLargeException;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.zippyttech.animelist.common.utility.UtilsImage;
import com.zippyttech.animelist.view.activity.EditActivity;
import com.zippyttech.animelist.R;
import com.zippyttech.animelist.common.utility.Utils;
import com.zippyttech.animelist.data.AnimesDB;
import com.zippyttech.animelist.common.ItemClickAnimes;
import com.zippyttech.animelist.common.utility.setup;
import com.zippyttech.animelist.model.Animes;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zippyttech on 27/10/18.
 */
class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

    private static final String SHARED_KEY = "shared_key";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    private final ImageView ivImage;
    private CardView cvItem;
    private TextView tvName;
    private TextView tvDateCreated;
    private TextView tvCapitule;
    private TextView tvStatus;
    private View vPosColor;

    private ViewDragHelper mViewDragHelper;
    private ItemClickAnimes itemClickListener;

    /**
     * @param itemView Item on List
     * @param act      Activity
     */

    @SuppressLint("CutPasteId")
    public RecyclerViewHolder(View itemView, AppCompatActivity act) {
        super(itemView);

        cvItem = (CardView) itemView.findViewById(R.id.cv_item);
        tvName = (TextView) itemView.findViewById(R.id.tvTitle);
        tvDateCreated = (TextView) itemView.findViewById(R.id.tvData);
        tvCapitule = (TextView) itemView.findViewById(R.id.tvCap);
        tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
        ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
        vPosColor = (View) itemView.findViewById(R.id.vPostColor);



        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setData(Context context, int Id, int IdAnime, int Capitule, String Name, String SubName,
                        String Status, String DateCreated, String DateUpdate, String Color, String image)
                                        throws MalformedURLException {
        settings = context.getSharedPreferences(SHARED_KEY, 0);
        editor = settings.edit();

        tvName.setSingleLine(settings.getBoolean(setup.general.AnimateText,true));
        tvName.setSelected(settings.getBoolean(setup.general.AnimateText,true));

        int id = Id;
        int idAnime = IdAnime;
        int capitule = Capitule;
        String name = TextUtils.isEmpty(Name) || Name.equals("null") ? "N/A" : Name;
        String subName = TextUtils.isEmpty(SubName) || SubName.equals("null") ? "N/A" : SubName;
        String status = TextUtils.isEmpty(Status) || Status.equals("null") ? "N/A" : Status;
        String dateCreated = TextUtils.isEmpty(DateCreated) || DateCreated.equals("null") ? "N/A" : DateCreated;
        String dateUpdate = TextUtils.isEmpty(DateUpdate) || DateUpdate.equals("null") ? "N/A" : DateUpdate;
        String color = TextUtils.isEmpty(Color) || Color.equals("null") ? "f5ff88" : Color;

//        Log.w("setData","Imagen #"+id+": "+image);
        setImage(context,image,ivImage);

//        Picasso.get().load(image).placeholder(R.drawable.ic_broken_image).error(R.drawable.ic_no_image).into(ivImage);
        switch (status) {
            case "En emisión":
                tvStatus.setTextColor(tvStatus.getContext().getResources().
                        getColorStateList(R.color.etProgress));
                tvName.setTextColor(tvName.getContext().getResources().getColor(R.color.cvPosBlack));
                tvCapitule.setTextColor(tvCapitule.getContext().getResources().getColor(R.color.cvPosBlack));
                tvDateCreated.setTextColor(tvDateCreated.getContext().getResources().getColor(R.color.cvPosBlack));
                tvName.setSelected(true);
                tvCapitule.setVisibility(View.VISIBLE);
                break;
            case "Finalizado":
                tvStatus.setTextColor(tvStatus.getContext().getResources().
                        getColorStateList(R.color.etClosed));
                tvName.setTextColor(tvName.getContext().getResources().getColor(R.color.semiTrans2));
                tvDateCreated.setTextColor(tvDateCreated.getContext().getResources().getColor(R.color.semiTrans2));
                tvName.setSelected(false);
                tvCapitule.setVisibility(View.GONE);

                break;
            case "Estreno":
                tvStatus.setTextColor(tvStatus.getContext().getResources().
                        getColorStateList(R.color.etPremiere));
                tvName.setTextColor(tvName.getContext().getResources().getColor(R.color.etPremiere));
                tvDateCreated.setTextColor(tvDateCreated.getContext().getResources().getColor(R.color.semiTrans2));
                tvName.setSelected(true);
                tvName.setSelected(false);
                tvCapitule.setVisibility(View.GONE);

                break;
            case "Olvidado":
                tvStatus.setTextColor(tvStatus.getContext().getResources().
                        getColorStateList(R.color.etOldDate));
                tvName.setTextColor(tvName.getContext().getResources().getColor(R.color.semiTrans2));
                tvCapitule.setTextColor(tvCapitule.getContext().getResources().getColor(R.color.semiTrans2));
                tvDateCreated.setTextColor(tvDateCreated.getContext().getResources().getColor(R.color.etClosed));
                tvName.setSelected(false);
                tvCapitule.setVisibility(View.VISIBLE);

                break;
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            switch (color){

                case "sinColor"://sin color
                    cvItem.setBackgroundTintList(cvItem.getContext().getResources().
                            getColorStateList(R.color.semiTrans2));

                    vPosColor.setBackgroundTintList(vPosColor.getContext().getResources().
                            getColorStateList(R.color.semiTrans));
                    break;
                case "rojo"://rojo
                    cvItem.setBackgroundTintList(cvItem.getContext().getResources().
                            getColorStateList(R.color.cvRed));

                    vPosColor.setBackgroundTintList(vPosColor.getContext().getResources().
                            getColorStateList(R.color.cvPosRed));
                    break;
                case "azul"://azul
                    cvItem.setBackgroundTintList(cvItem.getContext().getResources().
                            getColorStateList(R.color.cvBlue));

                    vPosColor.setBackgroundTintList(vPosColor.getContext().getResources().
                            getColorStateList(R.color.cvPosBlue));
                    break;
                case "amarillo"://amarillo
                    cvItem.setBackgroundTintList(cvItem.getContext().getResources().
                            getColorStateList(R.color.cvYellow));

                    vPosColor.setBackgroundTintList(vPosColor.getContext().getResources().
                            getColorStateList(R.color.cvPosYellow));
                    break;
                case "blanco"://blanco
                    cvItem.setBackgroundTintList(cvItem.getContext().getResources().
                            getColorStateList(R.color.cvWhite));

                    vPosColor.setBackgroundTintList(vPosColor.getContext().getResources().
                            getColorStateList(R.color.cvPosWhite));
                    break;
                case "negro"://negro
                    cvItem.setBackgroundTintList(cvItem.getContext().getResources().
                            getColorStateList(R.color.cvBlack));

                    vPosColor.setBackgroundTintList(vPosColor.getContext().getResources().
                            getColorStateList(R.color.cvPosBlack));
                    break;
                case "verde"://verde
                    cvItem.setBackgroundTintList(cvItem.getContext().getResources().
                            getColorStateList(R.color.cvGreen));

                    vPosColor.setBackgroundTintList(vPosColor.getContext().getResources().
                            getColorStateList(R.color.cvPosGreen));
                    break;
                case "violeta"://violeta
                    cvItem.setBackgroundTintList(cvItem.getContext().getResources().
                            getColorStateList(R.color.cvViolet));

                    vPosColor.setBackgroundTintList(vPosColor.getContext().getResources().
                            getColorStateList(R.color.cvPosViolet));
                    break;
                case "naranja"://naranja
                    cvItem.setBackgroundTintList(cvItem.getContext().getResources().
                            getColorStateList(R.color.cvOrange));

                    vPosColor.setBackgroundTintList(vPosColor.getContext().getResources().
                            getColorStateList(R.color.cvPosOrange));
                    break;
//                case "rosado"://naranja
//                    cvItem.setBackgroundTintList(cvItem.getContext().getResources().
//                            getColorStateList(R.color.cvPink));
//
//                    vPosColor.setBackgroundTintList(vPosColor.getContext().getResources().
//                            getColorStateList(R.color.cvPosPink));
//                    break;
                default:
                        cvItem.setBackgroundTintList(cvItem.getContext().getResources().
                                getColorStateList(R.color.cvYellow));

                    vPosColor.setBackgroundTintList(vPosColor.getContext().getResources().
                            getColorStateList(R.color.cvPosYellow));
                    break;
            }
            Log.w("rvAdapter","Animes:"+" "+id+" "+name+" "+status+" "+dateUpdate );

            tvName.setText(""+name);
            tvCapitule.setText(""+capitule);
            tvStatus.setText(""+status);
            tvDateCreated.setText(""+dateUpdate);


    }

    private void setImage(Context context, String mImage, ImageView imageuser){
        try {
               if (!mImage.substring(0,4).equals("http")) {
                   if (!mImage.substring(0,23).equals("data:image/jpeg;base64,")){
                       mImage = context.getResources().getString(R.string.image_complement) + mImage;
                   }
                    Bitmap bitmap = UtilsImage.b64ToBitmap(mImage);
                    imageuser.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 400, 400, false));
               }else {
                   Glide.with(context)
                                .load(mImage)
                                .placeholder(R.drawable.ic_broken_image)
                                .error(R.drawable.ic_no_image)
                                .into(imageuser);
               }

        }catch (VerifyError e){
            imageuser.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_no_image));
            e.printStackTrace();
            Log.e("setImage",e.getMessage());
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            Glide.with(context)
                    .load(mImage)
                    .placeholder(R.drawable.ic_broken_image)
                    .error(R.drawable.ic_no_image)
                    .into(imageuser);
            Log.e("setImage",e.getMessage());
        }catch (NullPointerException e){
            Log.e("setImage",e.getMessage());
        }
    }

    public void setItemClickListener(ItemClickAnimes itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(),false);

    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(),true);
        return false;
    }
}

public class rvAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private static final String TAG="rvAdapter";
    private List<Animes> listData = new ArrayList<>();
    private TextView tvCont;
    private AppCompatActivity acti;
    private Context context;
    private AppCompatActivity Actividad;
    private int origin;
    private int conta=0;
    private AnimesDB animesDB;


    /**
     * @param s
     * @param c
     */

    public rvAdapter(List<Animes> s, Context c, TextView tvcont) {
        context = c;
        listData = s;
        Actividad = acti;
        tvCont = tvcont;
        animesDB = new AnimesDB(context);
    }
    public rvAdapter( Context c) {
        context = c;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item, parent, false);

        return new RecyclerViewHolder(itemView, Actividad);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
/*int Id, int IdAnime, int Capitule, String Name, String SubName,
                        String Status, String DateCreated, String DateUpdate, String Color*/
        tvCont.setText("Items: "+getItemCount());
        try {
            holder.setData(context,listData.get(position).getId(),
                    listData.get(position).getIdAnime(),
                    listData.get(position).getCapitule(),
                    listData.get(position).getName(),
                    listData.get(position).getSubName(),
                    listData.get(position).getStatus(),
                    listData.get(position).getDateCreated(),
                    listData.get(position).getDateUpdate(),
                    listData.get(position).getColor(),
                    listData.get(position).getImage());

        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.setItemClickListener(new ItemClickAnimes() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick){

//                    Toast.makeText(context, "Borrando elemento de la posicion: "+position, Toast.LENGTH_SHORT).show();
                    alertDialogWarning(position,listData.get(position));
                }else {
                    Animes anime = listData.get(position);
                    conta=0;
                    myMenu(position,anime);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void changeDataItem(List<Animes> list_aux) {
        listData = list_aux;
        notifyDataSetChanged();
    }

    public void updateDataItem() {
        notifyDataSetChanged();
    }

    public void deleteDataItemById(int id) {
        int i=0;
        for (Animes a:listData){
            if (id==a.getId()){
                listData.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, listData.size());
                break;
            }
            i++;
        }
    }

    public void deleteDataItem(int index) {
        listData.remove(index);
        notifyItemRemoved(index);
    }

    public void addDataItem(List<Animes> list_aux) {
        listData.addAll(list_aux);
        notifyDataSetChanged();
    }

    public void alertDialogWarning(final int position, final Animes animes){
        final boolean[] x = {false};
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Eliminar Item Seleccionado");
        alertDialog.setCancelable(false);

        alertDialog.setMessage("¿esta seguro que desea eliminar, \""+ animes.getName() +"\"?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Eliminar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        animesDB.deleteItemProductById(""+listData.get(position).getId());
                        deleteDataItemById(animes.getId());
                        changeDataItem(listData);
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

    private void myMenu(final int position, final Animes animes) {
       final Dialog dialog;
        dialog = new Dialog(context, R.style.AnimateDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.item_menu);
        final String estado = animes.getStatus();
        Log.w(TAG,"ESTADO: "+estado);
        ImageButton btnEdit   = (ImageButton) dialog.findViewById(R.id.ivbtnOp1);
        final ImageButton btnColor  = (ImageButton) dialog.findViewById(R.id.ivbtnOp2);
        final ImageButton btnStatus = (ImageButton) dialog.findViewById(R.id.ivbtnOp3);
        final ImageButton btnAddCap   = (ImageButton) dialog.findViewById(R.id.ivbtnOp4);
        final ImageButton btnSustractCap   = (ImageButton) dialog.findViewById(R.id.ivbtnOp5);
        TextView tvId = (TextView) dialog.findViewById(R.id.tvId);
        TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
        tvTitle.setSelected(true);
        tvTitle.setTextColor(tvTitle.getContext().getResources().getColor(Utils.getColorList(animes.getColor())));

        tvId.setText(String.valueOf(animes.getId()));
        tvTitle.setText(animes.getName().toUpperCase());

        final String msgPro = context.getResources().getString(R.string.status_progress);
        final String msgEnd = context.getResources().getString(R.string.status_finish);
        final String msgOld = context.getResources().getString(R.string.status_old);
        final String msgPrem = context.getResources().getString(R.string.status_premiere);
        Log.w(TAG,"ESTADO: "+estado+" = "+msgPro);

        if (!estado.equals("N/A")) {
            if (estado.equals(msgPro)) {
                btnStatus.setImageResource(R.drawable.ic_doc_finish);
            } else if (estado.equals(msgEnd)) {
                btnStatus.setImageResource(R.drawable.ic_doc_wait);
            } else if (estado.equals(msgPrem)){
                btnStatus.setImageResource(R.drawable.ic_doc_premiere);
            } else {
                btnStatus.setImageResource(R.drawable.ic_doc_old);
            }
        }

        if (animes.getCapitule()<0){
            btnSustractCap.setVisibility(View.GONE);
            btnAddCap.setVisibility(View.VISIBLE);
        }
        if (animes.getCapitule()>9998){
            btnSustractCap.setVisibility(View.VISIBLE);
            btnAddCap.setVisibility(View.GONE);
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Log.w(TAG, "\nid: "+animes.getId()+" name: "+animes.getName()+
                        " capitule: "+animes.getCapitule()+" status: "+animes.getStatus()+
                        " created: "+animes.getDateCreated()+" update: "+animes.getDateUpdate()+
                        " color: "+animes.getColor()+"\n\nImage: "+animes.getImage()+ " = "
                        +animes.getBtpImage()+"\n\n" );

                Log.w(TAG,"edit anime");
                dialog.cancel();
            }
        });
        btnColor.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view)
            {
                myMenuColor(position,animes,btnColor);
                Log.w(TAG,"color");
                dialog.cancel();
            }
        });
        btnStatus.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view)
            {
                List<Animes> lis = new ArrayList<>();
                String msg = animes.getStatus();


                if(animes.getStatus().equals(msgPro)) {
                    animes.setStatus(msgEnd);
                    msg=msgEnd;
                }else if( animes.getStatus().equals(msgEnd) ){
                    animes.setStatus(msgPrem);
                    msg=msgPrem;
                }else if( animes.getStatus().equals(msgPrem) ){
                    animes.setStatus(msgOld);
                    msg=msgPro;
                }else {
                    animes.setStatus(msgPro);
                    msg=msgOld;
                }
                Toast.makeText(context, animes.getName()+", está: "+animes.getStatus(), Toast.LENGTH_SHORT).show();
                listData.set(position,animes);
                lis.add(animes);
                changeDataItem(listData);

                if( animes.getStatus().equals(msgPro) ) {
                    btnStatus.setImageResource(R.drawable.ic_doc_finish);
                }else if( animes.getStatus().equals(msgEnd) ){
                    btnStatus.setImageResource(R.drawable.ic_doc_wait);
                }else if( animes.getStatus().equals(msgPrem) ){
                    btnStatus.setImageResource(R.drawable.ic_doc_premiere);
                }else {
                    btnStatus.setImageResource(R.drawable.ic_doc_old);
                }
                animesDB.updateData(animes.getId(),lis);
                Log.w(TAG,"finish anime");
//                dialog.cancel();
            }
        });
        btnAddCap.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view)
            {
                int cap = animes.getCapitule();
                if (animes.getCapitule()<9999) {
                    cap++;
                    animes.setCapitule(cap);
                    animes.setDateUpdate(Utils.dateFormatAll(setup.date.FORMAT));
                    listData.set(position, animes);
                    List<Animes> list = new ArrayList<>();
                    list.add(animes);
                    animesDB.updateData(animes.getId(), list);
                    changeDataItem(listData);
                    Log.w(TAG, "Add Capitule");
                    btnSustractCap.setVisibility(View.VISIBLE);
//                dialog.cancel();
                }else {
                    btnSustractCap.setVisibility(View.VISIBLE);
                    btnAddCap.setVisibility(View.GONE);
                    Toast.makeText(context, "Numero capitulo sobrepasa la memoria", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnSustractCap.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view)
            {
                int cap = animes.getCapitule();
                if (animes.getCapitule()>0) {
                    cap--;
                    animes.setCapitule(cap);
                    animes.setDateUpdate(Utils.dateFormatAll(setup.date.FORMAT));
                    listData.set(position, animes);
                    List<Animes> list = new ArrayList<>();
                    list.add(animes);
                    animesDB.updateData(animes.getId(), list);
                    changeDataItem(listData);
                    Log.w(TAG, "Add Capitule");
                    btnAddCap.setVisibility(View.VISIBLE);
//                dialog.cancel();
                }else {
                    btnSustractCap.setVisibility(View.GONE);
                    btnAddCap.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "No se puede bajar de capitulo", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    private void myMenuColor(final int position, final Animes animes, final ImageView btnCol) {
        final Dialog dialog;
        final String[] mColor = new String[]{"sinColor","rojo","azul","amarillo","blanco","negro","verde","violeta","naranja"};
        dialog = new Dialog(context, R.style.AnimateDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.item_menu_color);
        final ImageButton btnColor1   = (ImageButton) dialog.findViewById(R.id.ivbtnColor1);
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
                animes.setColor(mColor[1]);
                listData.set(position,animes);
                changeDataItem(listData);
//                Toast.makeText(context, animes.getName()+", color: "+animes.getColor(), Toast.LENGTH_SHORT).show();
                Log.w(TAG,"color");
                List<Animes> list = new ArrayList<>();
                list.add(animes);
                animesDB.updateData(animes.getId(),list);
                btnCol.setImageResource(R.drawable.ic_color_1);
                dialog.cancel();
            }
        });
        btnColor2.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view)
            {
                animes.setColor(mColor[2]);
                listData.set(position,animes);
                changeDataItem(listData);
//                Toast.makeText(context, animes.getName()+", color: "+animes.getColor(), Toast.LENGTH_SHORT).show();
                Log.w(TAG,"color");
                List<Animes> list = new ArrayList<>();
                list.add(animes);
                animesDB.updateData(animes.getId(),list);
                btnCol.setImageResource(R.drawable.ic_color_2);
                dialog.cancel();
            }
        });
        btnColor3.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view)
            {
                animes.setColor(mColor[3]);
                listData.set(position,animes);
                changeDataItem(listData);
//                Toast.makeText(context, animes.getName()+", color: "+animes.getColor(), Toast.LENGTH_SHORT).show();
                Log.w(TAG,"color");
                List<Animes> list = new ArrayList<>();
                list.add(animes);
                animesDB.updateData(animes.getId(),list);
                btnCol.setImageResource(R.drawable.ic_color_3);
                dialog.cancel();
            }
        });
        btnColor4.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view)
            {
                animes.setColor(mColor[4]);
                listData.set(position,animes);
                changeDataItem(listData);
//                Toast.makeText(context, animes.getName()+", color: "+animes.getColor(), Toast.LENGTH_SHORT).show();
                Log.w(TAG,"color");
                List<Animes> list = new ArrayList<>();
                list.add(animes);
                animesDB.updateData(animes.getId(),list);
                btnCol.setImageResource(R.drawable.ic_color_4);
                dialog.cancel();
            }
        });
        btnColor5.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view)
            {
                animes.setColor(mColor[5]);
                listData.set(position,animes);
                changeDataItem(listData);
//                Toast.makeText(context, animes.getName()+", color: "+animes.getColor(), Toast.LENGTH_SHORT).show();
                Log.w(TAG,"color");
                List<Animes> list = new ArrayList<>();
                list.add(animes);
                animesDB.updateData(animes.getId(),list);
                btnCol.setImageResource(R.drawable.ic_color_5);
                dialog.cancel();
            }
        });
        btnColor6.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view)
            {
                animes.setColor(mColor[6]);
                listData.set(position,animes);
                changeDataItem(listData);
//                Toast.makeText(context, animes.getName()+", color: "+animes.getColor(), Toast.LENGTH_SHORT).show();
                Log.w(TAG,"color");
                List<Animes> list = new ArrayList<>();
                list.add(animes);
                animesDB.updateData(animes.getId(),list);
                btnCol.setImageResource(R.drawable.ic_color_6);
                dialog.cancel();
            }
        });
        btnColor7.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view)
            {
                animes.setColor(mColor[7]);
                listData.set(position,animes);
                changeDataItem(listData);
//                Toast.makeText(context, animes.getName()+", color: "+animes.getColor(), Toast.LENGTH_SHORT).show();
                Log.w(TAG,"color");
                List<Animes> list = new ArrayList<>();
                list.add(animes);
                animesDB.updateData(animes.getId(),list);
                btnCol.setImageResource(R.drawable.ic_color_7);
                dialog.cancel();
            }
        });
        btnColor8.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view)
            {
                animes.setColor(mColor[8]);
                listData.set(position,animes);
                changeDataItem(listData);
//                Toast.makeText(context, animes.getName()+", color: "+animes.getColor(), Toast.LENGTH_SHORT).show();
                Log.w(TAG,"color");
                List<Animes> list = new ArrayList<>();
                list.add(animes);
                animesDB.updateData(animes.getId(),list);
                btnCol.setImageResource(R.drawable.ic_color_8);
                dialog.cancel();
            }
        });
        dialog.show();
    }

}