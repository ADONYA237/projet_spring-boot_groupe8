package com.ecommerce.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CameroonPhoneValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CameroonPhone {
    String message() default "Numéro de téléphone camerounais invalide (+237 obligatoire)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
