package org.devgateway.toolkit.web.fm;

import org.danekja.java.util.function.serializable.SerializableBooleanSupplier;
import org.devgateway.toolkit.web.fm.service.DgFmService;

public interface DgFmSubject {

    default Boolean isFmEnabled() {
        return isFmAttached() ? getFmService().isFeatureEnabled(getFmName()) : FmConstants.DEFAULT_ENABLED;
    }

    void setFmName(String fmName);
    String getFmName();

    default boolean isFmAttached() {
        return getFmName() != null;
    }

    DgFmService getFmService();

    default void attachFm(DgFmService fmService, String fmName) {
        if (!getFmService().hasFeature(fmName)) {
           throw new RuntimeException(String.format("Nonexistent feature %s cannot be attached", fmName));
        }
        setFmName(fmName);
    }

    default void attachFm(String fmName) {
        attachFm(getFmService(), fmName);
    }

    default boolean isFmEnabled(SerializableBooleanSupplier enabledSupplier) {
        return enabledSupplier.getAsBoolean() && isFmEnabled();
    }
}
