package com.tang.bezierviews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.tang.bezierviews.adapter.FoamAdapter;
import com.tang.bezierviews.widget.MyRView;

/**
 * Created by Administrator on 2018/2/25.
 */

public class FoamActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foam);
        getSupportActionBar().hide();
        MyRView recyclerView = (MyRView) findViewById(R.id.rv_foam);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new FoamAdapter(this));
    }
}
