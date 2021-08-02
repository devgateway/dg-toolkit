package org.devgateway.toolkit.forms.wicket.converters;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.devgateway.toolkit.persistence.PersistenceConstants.ZONED_DATE_TIME_PATTER;

/**
 * @author Nadejda Mandrescu
 */
public class ZonedDateTimeFormatter implements IConverter<ZonedDateTime> {

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ZONED_DATE_TIME_PATTER);

    @Override
    public ZonedDateTime convertToObject(final String value, final Locale locale) throws ConversionException {
        throw new ConversionException("Not supported");
    }

    @Override
    public String convertToString(final ZonedDateTime value, final Locale locale) {
        return value == null ? "" : dateTimeFormatter.format(value);
    }

}
