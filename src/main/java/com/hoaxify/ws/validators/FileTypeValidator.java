package com.hoaxify.ws.validators;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import com.hoaxify.ws.annotations.FileType;
import com.hoaxify.ws.services.FileService;

public class FileTypeValidator implements ConstraintValidator<FileType, String>{

	@Autowired
	FileService fileService;
	
	String[] types;
	
	@Override
	public void initialize(FileType constraintAnnotation) {
		this.types = constraintAnnotation.types();	
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.isEmpty()) {
			return true;
		}
		String fileType = fileService.detectType(value);
		for(String supportedType : this.types) {
			if (fileType.contains(supportedType)) {
				return true;
			}
		}
		/* bu son 5 satır default olarak hata verirken örnek [jpeg, png] desteklenmiyor yazar client tarafında array şeklinde gözükmesi güzel olmuyor.
		bu yüzden sadece jpeg, png desteklenmiyor gibi arraysiz gözükmesi için yapıyoruz. */
		String supportedTypes = Arrays.stream(this.types).collect(Collectors.joining(", "));
		context.disableDefaultConstraintViolation();
		HibernateConstraintValidatorContext constraintValidatorContext = context.unwrap(HibernateConstraintValidatorContext.class);
		constraintValidatorContext.addMessageParameter("types", supportedTypes);
		constraintValidatorContext.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()).addConstraintViolation();
		
		
		return false;
		
	}

}
