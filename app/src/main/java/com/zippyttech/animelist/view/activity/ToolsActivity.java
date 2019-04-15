package com.zippyttech.animelist.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.zippyttech.animelist.R;
import com.zippyttech.animelist.common.utility.setup;

public class ToolsActivity extends AppCompatActivity {

    private TextView tvToolsMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
        initComponent();
    }

    private void initComponent() {
        tvToolsMsg = (TextView) findViewById(R.id.tvToolsMsg);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent data = new Intent();
        data.putExtra(setup.list.TYPE,"un dato");
        setResult(RESULT_OK,data);
    }
}
