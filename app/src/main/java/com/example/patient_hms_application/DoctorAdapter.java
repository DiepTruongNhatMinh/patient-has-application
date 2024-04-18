package com.example.patient_hms_application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DoctorAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Doctor> doctorList;
    public DoctorAdapter(Context context, ArrayList<Doctor> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
    }
    @Override
    public int getCount() {
        return doctorList.size();
    }

    @Override
    public Object getItem(int position) {
        return doctorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Tạo và trả về một View cho mỗi item trong ListView
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_doctor, parent, false);

        // Lấy đối tượng Doctor
        Doctor doctor = doctorList.get(position);

        // Hiển thị tên và chuyên khoa của bác sĩ trên các view trong layout item_list_doctor_2
        TextView textViewName = view.findViewById(R.id.textViewFullName);
        textViewName.setText(doctor.getFullName());

        TextView textViewSpecialist = view.findViewById(R.id.textViewSpecialist);
        textViewSpecialist.setText(doctor.getSpecialist());

        return view;
    }
}
