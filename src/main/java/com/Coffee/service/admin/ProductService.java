package com.Coffee.service.admin;

import com.Coffee.dto.admin.request.ProductRequestDTO;
import com.Coffee.dto.admin.response.ProductResponseDTO;
import com.Coffee.helper.ImageHelper;
import com.Coffee.mapper.admin.AdminProductMapper;
import com.Coffee.model.Category;
import com.Coffee.model.Product;
import com.Coffee.model.ProductSize;
import com.Coffee.repository.user.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ProductService {
    @Autowired
    AdminProductMapper adminProductMapper;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    private ImageHelper imageHelper;
    // get all products
    public List<ProductResponseDTO> getAllProduct( ){

        return productRepository.findAll().stream().map(productEntity ->adminProductMapper.toResponse(productEntity)).toList();

    }

    // get one product
    public ProductResponseDTO getOneProduct( Long productId ){
Product product = productRepository.findById(productId).orElseThrow(  ()-> new RuntimeException("Product not Found"));
        log.info("product : {}", product.getImageUrl());
        ProductResponseDTO productDTO = adminProductMapper.toResponse(product);

       return productDTO;

    }

    // create product
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO){
        Product product = adminProductMapper.productDtoToEntity(productRequestDTO);
        if (product.getProductSizes() != null) {
            product.getProductSizes().forEach(size -> size.setProduct(product));
        }
        try {
            String imageName = this.imageHelper.getImageOrDefault(productRequestDTO.imageUrl(),"Products");
            product.setImageUrl(imageName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to process product image", e);
        }
        ProductResponseDTO productResponseDTO  = adminProductMapper.toResponse(productRepository.save(product)) ;

        return productResponseDTO;
    }

    // update product
    public ProductResponseDTO updateProduct(Long productId, ProductRequestDTO productRequestDTO) throws IOException {
        Product product = this.productRepository.findById(productId).orElseThrow(()-> new RuntimeException("Product not found"));
        product.setName(productRequestDTO.name());
        product.setDescription(productRequestDTO.description());
      //  product.setImageUrl(productRequestDTO.imageUrl());
        // convert dto to entity
       Category category = new Category();
        category.setId(productRequestDTO.category().id());
        category.setName(productRequestDTO.category().name());
        product.setCategory(category);
        if (productRequestDTO.imageUrl() != null    && !productRequestDTO.imageUrl().isEmpty()) {
            this.imageHelper.deleteImage(product.getImageUrl());
            String image = this.imageHelper.getImageOrDefault(productRequestDTO.imageUrl(),"Products");
            product.setImageUrl(image);
        }
       // product.setProductSizes(productRequestDTO.productSizes());
// 1. Clear the old sizes (orphanRemoval = true will delete them from DB)
        product.getProductSizes().clear();
        // 2. Add the new sizes and link them to the product
        if (productRequestDTO.productSizes() != null) {
            productRequestDTO.productSizes().forEach(productSizeDto -> {
                ProductSize productSize = new ProductSize();
                productSize.setProduct(product);

                productSize.setSize(String.valueOf( productSizeDto.size()));

                productSize.setPrice(productSizeDto.price() );
              //  size.setProduct(product); // IMPORTANT: This fixes the product_id = null issue
                product.getProductSizes().add(productSize);
            });
        }
      //  log.debug("products : {}",product);
        ProductResponseDTO productResponseDTO  = adminProductMapper.toResponse(productRepository.save(product)) ;
        return productResponseDTO;
    }// end of update method

    // delete product
    public  void deleteProduct(Long productId) throws IOException {
        Product product = this.productRepository.findById(productId).orElseThrow(()-> new RuntimeException("Category not found"));
        if(product.getIsDeleted() == true){
            throw new RuntimeException("This Product Already deleted");
        }
        product.setIsDeleted(true);
        this.productRepository.save(product);

    } // end of deleted category

}
