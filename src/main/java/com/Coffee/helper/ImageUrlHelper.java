package com.Coffee.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public final class ImageUrlHelper {
    public static String buildImageUrl(String relativePath) {
        if (relativePath == null) return null;

        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/images/")
                .path(relativePath)
                .toUriString();
    }
}
