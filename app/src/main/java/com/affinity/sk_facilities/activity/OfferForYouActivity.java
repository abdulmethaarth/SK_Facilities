package com.affinity.sk_facilities.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.affinity.sk_facilities.R;

public class OfferForYouActivity extends AppCompatActivity {

    RelativeLayout layout_back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_for_you);

        layout_back_arrow = (RelativeLayout)findViewById(R.id.layout_back_arrow);


        layout_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}