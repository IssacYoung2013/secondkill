package com.issac.seckill.validator;

import com.issac.seckill.util.ValidatorUtil;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * author:  ywy
 * date:    2019-01-17
 * desc:
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile,String>{

    private boolean required = false;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(required) {
            if(StringUtils.isEmpty(value)) {
                return false;
            }
        } else {
            if(StringUtils.isEmpty(value)) {
                return true;
            }
        }

        return ValidatorUtil.isMobile(value);
    }

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }
}
