package com.affinity.sk_facilities.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.affinity.sk_facilities.R;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

import com.affinity.sk_facilities.Api;
import com.affinity.sk_facilities.Constants;
import com.affinity.sk_facilities.beens.OvertripDetailsList;
import com.affinity.sk_facilities.RetrofitClient;
import com.affinity.sk_facilities.beens.TripDetails;
import com.affinity.sk_facilities.adapter.TripDetailsAdapter;
import com.affinity.sk_facilities.utils.Common;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBookingsActivity extends AppCompatActivity  {

    TextView txt_all_trip,txtNextRide;
    RecyclerView recycle_all_trip;
    RelativeLayout layout_no_recourd_found,newRide,layout_back_arrow;
    LinearLayout layout_recycleview;
    SharedPreferences userPref;
    Typeface OpenSans_Bold,OpenSans_Regular,Roboto_Bold;
    Dialog ProgressDialog;
    RotateLoading cusRotateLoading;
    Common common = new Common();
    private ArrayList<TripDetails> tripDetailsList;
    private RecyclerView recyclerView;
    private TripDetailsAdapter eAdapter;
    String user_ID,ride_id;
    View view_past,view_activew;
    TextView booking_text,active,past;
    LinearLayout li_past,li_active;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_trip);

        SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        user_ID = prefs.getString(Constants.user_id, "");


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_my_booking);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    startActivity(new Intent(MyBookingsActivity.this, HomeActivity.class));
                    // on favorites clicked
                    return true;
                } if (item.getItemId() == R.id.navigation_more) {
                    startActivity(new Intent(MyBookingsActivity.this, MoreActivity.class));
                    // on favorites clicked
                    return true;
                } if (item.getItemId() == R.id.navigation_sk_black) {
                    startActivity(new Intent(MyBookingsActivity.this, Sk_BlackActivity.class));
                    // on favorites clicked
                    return true;
                }
                return false;
            }
        });
        //txt_all_trip = (TextView)findViewById(R.id.txt_all_trip);
        txtNextRide = (TextView)findViewById(R.id.txtNextRide);
        recycle_all_trip = (RecyclerView)findViewById(R.id.recycle_all_trip);
        layout_no_recourd_found = (RelativeLayout)findViewById(R.id.layout_no_recourd_found);
        newRide = (RelativeLayout)findViewById(R.id.newRide);
        view_past = (View)findViewById(R.id.view_past);
        view_activew = (View)findViewById(R.id.view_active);
        booking_text = (TextView)findViewById(R.id.booking_text);
        active = (TextView)findViewById(R.id.active);
        past = (TextView)findViewById(R.id.past);
        past.setTextColor(Color.parseColor("#f9c700"));
        li_past = (LinearLayout)findViewById(R.id.li_past);
        li_active = (LinearLayout)findViewById(R.id.li_active);

        layout_recycleview = (LinearLayout)findViewById(R.id.layout_recycleview);
        //layout_back_arrow = (RelativeLayout)findViewById(R.id.layout_back_arrow);

        OpenSans_Bold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold_0.ttf");
        OpenSans_Regular = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular_0.ttf");
        Roboto_Bold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

        txtNextRide.setTypeface(OpenSans_Bold);
        newRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MyBookingsActivity.this, com.affinity.sk_facilities.activity.HomeActivity.class);
                startActivity(intent);
            }
        });

        li_past.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                past.setTextColor(Color.parseColor("#f9c700"));
                view_past.setBackgroundColor(Color.parseColor("#f9c700"));

                active.setTextColor(Color.parseColor("#727272"));
                view_activew.setBackgroundColor(Color.parseColor("#727272"));
                booking_text.setText("No Bokkings yet");
            }
        });

        li_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                active.setTextColor(Color.parseColor("#f9c700"));
                view_activew.setBackgroundColor(Color.parseColor("#f9c700"));

                past.setTextColor(Color.parseColor("#727272"));
                view_past.setBackgroundColor(Color.parseColor("#727272"));
                booking_text.setText("No Active Bokkings");
            }
        });

        ProgressDialog = new Dialog(MyBookingsActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.custom_progress_dialog);
        ProgressDialog.setCancelable(false);
        cusRotateLoading = (RotateLoading)ProgressDialog.findViewById(R.id.rotateloading_register);
       /* ProgressDialog.show();
        cusRotateLoading.start();*/


        Api api = RetrofitClient.getApiService();

        //String id = "23";

      /*  Call<OvertripDetailsList> call = api.getTripDetails(user_ID);

        call.enqueue(new Callback<OvertripDetailsList>() {
            @Override
            public void onResponse(Call<OvertripDetailsList> call, Response<OvertripDetailsList> response) {
                OvertripDetailsList  users = response.body();
                if (users.status.equalsIgnoreCase("true")) {
                    ProgressDialog.cancel();
                    cusRotateLoading.stop();
                    tripDetailsList = response.body().getTripDetails();
                    recyclerView = (RecyclerView) findViewById(R.id.recycle_all_trip);
                    eAdapter = new TripDetailsAdapter(tripDetailsList);
                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyBookingsActivity.this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(eAdapter);
                    recyclerView.addOnItemTouchListener(new MyBookingsActivity.RecyclerTouchListener(MyBookingsActivity.this,
                            recyclerView, new MyBookingsActivity.ClickListener() {
                        @Override
                        public void onClick(View view, final int position) {
                            ride_id = tripDetailsList.get(position).getRide_id();
                           Intent intent = new Intent(MyBookingsActivity.this,SingleTripDetailsActivity.class);
                           intent.putExtra("ride_id",ride_id);
                            startActivity(intent);
                        }
                        @Override
                        public void onLongClick(View view, int position) {
                        }
                    }));
                }
                else{
                    ProgressDialog.cancel();
                    cusRotateLoading.stop();
                    layout_no_recourd_found.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<OvertripDetailsList> call, Throwable t) {
                ProgressDialog.cancel();
                cusRotateLoading.stop();
                layout_no_recourd_found.setVisibility(View.VISIBLE);
            }
        });
*/
       /* layout_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selected", false);
                setResult(100, resultIntent);
                finish();
            }
        });*/
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.really_exit))
                .setMessage(getResources().getString(R.string.are_you_sure))
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                    }
                }).create().show();
    }


    //Recycler view clicking functions
    public static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }


    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        private MyBookingsActivity.ClickListener clicklistener;
        private GestureDetector gestureDetector;
        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final MyBookingsActivity.ClickListener clicklistener){
            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }
            return false;
        }
        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }
        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
}
