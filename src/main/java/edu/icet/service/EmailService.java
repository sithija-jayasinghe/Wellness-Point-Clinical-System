package edu.icet.service;

import edu.icet.entity.Appointment;
import edu.icet.entity.Payment;
import edu.icet.entity.Prescription;

public interface EmailService {

    void sendSimpleEmail(String to, String subject, String text);

    // Appointment emails
    void sendAppointmentConfirmation(Appointment appointment, String patientEmail);
    void sendAppointmentCancellation(Appointment appointment, String patientEmail);
    void sendDoctorAppointmentNotification(Appointment appointment, String doctorEmail);

    // Payment emails
    void sendPaymentReceipt(Payment payment, String patientEmail, String patientName);

    // Prescription emails
    void sendPrescriptionIssuedEmail(Prescription prescription, String patientEmail, String patientName);
}
