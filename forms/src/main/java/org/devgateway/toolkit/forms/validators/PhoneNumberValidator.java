package org.devgateway.toolkit.forms.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.PatternValidator;

import java.util.regex.Pattern;

public class PhoneNumberValidator extends PatternValidator {
    private static final long serialVersionUID = 1798142964159123629L;
    private static final String PATTERN_REGEX = "[0-9\\+\\-\\(\\)\\s]*";
    private static final Integer MAX_LENGTH = 50;

    public PhoneNumberValidator() {
        super(Pattern.compile(PATTERN_REGEX));
    }

    @Override
    public void validate(final IValidatable<String> validatable) {
        Pattern pattern = super.getPattern();
        if (validatable.getValue() != null && (validatable.getValue().length() > MAX_LENGTH
                || !pattern.matcher(validatable.getValue()).matches())) {
            ValidationError error = new ValidationError(this);
            error.addKey("pattern");
            validatable.error(error);
        }
    }
}
