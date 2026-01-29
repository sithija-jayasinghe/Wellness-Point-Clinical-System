package edu.icet.service;

import edu.icet.entity.Refund;
import java.util.List;

public interface RefundService {
    Refund saveRefund(Refund refund);
    List<Refund> getAllRefunds();
    Refund getRefundById(Long id);
    void deleteRefund(Long id);
}
