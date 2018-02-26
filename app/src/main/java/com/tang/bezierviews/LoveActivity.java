package com.tang.bezierviews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tang.bezierviews.widget.FloatLoveView;

/**
 * Created by Administrator on 2018/2/25.
 */

public class LoveActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love);
//        Button addBtn = (Button) findViewById(R.id.btn_add);
//        final FloatLoveView flvTest = (FloatLoveView) findViewById(R.id.flv_test);
//        addBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                flvTest.addHeart();
//            }
//        });
        final FloatLoveView flvTest = (FloatLoveView) findViewById(R.id.flv_test);
        ImageView loveItem = (ImageView) flvTest.findViewById(R.id.id_love_item);
        loveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flvTest.addHeart();
            }
        });
    }
}
