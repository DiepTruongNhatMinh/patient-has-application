package com.example.patient_hms_application;

public class Appointment {
    private String appointmentId;
    private String doctorUid;
    private String patientName;
    private String Address;
    private String patientPhoneNumber;
    private String doctorPhoneNumber;
    private String appointmentTime;
    private String appointmentStatus;
    private String patientId;
    private String email;
    private String patientEmail;
    private String doctorName;
    private String reason;
    private String selectedUid;
    public Appointment() {
    }

    public Appointment(String appointmentId, String doctorUid, String patientName, String Address, String patientPhoneNumber, String doctorPhoneNumber, String appointmentTime, String appointmentStatus, String patientId, String email, String patientEmail, String doctorName, String reason, String selectedUid) {
        this.appointmentId = appointmentId;
        this.doctorUid = doctorUid;
        this.patientName = patientName;
        this.Address = Address;
        this.patientPhoneNumber = patientPhoneNumber;
        this.doctorPhoneNumber = doctorPhoneNumber;
        this.appointmentTime = appointmentTime;
        this.appointmentStatus = appointmentStatus;
        this.patientId = patientId;
        this.email = email;
        this.patientEmail = patientEmail;
        this.doctorName = doctorName;
        this.reason = reason;
        this.selectedUid = selectedUid;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDoctorUid() {
        return doctorUid;
    }
    public void setDoctorUid(String doctorUid) {
        this.doctorUid = doctorUid;
    }
    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String clinicAddress) {
        this.Address = Address;
    }

    public String getPatientPhoneNumber() {
        return patientPhoneNumber;
    }

    public void setPatientPhoneNumber(String patientPhoneNumber) {
        this.patientPhoneNumber = patientPhoneNumber;
    }

    public String getDoctorPhoneNumber() {
        return doctorPhoneNumber;
    }

    public void setDoctorPhoneNumber(String doctorPhoneNumber) {
        this.doctorPhoneNumber = doctorPhoneNumber;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSelectedUid() {
        return selectedUid;
    }

    public void setSelectedUid(String selectedUid) {
        this.selectedUid = selectedUid;
    }
}
