package org.devgateway.toolkit.persistence.validator.constraint;

import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.repository.PersonRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameConstraintValidator implements ConstraintValidator<UniqueUsername, Person> {

    private final PersonRepository personRepository;

    UniqueUsernameConstraintValidator(final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public boolean isValid(final Person user, final ConstraintValidatorContext context) {
        if (user == null) {
            return true;
        }
        Person person = personRepository.findByUsername(user.getUsername());
        return person == null || person.equals(user);
    }
}
