package org.devgateway.toolkit.persistence.validator.constraint;

import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.repository.PersonRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class UniqueUsernameConstraintValidator implements ConstraintValidator<UniqueUsername, Person> {

    private PersonRepository personRepository;

    UniqueUsernameConstraintValidator(final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void initialize(final UniqueUsername constraint) {
    }

    public boolean isValid(final Person user, final ConstraintValidatorContext context) {
        List<Person> persons = personRepository.findByName(user.getUsername());
        for (Person person : persons) {
            if (person != null && !person.getId().equals(user.getId())) {
                return false;
            }
        }
        return true;

    }

}