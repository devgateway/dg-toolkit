package org.devgateway.toolkit.persistence;

import java.math.MathContext;

/**
 * @author Nadejda Mandrescu
 */
public final class PersistenceConstants {
    private PersistenceConstants() {
    }

    public static final MathContext MATH_CONTEXT = MathContext.DECIMAL64;

    public static final String DATE_PATTERN = "dd/MM/yyyy";
    public static final String DATE_PATTERN_REVISION = "dd/MM/yyyy";
    public static final String DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss";
    public static final String DATE_TIME_PATTERN_REVISION = "dd/MM/yyyy HH:mm:ss";
    public static final String ZONED_DATE_TIME_PATTER = "dd/MM/yyyy HH:mm:ss z";

}
