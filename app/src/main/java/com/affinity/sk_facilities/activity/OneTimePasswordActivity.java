package com.affinity.sk_facilities.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import androidx.appcompat.widget.AppCompatButton;

import com.affinity.sk_facilities.BaseResponse;
import com.affinity.sk_facilities.beens.Drivers;
import com.affinity.sk_facilities.beens.Users;
import com.affinity.sk_facilities.countrypicker.CountryPickerCallbacks;
import com.affinity.sk_facilities.countrypicker.CountryPickerDialog;
import com.affinity.sk_facilities.utils.Country;
import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.affinity.sk_facilities.R;
import com.affinity.sk_facilities.Api;
import com.affinity.sk_facilities.Constants;
import com.affinity.sk_facilities.RetrofitClient;
import com.affinity.sk_facilities.utils.Common;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OneTimePasswordActivity extends AppCompatActivity {
    SharedPreferences pref;
    Context ctx;
    Button  btnGenerateOTP;
    EditText etPhoneNumber /*etOTP,*/;
    String phoneNumber, otp,TxtViewCountryCode ;
    FirebaseAuth auth;
    LinearLayout otpPasswordLayout;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String verificationCode;
    RotateLoading cusRotateLoading;
    RelativeLayout layout_back_arrow,layout_back,rl_new_otp,li_layout_next,layout_otp_next,layout_done;
    Dialog ProgressDialog;
    TextView loginWithEmail,edit_country_code,otp_phone_number;
    CountryPickerDialog countryPicker;
    Api myApi;
    PinEntryEditText otpText;

    SmsBroadcastReceiver smsBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_time_password);

        ProgressDialog = new Dialog(OneTimePasswordActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.custom_progress_dialog);
        ProgressDialog.setCancelable(false);
        cusRotateLoading = (RotateLoading)ProgressDialog.findViewById(R.id.rotateloading_register);

        otpPasswordLayout = (LinearLayout)findViewById(R.id.otpPasswordLayout);
        layout_back_arrow = (RelativeLayout) findViewById(R.id.layout_back_arrow);
        layout_back = (RelativeLayout) findViewById(R.id.layout_back);
        rl_new_otp = (RelativeLayout) findViewById(R.id.rl_new_otp);
        layout_otp_next = (RelativeLayout) findViewById(R.id.layout_otp_next);
        layout_done = (RelativeLayout) findViewById(R.id.layout_done);
        li_layout_next = (RelativeLayout) findViewById(R.id.li_layout_next);
        otpText = (PinEntryEditText) findViewById(R.id.otp);
        myApi = RetrofitClient.getRetrofitInstance().create(Api.class);

        btnGenerateOTP=findViewById(R.id.btn_generate_otp);
        etPhoneNumber=(EditText)findViewById(R.id.et_phone_number);
        edit_country_code = (TextView) findViewById(R.id.country_code);
        otp_phone_number = (TextView) findViewById(R.id.otp_phone_number);
        TxtViewCountryCode = edit_country_code.getText().toString();

            StartFirebaseLogin();

        rl_new_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genrateNewOtp();
            }
        });

        li_layout_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genrateOtp();
              /*  JsonObject gsonObject = new JsonObject();
                try {

                    JSONObject jsonObj_ = new JSONObject();
                    jsonObj_.put("user_mobile", etPhoneNumber.getText().toString());

                    JsonParser jsonParser = new JsonParser();
                    gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                    Log.e("MY gson.JSON:  ", "AS PARAMETER  " + gsonObject);
                    // Toast.makeText(this, "result"+gsonObject, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

                /*Call<BaseResponse> call = myApi.getUser(gsonObject);
                call.enqueue(new Callback<Users>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
                        BaseResponse users = response.body();

                        if (users.message.equalsIgnoreCase("success")) {
                            ProgressDialog.cancel();
                            cusRotateLoading.stop();
                            Users.LoginUserDetails userData = users.getUserDetails();
                            SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
                           // editor.putInt(Constants.user_id, userData.getUser_id());
                            editor.putString(Constants.firstname, userData.getName());
                            editor.putString(Constants.email_id, userData.getEmail());
                            editor.putString(Constants.mobileno, userData.getPhone());
                            // editor.putString(Constants.image, userData.getImage());
                            editor.apply();
                           *//* Intent intent = new Intent(OneTimePasswordActivity.this,HomeActivity.class);
                            startActivity(intent);*//*

                        } else {
                            startActivity(new Intent(OneTimePasswordActivity.this, SignUpActivity.class));
                            finish();
                            ProgressDialog.cancel();
                            cusRotateLoading.stop();
                        }
                    }

                    @Override
                    public void onFailure(Call<Users> call, Throwable t) {
                        ProgressDialog.cancel();
                        cusRotateLoading.stop();
                        Toast.makeText(OneTimePasswordActivity.this, "Check your internet connection...", Toast.LENGTH_SHORT).show();

                    }
                });*/
            }
        });
            btnGenerateOTP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        genrateOtp();
                }
            });

        layout_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(OneTimePasswordActivity.this,Get_Started_Activity.class);
                startActivity(inten);
            }
        });

        layout_otp_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(otpText.getText().toString().isEmpty()){
                   Toast.makeText(OneTimePasswordActivity.this, "Please enter your pin", Toast.LENGTH_SHORT).show();
               }else{
                   Toast.makeText(OneTimePasswordActivity.this, "Please Re-enter your pin", Toast.LENGTH_SHORT).show();
               }
            }
        });

        layout_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(otpText.getText().toString().isEmpty()){
                   Toast.makeText(OneTimePasswordActivity.this, "Please enter your pin", Toast.LENGTH_SHORT).show();
               }else{
                   Toast.makeText(OneTimePasswordActivity.this, "Please Re-enter your pin", Toast.LENGTH_SHORT).show();
               }
            }
        });

        layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(OneTimePasswordActivity.this,Get_Started_Activity.class);
                startActivity(inten);
            }
        });


        otpText.requestFocus();
        otpText.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(final CharSequence otp_pin) {
                if (otp_pin.length() == 6) {
                    ProgressDialog.show();
                    cusRotateLoading.start();
                    //this only checking purpose so remove only this commend line
                    otp = otpText.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
                    SigninWithPhone(credential);
                }
            }
        });


        edit_country_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countryPicker =
                        new CountryPickerDialog(OneTimePasswordActivity.this, new CountryPickerCallbacks() {
                            @Override
                            public void onCountrySelected(com.affinity.sk_facilities.countrypicker.Country country, int flagResId) {
                                // TODO handle callback
                                Log.d("country", country.getDialingCode());
                                Log.d("country", new Locale(getResources().getConfiguration().locale.getLanguage(),
                                        country.getIsoCode()).getDisplayCountry());
                                edit_country_code.setText("+" + country.getDialingCode());
                                //+new Locale(getResources().getConfiguration().locale.getLanguage(),country.getIsoCode()).getDisplayCountry()
                                Common.CountryCode = country.getDialingCode();
                                etPhoneNumber.requestFocus();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.showSoftInput(etPhoneNumber, InputMethodManager.SHOW_IMPLICIT);
                                    }
                                }, 100);
                            }
                        });

                countryPicker.show();
            }
        });
    }

    private void genrateNewOtp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,                     // Phone number to verify
                60,                           // Timeout duration
                TimeUnit.SECONDS,                // Unit of timeout
                OneTimePasswordActivity.this,        // Activity (for callback binding)
                mCallback);
    }

    private void genrateOtp() {
        if(etPhoneNumber.getText().toString().isEmpty()){
            Toast.makeText(OneTimePasswordActivity.this,"Please enter your Mobile number",Toast.LENGTH_SHORT).show();
        }
        else{

            new AlertDialog.Builder(OneTimePasswordActivity.this)
                    .setTitle(getResources().getString(R.string.edit_number))
                    .setNegativeButton(getResources().getString(R.string.yes), null)
                    .setPositiveButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            ProgressDialog.show();
                            cusRotateLoading.start();
                            phoneNumber = edit_country_code.getText().toString()+ etPhoneNumber.getText().toString();
                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    phoneNumber,                     // Phone number to verify
                                    60,                           // Timeout duration
                                    TimeUnit.SECONDS,                // Unit of timeout
                                    OneTimePasswordActivity.this,        // Activity (for callback binding)
                                    mCallback);
                        }
                    }).create().show();
        }
    }

    private void SigninWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            JsonObject gsonObject = new JsonObject();
                            try {

                                JSONObject jsonObj_ = new JSONObject();
                                jsonObj_.put("user_mobile", etPhoneNumber.getText().toString());

                                JsonParser jsonParser = new JsonParser();
                                gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
                                Log.e("MY gson.JSON:  ", "AS PARAMETER  " + gsonObject);
                                // Toast.makeText(this, "result"+gsonObject, Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Call<Users> call = RetrofitClient.getApiService().getUser(gsonObject);
                            call.enqueue(new Callback<Users>() {
                                @Override
                                public void onResponse(Call<Users> call, Response<Users> response) {
                                    Users users = response.body();

                                    if (users.message.equalsIgnoreCase("success")) {
                                        ArrayList<Users.LoginUserDetails> userData = users.getUserDetails();
                                        SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
                                        editor.putString(Constants.user_id, userData.get(0).getUser_id());
                                        editor.putString(Constants.firstname, userData.get(0).getName());
                                        editor.putString(Constants.email_id, userData.get(0).getEmail());
                                        editor.putString(Constants.mobileno, userData.get(0).getPhone());
                                        editor.putBoolean(Constants.KEY_OTP_LOGGED_IN,true);
                                       // editor.putString(Constants.image, userData.getImage());
                                        editor.apply();
                                        ProgressDialog.cancel();
                                        cusRotateLoading.stop();
                                        Intent intent = new Intent(OneTimePasswordActivity.this,HomeActivity.class);
                                        startActivity(intent);

                                    } else {
                                        Intent intent = new Intent(OneTimePasswordActivity.this,SignUpActivity.class);
                                        intent.putExtra("ph_no",phoneNumber);
                                        startActivity(intent);
                                        ProgressDialog.cancel();
                                        cusRotateLoading.stop();
                                    }
                                }
                                @Override
                                public void onFailure(Call<Users> call, Throwable t) {
                                    ProgressDialog.cancel();
                                    cusRotateLoading.stop();
                                    Toast.makeText(OneTimePasswordActivity.this, "Check your internet connection...", Toast.LENGTH_SHORT).show();

                                }
                            });
                        } else {
                            ProgressDialog.cancel();
                            cusRotateLoading.stop();
                            Snackbar.make(findViewById(android.R.id.content), "Incorrect OTP.", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void StartFirebaseLogin() {
        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Snackbar.make(findViewById(android.R.id.content), "verification completed.", Snackbar.LENGTH_LONG).show();
            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
                Snackbar.make(findViewById(android.R.id.content), "Check your mobile Number.", Snackbar.LENGTH_LONG).show();
                ProgressDialog.cancel();
                cusRotateLoading.stop();
            }
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                otpPasswordLayout.setVisibility(View.VISIBLE);
                ProgressDialog.cancel();
                cusRotateLoading.stop();
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
            //    startSmsUserConsent();
                otp_phone_number.setText(phoneNumber);
                Snackbar.make(findViewById(android.R.id.content), "Code sent.", Snackbar.LENGTH_LONG).show();
            }
        };
    }

    @Override
        public void onBackPressed() {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
    }
}