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

import com.affinity.sk_facilities.R;
import com.affinity.sk_facilities.RideTypeDtls;

import java.util.ArrayList;
import java.util.List;

public class RideTypeDtlsAdapter extends RecyclerView.Adapter<RideTypeDtlsAdapter.CustomViewHolder> {
    private List<RideTypeDtls> carTypes;
    private AdapterView.OnItemClickListener listener;
    public Context context;
    public ArrayList<HourEstDtlsAdapter> dataItems;
    int row_index;


    public RideTypeDtlsAdapter(List<RideTypeDtls> carTypes) {
        this.carTypes = carTypes;
    }

    @Override
    public RideTypeDtlsAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ride_type, parent, false);

        return new RideTypeDtlsAdapter.CustomViewHolder(itView);
    }

    @Override
    public void onBindViewHolder(RideTypeDtlsAdapter.CustomViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RideTypeDtls gobase = carTypes.get(position);
        holder.type_name.setText(gobase.getType());
        //  Picasso.get().load(carTypes.get(position).getIcon()).into(holder.icon);

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                notifyDataSetChanged();
            }
        });
        if (row_index == position) {
            holder.mCardView.setBackgroundResource(R.drawable.btn_accent_circle_white_outlined);
            holder.type_name.setTextColor(Color.BLACK);
            //  Picasso.get().load(carTypes.get(position).getActive_icon()).into(holder.icon);
        } else {
            holder.mCardView.setBackgroundResource(R.drawable.btn_accent_circle_white_outlined);
            holder.type_name.setTextColor(Color.BLACK);
            //   Picasso.get().load(carTypes.get(position).getIcon()).into(holder.icon);
        }

    }

    @Override
    public int getItemCount() {
        return carTypes.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView type_name;
        public CardView mCardView;


        public CustomViewHolder(View itView) {
            super(itView);
            type_name = (TextView) itView.findViewById(R.id.type_name);

            mCardView = (CardView) itemView.findViewById(R.id.card_view_top);

        }
    }
}