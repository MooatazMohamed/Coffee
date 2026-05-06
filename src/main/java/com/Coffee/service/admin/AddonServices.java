package com.Coffee.service.admin;

import com.Coffee.dto.admin.request.AddonRequest;
import com.Coffee.dto.admin.response.AddonResponse;
import com.Coffee.handler.ApiResponse;
import com.Coffee.mapper.admin.AddonMapper;
import com.Coffee.model.Addon;
import com.Coffee.repository.user.AddonRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddonServices {

    private final AddonRepository addonRepository;
    private final AddonMapper addonMapper;

    public AddonServices(AddonRepository addonRepository, AddonMapper addonMapper) {
        this.addonRepository = addonRepository;
        this.addonMapper = addonMapper;
    }

    //Save
    @Transactional
    public ApiResponse<AddonResponse> save(AddonRequest addonRequest){
        Addon addon = addonMapper.toEntity(addonRequest);
        return ApiResponse.success("Addon Saved Succesfully",
                addonMapper.toResponse(addonRepository.save(addon)));
    }


    //GET
    public ApiResponse<List<AddonResponse>> getAndSearchAddons(String search) {
        if (search != null && search.trim().isEmpty()) {
            search = null;
        }
        List<Addon> addons = addonRepository.getAndSearchAddons(search);
        return ApiResponse.success(
                "Addon Retrieved Successfully",
                addonMapper.toResponseList(addons)
        );
    }

    //Delete
    @Transactional
    public ApiResponse<Void> delete(Long id) {

        Addon addon = addonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Addon not found"));
        if (Boolean.TRUE.equals(addon.getIsDeleted())) {
            return ApiResponse.error("Addon already deleted", null);
        }
        addon.setIsDeleted(true);
        return ApiResponse.success("Addon Deleted Successfully", null);
    }

    //Update
    @Transactional
    public ApiResponse<AddonResponse> update(Long id, AddonRequest request) {

        Addon addon = addonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Addon not found"));
        if (Boolean.TRUE.equals(addon.getIsDeleted())) {
            return ApiResponse.error("Addon is deleted", null);
        }
        // update only if not null
        if (request.getName() != null) {
            addon.setName(request.getName());
        }
        if (request.getPrice() != null) {
            addon.setPrice(request.getPrice());
        }
        Addon saved = addonRepository.save(addon);
        return ApiResponse.success(
                "Addon Updated Successfully",
                addonMapper.toResponse(saved)
        );
    }

    //GET
    public ApiResponse<List<AddonResponse>> getAll(){

        return ApiResponse.success("Data Retrieved Successfully",
                addonMapper.toResponseList(addonRepository.findByIsDeletedFalse())
        );
    }
}
