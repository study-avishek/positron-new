package com.increff.pos.util;

import com.increff.pos.exception.ApiException;
import com.increff.pos.exception.UploadException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.isGetter;
import static org.springframework.beans.PropertyAccessorUtils.getPropertyName;

public class ValidationUtil {
    public static <T> Set<ConstraintViolation<T>> validate(T form) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(form);
    }

    public static <T> void checkValid(T obj) throws ApiException {
        Set<ConstraintViolation<T>> violations = validate(obj);
        if (violations.isEmpty()) {
            return;
        }
        List<String> errorList = new ArrayList<String>(violations.size());
        for (ConstraintViolation<T> violation : violations) {
            errorList.add(violation.getMessage());
        }
        throw new ApiException("Invalid entry", errorList);
    }

    public static <T> void checkValidMultiple(List<T> obj) throws UploadException{
        List<List<String>> error = new ArrayList<>();

        int i = 1;
        for(Object temp : obj) {
            Set<ConstraintViolation<T>> violations = validate((T) temp);
            for (ConstraintViolation<T> violation : violations) {
                error.add(new ArrayList<>());

                String value = Objects.equals(String.valueOf(violation.getInvalidValue()), "") ? "empty" : String.valueOf(violation.getInvalidValue());
                error.get(error.size() - 1).add(String.valueOf(i));
                error.get(error.size() - 1).add(value);
                error.get(error.size() - 1).add(violation.getMessage());
            }
            i++;
        }

        if(error.size() == 0) return;

        throw new UploadException("Input contains invalid data",error);
    }

    public static <T> void checkValidMultiple(List<T> obj, List<List<String>> error) throws UploadException{
        int i = 1;
        for(Object temp : obj) {
            Set<ConstraintViolation<T>> violations = validate((T) temp);
            for (ConstraintViolation<T> violation : violations) {
                error.add(new ArrayList<>());

                String value = Objects.equals(String.valueOf(violation.getInvalidValue()), "") ? "empty" : String.valueOf(violation.getInvalidValue());
                error.get(error.size() - 1).add(String.valueOf(i));
                error.get(error.size() - 1).add(value);
                error.get(error.size() - 1).add(violation.getMessage());
            }
            i++;
        }
    }

}
