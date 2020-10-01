package org.devgateway.toolkit.web.fm;

public interface DgFmInputSubject extends DgFmSubject {

    default Boolean isFmMandatory() {
        return isFmAttached() ? getFmService().isFeatureMandatory(getFmName()) : FmConstants.DEFAULT_MANDATORY;
    }
}
