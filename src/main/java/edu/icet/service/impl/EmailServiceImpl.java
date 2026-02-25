package edu.icet.service.impl;

import edu.icet.entity.Appointment;
import edu.icet.entity.Payment;
import edu.icet.entity.Prescription;
import edu.icet.entity.PrescriptionItem;
import edu.icet.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            javaMailSender.send(message);
            log.info("Email sent successfully to {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }

    @Override
    public void sendAppointmentConfirmation(Appointment appointment, String patientEmail) {
        if (patientEmail == null || patientEmail.isBlank()) return;
        String doctorName = (appointment.getDoctor() != null) ? appointment.getDoctor().getName() : "N/A";
        String subject = "Appointment Confirmation - Wellness Point Clinical System";
        String body = "Dear Patient,\n\n"
                + "Your appointment has been successfully booked.\n\n"
                + "Appointment Details:\n"
                + "  Appointment No : " + appointment.getAppointmentNo() + "\n"
                + "  Doctor         : Dr. " + doctorName + "\n"
                + "  Date & Time    : " + appointment.getAppointmentTime() + "\n"
                + "  Status         : " + appointment.getStatus() + "\n\n"
                + "Please arrive 10 minutes early.\n\n"
                + "Thank you,\nWellness Point Clinical System";
        sendSimpleEmail(patientEmail, subject, body);
    }

    @Override
    public void sendAppointmentCancellation(Appointment appointment, String patientEmail) {
        if (patientEmail == null || patientEmail.isBlank()) return;
        String doctorName = (appointment.getDoctor() != null) ? appointment.getDoctor().getName() : "N/A";
        String subject = "Appointment Cancellation - Wellness Point Clinical System";
        String body = "Dear Patient,\n\n"
                + "Your appointment has been cancelled.\n\n"
                + "Cancelled Appointment Details:\n"
                + "  Appointment No : " + appointment.getAppointmentNo() + "\n"
                + "  Doctor         : Dr. " + doctorName + "\n"
                + "  Date & Time    : " + appointment.getAppointmentTime() + "\n\n"
                + "If you did not request this cancellation, please contact us immediately.\n\n"
                + "Thank you,\nWellness Point Clinical System";
        sendSimpleEmail(patientEmail, subject, body);
    }

    @Override
    public void sendDoctorAppointmentNotification(Appointment appointment, String doctorEmail) {
        if (doctorEmail == null || doctorEmail.isBlank()) return;
        String subject = "New Appointment Booked - Wellness Point Clinical System";
        String body = "Dear Doctor,\n\n"
                + "A new appointment has been booked for you.\n\n"
                + "Appointment Details:\n"
                + "  Appointment No : " + appointment.getAppointmentNo() + "\n"
                + "  Patient ID     : " + appointment.getPatientId() + "\n"
                + "  Date & Time    : " + appointment.getAppointmentTime() + "\n\n"
                + "Thank you,\nWellness Point Clinical System";
        sendSimpleEmail(doctorEmail, subject, body);
    }

    @Override
    public void sendPaymentReceipt(Payment payment, String patientEmail, String patientName) {
        if (patientEmail == null || patientEmail.isBlank()) return;
        String subject = "Payment Receipt - Wellness Point Clinical System";
        String body = "Dear " + patientName + ",\n\n"
                + "Your payment has been received successfully.\n\n"
                + "Payment Details:\n"
                + "  Payment ID     : " + payment.getPaymentId() + "\n"
                + "  Amount         : LKR " + payment.getAmount() + "\n"
                + "  Payment Date   : " + payment.getPaymentDate() + "\n"
                + "  Payment Method : " + payment.getPaymentMethod() + "\n"
                + "  Status         : " + payment.getStatus() + "\n\n"
                + "Thank you for your payment.\n\n"
                + "Thank you,\nWellness Point Clinical System";
        sendSimpleEmail(patientEmail, subject, body);
    }

    @Override
    public void sendPrescriptionIssuedEmail(Prescription prescription, String patientEmail, String patientName) {
        if (patientEmail == null || patientEmail.isBlank()) return;
        String subject = "Your Prescription - Wellness Point Clinical System";
        StringBuilder body = new StringBuilder();
        body.append("Dear ").append(patientName).append(",\n\n")
            .append("Your prescription has been issued.\n\n")
            .append("Prescription Details:\n")
            .append("  Prescription ID : ").append(prescription.getPrescriptionId()).append("\n")
            .append("  Issued Date     : ").append(prescription.getIssuedDate()).append("\n\n")
            .append("Medications:\n");
        if (prescription.getPrescriptionItems() != null) {
            for (PrescriptionItem item : prescription.getPrescriptionItems()) {
                body.append("  - ").append(item.getMedicineName())
                    .append(" | Dosage: ").append(item.getDosage())
                    .append(" | Duration: ").append(item.getDuration())
                    .append("\n");
            }
        }
        body.append("\nPlease follow your doctor's instructions carefully.\n\n")
            .append("Thank you,\nWellness Point Clinical System");
        sendSimpleEmail(patientEmail, subject, body.toString());
    }
}
