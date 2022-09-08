package com.affinity.sk_facilities.activity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.affinity.sk_facilities.Api;
import com.affinity.sk_facilities.Constants;
import com.affinity.sk_facilities.R;
import com.affinity.sk_facilities.adapter.DriversAdapter;
import com.affinity.sk_facilities.adapter.PickupDropLocationAdapter;
import com.affinity.sk_facilities.beens.CarType;
import com.affinity.sk_facilities.beens.Drivers;
import com.affinity.sk_facilities.gpsLocation.GPSTracker;
import com.affinity.sk_facilities.gpsLocation.LocationAddress;
import com.affinity.sk_facilities.map.DirectionObject;
import com.affinity.sk_facilities.map.GsonRequest;
import com.affinity.sk_facilities.map.Helper;
import com.affinity.sk_facilities.map.LegsObject;
import com.affinity.sk_facilities.map.PolylineObject;
import com.affinity.sk_facilities.map.RouteObject;
import com.affinity.sk_facilities.map.StepsObject;
import com.affinity.sk_facilities.map.VolleySingleton;
import com.affinity.sk_facilities.utils.Common;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.victor.loading.rotate.RotateLoading;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;
import static android.os.Build.VERSION_CODES.M;
import static com.google.android.gms.maps.model.JointType.ROUND;

public class PlacesSearchActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener,
        GoogleMap.OnMapClickListener, PickupDropLocationAdapter.OnDraoppickupClickListener,
        GoogleMap.OnMyLocationClickListener, Animation.AnimationListener {

    TextView txt_ride, txt_now, txt_not_found, greetingName, exit_cancel, text_save, fav_location;
    EditText edt_pickup_location, DistanceTextView, edt_drop_location, other_favourite;
    boolean animationStart = true;
    String bothLocationString = "";
    boolean MapLayoutClick = false;
    Animation slideUpAnimation, slideDownAnimation;
    LinearLayout header, footer, layout_no_result, yourLocation;
    int devise_height;
    boolean FooterMapLayoutClick = false;
    ImageView img_pickup_close, img_drop_close, txt_home, fav_based_icon, drag_pinImage;
   // SparkButton img_fav;
    RadioButton home, work, other, selectedRadioButton;
    RecyclerView recycle_pickup_location;
    RelativeLayout layout_pickup_drag_location, layout_now, layout_back_arrow, rl_drag_map;
    CircleImageView profileImage;
    SharedPreferences userPref;
    Typeface OpenSans_Regular, OpenSans_Bold, Roboto_Regular, Roboto_Medium, Roboto_Bold;
    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
    private GPSTracker gpsTracker;
    private GoogleMap googleMap;
    MarkerOptions marker;
    LatLng PickupLarLng;
    LatLng DropLarLng;
    double PickupLongtude, PickupLatitude, DropLatitude, DropLongtude;
    String PickupLon, PickupLat, cabtypeName;
    String clickEditText;
    ArrayList<HashMap<String, String>> locationArray;
    TextView homeLocation, workLocation, otherLocation;
    RelativeLayout layout_one, layout_two, layout_three;
    LinearLayout close_icon, fav_layout, li_recenter;
    RelativeLayout layout_car_details, bttm_cab_dtls;
    String DayNight, address;
    String SelectedDate = "";
    int bikeType = 1;
    Api myApi;
    FloatingActionButton recenter_floatbtn;
    FloatingActionButton go_with_picklocation;
    FloatingActionButton go_with_droplocation;
    String Fav_loca_name, Home, Work, Other, selectedRadioButtonText;
    PickupDropLocationAdapter pickupDropLocationAdapter;
    LinearLayoutManager pickupDragLayoutManager;
    boolean ClickOkButton = false;
    Calendar myCalendar;
    Calendar TimeCalendar;
    TextView txt_date;
    DatePickerDialog.OnDateSetListener date;
    String BookingDateTime = "";
    SimpleDateFormat bookingFormate;
    Common common = new Common();
    Marker PickupMarker, neabyDriver;
    Marker DropMarker;
    ArrayList<HashMap<String, String>> FixRateArray;
    Dialog ProgressDialog, CarTypeDialog;
    RotateLoading cusRotateLoading;
    String LanguageCode;
    boolean dragMarker = false;
    private GoogleMap mMap;
    MarkerOptions nearBymarker;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    private MapStyleOptions mapStyle;
    private List<LatLng> latLngList;
    double x;
    double y;
    Geocoder geocoder;
    List<Address> addresses;
    String name, number, user_id, bikeTypeid;
    final String TAG = "GPS";
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    private PlacesSearchActivity ctx = this;
    LocationManager locationManager;
    Location loc;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    boolean isGPS = false;
    private LocationManager locManager;
    boolean isNetwork = false;
    boolean canGetLocation = true;
    PolylineOptions lineOptions, graylineOptions;
    Polyline polyline, greyPolyLine;
    List<Marker> driverMarkers = new ArrayList<>();
    private ArrayList<Drivers.DriverDetails> employeeList;
    private DriversAdapter eAdapter;
    static boolean running = false;
    String Pic_LocationName, Drop_LocationName, locationSwitch;
    TextView drag_pick_location_enable, drag_drop_location_enable;
    RelativeLayout pick_location_layout, drop_location_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_search);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (!pref.getBoolean("firstTime", false)) {
            finish();
            startActivity(getIntent());
            // first time has ran.
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }

        SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        // user_id = prefs.getString(Constants.user_id, "");
        locationSwitch = prefs.getString(Constants.pickupupLocation, "");

        pick_location_layout = (RelativeLayout) findViewById(R.id.pick_location_layout);
        drop_location_layout = (RelativeLayout) findViewById(R.id.drop_location_layout);
        go_with_picklocation = (FloatingActionButton) findViewById(R.id.go_with_picklocation);
        go_with_droplocation = (FloatingActionButton) findViewById(R.id.go_with_droplocation);
        drag_pick_location_enable = (TextView) findViewById(R.id.drag_pick_location_enable);
        drag_drop_location_enable = (TextView) findViewById(R.id.drag_drop_location_enable);

        if (locationSwitch.equals("pickupupLocation")) {
            pick_location_layout.setVisibility(View.VISIBLE);
            drop_location_layout.setVisibility(View.GONE);
            go_with_picklocation.setVisibility(View.VISIBLE);
            go_with_droplocation.setVisibility(View.GONE);
            drag_pick_location_enable.setVisibility(View.VISIBLE);
            drag_drop_location_enable.setVisibility(View.GONE);

        } else if (locationSwitch.equals("DropUpLocation")) {
            drag_drop_location_enable.setVisibility(View.VISIBLE);
            drag_pick_location_enable.setVisibility(View.GONE);
            pick_location_layout.setVisibility(View.GONE);
            drop_location_layout.setVisibility(View.VISIBLE);
            go_with_picklocation.setVisibility(View.GONE);
            go_with_droplocation.setVisibility(View.VISIBLE);
        }

        reCenter();
        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (!isGPS && !isNetwork) {
            Log.d(TAG, "Connection off");
            Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(viewIntent);
        } else {
            Log.d(TAG, "Connection on");
            getLocation();
        }
        latLngList = new ArrayList<LatLng>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.places_map);
        mapFragment.getMapAsync(this);


        go_with_picklocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pic_LocationName = edt_pickup_location.getText().toString();
                if (Pic_LocationName.isEmpty()) {
                    Common.showMkError(PlacesSearchActivity.this, getResources().getString(R.string.enter_pickup));
                } else {
                    SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString(Constants.pickup_location, Pic_LocationName);
                    editor.putString(Constants.pickup_locationlayout, "true");
                    editor.apply();
                    Intent value = new Intent(PlacesSearchActivity.this, HomeActivity.class);
                    startActivity(value);
                }
            }
        });

        go_with_droplocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drop_LocationName = edt_drop_location.getText().toString();
                if (Drop_LocationName.isEmpty()) {
                    Common.showMkError(PlacesSearchActivity.this, getResources().getString(R.string.enter_drop));
                } else {

                  /*  if (locationGPS != null) {
                        double lat = locationGPS.getLatitude();
                        double longi = locationGPS.getLongitude();*/

                       /* String latitude = String.valueOf( loc.getLatitude());
                        String longitude = String.valueOf( loc.getLongitude());*/
                        SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
                      /*  editor.putString(Constants.current_loc_lat, latitude);
                        editor.putString(Constants.current_loc_lng, longitude);*/
                        editor.putString(Constants.drop_location, Drop_LocationName);
                        editor.putString(Constants.pickup_latitude, String.valueOf(PickupLatitude));
                        editor.putString(Constants.pickup_longitude, String.valueOf(PickupLongtude));
                        editor.putString(Constants.drop_latitude, String.valueOf(DropLatitude));
                        editor.putString(Constants.drop_longitude, String.valueOf(DropLongtude));
                        editor.putString(Constants.drop_locationlayout, "true");
                        editor.putString(Constants.pickup_locationlayout, "false");
                        editor.apply();
                        Intent value = new Intent(PlacesSearchActivity.this,HomeActivity.class);
                        startActivity(value);
                      //  showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
                    /*} else {
                        Toast.makeText(PlacesSearchActivity.this, "Unable to find location.", Toast.LENGTH_SHORT).show();
                    }*/

                }
            }
        });

        recenter_floatbtn = (FloatingActionButton) findViewById(R.id.reCenterScreen);
        recenter_floatbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reCenter();
            }
        });

        LanguageCode = Locale.getDefault().getLanguage();

        Log.d("LanguageCode", "LanguageCode = " + LanguageCode);

        userPref = PreferenceManager.getDefaultSharedPreferences(PlacesSearchActivity.this);
        edt_pickup_location = (EditText) findViewById(R.id.edit_pickup_location);
        //other_favourite = (EditText)findViewById(R.id.other_favourite);
        edt_drop_location = (EditText) findViewById(R.id.edit_drop_location);
        img_pickup_close = (ImageView) findViewById(R.id.img_pick_close);
        //  img_fav = (SparkButton) findViewById(R.id.img_fav);
        // radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
       // home = (RadioButton) findViewById(R.id.home);
        // work = (RadioButton)findViewById(R.id.work);
        /*other = (RadioButton)findViewById(R.id.other);*/
        img_drop_close = (ImageView) findViewById(R.id.img_drop_close);
        drag_pinImage = (ImageView) findViewById(R.id.drag_pinImage);
        recycle_pickup_location = (RecyclerView) findViewById(R.id.recycle_search_location);
        layout_pickup_drag_location = (RelativeLayout) findViewById(R.id.rl_layout_pickup_drag_location);
        layout_back_arrow = (RelativeLayout) findViewById(R.id.layout_back_arrow);
        rl_drag_map = (RelativeLayout) findViewById(R.id.rl_drag_map);
        layout_no_result = (LinearLayout) findViewById(R.id.li_layout_no_result);
        li_recenter = (LinearLayout) findViewById(R.id.li_recenter);
        txt_not_found = (TextView) findViewById(R.id.txt_not_founds);
        /*txt_now = (TextView) findViewById(R.id.txt_now);
        txt_ride = (TextView) findViewById(R.id.txt_ride);
        homeLocation = (TextView) findViewById(R.id.homeLocation);
        workLocation = (TextView) findViewById(R.id.workLocation);
        otherLocation = (TextView) findViewById(R.id.otherLocation);*/

        ProgressDialog = new Dialog(PlacesSearchActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.custom_progress_dialog);
        ProgressDialog.setCancelable(false);
        cusRotateLoading = (RotateLoading) ProgressDialog.findViewById(R.id.rotateloading_register);

        if (Common.device_token != null && !Common.device_token.equals(""))
            new Common.CallUnSubscribeTaken(PlacesSearchActivity.this, Common.device_token).execute();
        else if (Common.device_refresh_token != null && !Common.device_refresh_token.equals(""))
            new Common.CallUnSubscribeTaken(PlacesSearchActivity.this, Common.device_refresh_token).execute();

        bookingFormate = new SimpleDateFormat("h:mm a, d, MMM yyyy,EEE");

        OpenSans_Bold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold_0.ttf");
        Roboto_Regular = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Roboto_Medium = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        Roboto_Bold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");


        txt_not_found.setTypeface(OpenSans_Bold);

        edt_pickup_location.setTypeface(OpenSans_Regular);
        edt_drop_location.setTypeface(OpenSans_Regular);

        pickupDragLayoutManager = new LinearLayoutManager(PlacesSearchActivity.this);
        recycle_pickup_location.setLayoutManager(pickupDragLayoutManager);

        gpsTracker = new GPSTracker(PlacesSearchActivity.this);

        if (PickupLongtude != 0.0 && PickupLatitude != 0.0) {
            bothLocationString = "pickeup";
            if (Common.isNetworkAvailable(PlacesSearchActivity.this)) {
                LocationAddress locationAddress = new LocationAddress();
                locationAddress.getAddressFromLocation(PickupLatitude, PickupLongtude,
                        getApplicationContext(), new GeocoderHandler());

                PickupLarLng = new LatLng(PickupLatitude, PickupLongtude);
                ClickOkButton = true;

               /* new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MarkerAdd();
                    }
                }, 1000);*/
            } else {
                Toast.makeText(PlacesSearchActivity.this, "No Network", Toast.LENGTH_LONG).show();
            }
        } else {

            if (gpsTracker.checkLocationPermission()) {

                PickupLatitude = gpsTracker.getLatitude();
                PickupLongtude = gpsTracker.getLongitude();
                PickupLarLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                ClickOkButton = true;

                bothLocationString = "pickeup";
                if (Common.isNetworkAvailable(PlacesSearchActivity.this)) {
                    LocationAddress locationAddress = new LocationAddress();
                    locationAddress.getAddressFromLocation(PickupLatitude, PickupLongtude,
                            getApplicationContext(), new GeocoderHandler());

                    PickupLarLng = new LatLng(PickupLatitude, PickupLongtude);
                    ClickOkButton = true;

                    /*new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MarkerAdd();
                        }
                    }, 1000);*/
                } else {
                    Toast.makeText(PlacesSearchActivity.this, "No Network", Toast.LENGTH_LONG).show();
                }

            } else {
                //  gpsTracker.showSettingsAlert();
            }
        }

        Log.d("gpsTracker", "gpsTracker =" + gpsTracker.canGetLocation() + "==" + gpsTracker.checkLocationPermission());

        EditorActionListener(edt_pickup_location, "pickeup");
        AddTextChangeListener(edt_pickup_location, "pickeup");
        AddSetOnClickListener(edt_pickup_location, "pickeup","pickeup");

        EditorActionListener(edt_drop_location, "drop");
        AddTextChangeListener(edt_drop_location, "drop");
        AddSetOnClickListener(edt_drop_location, "drop","drop");


        try {
            String currentLocalTime = currentTime.format(new Date());
            Date StarDateFrm = null;
            if (!Common.StartDayTime.equals(""))
                StarDateFrm = currentTime.parse(Common.StartDayTime);
            Date EndDateFrm = null;
            if (!Common.StartDayTime.equals(""))
                EndDateFrm = currentTime.parse(Common.EndDayTime);

            Date CurDateFrm = currentTime.parse(currentLocalTime);

            if (StarDateFrm != null && EndDateFrm != null) {
                if (CurDateFrm.before(StarDateFrm) || CurDateFrm.after(EndDateFrm)) {
                    Log.d("get time", "get time = before");
                    DayNight = "night";
                } else {
                    DayNight = "day";
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        myCalendar = Calendar.getInstance();
        TimeCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SelectedDate = dayOfMonth + "/" + monthOfYear + "/" + year;
                updateLabel();
            }
        };

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        img_pickup_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.getUiSettings().setScrollGesturesEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setAllGesturesEnabled(true);
                edt_pickup_location.setClickable(true);
                edt_pickup_location.setFocusable(true);
                edt_pickup_location.setText("");
                PickupLarLng = null;
                PickupLatitude = 0.0;
                PickupLongtude = 0.0;
                edt_pickup_location.setFocusableInTouchMode(true);
                edt_pickup_location.requestFocus();
                MarkerAdd();
            }
        });
        img_drop_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* floatingActionButton.setEnabled(false);*/
                edt_drop_location.setText("");
                drag_pinImage.setVisibility(View.GONE);
                mMap.getUiSettings().setScrollGesturesEnabled(false);
                mMap.getUiSettings().setZoomControlsEnabled(false);
                mMap.getUiSettings().setAllGesturesEnabled(false);
                edt_drop_location.setClickable(true);
                edt_drop_location.setFocusable(true);
                DropLarLng = null;
                DropLongtude = 0.0;
                DropLatitude = 0.0;
                MarkerAdd();
            }
        });

        layout_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlacesSearchActivity.this, HomeActivity.class));
                finish();
            }
        });

        drag_pick_location_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_pickup_location.setEnabled(true);
                edt_pickup_location.setClickable(true);
                edt_pickup_location.setFocusableInTouchMode(true);
                rl_drag_map.setVisibility(View.VISIBLE);
                drag_pinImage.setVisibility(View.VISIBLE);
                li_recenter.setVisibility(View.VISIBLE);
                drag_pick_location_enable.setVisibility(View.GONE);
                drag_drop_location_enable.setVisibility(View.GONE);
            }
        });

        drag_drop_location_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_drop_location.setEnabled(true);
                edt_drop_location.setClickable(true);
                edt_drop_location.setFocusableInTouchMode(true);
                rl_drag_map.setVisibility(View.VISIBLE);
                drag_pinImage.setVisibility(View.VISIBLE);
                li_recenter.setVisibility(View.VISIBLE);
                drag_pick_location_enable.setVisibility(View.GONE);
                drag_drop_location_enable.setVisibility(View.GONE);
            }
        });
    }


    private void updateUI(Location loc) {
        Log.d(TAG, "updateUI");
        x = loc.getLatitude();
        y = loc.getLongitude();
        // tvTime.setText(DateFormat.getTimeInstance().format(loc.getTime()));
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
        updateUI(location);


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
        getLocation();
    }

    @Override
    public void onProviderDisabled(String s) {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    private void getLocation() {
        gpsTracker = new GPSTracker(PlacesSearchActivity.this);
        if(gpsTracker.canGetLocation()) {
            x = gpsTracker.getLatitude();
            y = gpsTracker.getLongitude();
        }
        try {
            if (canGetLocation) {
                Log.d(TAG, "Can get location");
                if (isGPS) {
                    // from GPS
                    Log.d(TAG, "GPS on");
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null)
                            updateUI(loc);
                    }
                } else if (isNetwork) {
                    // from Network Provider
                    Log.d(TAG, "NETWORK_PROVIDER on");
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (loc != null)
                            updateUI(loc);
                    }
                } else {
                    loc.setLatitude(0);
                    loc.setLongitude(0);
                    updateUI(loc);
                }
            } else {
                Log.d(TAG, "Can't get location");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= JELLY_BEAN) {
                if (Build.VERSION.SDK_INT >= M) {
                    return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
                }
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


/*
    private void location() {
        geocoder = new Geocoder(PlacesSearchActivity.this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(x, y, 1);
            address = addresses.get(0).getAddressLine(0);
            edt_pickup_location.setText(address);// Here 1 represent max location result to returned, by documents it recommended 1 to 5
            *//* Toast.makeText(this, "location "+addresses, Toast.LENGTH_SHORT).show();*//*
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        //Toast.makeText(PlacesSearchActivity.this,"Date = "+sdf.format(myCalendar.getTime()),Toast.LENGTH_LONG).show();
       // txt_date.setText(sdf.format(myCalendar.getTime()));

    }


    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }
    private void dragMapLocation() {
        mMap.setOnCameraIdleListener(onCameraIdleListener);
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LocationAddress locationAddress = new LocationAddress();
                LatLng latLng = mMap.getCameraPosition().target;
                Geocoder geocoder = new Geocoder(PlacesSearchActivity.this);

               /* locationAddress.getAddressFromLocation(latLng.latitude, latLng.longitude,
                        getApplicationContext(), new GeocoderHandler());*/
                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        String locality = addressList.get(0).getAddressLine(0);
                        //String country = addressList.get(0).getCountryName();

                        if (!locality.isEmpty()){
                            if (clickEditText.equals("pickeup")) {
                                edt_pickup_location.setText(locality);
                            }if(clickEditText.equals("drop")){
                                edt_drop_location.setText(locality);
                            }

                        }
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        mMap = googleMap;
        mapStyle = MapStyleOptions.loadRawResourceStyle(this,R.raw.style_json_silver);
        mMap.setMapStyle(mapStyle);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15.0f));
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        dp10 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());


        googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener(){
            @Override
            public void onCameraMoveStarted(int i) {
                MapLayoutClick = true;

            }
        });
    }

    @Override
    protected void onStart() {

        reCenter();
        super.onStart();
        if (!isGPS && !isNetwork) {
            Log.d(TAG, "Connection off");
            Toast.makeText(ctx, "Please enable your GPS", Toast.LENGTH_LONG).show();
            Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(viewIntent);
        } else {
            Log.d(TAG, "Connection on");
            reCenter();
            getLocation();
            // MarkerAdd();

        }if (checkPermissions(false)) {
            if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(ctx, "Please enable your GPS", Toast.LENGTH_LONG).show();
                Intent viewIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(viewIntent);
                reCenter();
            }
        }
        edt_pickup_location.setEnabled(true);
        edt_pickup_location.setFocusable(true);
        edt_pickup_location.setFocusableInTouchMode(true);
        running = true;

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

  /*  public class DropDownAnim extends Animation {
        private final int targetHeight;
        private final LinearLayout linearLayout;
        private final boolean down;

        public DropDownAnim(LinearLayout rel, int targetHeight, boolean down) {
            this.linearLayout = rel;
            this.targetHeight = targetHeight;
            this.down = down;

        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int newHeight;
            if (down) {

                Log.d("newHeight","newHeight one= "+interpolatedTime);
                if(interpolatedTime != 0.0 && interpolatedTime >= 0.3) {
                    newHeight = (int) (targetHeight * interpolatedTime);
                    Log.d("newHeight","newHeight two= "+interpolatedTime+"=="+newHeight);
                }else{
                    newHeight = (int) getResources().getDimension(R.dimen.height_100);
                }
            } else {
                if(interpolatedTime <= 0.3) {
                    newHeight = (int) (targetHeight * (1 - interpolatedTime));
                }else{
                    newHeight = (int) getResources().getDimension(R.dimen.height_100);
                }
            }
            Log.d("newHeight","newHeight three= "+newHeight+"=="+interpolatedTime);
            linearLayout.getLayoutParams().height = newHeight;
            linearLayout.requestLayout();
            if(newHeight > targetHeight * 0.60)
                header.setVisibility(View.VISIBLE);
            else
                header.setVisibility(View.GONE);
            if(newHeight > targetHeight * 0.80) {
                footer.setVisibility(View.VISIBLE);
                FooterMapLayoutClick = true;
            }
            else {
                footer.setVisibility(View.GONE);
                FooterMapLayoutClick = false;
            }
        }

        @Override
        public void initialize(int width, int height, int parentWidth,
                               int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }*/
    /*@Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("MapLayoutClick","MapLayoutClick Down= "+MapLayoutClick+"=="+FooterMapLayoutClick);
                        if(FooterMapLayoutClick){

                            FooterMapLayoutClick = false;
                            DropDownAnim anim = new DropDownAnim(footer, (int) (devise_height * 0.55), animationStart);
                            anim.setDuration(200);
                            footer.startAnimation(anim);
                            animationStart = true;
                            header.startAnimation(anim);

                        }
                    }
                }, 20);

                break;
            case MotionEvent.ACTION_UP:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Log.d("MapLayoutClick","MapLayoutClick Up = "+MapLayoutClick);
                        if(MapLayoutClick) {
                            slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                                    R.anim.slide_up_map);
                           *//* footer.startAnimation(slideUpAnimation);
                            header.startAnimation(slideUpAnimation);*//*
                            MapLayoutClick = false;
                        }
                    }
                }, 50);
                break;
        }
        return super.dispatchTouchEvent(event);
    }*/

    private boolean checkReady() {
        if (googleMap == null) {
            //Toast.makeText(this, "Google Map not ready", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void CaculationDirationIon(){
        String CaculationLocUrl = "";


        CaculationLocUrl = "http://maps.googleapis.com/maps/api/directions/json?sensor=true&mode=driving&origin="+PickupLatitude+","+PickupLongtude+"&destination="+DropLatitude+","+DropLongtude;
        Log.d("CaculationLocUrl","CaculationLocUrl = "+CaculationLocUrl);
        Ion.with(PlacesSearchActivity.this)
                .load(CaculationLocUrl)
                .setTimeout(60*60*1000)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception error, JsonObject result) {
                        // do stuff with the result or error

                        ProgressDialog.cancel();
                        cusRotateLoading.stop();

                        Log.d("Login result", "Login result = " + result + "==" + error);
                        if (error == null) {

                        } else {
                            Common.ShowHttpErrorMessage(PlacesSearchActivity.this, error.getMessage());
                        }
                    }
                });

        if(!dragMarker) {
            dragMarker = false;
            MarkerAdd();
        }

    }

    public void GetLatLongFromAddress(String address,String StrValText){

        String GetLatLonLocUrl = null;
        try {
            GetLatLonLocUrl = "http://maps.googleapis.com/maps/api/geocode/json?address=AIzaSyAP3jFJ4MzBZHk4GZEnRkN8fxVOw-HHr70="+ URLEncoder.encode(address, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d("CaculationLocUrl","CaculationLocUrl = "+GetLatLonLocUrl);
        Ion.with(PlacesSearchActivity.this)
                .load(GetLatLonLocUrl)
                .setTimeout(60*60*1000)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception error, JsonObject result) {
                        // do stuff with the result or error

                        ProgressDialog.cancel();
                        cusRotateLoading.stop();

                        Log.d("Login result", "Login result = " + result + "==" + error);
                        if (error == null) {
                            try {
                                JSONObject resObj = new JSONObject(result.toString());
                                if (resObj.getString("status").toLowerCase().equals("ok")) {
                                    JSONArray routArray = new JSONArray(resObj.getString("results"));
                                    JSONObject routObj = routArray.getJSONObject(0);
                                    JSONObject geometryObj = new JSONObject(routObj.getString("geometry"));
                                    JSONObject locationObj = new JSONObject(geometryObj.getString("location"));

                                    Log.d("routArray","routArray "+locationObj);

                                    if (bothLocationString.equals("pickeup")) {
                                        PickupLatitude = Double.parseDouble(locationObj.getString("lat"));
                                        PickupLongtude = Double.parseDouble(locationObj.getString("lng"));
                                        PickupLarLng = new LatLng(Double.parseDouble(locationObj.getString("lat")), Double.parseDouble(locationObj.getString("lng")));
                                    }else if (bothLocationString.equals("drop")) {
                                        DropLongtude = Double.parseDouble(locationObj.getString("lng"));
                                        DropLatitude = Double.parseDouble(locationObj.getString("lat"));
                                        DropLarLng = new LatLng(Double.parseDouble(locationObj.getString("lat")), Double.parseDouble(locationObj.getString("lng")));
                                    }

                                    if (edt_drop_location.getText().length() > 0 && edt_pickup_location.getText().length() > 0) {
                                        if (checkReady() && Common.isNetworkAvailable(PlacesSearchActivity.this)) {
                                            PickupFixRateCall();
                                        } else {
                                            Common.showInternetInfo(PlacesSearchActivity.this, "");
                                        }
                                    }else{
                                        MarkerAdd();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Common.ShowHttpErrorMessage(PlacesSearchActivity.this, error.getMessage());
                        }
                    }
                });


    }
    public void PickupFixRateCall(){

        FixRateArray = new ArrayList<>();
        CaculationDirationIon();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(latLngList.size() > 1){
            refreshMap(mMap);
            latLngList.clear();
            PickupLarLng = null;
            DropLarLng = null;
        }
        latLngList.add(latLng);
        Log.d(TAG, "Marker number " + latLngList.size());
        createMarker(latLng, latLngList.size());
        if(latLngList.size() == 2){
            LatLng origin = latLngList.get(0);
            LatLng destination = latLngList.get(1);
            //use Google Direction API to get the route between these Locations
            String directionApiPath = Helper.getUrl(String.valueOf(origin.latitude), String.valueOf(origin.longitude),
                    String.valueOf(destination.latitude), String.valueOf(destination.longitude));
            Log.d(TAG, "Path " + directionApiPath);
       //     getDirectionFromDirectionApiServer(directionApiPath);
        }
    }
    private void refreshMap(GoogleMap mapInstance){
        mapInstance.clear();
    }
    private void createMarker(LatLng latLng, int position){
        MarkerOptions mOptions = new MarkerOptions().position(PickupLarLng);
        addCameraToMap(latLng);
        mMap.addMarker(mOptions);
    }
    private void addCameraToMap(LatLng latLng){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(11)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
  /*  private void getDirectionFromDirectionApiServer(String url){

        GsonRequest<DirectionObject> serverRequest = new GsonRequest<DirectionObject>(
                Request.Method.GET,
                url,
                DirectionObject.class,
                createRequestSuccessListener(),
                createRequestErrorListener());
        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(serverRequest);
    }
    private com.android.volley.Response.Listener<DirectionObject> createRequestSuccessListener() {
        return new com.android.volley.Response.Listener<DirectionObject>() {
            @Override
            public void onResponse(DirectionObject response) {
               *//* try {
                    Log.d("JSON Response", response.toString());*//*
                if(response.getStatus().equalsIgnoreCase("OK")){
                    List<LatLng> mDirections = getDirectionPolylines(response.getRoutes());
                    drawRouteOnMap(mMap, mDirections);
                }
                else{
                    //Toast.makeText(PlacesSearchActivity.this, "server error", Toast.LENGTH_SHORT).show();
                }

            };
        };
    }*/

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    private void setRouteDistanceAndDuration(String distance, String duration){
      //  DistanceTextView.setText(distance);
    }
   /* private List<LatLng> getDirectionPolylines(List<RouteObject> routes){
        List<LatLng> directionList = new ArrayList<LatLng>();
        for(RouteObject route : routes){
            List<LegsObject> legs = route.getLegs();
            for(LegsObject leg : legs){
                String routeDistance = leg.getDistance().getText();
                String routeDuration = leg.getDuration().getText();
                setRouteDistanceAndDuration(routeDistance, routeDuration);
                List<StepsObject> steps = leg.getSteps();
                for(StepsObject step : steps){
                    PolylineObject polyline = step.getPolyline();
                    String points = polyline.getPoints();
                    List<LatLng> singlePolyline = decodePoly(points);
                    for (LatLng direction : singlePolyline){
                        directionList.add(direction);
                    }
                }
            }
        }
        return directionList;
    }*/
    private com.android.volley.Response.ErrorListener createRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };
    }
    private void drawRouteOnMap(GoogleMap map, List<LatLng> positions){
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(false);
        graylineOptions = new PolylineOptions();
        graylineOptions.color(Color.GRAY);
        graylineOptions.width(5);
        graylineOptions.startCap(new SquareCap());
        graylineOptions.endCap(new SquareCap());
        graylineOptions.jointType(ROUND);
        graylineOptions.addAll(positions);
        greyPolyLine = mMap.addPolyline(graylineOptions);

        lineOptions = new PolylineOptions();
        lineOptions.width(5);
        lineOptions.color(Color.BLACK).geodesic(true);
        lineOptions.startCap(new SquareCap());
        lineOptions.endCap(new SquareCap());
        lineOptions.jointType(ROUND);
        polyline = map.addPolyline(lineOptions);
        ValueAnimator polylineAnimator = ValueAnimator.ofInt(0, 100);
        polylineAnimator.setDuration(2000);
        polylineAnimator.setInterpolator(new LinearInterpolator());
        polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                List<LatLng> points = greyPolyLine.getPoints();
                int percentValue = (int) valueAnimator.getAnimatedValue();
                int size = points.size();
                int newPoints = (int) (size * (percentValue / 100.0f));
                List<LatLng> p = points.subList(0, newPoints);
                polyline.setPoints(p);
            }

        });
        polylineAnimator.start();
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;

            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            if(locationAddress != null) {
                message.what = 1;
                if (locationAddress.equals("Unable connect to Geocoder")) {
                    Toast.makeText(PlacesSearchActivity.this, "No Network conection", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("locationAddress", "locationAddress = " + locationAddress + "==" + bothLocationString+"=="+dragMarker);
                    if (bothLocationString.equals("pickeup") && edt_pickup_location != null)
                        edt_pickup_location.setText(locationAddress);
                    else if (bothLocationString.equals("drop") && edt_drop_location != null)
                        edt_drop_location.setText(locationAddress);
                    LocationAddress.getAddressFromLocation(locationAddress, getApplicationContext(), new GeocoderHandlerLatitude());
                }

            }else{
                //   layout_reservation.setVisibility(View.VISIBLE);
                // Toast.makeText(PlacesSearchActivity.this, getResources().getString(R.string.location_long), Toast.LENGTH_LONG).show();

            }edt_pickup_location.setText(locationAddress);
        }
    }

    private class GeocoderHandlerLatitude extends Handler{
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            //  createMarker(latLng, latLngList.size());
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            Log.d("locationAddress","locationAddress = "+locationAddress);
            if(locationAddress != null) {
                if (locationAddress.equals("Unable connect to Geocoder")) {
                    Toast.makeText(PlacesSearchActivity.this, "No Network conection", Toast.LENGTH_LONG).show();
                } else {
                    String[] LocationSplit = locationAddress.split("\\,");
                    Log.d("locationAddress", "locationAddress = " + locationAddress + "==" + Double.parseDouble(LocationSplit[0]) + "==" + Double.parseDouble(LocationSplit[1]));
                    if (bothLocationString.equals("pickeup")) {
                        PickupLatitude = Double.parseDouble(LocationSplit[0]);
                        PickupLongtude = Double.parseDouble(LocationSplit[1]);
                        PickupLarLng = new LatLng(Double.parseDouble(LocationSplit[0]), Double.parseDouble(LocationSplit[1]));
                    }else if (bothLocationString.equals("drop")) {
                        DropLongtude = Double.parseDouble(LocationSplit[1]);
                        DropLatitude = Double.parseDouble(LocationSplit[0]);
                        DropLarLng = new LatLng(Double.parseDouble(LocationSplit[0]), Double.parseDouble(LocationSplit[1]));
                    }

                    if (edt_drop_location != null && edt_drop_location.getText().length() > 0 && edt_pickup_location.getText().length() > 0) {
                        if (checkReady() && Common.isNetworkAvailable(PlacesSearchActivity.this)) {
                            PickupFixRateCall();
                        } else {
                            Common.showInternetInfo(PlacesSearchActivity.this, "");
                        }
                    }else{

                        if(latLngList.size() == 2){
                            LatLng origin = latLngList.get(0);
                            LatLng destination = latLngList.get(1);
                            //use Google Direction API to get the route between these Locations
                            String directionApiPath = Helper.getUrl(String.valueOf(origin.latitude), String.valueOf(origin.longitude),
                                    String.valueOf(destination.latitude), String.valueOf(destination.longitude));
                            Log.d(TAG, "Path " + directionApiPath);
                          //  getDirectionFromDirectionApiServer(directionApiPath);
                        }
                    }
                }
            }else{

                if (Common.isNetworkAvailable(PlacesSearchActivity.this)) {
                    Log.d("locationAddress","No Address Found = ");
                    ProgressDialog.show();
                    cusRotateLoading.start();
                    if(bothLocationString.equals("pickeup")) {
                        GetLatLongFromAddress(edt_pickup_location.getText().toString(),bothLocationString);
                    }else if (bothLocationString.equals("drop")) {
                        GetLatLongFromAddress(edt_drop_location.getText().toString(),bothLocationString);
                    }
                }else {
                    Common.showInternetInfo(PlacesSearchActivity.this, "No internet");
                }
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        reCenter();
    }

    /*Add marker function*/
    public void MarkerAdd(){
        String directionApiPath = Helper.getUrl(String.valueOf(PickupLatitude), String.valueOf(PickupLongtude),
                String.valueOf(DropLatitude), String.valueOf(DropLongtude));
     //   getDirectionFromDirectionApiServer(directionApiPath);

        if(checkReady()) {

            if(marker != null)
                googleMap.clear();

            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            if(PickupLarLng != null) {
                marker = new MarkerOptions()
                        .position(PickupLarLng)
                        .title("Pickup Location")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pin_icon));
                drag_pinImage.setVisibility(View.GONE);
                mMap.getUiSettings().setScrollGesturesEnabled(false);
                mMap.getUiSettings().setZoomControlsEnabled(false);
                mMap.getUiSettings().setAllGesturesEnabled(false);
                PickupMarker =  googleMap.addMarker(marker);
                /*PickupMarker.setDraggable(true);*/
                builder.include(marker.getPosition());
                   /* mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(PickupLarLng)
                            .zoom(15)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
            }

            if(DropLarLng != null) {
                marker = new MarkerOptions()
                        .position(DropLarLng)
                        .title("Drop Location")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pin_icon));
                drag_pinImage.setVisibility(View.GONE);
                mMap.getUiSettings().setScrollGesturesEnabled(false);
                mMap.getUiSettings().setZoomControlsEnabled(false);
                mMap.getUiSettings().setAllGesturesEnabled(false);
                DropMarker = googleMap.addMarker(marker);
                /* DropMarker.setDraggable(true);*/
                builder.include(marker.getPosition());
            }

                /*if(DropLarLng != null || PickupLarLng != null) {
                    LatLngBounds bounds = builder.build();

                    //CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    Log.d("areBoundsTooSmall", "areBoundsTooSmall = " + areBoundsTooSmall(bounds, 300));
                    if (areBoundsTooSmall(bounds, 300)) {
                        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 10));
                        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 20);
                        googleMap.animateCamera(cu, new GoogleMap.CancelableCallback() {

                            @Override
                            public void onFinish() {
                                CameraUpdate zout = CameraUpdateFactory.zoomBy((float) -2.0);
                                googleMap.animateCamera(zout);
                                if(PickupMarker!=null)
                                    BounceAnimationMarker(PickupMarker,PickupLarLng);
                                if(DropMarker != null)
                                    BounceAnimationMarker(DropMarker,DropLarLng);
                            }

                            @Override
                            public void onCancel() {

                            }
                        });

                    } else {

                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
                        googleMap.animateCamera(cu, new GoogleMap.CancelableCallback() {

                            @Override
                            public void onFinish() {
                                CameraUpdate zout = CameraUpdateFactory.zoomBy((float) -1.0);
                                googleMap.animateCamera(zout);
                                BounceAnimationMarker(PickupMarker,PickupLarLng);
                                BounceAnimationMarker(DropMarker,DropLarLng);
                            }

                            @Override
                            public void onCancel() {
//                            CameraUpdate zout = CameraUpdateFactory.zoomBy((float) -1.0);
//                            googleMap.animateCamera(zout);
                            }
                        });

                    }
                }*/

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(final Marker pickMarker) {

                    Log.d("bothLocationString","bothLocationString pickup= "+bothLocationString+"=="+marker.getTitle()+"=="+edt_pickup_location.getText().toString());
                    if(marker.getTitle().equals("Pick Up Location"))
                        bothLocationString = "pickeup";
                    else if(marker.getTitle().equals("Drop Location"))
                        bothLocationString = "drop";
                    Log.d("bothLocationString","bothLocationString pickup= "+bothLocationString+"=="+marker.getTitle()+"=="+edt_pickup_location.getText().toString());
                    Log.d("bothLocationString", "bothLocationString drop= " + bothLocationString + "==" + marker.getTitle() + "==" + edt_drop_location.getText().toString());

                    return false;
                }
            });


            googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                    dragMarker = true;

                    if (marker.getTitle().equals("Pick Up Location"))
                        bothLocationString = "pickeup";
                    else if (marker.getTitle().equals("Drop Location"))
                        bothLocationString = "drop";
                }

                @Override
                public void onMarkerDrag(Marker marker) {
                    Log.d("latitude", "latitude two= " + marker.getPosition().latitude);
                }

                @Override
                public void onMarkerDragEnd(Marker mrk) {

                    Log.d("latitude", "latitude three = " + mrk.getPosition().latitude + "==" + mrk.getPosition().longitude);
                    if (Common.isNetworkAvailable(PlacesSearchActivity.this)) {
                        ClickOkButton = true;
                        LocationAddress locationAddress = new LocationAddress();
                        locationAddress.getAddressFromLocation(mrk.getPosition().latitude, mrk.getPosition().longitude,
                                getApplicationContext(), new GeocoderHandler());

                    } else {
                        Toast.makeText(PlacesSearchActivity.this, "No network", Toast.LENGTH_LONG).show();
                    }
                }
            });


        }
    }


    public void BounceAnimationMarker(final Marker animationMarker, final LatLng animationLatLng){
        if(animationLatLng != null) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            Projection proj = googleMap.getProjection();
            Point startPoint = proj.toScreenLocation(animationLatLng);
            startPoint.offset(10, -100);
            final LatLng startLatLng = proj.fromScreenLocation(startPoint);
            final long duration = 1500;
            final Interpolator interpolator = new BounceInterpolator();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);
                    double lng = t * animationLatLng.longitude + (1 - t) * startLatLng.longitude;
                    double lat = t * animationLatLng.latitude + (1 - t) * startLatLng.latitude;
                    animationMarker.setPosition(new LatLng(lat, lng));
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    }
                }
            });
        }
    }

    private boolean areBoundsTooSmall(LatLngBounds bounds, int minDistanceInMeter) {
        float[] result = new float[1];
        Location.distanceBetween(bounds.southwest.latitude, bounds.southwest.longitude, bounds.northeast.latitude, bounds.northeast.longitude, result);
        return result[0] < minDistanceInMeter;
    }

    public void EditorActionListener(final EditText locationEditext, final String clickText){

        locationEditext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                Log.d("Edit text", "Edit text = " + v.getText().toString());

                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    Log.d("locationEditext", "locationEditext = " + locationEditext.getText().toString());
                    if (locationEditext.getText().toString().length() > 0) {

                        if (clickText.equals("pickeup")) {
                            if (Common.isNetworkAvailable(PlacesSearchActivity.this)) {
                                bothLocationString = "pickeup";
                                dragMapLocation();
                                mMap.getUiSettings().setScrollGesturesEnabled(true);
                                mMap.getUiSettings().setZoomControlsEnabled(true);
                                mMap.getUiSettings().setAllGesturesEnabled(true);
                                LocationAddress.getAddressFromLocation(edt_pickup_location.getText().toString(), getApplicationContext(), new GeocoderHandlerLatitude());
                            } else {
                                Toast.makeText(PlacesSearchActivity.this, "No Network", Toast.LENGTH_LONG).show();
                            }
                        } else if (clickText.equals("drop")) {
                            if (Common.isNetworkAvailable(PlacesSearchActivity.this)) {
                                bothLocationString = "drop";
                                dragMapLocation();
                                mMap.getUiSettings().setScrollGesturesEnabled(true);
                                mMap.getUiSettings().setZoomControlsEnabled(true);
                                mMap.getUiSettings().setAllGesturesEnabled(true);
                                LocationAddress.getAddressFromLocation(edt_drop_location.getText().toString(), getApplicationContext(), new GeocoderHandlerLatitude());
                            } else {
                                Toast.makeText(PlacesSearchActivity.this, "No Network", Toast.LENGTH_LONG).show();
                            }
                        }
                        layout_pickup_drag_location.setVisibility(View.GONE);
                        if (edt_drop_location.getText().length() > 0 && edt_pickup_location.getText().length() > 0) {
                            if (checkReady() && Common.isNetworkAvailable(PlacesSearchActivity.this)) {
                                //new CaculationDiration().execute();
                                //CaculationDirationIon();
                            } else {
                                Common.showInternetInfo(PlacesSearchActivity.this, "");
                            }
                        }

                    } else {
                        PickupLarLng = null;
                        PickupLatitude = 0.0;
                        PickupLongtude = 0.0;
                        Snackbar.make(findViewById(android.R.id.content), "Please Enter Location.", Snackbar.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });
    }

    public void AddTextChangeListener(final EditText locationEditext, final String clickText){
        clickEditText = clickText;
        locationEditext.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Log.d("clickText", "clickText = " + clickText);
                if (s.length() != 0) {

                    if (clickText.equals("drop")) {
                        dragMapLocation();
                        mMap.getUiSettings().setScrollGesturesEnabled(true);
                        mMap.getUiSettings().setZoomControlsEnabled(true);
                        mMap.getUiSettings().setAllGesturesEnabled(true);
                        img_drop_close.setVisibility(View.VISIBLE);
                    } else if (clickText.equals("pickeup")) {
                        mMap.getUiSettings().setScrollGesturesEnabled(true);
                        mMap.getUiSettings().setZoomControlsEnabled(true);
                        mMap.getUiSettings().setAllGesturesEnabled(true);
                        img_pickup_close.setVisibility(View.VISIBLE);
                        dragMapLocation();
                        // img_fav.setVisibility(View.VISIBLE);
                    }
                    Log.d("ClickOkButton", "ClickOkButton = " + ClickOkButton);
                    if (!ClickOkButton) {
                        layout_pickup_drag_location.setVisibility(View.VISIBLE);
                        Log.d("ClickOkButton", "ClickOkButton = " + s.toString());
                        //new getPickupDropAddress(s.toString()).execute();
                        getPickupDropAddressIon(s.toString());
                    }
                } else {
                    if (clickText.equals("drop")) {
                        img_drop_close.setVisibility(View.GONE);
                        DropLarLng = null;
                        DropLongtude = 0.0;
                        DropLatitude = 0.0;
                    } else if (clickText.equals("pickeup")) {
                        img_pickup_close.setVisibility(View.GONE);
                        // img_fav.setVisibility(View.GONE);
                        PickupLarLng = null;
                        PickupLatitude = 0.0;
                        PickupLongtude = 0.0;
                    }
                    layout_pickup_drag_location.setVisibility(View.GONE);
                    MarkerAdd();
                }
            }
        });

    }

    public void AddSetOnClickListener(EditText locationEditext, final String ClickValuepick, final String ClickValuedrop){

        locationEditext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ClickOkButton = false;
                bothLocationString = ClickValuepick;
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                if (edt_drop_location.isFocusable()){
                    if (ClickValuedrop.equals("drop")) {
                        mMap.getUiSettings().setScrollGesturesEnabled(true);
                        mMap.getUiSettings().setZoomControlsEnabled(true);
                        mMap.getUiSettings().setAllGesturesEnabled(true);

                        params.setMargins(0, (int) getResources().getDimension(R.dimen.height_175), 0, 0);
                    } else if (ClickValuepick.equals("pickeup")) {
                        dragMapLocation();
                        mMap.getUiSettings().setScrollGesturesEnabled(true);
                        mMap.getUiSettings().setZoomControlsEnabled(true);
                        mMap.getUiSettings().setAllGesturesEnabled(true);

                        params.setMargins(0, (int) getResources().getDimension(R.dimen.height_130), 0, 0);
                    }
                    layout_pickup_drag_location.setLayoutParams(params);
                }
                return false;

            }
        });

        locationEditext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickOkButton = false;
                bothLocationString = ClickValuedrop;
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                if (ClickValuedrop.equals("drop")) {
                    mMap.getUiSettings().setScrollGesturesEnabled(false);
                    mMap.getUiSettings().setZoomControlsEnabled(false);
                    mMap.getUiSettings().setAllGesturesEnabled(false);
                    params.setMargins(0, (int) getResources().getDimension(R.dimen.height_175), 0, 0);
                } else if (ClickValuepick.equals("pickeup")) {
                    dragMapLocation();
                    mMap.getUiSettings().setScrollGesturesEnabled(true);
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    mMap.getUiSettings().setAllGesturesEnabled(true);
                    params.setMargins(0, (int) getResources().getDimension(R.dimen.height_130), 0, 0);
                }
                layout_pickup_drag_location.setLayoutParams(params);
            }
        });
    }


    public void getPickupDropAddressIon(String inputSting){
        String locatinUrl = "";
        locationArray = new ArrayList<>();
        try {
            //restriction specific country
            //locatinUrl = "https://maps.googleapis.com/maps/api/place/autocomplete/json?components=country:in&sensor=false&key=AIzaSyArDrpO2VcEXtvkcc0r0GabkKUelC2mEMc&input="+URLEncoder.encode(inputSting, "UTF-8");
            //without restriction
            locatinUrl = "https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyBqfu4fTAcwFS-z44KfvESWg8CcP1iDjOU&input="+URLEncoder.encode(inputSting, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d("locatinUrl","Login locatinUrl = "+locatinUrl);
        Ion.with(PlacesSearchActivity.this)
                .load(locatinUrl)
                .setTimeout(60*60*1000)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception error, JsonObject result) {
                        // do stuff with the result or error
                        Log.d("Login result", "Login result = " + result + "==" + error);

                        if (error == null) {
                            try {
                                JSONObject resObj = new JSONObject(result.toString());
                                if (resObj.getString("status").toLowerCase().equals("ok")) {
                                    JSONArray predsJsonArray = resObj.getJSONArray("predictions");
                                    for (int i = 0; i < predsJsonArray.length(); i++) {
                                        HashMap<String, String> locHashMap = new HashMap<String, String>();
                                        locHashMap.put("location name", predsJsonArray.getJSONObject(i).getString("description"));
                                        locationArray.add(locHashMap);
                                    }

                                    if (locationArray != null && locationArray.size() > 0) {
                                        recycle_pickup_location.setVisibility(View.VISIBLE);
                                        layout_no_result.setVisibility(View.GONE);
                                        pickupDropLocationAdapter = new PickupDropLocationAdapter(PlacesSearchActivity.this, locationArray);
                                        recycle_pickup_location.setAdapter(pickupDropLocationAdapter);
                                        pickupDropLocationAdapter.setOnDropPickupClickListener(PlacesSearchActivity.this);
                                        pickupDropLocationAdapter.updateItems();
                                    }

                                    Log.d("locationArray", "locationArray = " + locationArray.size());
                                } else if (resObj.getString("status").equals("ZERO_RESULTS")) {
                                    if (locationArray != null && locationArray.size() > 0)
                                        locationArray.clear();

                                    layout_no_result.setVisibility(View.VISIBLE);
                                    recycle_pickup_location.setVisibility(View.GONE);

                                    Log.d("locationArray", "locationArray = " + locationArray.size());
                                    if (pickupDropLocationAdapter != null)
                                        pickupDropLocationAdapter.updateBlankItems(locationArray);
                                }else if (resObj.getString("status").equals("OVER_QUERY_LIMIT")) {
                                    if (locationArray != null && locationArray.size() > 0)
                                        locationArray.clear();

                                    layout_no_result.setVisibility(View.GONE);
                                    recycle_pickup_location.setVisibility(View.GONE);

                                    Log.d("locationArray", "locationArray = " + locationArray.size());
                                    if (pickupDropLocationAdapter != null)
                                        pickupDropLocationAdapter.updateBlankItems(locationArray);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Common.ShowHttpErrorMessage(PlacesSearchActivity.this, error.getMessage());
                        }
                    }
                });
    }

    @Override
    public void PickupDropClick(int position) {

        Log.d("bothLocationString","bothLocationString = "+locationArray.size());
        if(locationArray != null && locationArray.size() > 0){
            HashMap<String,String> picDrpHash = locationArray.get(position);
            Log.d("bothLocationString","bothLocationString = "+bothLocationString);
            if(!bothLocationString.equals("")) {
                if (bothLocationString.equals("pickeup")) {
                    edt_pickup_location.setText(picDrpHash.get("location name"));
                    if (Common.isNetworkAvailable(PlacesSearchActivity.this)) {
                        Log.d("Location name", "Location name = " + edt_pickup_location.getText().toString());
                        //  bothLocationString = "pickeup";
                        LocationAddress.getAddressFromLocation(picDrpHash.get("location name"), getApplicationContext(), new GeocoderHandlerLatitude());
                    } else {
                        Toast.makeText(PlacesSearchActivity.this, "No Network", Toast.LENGTH_LONG).show();
                    }
                } else if (bothLocationString.equals("drop")) {
                    edt_drop_location.setText(picDrpHash.get("location name"));
                    if (Common.isNetworkAvailable(PlacesSearchActivity.this)) {
                        Log.d("Location name", "Location name = " + edt_pickup_location.getText().toString());
                        //  bothLocationString = "drop";
                        LocationAddress.getAddressFromLocation(picDrpHash.get("location name"), getApplicationContext(), new GeocoderHandlerLatitude());
                    } else {
                        Toast.makeText(PlacesSearchActivity.this, "No Network", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        layout_pickup_drag_location.setVisibility(View.GONE);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
        txt_home = null;
        edt_pickup_location = null;
        edt_drop_location = null;
        layout_now = null;
        gpsTracker = null;
        googleMap = null;
        marker = null;
        PickupLarLng = null;
        DropLarLng = null;
        layout_one = null;
        layout_two = null;
        layout_three = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Okayswiss","onActivityResult = "+requestCode);
        if (requestCode == 3) {
            if(data != null){
                String userUpd = data.getStringExtra("update_user_profile").toString();
                Log.e("Okayswiss","requestCode = "+userUpd);
                if(userUpd.equals("1")){
                }
            }
        }
    }
    private static final String[] permission = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
    };
    private boolean checkPermissions(boolean checked) {
        if (checked) {
            ActivityCompat.requestPermissions(this, permission, 100);
        } else {
            for (String permiss : permission) {
                if (ActivityCompat.checkSelfPermission(this, permiss) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permission, 100);
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void onBackPressed() {
         Intent a = new Intent(PlacesSearchActivity.this,HomeActivity.class);
         a.addCategory(Intent.CATEGORY_HOME);
         a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         startActivity(a);
    }

    int dp10 = 0;
    public void reCenter() {

        if (mMap == null) {
            return;
        }
        Polyline polyline = null;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(x, y));
//        builder.include(marker.getPosition());
        if (polyline!= null) {
            List<LatLng> lats = polyline.getPoints();
            for (LatLng lat : lats) {
                builder.include(lat);
            }
        }
        CameraUpdate cau = CameraUpdateFactory.newLatLngZoom(new LatLng(x,y), 16);
        mMap.animateCamera(cau);
    }

    //Recycler view clicking functions
    public static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }


    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
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