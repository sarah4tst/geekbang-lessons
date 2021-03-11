package org.geektimes.projects.user.validator.bean.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidAnnotationValidator.class)
public @interface PhoneValid {
  String message() default "doesn't seem to be a valid phone number";

  String pattern() default PhoneValidAnnotationValidator.ChinaPhoneNoPatternString;

  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
