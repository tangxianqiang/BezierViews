package com.tang.bezierviews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tang.bezierviews.adapter.FoamAdapter;
import com.tang.bezierviews.widget.DragFoamView;
import com.tang.bezierviews.widget.MyRView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "DragFoamView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        Button foamBtn = (Button) findViewById(R.id.btn_foam);
        foamBtn.setOnClickListener(this);
        Button loveBtn = (Button) findViewById(R.id.btn_love);
        loveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_foam://粘性气泡设置
                intent.setClass(this,FoamActivity.class);
                break;
            case R.id.btn_love://漂浮的桃心
                intent.setClass(this,LoveActivity.class);
                break;
        }
        startActivity(intent);
    }
}
