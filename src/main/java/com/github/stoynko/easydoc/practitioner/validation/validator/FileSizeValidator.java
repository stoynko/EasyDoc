package com.github.stoynko.easydoc.practitioner.validation.validator;

import com.github.stoynko.easydoc.practitioner.validation.annotation.MaxFileSize;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileSizeValidator implements ConstraintValidator<MaxFileSize, MultipartFile> {

    private long maxBytes;

    @Override
    public void initialize(MaxFileSize constraintAnnotation) {
        this.maxBytes = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return true;
        }
        return multipartFile.getSize() <= maxBytes;
    }
}
