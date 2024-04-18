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

public class BookedSchedules extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    ListView timetableListView;
    String patientUid;
    Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_schedules);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        patientUid = user.getUid();
        timetableListView = findViewById(R.id.timetableListView);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(BookedSchedules.this, Home.class);
                startActivity(home);
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Appointments");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> scheduleItems = new ArrayList<>();
                List<String> displaySchedule = new ArrayList<>();
                for (DataSnapshot scheduleSnapshot : dataSnapshot.getChildren()) {
                    String patientEmail = scheduleSnapshot.child("patientEmail").getValue(String.class);
                    String appointmentStatus = scheduleSnapshot.child("appointmentStatus").getValue(String.class);
                    if (patientEmail != null && patientEmail.equals(user.getEmail()) && appointmentStatus.equals("Appointment Has Not Been Completed")){
                        String appointmentTime = scheduleSnapshot.child("appointmentTime").getValue(String.class);
                        String patientName = scheduleSnapshot.child("patientName").getValue(String.class);
                        String patientPhoneNumber = scheduleSnapshot.child("patientPhoneNumber").getValue(String.class);
                        String address = scheduleSnapshot.child("address").getValue(String.class);
                        String appointmentId = scheduleSnapshot.child("appointmentId").getValue(String.class);
                        String doctorName = scheduleSnapshot.child("doctorName").getValue(String.class);

                        // Extract the doctor UID from the appointmentTime field
                        scheduleItems.add(appointmentTime + "/" + patientName + "/" + patientPhoneNumber + "/" + address + "/"+ appointmentId + "/" + doctorName);
                        displaySchedule.add(appointmentTime +"\n"+ patientName +"\n"+ patientPhoneNumber);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(BookedSchedules.this, android.R.layout.simple_list_item_1, displaySchedule);
                timetableListView.setAdapter(adapter);
                timetableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        // Lấy dữ liệu appointment từ item được chọn
                        String appointmentId = scheduleItems.get(position).split("/")[4];
                        // Tạo AlertDialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(BookedSchedules.this);
                        builder.setTitle("Which interaction do you want to choose for this appointment?\n" +
                                "\u200B");
                        builder.setItems(new String[]{"Cancel this appointment", "Go Back To See Other Booked Schedules"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    // Build the AlertDialog with the EditText
                                    EditText inputReason = new EditText(BookedSchedules.this);
                                    inputReason.setHint("Enter reason");
                                    AlertDialog.Builder cancelDialogBuilder = new AlertDialog.Builder(BookedSchedules.this);
                                    cancelDialogBuilder.setTitle("Enter Your Cancellation Reason");
                                    cancelDialogBuilder.setView(inputReason);
                                    cancelDialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Change Appointment Status from "Has not been completed" to "Canceled"
                                            DatabaseReference appointmentReference = FirebaseDatabase.getInstance().getReference("Appointments");
                                            appointmentReference.child(appointmentId).child("appointmentStatus").setValue("Canceled By Patient");

                                            // Store the cancellation reason in the database
                                            DatabaseReference databaseCanceledReference = FirebaseDatabase.getInstance().getReference("CanceledAppointment");
                                            DatabaseReference reference = databaseCanceledReference.push();


                                            String appointmentCancelDay = scheduleItems.get(position).split("/")[0];
                                            String appointmentCanceledId = scheduleItems.get(position).split("/")[4];
                                            String patientName = scheduleItems.get(position).split("/")[1];
                                            String doctorName = scheduleItems.get(position).split("/")[5];
                                            Map<String, String> data = new HashMap<>();
                                            data.put("doctorName", doctorName);
                                            data.put("appointmentCancelDay", appointmentCancelDay);
                                            data.put("appointmentCanceledId", appointmentCanceledId);
                                            data.put("patientName", patientName);
                                            reference.setValue(data);
                                        }
                                    });

                                    cancelDialogBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // User clicked cancel, do nothing or handle it accordingly
                                            dialog.cancel();
                                        }
                                    });

                                    cancelDialogBuilder.show();

                                } else if (which == 1) {
                                    // Hoàn thành lịch khám
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

    private String getDoctorUidFromAppointment(String appointmentTime) {
        // Extract the doctor UID from the appointmentTime field
        String[] parts = appointmentTime.split(" - ");
        if (parts.length >= 3) {
            return parts[2];
        }
        return null;
    }
}