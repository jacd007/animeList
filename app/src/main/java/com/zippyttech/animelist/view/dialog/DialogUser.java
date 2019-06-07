package com.zippyttech.animelist.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.zippyttech.animelist.R;
import com.zippyttech.animelist.common.utility.setup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zippyttech on 18/10/18.
 */

public class DialogUser {
    private Dialog dialog;
    private Context context;
    private static final String SHARED_KEY = "shared_key";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    public DialogUser(@NonNull Context context) {
        this.context = context;
        settings = context.getSharedPreferences(SHARED_KEY, 0);
        editor = settings.edit();
        EditDataUser();
    }
    @SuppressLint("SetTextI18n")
    private void EditDataUser(){

        final String TAG="DialogMessage";
        final EditText et_email,et_name;
        Button btn_dAceptar;
        Button btn_dCancelar;
        Button btn_dMantener;
        String msg="";

        dialog = new Dialog(context, R.style.Theme_AppCompat_DayNight_Dialog);
//        dialog = new Dialog(context,R.style.Animate3);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_user);

        et_name       = (EditText) dialog.findViewById(R.id.etDUser);
        et_email       = (EditText) dialog.findViewById(R.id.etForgotEmail);
        btn_dAceptar     = (Button)   dialog.findViewById(R.id.btnDAceptar);
        btn_dCancelar    = (Button)   dialog.findViewById(R.id.btnDCancelar);


        btn_dAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=et_email.getText().toString();
                String name=et_name.getText().toString();

                boolean FlagEmail=false,FlagName=false;
                if (email.isEmpty()){
                    et_email.setError("Requerido");
                    FlagEmail = false;
                }else {
                    if (isEmailValid(email)){
                        editor.putString(setup.general.UserEmail,et_email.getText().toString());
                        editor.commit();
                        editor.apply();
                        FlagEmail = true;
                    }else {
                        et_email.setError("No es un email valido.");
                        FlagEmail = false;
                    }
                }

                if (name.isEmpty()){
                    et_name.setError("Requerido");
                    FlagName = false;
                }else {
                       editor.putString(setup.general.UserName,et_name.getText().toString());
                       editor.commit();
                       editor.apply();
                    FlagName = true;

                }

                if (FlagEmail && FlagName)
                    dialog.dismiss();


            }
        });
        btn_dCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private String jsonForgot(String email) {
        String OPEN="{",CLOCED="}";
        String POINT=":",STEP=",",COM="\"";
        String TAB="\n";

        return OPEN + COM + "email" + COM + POINT + COM +  email + COM + CLOCED;
    }

    private boolean isEmailValid(String email){
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

}//*//