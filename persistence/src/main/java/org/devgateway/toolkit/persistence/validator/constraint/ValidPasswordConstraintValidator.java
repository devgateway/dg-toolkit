package org.devgateway.toolkit.persistence.validator.constraint;

import org.devgateway.toolkit.persistence.dao.Person;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ValidPasswordConstraintValidator implements ConstraintValidator<ValidPassword, Person> {

    private final Pattern content = Pattern.compile("((?=.*\\d)(?=.*[a-z]).{10,20})");

    @Override
    public void initialize(final ValidPassword constraint) {
    }

    public boolean isValid(final Person value, final ConstraintValidatorContext context) {
        if (!value.getChangePass()) {
            return true;
        }
        return content.matcher(value.getPlainPassword()).matches();
    }

}