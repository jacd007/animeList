package com.zippyttech.animelist.view.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zippyttech.animelist.R;
import com.zippyttech.animelist.adapter.rvAdapter;
import com.zippyttech.animelist.adapter.rvAdapterGrid;
import com.zippyttech.animelist.common.utility.Utils;
import com.zippyttech.animelist.common.utility.UtilsDate;
import com.zippyttech.animelist.common.utility.setup;
import com.zippyttech.animelist.data.AnimesDB;
import com.zippyttech.animelist.model.Animes;
import com.zippyttech.animelist.view.activity.NavigationActivity;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG="ListFragment";
    private static final String SHARED_KEY = "shared_key";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private List<Animes> animes;
    private RecyclerView rv;
    private Animes a;
    private TextView tvSize;
    private LinearLayoutManager llm;
    private GridLayoutManager glm;
    public static rvAdapter adapter;
    public static rvAdapterGrid adapter2;
    private String FORMATE_DAY;
    private AnimesDB animesDB;
    private SwipeRefreshLayout refreshLayout;
    private boolean GridType;


    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
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
    public void onStop() {
        super.onStop();
        if (GridType){
            adapter2.updateDataItem();
        }else {
            adapter.updateDataItem();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        initComponent(view);
//        saveDate();
        return view;
    }

    private void saveDate() {
        FORMATE_DAY = Utils.dateFormatAll(setup.date.FORMAT);
        FORMATE_DAY = Utils.DateVariable(FORMATE_DAY,-1,setup.date.FORMAT);
        a = new Animes();
        a.setId(animes.size());
        a.setIdAnime(1000+animes.size());
        a.setName("anime de prueba ("+animes.size()+")");
        a.setSubName("subName "+animes.size());
        a.setStatus(getString(R.string.status_old));
        a.setCapitule(1+animes.size());
        a.setDateCreated(FORMATE_DAY);
        a.setDateUpdate(FORMATE_DAY);
        a.setColor("amarillo");
        animes.add(a);
        animesDB.setAnimes(animes);

        print(animes);
    }

    public void print(List<Animes> LISTA){
        if (settings.getBoolean(setup.tools.ITEM_TYPE,false)){
            glm = new GridLayoutManager(getContext(),2);//GRID
            rv.setLayoutManager(glm);
            Log.w(TAG,"Nro. ITEMS: "+LISTA.size());
            adapter2 = new rvAdapterGrid(LISTA,getContext(),tvSize);
            rv.setAdapter(adapter2);
            String msg;
            msg = "Total de Animes: " + LISTA.size();
            tvSize.setText(msg);
            editor.putString(setup.general.adapter,""+rv.getAdapter().toString());
            editor.commit();
        }else {
            llm = new LinearLayoutManager(getContext());
            rv.setLayoutManager(llm);
            Log.w(TAG,"Nro. ITEMS: "+LISTA.size());
            adapter = new rvAdapter(LISTA,getContext(),tvSize);
            rv.setAdapter(adapter);
            String msg;
            msg = "Total de Animes: " + LISTA.size();
            tvSize.setText(msg);
            editor.putString(setup.general.adapter,""+rv.getAdapter().toString());
            editor.commit();
        }

    }

    private void initComponent(View view) {
        settings = getContext().getSharedPreferences(SHARED_KEY,0);
        editor = settings.edit();
        GridType = settings.getBoolean(setup.tools.ITEM_TYPE,false);
        editor.putString(setup.general.adapter,null);
        editor.apply();
        animesDB = new AnimesDB(getContext());
        animes = new ArrayList<>();
        tvSize = (TextView) view.findViewById(R.id.tvSize);
        rv = (RecyclerView) view.findViewById(R.id.rvList);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        List<Animes> list = animesDB.getAnimes();
//        for (Animes a: list){
//            if (UtilsDate.differencesDate(a.getDateCreated(),a.getDateUpdate())>9){
//                a.setStatus(getResources().getString(R.string.status_old));
//
//            }
//        }

        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.refresh),getResources().getColor(R.color.refresh1));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (GridType){
                            adapter2.changeDataItem(animesDB.getAnimes());
                        }else {
                            adapter.changeDataItem(animesDB.getAnimes());
                        }

                        refreshLayout.setRefreshing(false);
                    }
                },setup.tools.TIME_THREAD);
            }
        });
            Log.w(TAG,"GRID? "+GridType);
    }



    @Override
    public void onResume() {
        super.onResume();

        try {
            print(animesDB.getAnimes());
        }catch (IllegalStateException e){
            e.printStackTrace();
        }


        Bundle bundle = new Bundle();
       String param = bundle.getString("edttext", "nada guardado");
// set Fragmentclass Arguments
        ListFragment fragobj = new ListFragment();
        fragobj.setArguments(bundle);
        Log.w(TAG,"DATA: "+param);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == setup.key.keyListEdit && resultCode == RESULT_OK) {
                if (data!=null) {
                    Boolean editData = data.getBooleanExtra(setup.tools.RECHARGE_LIST,false);
//                    List<Animes> listAux = new ArrayList<>();
//                    Animes a = new Animes();
//                    int id = data.getIntExtra(setup.list.ID,-1);
//                    String name = data.getStringExtra(setup.list.NAME);
//                    String subName = data.getStringExtra(setup.list.SUB_NAME);
//                    String status = data.getStringExtra(setup.list.STATUS);
//                    int capitule = data.getIntExtra(setup.list.CAPITULE,-1);
//                    String color = data.getStringExtra(setup.list.COLOR);
//                    String dateCreated = data.getStringExtra(setup.list.DATE_CREATED);
//                    String dateUpdate = data.getStringExtra(setup.list.DATE_UPDATE);
//                    String image = data.getStringExtra(setup.list.IMAGE);
//
//                    a.setId(id);
//                    a.setName(name);
//                    a.setSubName(subName);
//                    a.setStatus(status);
//                    a.setCapitule(capitule);
//                    a.setColor(color);
//                    a.setDateCreated(dateCreated);
//                    a.setDateUpdate(dateUpdate);
//                    a.setImage(image);
//
//                    listAux.add(a);
//                    adapter.changeDataItem(listAux);
                    if (editData)
                        print(animesDB.getAnimes());
                }
            }
            if (requestCode == setup.key.keyListAdd && resultCode == RESULT_OK) {
                if (data!=null) {
                    List<Animes> listAux = new ArrayList<>();
                    Animes a = new Animes();
                        String xxxxxx = data.getStringExtra("xxxxx");
                    a.setName(xxxxxx);
                    listAux.add(a);
                    if (GridType){
                        adapter2.changeDataItem(listAux);
                    }else {
                        adapter.changeDataItem(listAux);
                    }
                }
            }
            if (requestCode == setup.key.keyListDelete && resultCode == RESULT_OK) {
                if (data!=null) {
                    int pos = data.getIntExtra("xxxxx",-1);
                    if (GridType){
                        adapter2.deleteDataItem(pos);
                    }else {
                        adapter.deleteDataItem(pos);
                    }
                }
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
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

//
//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//        List<Animes> list_aux = new ArrayList<>();
//        List<Animes> list= animesDB.getAnimes();
//
//        for (Animes aux:
//                list ) {
//            try {
//                String name = "";
//
//                name = aux.getName();
//                String text = newText.toLowerCase();
//
//
//                if (name.contains(text))
//                    list_aux.add(aux);
//
//            }catch (Exception e){
//                e.printStackTrace();
//                continue;
//            }
//        }
//        adapter.changeDataItem(list_aux);
//
//        return false;
//    }


}
