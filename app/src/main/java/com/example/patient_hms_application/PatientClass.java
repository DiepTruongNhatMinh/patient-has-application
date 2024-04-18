package com.example.patient_hms_application;

public class PatientClass {
    private String email;
    private String password;
    private String fullName;
    private String DoB;
    private String mobile;
    private String gender;

    public PatientClass() {
        // Default constructor required for calls to DataSnapshot.getValue(HelperClass.class)
    }

    public PatientClass(String email, String password, String fullName, String DoB, String mobile, String gender) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.DoB = DoB;
        this.mobile = mobile;
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getfullName() {
        return fullName;
    }

    public void setfullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDoB() {
        return DoB;
    }

    public void setDoB(String DoB) {
        this.DoB = DoB;
    }

    public String getmobile() {
        return mobile;
    }

    public void setmobile(String ed_mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
