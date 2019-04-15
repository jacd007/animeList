package com.zippyttech.animelist.common.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by zippyttech on 18/01/19.
 */

public class Draw2d extends View {

    public Draw2d(Context context) {
        super(context);
        setDrawingCacheEnabled(true);
    }

    @Override
    protected void onDraw(Canvas c) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        c.drawCircle(50, 50, 30, paint);

        try {
            getDrawingCache().compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File("/mnt/sdcard/arun.jpg")));
        } catch (Exception e) {
            Log.e("Error--------->", e.toString());
        }
        super.onDraw(c);
    }
}