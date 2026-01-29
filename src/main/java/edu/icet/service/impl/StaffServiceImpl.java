package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.StaffDto;
import edu.icet.entity.Staff;
import edu.icet.repository.StaffRepository;
import edu.icet.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final ObjectMapper mapper;

    @Override
    public StaffDto createStaff(StaffDto dto) {
        Staff staff = mapper.convertValue(dto, Staff.class);
        Staff saved = staffRepository.save(staff);
        return mapper.convertValue(saved, StaffDto.class);
    }

    @Override
    public List<StaffDto> getAllStaff() {
        List<Staff> all = staffRepository.findAll();
        List<StaffDto> dtos = new ArrayList<>();
        all.forEach(staff -> dtos.add(mapper.convertValue(staff, StaffDto.class)));
        return dtos;
    }

    @Override
    public StaffDto getStaffById(Long id) {
        return staffRepository.findById(id)
                .map(staff -> mapper.convertValue(staff, StaffDto.class))
                .orElse(null);
    }

    @Override
    public void deleteStaff(Long id) {
        if (staffRepository.existsById(id)) {
            staffRepository.deleteById(id);
        }
    }

    @Override
    public StaffDto updateStaff(Long id, StaffDto dto) {
        Optional<Staff> existingOpt = staffRepository.findById(id);
        if (existingOpt.isPresent()) {
            Staff existing = existingOpt.get();
            existing.setUserId(dto.getUserId());
            existing.setDesignation(dto.getDesignation());
            existing.setStatus(dto.getStatus());

            Staff saved = staffRepository.save(existing);
            return mapper.convertValue(saved, StaffDto.class);
        }
        return null;
    }
}
