package com.affinity.sk_facilities.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.affinity.sk_facilities.Api;
import com.affinity.sk_facilities.BaseResponse;
import com.affinity.sk_facilities.RetrofitClient;
import com.affinity.sk_facilities.beens.Users;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.victor.loading.rotate.RotateLoading;

import net.gotev.uploadservice.MultipartUploadRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.affinity.sk_facilities.R;
import com.affinity.sk_facilities.Constants;
import com.affinity.sk_facilities.ImageFilePath;
import com.affinity.sk_facilities.beens.RegUser;
import com.affinity.sk_facilities.utils.Common;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    //EditText edit_username;
    EditText edit_name, edit_email;
    TextView edit_mobile;

    Dialog ProgressDialog;
    RotateLoading cusRotateLoading;
    Dialog OpenCameraDialog;
    String userImage,name,email,password,phone_no,dob,gender;
    private RegUser regUser = new RegUser();
    Api myApi;
    Button Register;
    Bitmap bitmap;
    private static final String TAG = "SignUpActivity";
    private ProgressDialog pDialog;
    boolean connected = false;
    DatePickerDialog picker;
    int value;
    RadioGroup radioGroup;
    String phoneNUmber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Intent intent=getIntent();
       phoneNUmber = intent.getStringExtra("ph_no");

        Register = (Button) findViewById(R.id.signup);
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_mobile = (TextView) findViewById(R.id.edit_mobile);
        edit_mobile.setText(phoneNUmber);
        edit_email = (EditText) findViewById(R.id.edit_email);
        myApi = RetrofitClient.getRetrofitInstance().create(Api.class);

            Register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (edit_name.getText().toString().isEmpty()) {
                        Common.showMkError(SignUpActivity.this, getResources().getString(R.string.please_enter_name));
                        return;
                    }
                    else if (edit_email.getText().toString().isEmpty()) {
                        Common.showMkError(SignUpActivity.this, getResources().getString(R.string.please_enter_email));
                        return;
                    }
                    else if(edit_email.getText().toString().trim().length() != 0 && !isValidEmail(edit_email.getText().toString().trim())){
                        Common.showMkError(SignUpActivity.this, getResources().getString(R.string.please_enter_valid_email));
                        return;
                    }  else if (edit_mobile.getText().toString().isEmpty()) {
                        Common.showMkError(SignUpActivity.this, getResources().getString(R.string.please_enter_mobile));
                        return;
                    } else {
                        signup();
                    }
                }
            });

    }

    private void signup() {

        pDialog = new ProgressDialog(SignUpActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Creating Account...");
        pDialog.setCancelable(false);
        pDialog.show();

         name = edit_name.getText().toString();
         email = edit_email.getText().toString();
        phone_no = edit_mobile.getText().toString();
        gender = Integer.toString(value);

       // File user_im = new File(userImage);
        //File user_im = new File(getRealPathFromURI(tempUri));
        String image = String.valueOf(userImage);
        JsonObject gsonObject = new JsonObject();
        try {

            JSONObject jsonObj_ = new JSONObject();
            jsonObj_.put("name", name);
            jsonObj_.put("email_id", email);
            jsonObj_.put("phone_no", phone_no);

            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
            Log.e("MY gson.JSON:  ", "AS PARAMETER  " + gsonObject);
            // Toast.makeText(this, "result"+gsonObject, Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<BaseResponse> call = myApi.register(gsonObject);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse users = response.body();

                if (users.status.equalsIgnoreCase("true")) {
                    Toast.makeText(SignUpActivity.this, "Thank you. your details Updated..", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean(Constants.KEY_OTP_LOGGED_IN,true);
                    editor.apply();
                    Intent intent = new Intent(SignUpActivity.this,HomeActivity.class);
                    startActivity(intent);
                    pDialog.dismiss();
                } else {
                    Toast.makeText(SignUpActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(SignUpActivity.this, "Check your internet connection...", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.really_exit))
                .setMessage(getResources().getString(R.string.are_you_sure))
                .setNegativeButton(getResources().getString(R.string.cancel), null)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        //  HomeActivity.super.onBackPressed();
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                    }
                }).create().show();
    }
}





