package com.example.patient_hms_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    TextView tv_email, tv_Gender, tv_firstName, tv_DoB, tv_mobile;;
    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(Profile.this, Home.class);
                startActivity(home);
            }
        });
        tv_email = findViewById(R.id.tv_email);
        tv_Gender = findViewById(R.id.tv_gender);
        tv_firstName = findViewById(R.id.tv_fullName);
        tv_DoB = findViewById(R.id.tv_DoB);
        tv_mobile = findViewById(R.id.tv_mobile);

        tv_email.setText(user.getEmail());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("patient");
        // Add ValueEventListener to update TextViews
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the user's UID exists in the database
                if (dataSnapshot.child(user.getUid()).exists()) {
                    DataSnapshot userData = dataSnapshot.child(user.getUid());
                    String fullName = userData.child("fullName").getValue(String.class);
                    tv_firstName.setText(fullName);

                    String dateOfBirth = userData.child("doB").getValue(String.class);
                    tv_DoB.setText(dateOfBirth);

                    String mobile = userData.child("mobile").getValue(String.class);
                    tv_mobile.setText(mobile);

                    String gender = userData.child("gender").getValue(String.class);
                    tv_Gender.setText(gender);
                }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}