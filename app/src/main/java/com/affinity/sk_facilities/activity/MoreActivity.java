package com.affinity.sk_facilities.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.affinity.sk_facilities.R;

import org.w3c.dom.Text;

public class MoreActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TextView profile,payments,refer_and_earn,fare_chart,about_us,contact_us,offers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_more);
        profile = (TextView) findViewById(R.id.profile);
        payments = (TextView) findViewById(R.id.payments);
        offers = (TextView) findViewById(R.id.offers);
        refer_and_earn = (TextView) findViewById(R.id.refer_and_earn);
        fare_chart = (TextView) findViewById(R.id.fare_chart);
        about_us = (TextView) findViewById(R.id.about_us);
        contact_us = (TextView) findViewById(R.id.contact_us);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    startActivity(new Intent(MoreActivity.this, HomeActivity.class));
                    // on favorites clicked
                    return true;
                } if (item.getItemId() == R.id.navigation_my_booking) {
                    startActivity(new Intent(MoreActivity.this, MyBookingsActivity.class));
                    // on favorites clicked
                    return true;
                } if (item.getItemId() == R.id.navigation_sk_black) {
                    startActivity(new Intent(MoreActivity.this, Sk_BlackActivity.class));
                    // on favorites clicked
                    return true;
                }
                return false;
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoreActivity.this, UserProfileActivity.class));
            }
        });

        payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoreActivity.this, Payments.class));
            }
        });

        offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoreActivity.this, OfferForYouActivity.class));
            }
        });

        refer_and_earn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoreActivity.this, Refer_and_Earn.class));
            }
        });

        about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoreActivity.this, AboutActivity.class));
            }
        });

        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoreActivity.this, Faq.class));
            }
        });

        fare_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoreActivity.this, RateCardActivity.class));
            }
        });


    }
}