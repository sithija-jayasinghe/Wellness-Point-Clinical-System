package edu.icet.service;

import edu.icet.dto.RefundDto;
import java.util.List;

public interface RefundService {
    void addRefund(RefundDto refundDto);
    List<RefundDto> getAllRefunds();
    RefundDto getRefundById(Long id);
    void updateRefund(Long id, RefundDto refundDto);
    void deleteRefund(Long id);
}
