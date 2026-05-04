package com.Coffee.controller.admin;

import com.Coffee.dto.admin.request.AddonRequest;
import com.Coffee.dto.admin.response.AddonResponse;
import com.Coffee.handler.ApiResponse;
import com.Coffee.service.admin.AddonServices;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addon")
public class AddonController {

    private final AddonServices addonServices;

    public AddonController(AddonServices addonServices) {
        this.addonServices = addonServices;
    }

    // Public endpoint - anyone can access
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<AddonResponse>>> getAll(){
        return ResponseEntity.ok(addonServices.getAll());
    }

    // Admin only endpoints
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AddonResponse>> save(
            @RequestBody @Valid AddonRequest addonRequest
    ) {
        return ResponseEntity.ok(addonServices.save(addonRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){
        return ResponseEntity.ok(addonServices.delete(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AddonResponse>> update(
            @PathVariable Long id,
            @RequestBody @Valid AddonRequest request
    ) {
        return ResponseEntity.ok(addonServices.update(id, request));
    }
}
