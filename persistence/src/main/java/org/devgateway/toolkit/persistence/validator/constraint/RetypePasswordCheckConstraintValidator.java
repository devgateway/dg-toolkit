package org.devgateway.toolkit.persistence.validator.constraint;

import org.devgateway.toolkit.persistence.dao.Person;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RetypePasswordCheckConstraintValidator implements ConstraintValidator<RetypePasswordCheck, Person> {

    @Override
    public void initialize(final RetypePasswordCheck constraint) {
    }

    public boolean isValid(final Person value, final ConstraintValidatorContext context) {
        if (!value.getChangePass()) {
            return true;
        }
        return value.getPlainPassword().equals(value.getPlainPasswordCheck());
    }

}