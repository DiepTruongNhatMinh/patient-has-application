package com.example.patient_hms_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultOfAppointment extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    ListView timetableListView;
    String patientUid;
    Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_of_appointment);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        patientUid = user.getUid();
        timetableListView = findViewById(R.id.timetableListView);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(ResultOfAppointment.this, Home.class);
                startActivity(home);
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CompletedAppointment");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> apointmentItems = new ArrayList<>();
                List<String> displayItems = new ArrayList<>();
                for (DataSnapshot apmSnapshot : dataSnapshot.getChildren()) {
                    String patientEmail = apmSnapshot.child("patientEmail").getValue(String.class);
                    if (patientEmail != null && patientEmail.equals(user.getEmail())){
                        String appointmentTime = apmSnapshot.child("appointmentTime").getValue(String.class);
                        String patientName = apmSnapshot.child("pName").getValue(String.class);
                        String patientPhoneNumber = apmSnapshot.child("pPhoneNo").getValue(String.class);
                        String address = apmSnapshot.child("dAddress").getValue(String.class);
                        String appointmentId = apmSnapshot.child("appointmentId").getValue(String.class);
                        String doctorName = apmSnapshot.child("doctorName").getValue(String.class);
                        String doctorEmail = apmSnapshot.child("doctorEmail").getValue(String.class);

                        // Extract the doctor UID from the appointmentTime field
                        apointmentItems.add(appointmentTime + "/" + patientName + "/" + patientPhoneNumber + "/" + address + "/"+ appointmentId + "/" + doctorName+"/" +patientEmail+"/" +doctorEmail);
                        displayItems.add(appointmentTime +"\n"+ patientName +"\n"+ patientPhoneNumber);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ResultOfAppointment.this, android.R.layout.simple_list_item_1, displayItems);
                timetableListView.setAdapter(adapter);
                timetableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        // Lấy dữ liệu appointment từ item được chọn
                        String appointmentId = apointmentItems.get(position).split("/")[4];
                        // Tạo AlertDialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(ResultOfAppointment.this);
                        builder.setTitle("Which interaction do you want to choose for this appointment?\n" +
                                "\u200B");
                        builder.setItems(new String[]{"Go back", "View the detail of this appoinment result"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                } else if (which == 1) {
                                    // Hoàn thành lịch khám
                                    // Tạo intent
                                    String doctorEmail = apointmentItems.get(position).split("/")[7];
                                    String patientEmail = apointmentItems.get(position).split("/")[6];
                                    Intent intent = new Intent(ResultOfAppointment.this, DetailResult.class);
                                    intent.putExtra("appointmentId", appointmentId);
                                    intent.putExtra("doctorEmail", doctorEmail);
                                    intent.putExtra("patientEmail", patientEmail);
                                    // Bắt đầu activity
                                    startActivity(intent);
                                }
                            }
                        });

                        // Hiển thị AlertDialog
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }
}