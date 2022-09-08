package com.affinity.sk_facilities.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.affinity.sk_facilities.FetchOutStationDtls;
import com.affinity.sk_facilities.R;

import java.util.ArrayList;
import java.util.List;

public class FetchOutDtlsAdapter extends RecyclerView.Adapter<FetchOutDtlsAdapter.CustomViewHolder> {
    private List<FetchOutStationDtls> carTypes;
    private AdapterView.OnItemClickListener listener;
    public Context context;
    public ArrayList<FetchOutStationDtls> dataItems;
    int row_index;


    public FetchOutDtlsAdapter(List<FetchOutStationDtls> carTypes) {
        this.carTypes = carTypes;
    }

    @Override
    public FetchOutDtlsAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.outstation_type, parent, false);

        return new FetchOutDtlsAdapter.CustomViewHolder(itView);
    }

    @Override
    public void onBindViewHolder(FetchOutDtlsAdapter.CustomViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FetchOutStationDtls gobase = carTypes.get(position);
        holder.type_names.setText(gobase.getName());
        //  Picasso.get().load(carTypes.get(position).getIcon()).into(holder.icon);

       /* holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                notifyDataSetChanged();
            }
        });
        if (row_index == position) {
            holder.mCardView.setCardBackgroundColor(Color.BLACK);
            holder.hours_txt.setTextColor(Color.WHITE);
            //  Picasso.get().load(carTypes.get(position).getActive_icon()).into(holder.icon);
        } else {
            holder.mCardView.setCardBackgroundColor(Color.WHITE);
            holder.hours_txt.setTextColor(Color.BLACK);
            //   Picasso.get().load(carTypes.get(position).getIcon()).into(holder.icon);
        }*/

    }

    @Override
    public int getItemCount() {
        return carTypes.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView type_names;


        public CustomViewHolder(View itView) {
            super(itView);
            type_names = (TextView) itView.findViewById(R.id.type_name);

        }
    }
}