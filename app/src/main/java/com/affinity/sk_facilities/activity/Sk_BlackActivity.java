package com.affinity.sk_facilities.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.affinity.sk_facilities.R;

public class Sk_BlackActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sk__black);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_sk_black);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    startActivity(new Intent(Sk_BlackActivity.this, HomeActivity.class));
                    // on favorites clicked
                    return true;
                } if (item.getItemId() == R.id.navigation_my_booking) {
                    startActivity(new Intent(Sk_BlackActivity.this, MyBookingsActivity.class));
                    // on favorites clicked
                    return true;
                } if (item.getItemId() == R.id.navigation_more) {
                    startActivity(new Intent(Sk_BlackActivity.this, HomeActivity.class));
                    // on favorites clicked
                    return true;
                }
                return false;
            }
        });
    }
}