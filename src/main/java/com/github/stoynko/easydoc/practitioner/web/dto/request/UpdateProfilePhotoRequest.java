package com.github.stoynko.easydoc.practitioner.web.dto.request;

import com.github.stoynko.easydoc.practitioner.validation.annotation.MaxFileSize;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@NoArgsConstructor
public class UpdateProfilePhotoRequest {

    @NotNull(message = "Please upload a profile photo")
    @MaxFileSize(value = 10 * 1024 * 1024, message = "Profile photo must be at most 10 MB")
    private MultipartFile fileUpload;
}



