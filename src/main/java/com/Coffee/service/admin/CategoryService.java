package com.Coffee.service.admin;

import com.Coffee.dto.admin.request.AddonRequest;
import com.Coffee.dto.admin.request.CategoryRequestDTO;
import com.Coffee.dto.admin.response.AddonResponse;
import com.Coffee.dto.admin.response.CategoryResponseDTO;
import com.Coffee.model.Addon;
import com.Coffee.model.Branch;
import com.Coffee.model.Category;
import com.Coffee.repository.user.AddonRepository;
import com.Coffee.repository.user.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class CategoryService {
    @Autowired
    private  CategoryRepository categoryRepository;
    @Autowired
    private  AddonRepository addonRepository;

    // get all categories
    public List<CategoryResponseDTO> getAllCategoryWithAddons(){
    List<Category> categories = categoryRepository.findAll();
    return  categories.stream().map(category -> this.mapToDTO(category) ).toList();
    }  // end of get all categories

    public CategoryResponseDTO createCategoryWithAddons(CategoryRequestDTO categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.name());
        category.setImageUrl(categoryRequest.imageUrl());

        if (categoryRequest.addonList()!= null && !categoryRequest.addonList().isEmpty()) {
            // Fetch all addons by the provided IDs
            List<Addon> addons = addonRepository.findAllById(categoryRequest.addonList());

            // Set the relationship
            category.setAddons(addons);

            // Important: In a bidirectional relationship, you usually update both sides
            // though for ManyToMany, updating the owning side (Category) is enough for the DB
            for (Addon addon : addons) {
                addon.getCategories().add(category);
            }
        }

//        List<Addon> addons = new ArrayList<>();
//
//        if (categoryRequest.addonList() != null) {
//
//            for (AddonResponse addonDTO : categoryRequest.addonList()) {
//
//                Addon addon;
//
//                // ✅ لو فيه id → هاته من DB
//                if (addonDTO.getId() != null) {
//                    addon = addonRepository.findById(addonDTO.getId())
//                            .orElseThrow(() -> new RuntimeException("Addon not"+addonDTO.getName()+" found"));
//                }else{
//                    addon = new Addon();
//                    addon.setName(addonDTO.getName());
//                    addon.setPrice(addonDTO.getPrice());
//
//                }

             //   addons.add(addon);


                // maintain bidirectional relation
              //  addon.getCategories().add(category);
          //  }
      //  }

       // category.setAddons(addons);

        return mapToDTO(categoryRepository.save(category));

       // return  mapToDTO(categoryRepository.save(category));
    } // end of createCategoryWithAddons

    // update category addon method
    public CategoryResponseDTO updateCategoryWithAddons(  Long categoryId , CategoryRequestDTO categoryRequest) {
        Category category =  categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
//                Optional<Category> category =  categoryRepository.findById(categoryId);
//      if( !category.isPresent() )
//      {
//          throw new RuntimeException("Category not  found");
//      }

        category.setName(categoryRequest.name());
        category.setImageUrl(categoryRequest.imageUrl());
        List<Addon> addons = new ArrayList<>();

        if (categoryRequest.addonList() != null) {

            for (Long addonID : categoryRequest.addonList()) {

                Addon addon;

                    addon = addonRepository.findById(addonID)
                            .orElseThrow(() -> new RuntimeException("Addon not found"));
                    addons.add(addon);
                    // maintain bidirectional relation
                    addon.getCategories().add(category);


            }
        }

        category.setAddons(addons);

        return mapToDTO(categoryRepository.save(category));

        // return  mapToDTO(categoryRepository.save(category));
    } // end of updateCategoryWithAddons
    // get category by id
    public CategoryResponseDTO  getCategoryWithAddons(Long categoryId){
      //  log.info("categoryId : {}" , categoryId);
        Category category =  categoryRepository.findByIdWithAddons(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
       // log.info("category : {}" , category);
       // CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();

        return mapToDTO(category);

    }

    // delete category
    //@Transactional  // because lazy in category entity
    public  void deleteCategory(Long categoryId) throws IOException {
    Category category = this.categoryRepository.findByIdWithAddons(categoryId).orElseThrow(()-> new RuntimeException("Category not found"));
        if(category.getIsDeleted() == true){
            throw new RuntimeException("This Category Already deleted");
        }
long productCount = category.getProducts().stream().count();
       // log.info("category products{} ", productCount);
        if(productCount > 0){
            throw new RuntimeException("Should not delete Category Contain Products ");
        }
        category.setIsDeleted(true);
    this.categoryRepository.save(category);

} // end of deleted category

    // convert category to dto
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
        ,category.getIsDeleted());
    }
}
