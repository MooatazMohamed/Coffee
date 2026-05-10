package com.Coffee.mapper.admin;

import com.Coffee.dto.admin.request.AddonRequest;
import com.Coffee.dto.admin.response.AddonResponse;
import com.Coffee.model.Addon;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddonMapper {

    Addon toEntity(AddonRequest request);

    AddonResponse toResponse(Addon addon);

    List<AddonResponse> toResponseList(List<Addon> addons);

}
