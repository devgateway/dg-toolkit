package org.devgateway.toolkit.forms.fm;

import org.danekja.java.util.function.serializable.SerializableBooleanSupplier;
import org.devgateway.toolkit.web.fm.DgFmInputSubject;

public interface DgFmFormComponentSubject extends DgFmInputSubject {

    default boolean isFmMandatory(SerializableBooleanSupplier requiredSupplier) {
        return requiredSupplier.getAsBoolean() && isFmMandatory();
    }
}
