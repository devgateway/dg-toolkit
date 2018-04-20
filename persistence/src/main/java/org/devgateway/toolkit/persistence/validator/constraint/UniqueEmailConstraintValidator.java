package org.devgateway.toolkit.persistence.validator.constraint;

import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.repository.PersonRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailConstraintValidator implements ConstraintValidator<UniqueEmail, Person> {

    private PersonRepository personRepository;

    UniqueEmailConstraintValidator(final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void initialize(final UniqueEmail constraint) {
    }

    public boolean isValid(final Person user, final ConstraintValidatorContext context) {
        Person person = personRepository.findByEmail(user.getEmail());
        return person == null || person.getId().equals(user.getId());
    }

}