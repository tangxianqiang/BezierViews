package com.tang.bezierviews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tang.bezierviews.R;
import com.tang.bezierviews.widget.DragFoamView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/2/6.
 */

public class FoamAdapter extends RecyclerView.Adapter<FoamAdapter.ViewHolder> {
    private SparseArray<String> data = new SparseArray<>();
    private Context context;

    public FoamAdapter(Context context) {
        this.context = context;
        for (int i = 0; i < 21; i++) {
            data.put(i,(100-i)+"");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_foam,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.foamTest.setFoamListener(new DragFoamView.OnFoamActionListener() {
            @Override
            public void foamBoom() {

            }

            @Override
            public void foamBounce() {

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        DragFoamView foamTest;
        public ViewHolder(View itemView) {
            super(itemView);
            foamTest = itemView.findViewById(R.id.foam_test);
        }
    }
}
