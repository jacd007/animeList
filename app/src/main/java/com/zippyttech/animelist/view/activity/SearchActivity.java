package com.zippyttech.animelist.view.activity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.zippyttech.animelist.adapter.rvAdapter;
import com.zippyttech.animelist.model.Animes;
import com.zippyttech.animelist.R;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;



public class SearchActivity extends AppCompatActivity
        implements android.support.v7.widget.SearchView.OnQueryTextListener{

    //CONSTANT FIELDS
    public static final String SHARED_PREFERENCES_KEY = "SharedPreferences_data";
    public static final String TAG = "ListActivity";

    private static String nbox="000";
    private static String estatus="sin estatus";
    private static String visita="sin fecha";
    private int [] arrayContainers;
    private String [] hallazgosText = new String[]{"Estatus"};

    //PRIVATE FIELDS
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private RecyclerView rv;
    private TextView tv_Event;
    private List<Animes> operaciones;
    private List<Animes> listaFull;
    private Intent intent;

    private Animes op;
    private int EpochHour=3600;
    private int EpochDay=86400;
    private int EpochWeek=604800;
    private rvAdapter adapter;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initComponent();
    }


    private void initComponent() {
        settings = getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
        editor = settings.edit();

        setTitle(getString(R.string.app_name));

        nbox = settings.getString("nbox", getString(R.string.default_numbers_box));
        String capString = settings.getString("status", getString(R.string.default_status_list));
        estatus = capString.toUpperCase();
        visita = settings.getString("fechaVisita", getString(R.string.default_data));

        operaciones=null;
        listaFull=null;
        listaFull = new ArrayList<>();//iniciando lista de contenedores
        operaciones = new ArrayList<>();//iniciando lista de operaciones

        Toolbar tb = (Toolbar) findViewById(R.id.toolbarSe);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tb.setTitle(R.string.app_name);
        rv = (RecyclerView) findViewById(R.id.rvList);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.dashboard, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) SearchActivity.this.getSystemService(Context.SEARCH_SERVICE);

        android.support.v7.widget.SearchView searchView = null;
        if (searchItem != null) {
            searchView = (android.support.v7.widget.SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(SearchActivity.this.getComponentName()));
            searchView.setOnQueryTextListener(this);
            searchView.setInputType(InputType.TYPE_CLASS_NUMBER);

        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<Animes> list_aux = new ArrayList<>();
        List<Animes> list= operaciones;

        for (Animes aux:
                list ) {
            try {
                String caja = "0";

                caja = String.valueOf(aux.getName());
                String text = newText.toLowerCase();


                if (caja.contains(text))
                    list_aux.add(aux);

            }catch (Exception e){
                e.printStackTrace();
                continue;
            }
        }
        adapter.changeDataItem(list_aux);

        return false;
    }

}
