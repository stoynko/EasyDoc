package com.github.stoynko.easydoc.media.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.github.stoynko.easydoc.media.dto.CloudinaryUploadResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryUploadResult uploadPhoto(MultipartFile multipartFile, String folder) throws IOException {

        Map<String, Object> result = cloudinary.uploader().upload(multipartFile.getBytes(),
                                                        ObjectUtils.asMap(
                                                "public_id", UUID.randomUUID().toString(),
                                                        "folder", folder,
                                                        "overwrite", true,
                                                        "invalidate", true,
                                                        "resource_type", "image"
        ));

        String secureUrl = (String) result.getOrDefault("secure_url", result.get("url"));
        String publicId = (String) result.get("public_id");
        log.info("[profile-photo-deleted] Photo with id {} was successfully uploaded", publicId);
        return new CloudinaryUploadResult(publicId, secureUrl);
    }

    public void deletePhoto(String profilePhotoPublicId) {

        if (profilePhotoPublicId == null || profilePhotoPublicId.isBlank()) {
            return;
        }

        try {
            cloudinary.uploader().destroy(profilePhotoPublicId, ObjectUtils.asMap(
                    "invalidate", true,
                    "resource_type", "image"
            ));
            log.info("[profile-photo-deleted] Photo with id {} was successfully deleted", profilePhotoPublicId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
