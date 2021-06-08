package org.devgateway.toolkit.persistence.converter;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author Nadejda Mandrescu
 */
public interface INumberFormatter {

    NumberFormat get(final Locale locale);
}
