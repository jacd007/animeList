package com.zippyttech.animelist.data;

import android.content.Context;

import com.zippyttech.animelist.model.Animes;

/**
 * Created by zippyttech on 28/09/17.
 */

public class AnimesSet {

    private  int DATA_SET_SIZE ;//= 100;
    private   float NUM_HUES ;// = 359;
    private   Animes[] mColors; //= new Companies[DATA_SET_SIZE];
    private AnimesDB productDB;
    private  Context context;


    public AnimesSet(Context context) {
        productDB = new AnimesDB(context);
        this.context=context;
        DATA_SET_SIZE = productDB.getAnimes().size();
        mColors = new Animes[DATA_SET_SIZE];
        setDataArray();
    }


    private void setDataArray() {
        mColors = (Animes[]) productDB.getAnimes().toArray();
    }

    public int getSize() {
        return mColors.length;
    }

    public Animes get(int position) {
        return mColors[position];
    }
}
