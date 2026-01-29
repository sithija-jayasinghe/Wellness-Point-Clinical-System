package edu.icet.service;

import edu.icet.dto.StaffDto;
import java.util.List;

public interface StaffService {
    StaffDto createStaff(StaffDto dto);
    List<StaffDto> getAllStaff();
    StaffDto getStaffById(Long id);
    void deleteStaff(Long id);
    StaffDto updateStaff(Long id, StaffDto dto);
}
