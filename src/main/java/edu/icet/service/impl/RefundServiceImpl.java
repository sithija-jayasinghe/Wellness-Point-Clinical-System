package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.RefundDto;
import edu.icet.entity.Refund;
import edu.icet.repository.RefundRepository;
import edu.icet.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    private final RefundRepository refundRepo;
    private final ObjectMapper mapper;

    @Override
    public void addRefund(RefundDto refundDto) {
        Refund refund = mapper.convertValue(refundDto, Refund.class);
        refundRepo.save(refund);
    }

    @Override
    public List<RefundDto> getAllRefunds() {
        List<Refund> list = refundRepo.findAll();
        List<RefundDto> dtoList = new ArrayList<>();
        list.forEach(entity -> dtoList.add(mapper.convertValue(entity, RefundDto.class)));
        return dtoList;
    }

    @Override
    public RefundDto getRefundById(Long id) {
        Optional<Refund> byId = refundRepo.findById(id);
        return byId.map(entity -> mapper.convertValue(entity, RefundDto.class)).orElse(null);
    }

    @Override
    public void updateRefund(Long id, RefundDto refundDto) {
        if (refundRepo.existsById(id)) {
            Refund refund = mapper.convertValue(refundDto, Refund.class);
            if (refundDto.getPayment() != null && refundDto.getPayment().getPaymentId() != null) {
                Payment payment = paymentRepo.findById(refundDto.getPayment().getPaymentId())
                        .orElse(null);
                refund.setPayment(payment);
            }
            refund.setRefundId(id);
            refundRepo.save(refund);
        }
    }

    @Override
    public void deleteRefund(Long id) {
        refundRepo.deleteById(id);
    }
}
