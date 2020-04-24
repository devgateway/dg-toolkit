package org.devgateway.toolkit.forms.validators;

import java.time.LocalDate;

/**
 * @author Nadejda Mandrescu
 */
public class EarlierThanLocalDateValidator extends AbstractEarlierThanDateValidator<LocalDate> {
    private static final long serialVersionUID = 3649981314668993772L;

    public EarlierThanLocalDateValidator(LocalDate highDate) {
        super(highDate);
    }

    protected boolean isBefore(LocalDate highValue, LocalDate currentValue) {
        return highValue.isBefore(currentValue);
    }
}
