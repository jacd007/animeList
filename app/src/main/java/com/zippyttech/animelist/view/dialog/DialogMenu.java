package com.zippyttech.animelist.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.zippyttech.animelist.R;
import com.zippyttech.animelist.view.activity.ScrollingActivity;
import com.zippyttech.animelist.adapter.rvAdapter;
import com.zippyttech.animelist.common.utility.setup;
import com.zippyttech.animelist.model.Animes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zippyttech on 28/12/18.
 */

@SuppressLint("Registered")
public class DialogMenu extends AppCompatActivity{

    private static final String TAG="DialogMenu";
    private static final String SHARED_KEY = "shared_key";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private Context context;
    private Animes animes;
    private Dialog dialog;
    private ArrayList<Animes> lis;

    public DialogMenu(Context context, Animes animes) {
        this.context = context;
        this.animes = animes;

        settings = context.getSharedPreferences(SHARED_KEY,0);
        editor = settings.edit();
    }

    public void myMenu(final int position) {
//        final Dialog dialog = new Dialog(context, R.style.Theme_AppCompat_DayNight_Dialog);
        dialog = new Dialog(context, R.style.AnimateDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.item_menu);
        final String estado = animes.getStatus();
        Log.w(TAG,"ESTADO: "+estado);
        ImageButton btnEdit   = (ImageButton) dialog.findViewById(R.id.ivbtnOp1);
        ImageButton btnColor  = (ImageButton) dialog.findViewById(R.id.ivbtnOp2);
        final ImageButton btnStatus = (ImageButton) dialog.findViewById(R.id.ivbtnOp3);
        ImageButton btnBack   = (ImageButton) dialog.findViewById(R.id.ivbtnOp4);

        final String msgPro = context.getResources().getString(R.string.status_progress);
        final String msgEnd = context.getResources().getString(R.string.status_finish);
        Log.w(TAG,"ESTADO: "+estado+" = "+msgPro);

        if(estado.equals(msgPro) ) {
            btnStatus.setImageResource(R.drawable.ic_doc_finish);
        }else {
            btnStatus.setImageResource(R.drawable.ic_doc_wait);
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context,ScrollingActivity.class);
                intent.putExtra(setup.list.ID,position);
                intent.putExtra(setup.list.ID_ANIME,animes.getIdAnime());
                intent.putExtra(setup.list.NAME,animes.getName());
                intent.putExtra(setup.list.SUB_NAME,animes.getSubName());
                intent.putExtra(setup.list.DATE_CREATED,animes.getDateCreated());
                intent.putExtra(setup.list.DATE_UPDATE,animes.getDateCreated());
                intent.putExtra(setup.list.STATUS,animes.getStatus());
                intent.putExtra(setup.list.IMAGE,animes.getImage());
                intent.putExtra(setup.list.CAPITULE,animes.getCapitule());
                intent.putExtra(setup.list.COLOR,animes.getColor());
                context.startActivity(intent);
                Log.w(TAG,"edit anime");
                dialog.cancel();
            }
        });
        btnColor.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view)
            {
                 lis = new ArrayList<>();
                animes.setColor("negro");
                lis.add(animes);
                Toast.makeText(context, animes.getName()+", color: "+animes.getColor(), Toast.LENGTH_SHORT).show();

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
                String msg = "";

                if(animes.getStatus().equals(msgPro)) {
                    animes.setStatus(msgEnd);
                    msg=msgEnd;
                }else {
                    animes.setStatus(msgPro);
                    msg=msgPro;
                }

                lis.add(animes);
                rvAdapter rv = new rvAdapter(context);
                rv.changeDataItem(lis);
                Toast.makeText(context, animes.getName()+", está: "+msg, Toast.LENGTH_SHORT).show();
                if( animes.getStatus().equals(msgPro) ) {
                    btnStatus.setImageResource(R.drawable.ic_doc_finish);
                }else {
                    btnStatus.setImageResource(R.drawable.ic_doc_wait);
                }

                Log.w(TAG,"finish anime");
//                dialog.cancel();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view)
            {

                Log.w(TAG,"back");
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
