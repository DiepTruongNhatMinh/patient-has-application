package com.example.patient_hms_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ScheduleOfDoctor extends AppCompatActivity {
    TextView price, doctorWorkExperience;
    EditText ed_reason;
    ListView timetableListView;
    FirebaseAuth auth;
    FirebaseUser user;
    String patientName, patientMobilePhone;
    Button btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_of_doctor);
        List<String> scheduleUids = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        price = findViewById(R.id.price);
        doctorWorkExperience = findViewById(R.id.doctor_ex);
        timetableListView = findViewById(R.id.timetableListView);
        ed_reason = findViewById(R.id.ed_reason);
        user = auth.getCurrentUser();
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(ScheduleOfDoctor.this, Home.class);
                startActivity(home);
            }
        });
        DatabaseReference patientReference = FirebaseDatabase.getInstance().getReference("patient");
        patientReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(user.getUid()).exists()) {
                    DataSnapshot userData = snapshot.child(user.getUid());
                    patientName = userData.child("fullName").getValue(String.class);
                    patientMobilePhone = userData.child("mobile").getValue(String.class);
            }
        }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
            });

        Intent intent = getIntent();
        String doctorEmail = intent.getStringExtra("doctorEmail");
        String doctorUid = intent.getStringExtra("doctorUid");
        String address = intent.getStringExtra("address");
        String doctorPhoneNumber = intent.getStringExtra("doctorPhoneNumber");
        String patientId = user.getUid();
        String doctorName = intent.getStringExtra("doctorName");
        String doctorprice = intent.getStringExtra("price");
        String workExperience = intent.getStringExtra("workExperience");
        price.setText(doctorprice);
        doctorWorkExperience.setText(workExperience);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user_schedules").child(doctorUid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> scheduleItems = new ArrayList<>();
                for (DataSnapshot scheduleSnapshot : dataSnapshot.getChildren()) {
                    String scheduleStatus = scheduleSnapshot.child("scheduleStatus").getValue(String.class);
                        if (scheduleStatus != null && scheduleStatus.equals("available")) {
                        String scheduleDate = scheduleSnapshot.child("fullDateTime").getValue(String.class);
                        String cancellationReason = scheduleSnapshot.child("cancellationReason").getValue(String.class);
                        String uid = scheduleSnapshot.getKey(); // Get the UID
                        scheduleItems.add(scheduleDate);
                        scheduleUids.add(uid);
                    }
                }
                if (scheduleItems.isEmpty()) {
                    Log.d("ScheduleOfDoctor", "No schedules found.");
                } else {
                    Log.d("ScheduleOfDoctor", "Schedules found. Count: " + scheduleItems.size());
                }ArrayAdapter<String> adapter = new ArrayAdapter<>(ScheduleOfDoctor.this, android.R.layout.simple_list_item_1, scheduleItems);
                timetableListView.setAdapter(adapter);
                timetableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        String selectedScheduleItem = scheduleItems.get(position);
                        String selectedUid = scheduleUids.get(position);

                        Log.d("ScheduleOfDoctor", "Selected UID: " + selectedUid);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleOfDoctor.this);
                        builder.setTitle("Confirm appointment");
                        builder.setMessage("Are you sure to schedule an appointment at " + selectedScheduleItem + " ?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("ScheduleOfDoctor", "User clicked 'Yes' button");
                                DatabaseReference appointmentsReference = FirebaseDatabase.getInstance().getReference("Appointments");
                                String appointmentId = appointmentsReference.push().getKey();
                                String patientEmail = user.getEmail();
                                String reason = ed_reason.getText().toString();
                                Appointment appointment = new Appointment(
                                        appointmentId,
                                        doctorUid,
                                        patientName,
                                        address,
                                        patientMobilePhone,
                                        doctorPhoneNumber,
                                        extractScheduleDateTime(selectedScheduleItem),
                                        "Appointment Has Not Been Completed",
                                        patientId,
                                        doctorEmail,
                                        patientEmail,
                                        doctorName,
                                        reason,
                                        selectedUid
                                );

                                // Write the appointment data
                                appointmentsReference.child(appointment.getAppointmentId()).setValue(appointment)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("ScheduleOfDoctor", "Appointment data written successfully");

                                            DatabaseReference userSchedulesReference = FirebaseDatabase.getInstance().getReference("user_schedules")
                                                    .child(doctorUid);

                                            userSchedulesReference.child(selectedUid).child("scheduleStatus").setValue("booked")
                                                    .addOnSuccessListener(aVoid1 -> {
                                                        Log.d("ScheduleOfDoctor", "ScheduleStatus updated successfully");
                                                        Toast.makeText(ScheduleOfDoctor.this, "Congratulations, you have successfully booked your appointment", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Log.e("ScheduleOfDoctor", "Error updating scheduleStatus: " + e.getMessage());
                                                        Toast.makeText(ScheduleOfDoctor.this, "Failed to update scheduleStatus", Toast.LENGTH_SHORT).show();
                                                    });
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("ScheduleOfDoctor", "Error writing appointment data: " + e.getMessage());
                                            Toast.makeText(ScheduleOfDoctor.this, "Failed to write appointment data", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("ScheduleOfDoctor", "User clicked 'No' button");
                                // Perform action when patient clicks "No"
                                Intent intent1 = new Intent(ScheduleOfDoctor.this, BookingSchedule.class);
                                startActivity(intent1);
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ScheduleOfDoctor", "Error updating schedule status: " + databaseError.getMessage());
            }
        });
    }

    private String extractScheduleDateTime(String fullSchedule) {
        return fullSchedule.trim();
    }
}
