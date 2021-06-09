package org.devgateway.toolkit.forms.wicket.converters;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import java.time.Month;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.util.Locale;

import static java.time.temporal.ChronoField.MONTH_OF_YEAR;

/**
 * @author Octavian Ciubotaru
 */
public class MonthConverter implements IConverter<Month> {

    private static final TextStyle STYLE = TextStyle.FULL;

    @Override
    public Month convertToObject(String value, Locale locale) throws ConversionException {
        return new DateTimeFormatterBuilder()
                .appendText(MONTH_OF_YEAR, STYLE)
                .toFormatter(locale)
                .parse(value, Month::from);
    }

    @Override
    public String convertToString(Month value, Locale locale) {
        return value.getDisplayName(STYLE, locale);
    }
}
