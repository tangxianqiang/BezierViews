package com.tang.bezierviews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tang.bezierviews.adapter.FoamAdapter;
import com.tang.bezierviews.widget.DragFoamView;
import com.tang.bezierviews.widget.MyRView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DragFoamView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        MyRView recyclerView = (MyRView) findViewById(R.id.rv_foam);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new FoamAdapter(this));
    }

}
