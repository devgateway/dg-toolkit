package org.devgateway.toolkit.forms.wicket.converters;

import org.apache.wicket.util.convert.converter.DoubleConverter;
import org.devgateway.toolkit.persistence.converter.INumberFormatter;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author Nadejda Mandrescu
 */
public class FormattedDoubleConverter extends DoubleConverter {

    private INumberFormatter numberFormatter;

    public FormattedDoubleConverter(final INumberFormatter numberFormatter) {
        this.numberFormatter = numberFormatter;
    }

    @Override
    protected NumberFormat newNumberFormat(final Locale locale) {
        return numberFormatter.get(locale);
    }

}
