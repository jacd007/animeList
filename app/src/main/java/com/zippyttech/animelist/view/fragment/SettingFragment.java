package com.zippyttech.animelist.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.zippyttech.animelist.R;
import com.zippyttech.animelist.common.SyncService;
import com.zippyttech.animelist.common.utility.Utils;
import com.zippyttech.animelist.common.utility.setup;
import com.zippyttech.animelist.data.AnimesDB;
import com.zippyttech.animelist.model.Animes;

import java.util.List;


public class SettingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "SettingFragment";
    private static final String SHARED_KEY = "shared_key";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private Switch sEmail,sMoved,sNotification,sTextSize,sItemType;
    private TextView tvD1,tvD2,tvD3;
    private EditText etContent;

    private AnimesDB animesDB;
    private Animes animes;
    private List<Animes> listA;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        // Inflate the layout for this fragment
        initComponent(view);
        return view;
    }

    private void initComponent(View view) {
        settings = getContext().getSharedPreferences(SHARED_KEY, 0);
        editor = settings.edit();

        animesDB = new AnimesDB(getContext());

        sEmail = (Switch) view.findViewById(R.id.swEmail);
        sMoved = (Switch) view.findViewById(R.id.swTextMoved);
        sNotification = (Switch) view.findViewById(R.id.swNotification);
        sTextSize = (Switch) view.findViewById(R.id.swTextSize);
        sItemType = (Switch) view.findViewById(R.id.swItemType);

        tvD1 = (TextView) view.findViewById(R.id.tvDescription1);
        tvD2 = (TextView) view.findViewById(R.id.tvDescription2);
        tvD3 = (TextView) view.findViewById(R.id.tvDescription3);

        etContent = (EditText) view.findViewById(R.id.etContent);

        sEmail.setChecked(settings.getBoolean(setup.tools.ENABLED_EMAIL,false));
        sMoved.setChecked(settings.getBoolean(setup.tools.ENABLED_MOVED,false));
        sTextSize.setChecked(settings.getBoolean(setup.tools.ENABLED_TEXTSIZE,false));
        sNotification.setChecked(settings.getBoolean(setup.tools.ENABLED_NOTIFICACION,false));
        sItemType.setChecked(settings.getBoolean(setup.tools.ITEM_TYPE,false));

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onResume() {
        super.onResume();

        sEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(setup.tools.ENABLED_EMAIL,sEmail.isChecked());
                etContent.setEnabled(sEmail.isChecked());
                editor.commit();
                editor.apply();
                if (sEmail.isChecked())
                    Utils.createFileAnimeListDB(getContext(),Utils.getListAn(animesDB.getAnimes()),"data","json");
//                    Log.w(TAG,"//---//\n"+ Utils.getListAn(animesDB.getAnimes()));

                Log.w(TAG,"\nEMAIL: "+settings.getBoolean(setup.tools.ENABLED_EMAIL,false));
            }
        });
        sMoved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(setup.tools.ENABLED_MOVED,sMoved.isChecked());
                editor.commit();
                editor.apply();
                Log.w(TAG,"\nMOVIMIENTO: "+settings.getBoolean(setup.tools.ENABLED_MOVED,false));
            }
        });
        sTextSize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(setup.tools.ENABLED_TEXTSIZE,sTextSize.isChecked());
                editor.commit();
                editor.apply();
                Log.w(TAG,"\nTextSize: "+settings.getBoolean(setup.tools.ENABLED_TEXTSIZE,false));
            }
        });
        sNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(setup.tools.ENABLED_NOTIFICACION,sNotification.isChecked());
                editor.commit();
                editor.apply();
                Log.w(TAG,"\nNOTIFICACION: "+settings.getBoolean(setup.tools.ENABLED_NOTIFICACION,false));

                if (sNotification.isChecked()){
                    getContext().startService(new Intent(getContext(), SyncService.class));
                }else {
                    getContext().stopService(new Intent(getContext(), SyncService.class));
                }

            }
        });
        sItemType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(setup.tools.ITEM_TYPE,sItemType.isChecked());
                editor.commit();
                editor.apply();
                Log.w(TAG,"\nTextSize: "+settings.getBoolean(setup.tools.ITEM_TYPE,false));
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
