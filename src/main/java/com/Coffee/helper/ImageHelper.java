package com.Coffee.helper;

import com.Coffee.service.ImageService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Getter
public class ImageHelper {
    private final ImageService imageService;

    private final String defaultImage;

    public ImageHelper(ImageService imageService, @Value("${app.default.image}") String defaultImage) {
        this.imageService = imageService;
        this.defaultImage = defaultImage;
    }

    public String getImageOrDefault(MultipartFile imageFile, String folder) throws IOException {
        if (imageFile == null || imageFile.isEmpty()) {
            return defaultImage;
        }
        return this.imageService.saveImageToStorage(folder, imageFile);
    }

    public  void deleteImage(String image) throws IOException {
        if(image!=null && !image.equals(defaultImage)){
            this.imageService.deleteImage(image);
        }
    }
}
