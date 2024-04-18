package com.example.patient_hms_application;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {
    Button bt_logout;
    private DrawerLayout drawerLayout;
    private FirebaseAuth auth;
    ImageView imageView, imageViewListOfDoctor, imageViewSchedule, imageViewHistory, imageViewResult;
    TextView textView, tvListOfDoctor, tv_schedule, tv_history, tv_textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        bt_logout = findViewById(R.id.bt_logout);

        drawerLayout = findViewById(R.id.drawerLayout);
        auth = FirebaseAuth.getInstance();

        imageView=findViewById(R.id.imageView);
        textView=findViewById(R.id.text2);

        imageViewListOfDoctor = findViewById(R.id.imageViewListOfDoctor);
        tvListOfDoctor = findViewById(R.id.tvListOfDoctor);

        imageViewSchedule = findViewById(R.id.imageViewSchedule);
        tv_schedule = findViewById(R.id.tv_schedule);

        imageViewHistory = findViewById(R.id.imageViewHistory);
        tv_history = findViewById(R.id.tv_history);

        imageViewResult = findViewById(R.id.imageViewResult);
        tv_textViewResult = findViewById(R.id.tv_textViewResult);


        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_open, R.string.navigation_close);
        //drawerLayout.addDrawerListener(toggle);
        //toggle.syncState();

        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }

        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Home.this, Profile.class);
                startActivity(intent);
                finish();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Home.this, Profile.class);
                startActivity(intent);
                finish();
            }
        });

        imageViewListOfDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Home.this, BookingSchedule.class);
                startActivity(intent);
                finish();
            }
        });

        tvListOfDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Home.this, BookingSchedule.class);
                startActivity(intent);
                finish();
            }
        });
        imageViewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Home.this, BookedSchedules.class);
                startActivity(intent);
                finish();
            }
        });

        tv_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Home.this, BookedSchedules.class);
                startActivity(intent);
                finish();
            }
        });

        imageViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Home.this, History.class);
                startActivity(intent);
                finish();
            }
        });

        tv_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Home.this, History.class);
                startActivity(intent);
                finish();
            }
        });
        imageViewResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Home.this, ResultOfAppointment.class);
                startActivity(intent);
                finish();
            }
        });

        tv_textViewResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Home.this, ResultOfAppointment.class);
                startActivity(intent);
                finish();
            }
        });

    }
}