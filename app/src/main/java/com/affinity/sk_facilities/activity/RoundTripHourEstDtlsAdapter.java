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
import com.affinity.sk_facilities.RoundTripHourEstDtls;

import java.util.ArrayList;
import java.util.List;

public class RoundTripHourEstDtlsAdapter extends RecyclerView.Adapter<RoundTripHourEstDtlsAdapter.CustomViewHolder> {
    private List<RoundTripHourEstDtls> carTypes;
    private AdapterView.OnItemClickListener listener;
    public Context context;
    public ArrayList<RoundTripHourEstDtlsAdapter> dataItems;
    int row_index;


    public RoundTripHourEstDtlsAdapter(List<RoundTripHourEstDtls> carTypes) {
        this.carTypes = carTypes;
    }

    @Override
    public RoundTripHourEstDtlsAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.round_trip_type, parent, false);

        return new RoundTripHourEstDtlsAdapter.CustomViewHolder(itView);
    }

    @Override
    public void onBindViewHolder(RoundTripHourEstDtlsAdapter.CustomViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RoundTripHourEstDtls gobase = carTypes.get(position);
        holder.hours_txt.setText(gobase.getName());
        //  Picasso.get().load(carTypes.get(position).getIcon()).into(holder.icon);

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
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
        }

    }

    @Override
    public int getItemCount() {
        return carTypes.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView hours_txt;
        public CardView mCardView;


        public CustomViewHolder(View itView) {
            super(itView);
            hours_txt = (TextView) itView.findViewById(R.id.hours_txt);

            mCardView = (CardView) itemView.findViewById(R.id.card_view_top);

        }
    }

}