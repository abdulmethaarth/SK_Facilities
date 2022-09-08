package com.affinity.sk_facilities.activity;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.affinity.sk_facilities.HourPopUpDialog;
import com.affinity.sk_facilities.HoursList;
import com.affinity.sk_facilities.R;
import com.affinity.sk_facilities.RoundTripHourEstDtls;
import com.affinity.sk_facilities.RoundTripHoursList;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.victor.loading.rotate.RotateLoading;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.affinity.sk_facilities.Api;
import com.affinity.sk_facilities.ArrivingDriver;
import com.affinity.sk_facilities.Constants;
import com.affinity.sk_facilities.DriverConstans;
import com.affinity.sk_facilities.beens.RatePer;
import com.affinity.sk_facilities.RetrofitClient;
import com.affinity.sk_facilities.map.DirectionObject;
import com.affinity.sk_facilities.map.GsonRequest;
import com.affinity.sk_facilities.map.Helper;
import com.affinity.sk_facilities.map.LegsObject;
import com.affinity.sk_facilities.map.PolylineObject;
import com.affinity.sk_facilities.map.RouteObject;
import com.affinity.sk_facilities.map.StepsObject;
import com.affinity.sk_facilities.map.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;

import static com.google.android.gms.maps.model.JointType.ROUND;

public class BookingDetailActivity extends FragmentActivity {
    NotificationManager notificationManager;
    private GoogleMap mMap;
    private MapStyleOptions mapStyle;
    MarkerOptions options;
    TextView txt_car_name;

    ImageView img_car_image;
    RelativeLayout layout_back_arrow,layout_cancel,payment_dialog_layout;
    TextView txt_confirm_request,txtdistace,txtduration,txt_cancel_ride;

    Typeface OpenSans_Regular,Roboto_Regular,Roboto_Medium,Roboto_Bold,OpenSans_Semibold;
    String pickup_point;
    String drop_point;
    String pickup_lat;
    String pickup_lng;
    String ride_type="ride";
    String truckType,truckTypeArb;
    String CabId,user_id,cabtypeeNumer,bikename,distances,mobile_no,name,duration,destination,origin,rideTypeName;
    String booking_date;
    double PickupLatitude;
    double PickupLongtude;
    double DropLatitude;
    double DropLongtude;

    String ride_otp,payment_method;
    String amount = "0";
    Button done;
    SharedPreferences userPref;
    Dialog ProgressDialog,SetUpPayment;
    Api myApi;
    RotateLoading cusRotateLoading;
    TextView txtfromView,txtToView,nomalRide_btn,ride_msg,hourly_btn,txtTotal,setPayment,oneway,twoway,personal;
    String paymentType = "0";
    TextView confirmTripBooking,oneWayTwoway,layout_cash,deb_crd_card_layout;
    LinearLayout toAddressLayout,km_dur_layout;
    ImageView edt_loc_icon;
    PolylineOptions lineOptions,graylineOptions;
    Polyline polyline,greyPolyLine;
    TableRow tableRow;
    CheckBox chk;
    View oneway_line,twoway_line;
    Typeface  OpenSans_Bold;
    String hour ;

    private ArrayList<HourEstDtls> hourEstDtls;
    private HourEstDtlsAdapter hourEstDtlsAdapter;
    private ArrayList<RoundTripHourEstDtls> roundTripHourEstDtls;
    private RoundTripHourEstDtlsAdapter roundTripHourEstDtlsAdapter;
    RecyclerView Hours_recyclerview;
    int hourPosition;
    Dialog popUP_dialog;
    TextView txt_pickup,txt_drop,time_dur,cab_type,rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

      //  toAddressLayout = (LinearLayout) findViewById(R.id.toAddressLayout);
       /* km_dur_layout = (LinearLayout) findViewById(R.id.km_dur_layout);
        oneWayTwoway = (RelativeLayout) findViewById(R.id.oneWayTwoway);
        layout_cancel = (RelativeLayout) findViewById(R.id.layout_cancel);*/
        layout_back_arrow = (RelativeLayout) findViewById(R.id.layout_back_arrow);
       /* personal = (TextView)findViewById(R.id.personal);
        setPayment = (TextView)findViewById(R.id.setPayment);
        oneway_line = (View)findViewById(R.id.oneway_line);
        twoway_line = (View)findViewById(R.id.twoway_line);*/
        //chk=(CheckBox)findViewById(R.id.handicap);

        OpenSans_Bold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold_0.ttf");
        myApi = RetrofitClient.getRetrofitInstance().create(Api.class);
        SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        user_id = prefs.getString(String.valueOf(Constants.user_id), "");
        bikename = prefs.getString(Constants.bikeName, "");//"No name defined" is the default value
        distances = prefs.getString(Constants.distance, "");
        mobile_no = prefs.getString(Constants.mobileno, "");
        name = prefs.getString(Constants.firstname, "");
        amount = prefs.getString(Constants.amount, "");
        origin = prefs.getString(Constants.origin, "");
        destination = prefs.getString(Constants.destination, "");
        duration = prefs.getString(Constants.duration, "");

        txt_pickup = (TextView)findViewById(R.id.txt_pickup);
        txt_drop = (TextView)findViewById(R.id.txt_drop);
        rate = (TextView)findViewById(R.id.rate);
        cab_type = (TextView)findViewById(R.id.cab_type);
        time_dur = (TextView)findViewById(R.id.time_dur);
        txt_pickup.setText(origin);
        txt_drop.setText(destination);
        time_dur.setText("In "+duration);
        cab_type.setText(bikename);
        rate.setText("₹"+amount);


        Bundle bundle = getIntent().getExtras();
       /* final String from = bundle.getString("from");
        final String to = bundle.getString("to");*/
        PickupLatitude = getIntent().getExtras().getDouble("PickupLatitude");
        PickupLongtude = getIntent().getExtras().getDouble("PickupLongtude");
        DropLatitude = getIntent().getExtras().getDouble("DropLatitude");
        DropLongtude = getIntent().getExtras().getDouble("DropLongtude");
        cabtypeeNumer = getIntent().getExtras().getString("cabtypeeNumer");
        rideTypeName = getIntent().getExtras().getString("rideTypeName");


        if(rideTypeName.equalsIgnoreCase("oneway")){
            gatHoursList();
        }else if(rideTypeName.equalsIgnoreCase("twoway")){
            gatHoursListBox();
        }
        confirmTripBooking = (TextView)findViewById(R.id.request_driver) ;
       /* confirmTripBooking.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                ProgressDialog.show();
                cusRotateLoading.start();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String CurrentTime = formatter.format(date);

                pickup_lat = Double.toString(PickupLatitude);
                pickup_lng = Double.toString(PickupLongtude);
                String drop_lat = Double.toString(DropLatitude);
                String drop_lng = Double.toString(DropLongtude);

                Random random = new Random();
                String otp = String.format("%04d", random.nextInt(10000));
                ride_otp =String.valueOf(otp);
               // Toast.makeText(BookingDetailActivity.this, "resRandom "+otp, Toast.LENGTH_SHORT).show();

                Call<ArrivingDriver> call = myApi.postBooking(user_id,pickup_lat,pickup_lng,from,cabtypeeNumer,drop_lat,drop_lng,to,amount,distances,CurrentTime,ride_otp,paymentType,ride_type);
                call.enqueue(new Callback<ArrivingDriver>() {
                    @Override
                    public void onResponse(Call<ArrivingDriver> call, retrofit2.Response<ArrivingDriver> response) {
                        ArrivingDriver users = response.body();
                        if (users.status.equalsIgnoreCase("true")) {
                            ArrivingDriver.ArrivingDriverDetails userData = users.getDriverDetails();
                            ProgressDialog.cancel();
                            cusRotateLoading.stop();
                            issueNotification();
                            SharedPreferences.Editor editor = getSharedPreferences(DriverConstans.MY_Driver_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString(DriverConstans.USER_FROM_LOCATION, from);
                            editor.putString(DriverConstans.USER_TO_LOCATION,to);
                            editor.putString(DriverConstans.otp,ride_otp);
                            editor.putString(DriverConstans.ride_id,userData.getRide_id());
                            editor.putString(DriverConstans.tempDateTime, CurrentTime);
                            editor.apply();
                            startActivity(new Intent(BookingDetailActivity.this, TrackTruckActivity.class));

                        } else {
                            ProgressDialog.cancel();
                            cusRotateLoading.stop();
                            Toast.makeText(BookingDetailActivity.this, "false...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrivingDriver> call, Throwable t) {
                        ProgressDialog.cancel();
                        cusRotateLoading.stop();
                        Toast.makeText(BookingDetailActivity.this, "Check Your internet connection...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });*/


       /* txt_confirm_request = (TextView)findViewById(R.id.txt_confirm_request);
        txt_cancel_ride = (TextView)findViewById(R.id.txt_cancel_ride);
        txt_confirm_request.setTypeface(OpenSans_Bold);
        txt_cancel_ride.setTypeface(OpenSans_Bold);*/
        ProgressDialog = new Dialog(BookingDetailActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.custom_progress_dialog);
        ProgressDialog.setCancelable(false);
        cusRotateLoading = (RotateLoading)ProgressDialog.findViewById(R.id.rotateloading_register);


    }

    private void gatHoursListBox() {
        JsonObject gsonObject = new JsonObject();
        try {

            JSONObject jsonObj_ = new JSONObject();
            jsonObj_.put("type", "roundtrip");

            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
            Log.e("MY gson.JSON:  ", "AS PARAMETER  " + gsonObject);
            // Toast.makeText(this, "result"+gsonObject, Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Api api = RetrofitClient.getApiService();
        Call<RoundTripHoursList> call = api.showHoursBox(gsonObject);
       // TextView bottom_view_loading = (TextView)findViewById(R.id.bottom_view_loading);

        call.enqueue(new Callback<RoundTripHoursList>() {
            @Override
            public void onResponse(Call<RoundTripHoursList> call, retrofit2.Response<RoundTripHoursList> response) {
                if (response.isSuccessful()) {
                    roundTripHourEstDtls = response.body().getRoundTripHourEstDtls();
                    Hours_recyclerview = (RecyclerView) findViewById(R.id.estimate_recyclerview);
                    roundTripHourEstDtlsAdapter = new RoundTripHourEstDtlsAdapter(roundTripHourEstDtls);
                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BookingDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    //  GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
                    Hours_recyclerview.setLayoutManager(linearLayoutManager);
                    Hours_recyclerview.setItemAnimator(new DefaultItemAnimator());
                    Hours_recyclerview.setAdapter(roundTripHourEstDtlsAdapter);
                    hour = roundTripHourEstDtls.get(hourPosition).getName();
                    Hours_recyclerview.addOnItemTouchListener(new RecyclerTouchListener(BookingDetailActivity.this,
                            Hours_recyclerview, new ClickListener() {
                        @Override
                        public void onClick(View view, final int position) {
                            hour = roundTripHourEstDtls.get(position).getName();
                            //Toast.makeText(BookingDetailActivity.this, hour, Toast.LENGTH_SHORT).show();
                            popUpMethod(hour);
                        }
                        @Override
                        public void onLongClick(View view, int position) {
                        }
                    }));
                }
            }

            @Override
            public void onFailure(Call<RoundTripHoursList> call, Throwable t) {
                //  bottom_view_loading.setVisibility(View.VISIBLE);
            }
        });
    }

    private void gatHoursList() {
        JsonObject gsonObject = new JsonObject();
        try {

            JSONObject jsonObj_ = new JSONObject();
            jsonObj_.put("type", "oneway");

            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
            Log.e("MY gson.JSON:  ", "AS PARAMETER  " + gsonObject);
            // Toast.makeText(this, "result"+gsonObject, Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Api api = RetrofitClient.getApiService();
        Call<HoursList> call = api.showHours(gsonObject);
        TextView bottom_view_loading = (TextView)findViewById(R.id.bottom_view_loading);

        call.enqueue(new Callback<HoursList>() {
            @Override
            public void onResponse(Call<HoursList> call, retrofit2.Response<HoursList> response) {
                if (response.isSuccessful()) {
                    hourEstDtls = response.body().getHourEstDtls();
                    Hours_recyclerview = (RecyclerView) findViewById(R.id.estimate_recyclerview);
                    hourEstDtlsAdapter = new HourEstDtlsAdapter(hourEstDtls);
                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BookingDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    //  GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
                    Hours_recyclerview.setLayoutManager(linearLayoutManager);
                    Hours_recyclerview.setItemAnimator(new DefaultItemAnimator());
                    Hours_recyclerview.setAdapter(hourEstDtlsAdapter);
                    hour = hourEstDtls.get(hourPosition).getName();
                    Hours_recyclerview.addOnItemTouchListener(new RecyclerTouchListener(BookingDetailActivity.this,
                            Hours_recyclerview, new ClickListener() {
                        @Override
                        public void onClick(View view, final int position) {
                            hour = hourEstDtls.get(position).getName();
                            //Toast.makeText(BookingDetailActivity.this, hour, Toast.LENGTH_SHORT).show();
                            popUpMethod(hour);
                        }
                        @Override
                        public void onLongClick(View view, int position) {
                        }
                    }));
                }
            }

            @Override
            public void onFailure(Call<HoursList> call, Throwable t) {
              //  bottom_view_loading.setVisibility(View.VISIBLE);
            }
        });
    }

    private void popUpMethod(String hour) {
        popUP_dialog = new Dialog(BookingDetailActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        popUP_dialog.setContentView(R.layout.hour_dialog);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            popUP_dialog.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        popUP_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });
        Button done = (Button)popUP_dialog.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUP_dialog.cancel();
            }
        });

        TextView per_hour = (TextView)popUP_dialog.findViewById(R.id.per_hour);
        TextView one_way_charge = (TextView)popUP_dialog.findViewById(R.id.one_way_charge);
        TextView gst = (TextView)popUP_dialog.findViewById(R.id.gst);
        TextView ppe_charge = (TextView)popUP_dialog.findViewById(R.id.ppe_charge);
        TextView sub_total = (TextView)popUP_dialog.findViewById(R.id.sub_total);
        TextView rounding_down = (TextView)popUP_dialog.findViewById(R.id.rounding_down);
        TextView total = (TextView)popUP_dialog.findViewById(R.id.total);


        JsonObject gsonObject = new JsonObject();
        try {

            JSONObject jsonObj_ = new JSONObject();
            jsonObj_.put("fromLatitude", PickupLatitude);
            jsonObj_.put("fromLongitude", PickupLongtude);
            jsonObj_.put("toLatitude", DropLatitude);
            jsonObj_.put("toLongitude", DropLongtude);
            jsonObj_.put("type", "oneway");
            jsonObj_.put("carid", 3);
            jsonObj_.put("hours", hour);

            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
            Log.e("MY gson.JSON:  ", "AS PARAMETER  " + gsonObject);
            // Toast.makeText(this, "result"+gsonObject, Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Api api = RetrofitClient.getApiService();
        Call<HourPopUpDialog> call = api.estimatePopUp(gsonObject);
        call.enqueue(new Callback<HourPopUpDialog>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<HourPopUpDialog> call, retrofit2.Response<HourPopUpDialog> response) {

                HourPopUpDialog datas = response.body();
                if (datas.status.equalsIgnoreCase("true")) {
                    popUP_dialog.show();
                    HourPopUpDialog.HourPopDetails userDatas = datas.getHourPopDetails();
                    per_hour.setText(userDatas.getPerHour());
                    one_way_charge.setText(userDatas.getOnewayCharge());
                    gst.setText(userDatas.getGst());
                    ppe_charge.setText(userDatas.getPerHour());
                   // sub_total.setText(userDatas.);
                  //  rounding_down.setText(userDatas.);
                    total.setText(userDatas.getTotalCharge());

                }
            }

            @Override
            public void onFailure(Call<HourPopUpDialog> call, Throwable t) {
                Toast.makeText(BookingDetailActivity.this, "Check your internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void issueNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannel("CHANNEL_1", "Example channel", NotificationManager.IMPORTANCE_DEFAULT);
        }
        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(this, "CHANNEL_1");
        notification
                .setSmallIcon(R.mipmap.ic_launcher) // can use any other icon
                .setContentTitle("Booking Details!")
                .setContentText("Your booking is confirmed...")
                .setDefaults(Notification.DEFAULT_ALL) // must requires VIBRATE permission
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.empty_car_icon))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Dear customer your booking has recevied \n" + name + "\n" + mobile_no + "\n" + bikename))
                .setNumber(1); // this shows a number in the notification dots

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(1, notification.build());
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    void makeNotificationChannel(String id, String name, int importance)
    {
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setShowBadge(true); // set false to disable badges, Oreo exclusive

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }


    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("selected", false);
        setResult(102, resultIntent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        txt_car_name = null;
        img_car_image = null;
        layout_back_arrow = null;
    }


   //Recycler view clicking functions
   public static interface ClickListener{
       public void onClick(View view,int position);
       public void onLongClick(View view,int position);
   }


    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        private ClickListener clicklistener;
        private GestureDetector gestureDetector;
        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){
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
