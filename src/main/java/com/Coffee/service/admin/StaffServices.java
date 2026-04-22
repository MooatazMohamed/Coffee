package com.Coffee.service.admin;

import com.Coffee.dto.admin.response.StaffResponse;
import com.Coffee.handler.ApiResponse;
import com.Coffee.repository.admin.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffServices {

    private final StaffRepository staffRepository;

    public StaffServices(StaffRepository staffRepository){
        this.staffRepository=staffRepository;
    }

    public ApiResponse<List<StaffResponse>> getAllStaff(){
        return ApiResponse.success("Data Retrieved Successfully",
                staffRepository.getStaff());
    }
}
