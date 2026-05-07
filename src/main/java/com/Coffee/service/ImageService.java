package com.Coffee.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {
    private String baseUrl;

    @Value("${app.upload.base-dir}")
    private String baseDir;

    // Allowed types
    private final List<String> allowedTypes = Arrays.asList("image/png", "image/jpeg", "image/jpg", "image/webp");

    // Max size 2MB
    private final long maxSize = 2 * 1024 * 1024;

    // Save image in a local directory
    public String saveImageToStorage(String uploadDirectory, MultipartFile imageFile) throws IOException {
        validateImage(imageFile);

        String uniqueFileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();

        Path uploadPath = Path.of(baseDir,uploadDirectory);
        Path filePath = uploadPath.resolve(uniqueFileName);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return uploadDirectory+"/"+uniqueFileName;
    }

    // validate the image
    private void validateImage(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new RuntimeException("No image provided");
        }

        if (!allowedTypes.contains(imageFile.getContentType())) {
            throw new RuntimeException("Invalid image type. Allowed: " + allowedTypes);
        }

        if (imageFile.getSize() > maxSize) {
            throw new RuntimeException("Image too large. Max size: 2MB");
        }
    }

    // Delete an image
    public String deleteImage(String imageName) throws IOException {
        Path imagePath = Path.of(baseDir, imageName);
        System.out.println(imagePath);

        if (Files.exists(imagePath)) {
            Files.delete(imagePath);
            return "Success";
        } else {
            return "Failed";
        }
    }
}
