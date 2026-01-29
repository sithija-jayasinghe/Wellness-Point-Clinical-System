package edu.icet.service.impl;

import edu.icet.entity.Refund;
import edu.icet.repository.RefundRepository;
import edu.icet.service.RefundService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefundServiceImpl implements RefundService {

    private final RefundRepository refundRepository;

    public RefundServiceImpl(RefundRepository refundRepository) {
        this.refundRepository = refundRepository;
    }

    @Override
    public Refund saveRefund(Refund refund) {
        return refundRepository.save(refund);
    }

    @Override
    public List<Refund> getAllRefunds() {
        return refundRepository.findAll();
    }

    @Override
    public Refund getRefundById(Long id) {
        return refundRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteRefund(Long id) {
        refundRepository.deleteById(id);
    }
}
