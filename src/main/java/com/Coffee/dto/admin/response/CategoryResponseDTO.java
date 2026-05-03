package com.Coffee.dto.admin.response;

import com.Coffee.model.Addon;

import java.util.List;

public record CategoryResponseDTO(Long id,
                                  String name,
                                  String imageUrl,
                                  List<AddonResponse> addonList,
                                  Boolean isDeleted) {
}
