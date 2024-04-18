package com.example.patient_hms_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    EditText ed_emailRes, ed_passwordRes, ed_fullName, ed_DoB, ed_mobile;
    String gender;
    FirebaseAuth mAuth;
    Button bt_resgister;
    private static final String TAG = "Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ed_emailRes = findViewById(R.id.ed_emailRes);
        ed_passwordRes = findViewById(R.id.ed_passwordRes);
        mAuth = FirebaseAuth.getInstance();
        bt_resgister = findViewById(R.id.bt_register);
        ed_fullName = findViewById(R.id.ed_fullName);
        ed_DoB = findViewById(R.id.ed_DoB);
        ed_mobile = findViewById(R.id.ed_mobile);

        TextView tv_backLoginRes = findViewById(R.id.tv_loginRes);

        tv_backLoginRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        bt_resgister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailRes = ed_emailRes.getText().toString();
                String passwordRes = ed_passwordRes.getText().toString();

                if (TextUtils.isEmpty(emailRes)) {
                    showToast("Please enter your email");
                    ed_emailRes.setError("Email is required");
                    ed_emailRes.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailRes).matches()) {
                    showToast("Please re-enter your email");
                    ed_emailRes.setError("Valid email is required");
                    ed_emailRes.requestFocus();
                } else if (TextUtils.isEmpty(passwordRes)) {
                    showToast("Please enter your password");
                    ed_passwordRes.setError("Password is required");
                    ed_passwordRes.requestFocus();
                } else if (passwordRes.length() < 6) {
                    showToast("Password should be at least 6 digits");
                    ed_passwordRes.setError("Password too weak");
                    ed_passwordRes.requestFocus();
                } else {
                    registerUser(emailRes, passwordRes);
                }
            }

            private void registerUser(String emailRes, String passwordRes) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.createUserWithEmailAndPassword(emailRes, passwordRes).addOnCompleteListener(Register.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = auth.getCurrentUser();
                                    String doctorName = ed_fullName.getText().toString();
                                    String DoB = ed_DoB.getText().toString();
                                    String mobile = ed_mobile.getText().toString();
                                    RadioGroup radioGroupGender = findViewById(R.id.radioGroupGender);
                                    int selectedId = radioGroupGender.getCheckedRadioButtonId();
                                    if (selectedId != -1) {
                                        RadioButton selectedGender = findViewById(selectedId);
                                        gender = selectedGender.getText().toString();
                                    } else {
                                    }
                                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("patient");
                                    PatientClass patientClass = new PatientClass(emailRes, passwordRes, doctorName, DoB, mobile, gender);
                                    referenceProfile.child(firebaseUser.getUid()).setValue(patientClass)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        firebaseUser.sendEmailVerification();
                                                        showToast("User registered successfully. Please verify your email");
                                                        // Notify the admin about the new registration
                                                        notifyAdminAboutRegistration(firebaseUser.getUid());
                                                        Intent login = new Intent(Register.this, Home.class);
                                                        startActivity(login);
                                                    } else {
                                                        showToast("User registration failed. Please try again");
                                                    }
                                                }

                                                private void notifyAdminAboutRegistration(String uid) {
                                                }
                                            });
                                } else {
                                    handleRegistrationFailure(task.getException());
                                }
                            }
                        });
            }
            private void handleRegistrationFailure(Exception exception) {
                try {
                    throw exception;
                } catch (FirebaseAuthWeakPasswordException e) {
                    showToast("Your password is too weak");
                    ed_passwordRes.setError("Your password is too weak");
                    ed_passwordRes.requestFocus();
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    showToast("Your email is invalid or already in use");
                    ed_passwordRes.setError("Your email is invalid or already in use");
                    ed_passwordRes.requestFocus();
                } catch (FirebaseAuthUserCollisionException e) {
                    showToast("User is already registered with this email");
                    ed_passwordRes.setError("User is already registered with this email");
                    ed_passwordRes.requestFocus();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    showToast(e.getMessage());
                }
            }

            private void showToast(String message) {
                Toast.makeText(Register.this, message, Toast.LENGTH_SHORT).show();
            }

        });
    }
}