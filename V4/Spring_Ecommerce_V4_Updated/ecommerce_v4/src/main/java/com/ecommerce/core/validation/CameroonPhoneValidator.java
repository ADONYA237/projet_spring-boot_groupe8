package com.ecommerce.core.validation;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CameroonPhoneValidator implements ConstraintValidator<CameroonPhone, String> {

    private final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        try {
            Phonenumber.PhoneNumber cameroonNumber = phoneUtil.parse(value, "CM");
            return phoneUtil.isValidNumber(cameroonNumber) && "CM".equals(phoneUtil.getRegionCodeForNumber(cameroonNumber));
        } catch (Exception e) {
            return false;
        }
    }
}
