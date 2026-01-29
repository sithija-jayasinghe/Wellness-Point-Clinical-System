package edu.icet.controller;

import edu.icet.dto.StaffDto;
import edu.icet.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService service;

    @PostMapping("/add")
    public StaffDto addStaff(@RequestBody StaffDto staffDto) {
        return service.createStaff(staffDto);
    }

    @GetMapping("/get-all")
    public List<StaffDto> getAllStaff() {
        return service.getAllStaff();
    }

    @GetMapping("/{id}")
    public StaffDto getStaffById(@PathVariable Long id) {
        return service.getStaffById(id);
    }

    @PutMapping("/update/{id}")
    public StaffDto updateStaff(@PathVariable Long id, @RequestBody StaffDto staffDto) {
        return service.updateStaff(id, staffDto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteStaff(@PathVariable Long id) {
        service.deleteStaff(id);
    }
}
