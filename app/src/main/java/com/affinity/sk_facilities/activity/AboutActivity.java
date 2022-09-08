package com.affinity.sk_facilities.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.affinity.sk_facilities.AboutUsContent;
import com.affinity.sk_facilities.Constants;
import com.affinity.sk_facilities.R;
import com.affinity.sk_facilities.RetrofitClient;
import com.affinity.sk_facilities.beens.Users;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutActivity extends AppCompatActivity {

    RelativeLayout layout_back_arrow;
    TextView about_us_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        layout_back_arrow = (RelativeLayout)findViewById(R.id.layout_back_arrow);
        about_us_txt = (TextView) findViewById(R.id.about_us_txt);

        layout_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        layout_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        aboutUsContent();
    }

    private void aboutUsContent() {
        JsonObject gsonObject = new JsonObject();
        try {

            JSONObject jsonObj_ = new JSONObject();
            jsonObj_.put("setting_id", 1);

            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
            Log.e("MY gson.JSON:  ", "AS PARAMETER  " + gsonObject);
            // Toast.makeText(this, "result"+gsonObject, Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<AboutUsContent> getTokenCall = RetrofitClient.getApiService().getContent(gsonObject);
        getTokenCall.enqueue(new retrofit2.Callback<AboutUsContent>(){

            @Override
            public void onResponse(Call<AboutUsContent> call, Response<AboutUsContent> response) {
                if(response.isSuccessful()){
                   /* Toast.makeText(AboutActivity.this, "successfully" +response.body().getUserdetails().get(0).getContent(), Toast.LENGTH_LONG).show();*/
                    about_us_txt.setText(response.body().getUserdetails().get(0).getContent());
                }else {
                    Toast.makeText(AboutActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AboutUsContent> call, Throwable t) {

                Toast.makeText(AboutActivity.this, "Check your internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
