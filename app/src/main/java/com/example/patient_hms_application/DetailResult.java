package com.example.patient_hms_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DetailResult extends AppCompatActivity {
    TextView tv_patient_name, tv_patient_mobilephone, tv_appointment_time, tv_doctor_name, tv_Reason, tv_doctor_address,
            tv_patient_insurance, tv_clinical_result, tv_diagnosis, tv_conclusion, tv_recommendation, tv_notes;
    Button btn_back, btn_feedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_result);
        //Data mapping
        tv_appointment_time = findViewById(R.id.tv_appointment_time);
        tv_doctor_name = findViewById(R.id.tv_doctor_name);
        tv_Reason = findViewById(R.id.tv_Reason);
        tv_patient_name = findViewById(R.id.tv_patient_name);
        tv_patient_mobilephone = findViewById(R.id.tv_patient_mobilephone);
        tv_doctor_address = findViewById(R.id.tv_doctor_address);

        tv_clinical_result = findViewById(R.id.tv_clinical_result);
        tv_patient_insurance = findViewById(R.id.tv_patient_insurance);
        tv_diagnosis = findViewById(R.id.tv_diagnosis);
        tv_conclusion = findViewById(R.id.tv_conclusion);
        tv_recommendation = findViewById(R.id.tv_recommendation);
        tv_notes = findViewById(R.id.tv_notes);

        btn_back = findViewById(R.id.btn_back);
        btn_feedback = findViewById(R.id.btn_feedback);

        // Get a reference to the "CompletedAppointment" node in Firebase database
        DatabaseReference completedAppointment = FirebaseDatabase.getInstance().getReference("CompletedAppointment");
        // Retrieve result details from the database
        completedAppointment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot resultSnapshot : snapshot.getChildren()){
                    String appointmentId = resultSnapshot.child("appointmentId").getValue(String.class);
                    String appointmentId_intent = getIntent().getStringExtra("appointmentId");
                    if (appointmentId.equals(appointmentId_intent)) {
                        // Retrieve result data
                        String patientName = resultSnapshot.child("pName").getValue(String.class);
                        String doctorAddress = resultSnapshot.child("dAddress").getValue(String.class);
                        String appointmentTime = resultSnapshot.child("appointmentTime").getValue(String.class);
                        String doctorName = resultSnapshot.child("dName").getValue(String.class);
                        String reason = resultSnapshot.child("reason").getValue(String.class);
                        String mobilephone = resultSnapshot.child("pPhoneNo").getValue(String.class);
                        String insuranceType = resultSnapshot.child("insuranceType").getValue(String.class);
                        String diagnosis = resultSnapshot.child("diagnosis").getValue(String.class);
                        String clinicalER = resultSnapshot.child("clinicalER").getValue(String.class);
                        String conclusion = resultSnapshot.child("conclusion").getValue(String.class);
                        String recommendation = resultSnapshot.child("recommendation").getValue(String.class);
                        String notes = resultSnapshot.child("notes").getValue(String.class);

                        // Set text in TextViews
                        tv_patient_name.setText(patientName);
                        tv_doctor_address.setText(doctorAddress);
                        tv_appointment_time.setText(appointmentTime);
                        tv_patient_mobilephone.setText(mobilephone);
                        tv_doctor_name.setText(doctorName);
                        tv_Reason.setText(reason);
                        tv_conclusion.setText(conclusion);
                        tv_diagnosis.setText(diagnosis);
                        tv_notes.setText(notes);
                        tv_clinical_result.setText(clinicalER);
                        tv_recommendation.setText(recommendation);
                        tv_patient_insurance.setText(insuranceType);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //btn_back
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBackIntent = new Intent(DetailResult.this, ResultOfAppointment.class);
                startActivity(goBackIntent);
            }
        });

        //btn_feedback
        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tạo dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailResult.this);

                // Thiết lập tiêu đề và nội dung của dialog
                builder.setTitle("Gửi phản hồi");
                builder.setMessage("Vui lòng nhập phản hồi của bạn");

                // Tạo một edit text để người dùng nhập text
                EditText editText = new EditText(DetailResult.this);
                builder.setView(editText);

                // Tạo các nút cho dialog
                builder.setPositiveButton("Gửi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Lấy text từ edit text
                        String feedback = editText.getText().toString();
                        String appointmentId_intent = getIntent().getStringExtra("appointmentId");
                        String doctorEmail_intent = getIntent().getStringExtra("doctorEmail");
                        String patientEmail = getIntent().getStringExtra("patientEmail");
                        DatabaseReference feedbackOfPatientReference = FirebaseDatabase.getInstance().getReference("FeedbackOfPatient");
                        DatabaseReference reference = feedbackOfPatientReference.push();
                        Map<String, String> data = new HashMap<>();
                        data.put("Feedback", feedback);
                        data.put("appointmentId", appointmentId_intent);
                        data.put("doctorEmail", doctorEmail_intent);
                        data.put("patientEmail", patientEmail);
                        reference.setValue(data);
                        Toast.makeText(DetailResult.this, "Thank You For Your FeedBack! Hope You Doing Great!", Toast.LENGTH_SHORT).show();
                        Intent back = new Intent(DetailResult.this, ResultOfAppointment.class);
                        startActivity(back);
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                // Hiển thị dialog
                builder.show();
            }
        });
    }
}