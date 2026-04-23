package com.Coffee.mapper.user;

import com.Coffee.dto.user.response.AddonResponse;
import com.Coffee.model.Addon;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddonMapper {

    AddonResponse  toAddonResponse(Addon addon);

    List<AddonResponse>  toAddonResponseList(List<Addon> addons);
}
