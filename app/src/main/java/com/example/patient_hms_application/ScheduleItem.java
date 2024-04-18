package com.example.patient_hms_application;

public class ScheduleItem {
    private String dateTime;
    private boolean isChecked;

    public ScheduleItem(String dateTime, boolean isChecked) {
        this.dateTime = dateTime;
        this.isChecked = isChecked;
    }

    public String getDateTime() {
        return dateTime;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}