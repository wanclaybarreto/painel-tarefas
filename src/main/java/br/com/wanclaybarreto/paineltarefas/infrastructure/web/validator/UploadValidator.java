package br.com.wanclaybarreto.paineltarefas.infrastructure.web.validator;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

import br.com.wanclaybarreto.paineltarefas.util.FileType;

public class UploadValidator implements ConstraintValidator<UploadConstraint, MultipartFile> {
	
	private List<FileType> acceptedFileTypes;
	
	@Override
	public void initialize(UploadConstraint constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
		acceptedFileTypes = Arrays.asList(constraintAnnotation.acceptedTypes());
	}
	
	@Override
	public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
		if (multipartFile == null || multipartFile.isEmpty()) {
			return true;
		}
		
		for (FileType fileType : acceptedFileTypes) {
			if (fileType.sameOf(multipartFile.getContentType())) {
				return true;
			}
		}
		
		return false;
	}
	
}
