package edu.icet.controller;

import edu.icet.dto.RefundDto;
import edu.icet.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/refunds")
public class RefundController {

    private final RefundService refundService;

    @PostMapping("/add")
    public void addRefund(@RequestBody RefundDto refundDto) {
        refundService.addRefund(refundDto);
    }

    @GetMapping("/get-all")
    public List<RefundDto> getAllRefunds() {
        return refundService.getAllRefunds();
    }

    @GetMapping("/{id}")
    public RefundDto getRefund(@PathVariable Long id) {
        return refundService.getRefundById(id);
    }

    @PutMapping("/update/{id}")
    public void updateRefund(@PathVariable Long id, @RequestBody RefundDto refundDto) {
        refundService.updateRefund(id, refundDto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRefund(@PathVariable Long id) {
        refundService.deleteRefund(id);
    }
}
