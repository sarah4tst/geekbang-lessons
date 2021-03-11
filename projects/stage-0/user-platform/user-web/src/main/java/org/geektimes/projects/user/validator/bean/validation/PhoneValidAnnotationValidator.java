package org.geektimes.projects.user.validator.bean.validation;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidAnnotationValidator implements ConstraintValidator<PhoneValid,String> {
  protected static final String ChinaPhoneNoPatternString = "^1\\d{10}$";
  private String patternString;

  @Override
  public void initialize(PhoneValid phoneValid) {
    patternString = phoneValid.pattern();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    return Pattern.compile(patternString).matcher(value).matches();
  }

}
