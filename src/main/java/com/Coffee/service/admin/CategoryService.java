package com.Coffee.service.admin;

import com.Coffee.dto.admin.request.AddonRequest;
import com.Coffee.dto.admin.request.CategoryRequestDTO;
import com.Coffee.dto.admin.response.AddonResponse;
import com.Coffee.dto.admin.response.CategoryResponseDTO;
import com.Coffee.model.Addon;
import com.Coffee.model.Category;
import com.Coffee.repository.user.AddonRepository;
import com.Coffee.repository.user.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private  CategoryRepository categoryRepository;
    @Autowired
    private  AddonRepository addonRepository;


    @Transactional
    public CategoryResponseDTO createCategoryWithAddons(CategoryRequestDTO categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.name());
        category.setImageUrl(categoryRequest.imageUrl());

//        if (categoryRequest.addonIds()!= null && !categoryRequest.addonIds().isEmpty()) {
//            // Fetch all addons by the provided IDs
//            List<Addon> addons = addonRepository.findAllById(categoryRequest.addonIds());
//
//            // Set the relationship
//            category.setAddons(addons);
//
//            // Important: In a bidirectional relationship, you usually update both sides
//            // though for ManyToMany, updating the owning side (Category) is enough for the DB
//            for (Addon addon : addons) {
//                addon.getCategories().add(category);
//            }
//        }

        List<Addon> addons = new ArrayList<>();

        if (categoryRequest.addonList() != null) {

            for (AddonResponse addonDTO : categoryRequest.addonList()) {

                Addon addon;

                // ✅ لو فيه id → هاته من DB
                if (addonDTO.getId() != null) {
                    addon = addonRepository.findById(addonDTO.getId())
                            .orElseThrow(() -> new RuntimeException("Addon not"+addonDTO.getName()+" found"));
                }else{
                    addon = new Addon();
                    addon.setName(addonDTO.getName());
                    addon.setPrice(addonDTO.getPrice());

                }

                addons.add(addon);


                // maintain bidirectional relation
                addon.getCategories().add(category);
            }
        }

        category.setAddons(addons);

        return mapToDTO(categoryRepository.save(category));

       // return  mapToDTO(categoryRepository.save(category));
    } // end of createCategoryWithAddons

    public CategoryResponseDTO mapToDTO(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getImageUrl(),
                category.getAddons().stream()
                        .map(addon -> new AddonResponse(
                                addon.getId(),
                                addon.getName(),
                                addon.getPrice()
                        ))
                        .toList()
        );
    }
}
