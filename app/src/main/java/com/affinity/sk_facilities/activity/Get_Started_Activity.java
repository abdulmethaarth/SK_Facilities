package com.affinity.sk_facilities.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.affinity.sk_facilities.R;

public class Get_Started_Activity extends AppCompatActivity {

    RelativeLayout getSTart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get__started_);

        getSTart = (RelativeLayout)findViewById(R.id.layout_getstart);
        getSTart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Get_Started_Activity.this, OneTimePasswordActivity.class));
                finish();
            }
        });

    }
}