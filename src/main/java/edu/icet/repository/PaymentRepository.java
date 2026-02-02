package edu.icet.repository;

import edu.icet.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentDate = :date")
    Double sumAmountByPaymentDate(@Param("date") LocalDate date);

    @Query("SELECT SUM(p.amount) FROM Payment p")
    Double sumTotalAmount();
}
