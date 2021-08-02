package org.devgateway.toolkit.forms.wicket.converters;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.devgateway.toolkit.persistence.PersistenceConstants.DATE_PATTERN;

/**
 * @author Nadejda Mandrescu
 */
public class LocalDateFormatter implements IConverter<LocalDate> {

    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(DATE_PATTERN);

    @Override
    public LocalDate convertToObject(final String value, final Locale locale) throws ConversionException {
        throw new ConversionException("Not supported");
    }

    @Override
    public String convertToString(final LocalDate value, final Locale locale) {
        return value == null ? "" : dateFormat.format(value);
    }

}
