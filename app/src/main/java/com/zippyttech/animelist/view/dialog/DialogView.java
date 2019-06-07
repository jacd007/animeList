package com.zippyttech.animelist.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zippyttech.animelist.R;
import com.zippyttech.animelist.common.utility.UtilsImage;
import com.zippyttech.animelist.common.utility.setup;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zippyttech on 18/10/18.
 */

public class DialogView {
    private Dialog dialog;
    private Context context;
    private static final String SHARED_KEY = "shared_key";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private ImageView iv_dPreview;

    public DialogView(@NonNull Context context) {
        this.context = context;
        settings = context.getSharedPreferences(SHARED_KEY, 0);
        editor = settings.edit();
    }
    @SuppressLint("SetTextI18n")
    public void preView(String image){

        final String TAG="DialogMessage";
        Button btn_dAceptar;
        dialog = new Dialog(context, R.style.Theme_AppCompat_DayNight_Dialog);
//        dialog = new Dialog(context,R.style.Animate3);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_view);

        btn_dAceptar    = (Button)   dialog.findViewById(R.id.btnDAceptarPrev);
        iv_dPreview     = (ImageView) dialog.findViewById(R.id.ivDPrev);

//        setImage(image);

        btn_dAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setImage(String Image) {
        Log.w("setImage","configurando imagen");
        try {
            if (!Image.equals("null")) {
                if (!Image.contains("http")) {
                    if (!Image.substring(0, 23).equals("data:image/jpeg;base64,")) {
                        Image = context.getResources().getString(R.string.image_complement) + Image;
                    }
                    Bitmap bitmap = UtilsImage.b64ToBitmap(Image);
                    iv_dPreview.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 480, 480, false));
                } else {
                    Glide.with(context)
                            .load(Image)
                            .placeholder(R.drawable.ic_broken_image)
                            .error(R.drawable.ic_no_image)
                            .into(iv_dPreview);
                }
            }else {
                Glide.with(context)
                        .load("xxxx")
                        .error(R.drawable.ic_broken_image)
                        .into(iv_dPreview);
            }
        }catch (VerifyError e){
            iv_dPreview.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_no_image));
            e.printStackTrace();
            Log.e("setImage",e.getMessage());
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            Glide.with(context)
                    .load(Image)
                    .placeholder(R.drawable.ic_broken_image)
                    .error(R.drawable.ic_no_image)
                    .into(iv_dPreview);
            Log.e("setImage",e.getMessage());
        }catch (NullPointerException e){
            Log.e("setImage",e.getMessage());
        }
    }

}//*//