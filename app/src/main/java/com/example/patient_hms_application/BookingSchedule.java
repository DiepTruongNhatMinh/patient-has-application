package com.example.patient_hms_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookingSchedule extends AppCompatActivity implements AdapterView.OnItemClickListener{
    Doctor doctor = new Doctor();
    ListView doctorListView;
    ArrayList<Doctor> doctorsList = new ArrayList<>();
    Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_schedule);
        // Lấy ID của user hiện tại
        String uid = FirebaseAuth.getInstance().getUid();
        doctorListView = findViewById(R.id.list_view);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(BookingSchedule.this, Home.class);
                startActivity(home);
            }
        });

        // Truy vấn dữ liệu của node "users"
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Duyệt dữ liệu
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Lấy dữ liệu từ Doctor
                    String name = snapshot.child("fullName").getValue(String.class);
                    String dateOfBirth = snapshot.child("date_of_birth").getValue(String.class);
                    ArrayList<String> certList = (ArrayList<String>) snapshot.child("cert").getValue();
                    String gender = snapshot.child("gender").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    String mobile = snapshot.child("mobile_phone").getValue(String.class);
                    String specialist = snapshot.child("specialist").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String price = snapshot.child("price").getValue(String.class);;
                    String workExperience = snapshot.child("workExperience").getValue(String.class);;
                    // Inside the loop where you create a new Doctor instance
                    doctor = new Doctor(certList, address, dateOfBirth, mobile, specialist, gender, name, "none", email, "none", "none", uid, price, workExperience);
                    doctor.setUid(snapshot.getKey()); // Set the UID
                    doctorsList.add(doctor);
                }
                // Tạo adapter
                DoctorAdapter adapter = new DoctorAdapter(BookingSchedule.this, doctorsList);

                // Hiển thị dữ liệu trong ListView
                ListView listView = findViewById(R.id.list_view);

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(BookingSchedule.this);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Lỗi xảy ra
            }
        });
    

    }@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Lấy thông tin bác sĩ được chọn
        Doctor selectedDoctor = doctorsList.get(position);

        // Tạo Intent để chuyển sang activity khác
        Intent intent = new Intent(BookingSchedule.this, ScheduleOfDoctor.class);

        // Gửi thông tin bác sĩ sang activity khác
        intent.putExtra("doctorEmail", selectedDoctor.getEmail());
        intent.putExtra("doctorUid", selectedDoctor.getUid()); // Pass the UID to ScheduleOfDoctor
        intent.putExtra("address", selectedDoctor.getAddress());
        intent.putExtra("doctorPhoneNumber", selectedDoctor.getMoblie_phone());
        intent.putExtra("doctorName", selectedDoctor.getFullName());
        intent.putExtra("price", selectedDoctor.getPrice());
        intent.putExtra("workExperience", selectedDoctor.getWorkExperience());
        // Khởi động activity
        startActivity(intent);
    }
        }