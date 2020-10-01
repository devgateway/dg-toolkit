package org.devgateway.toolkit.web.fm.service;

import org.devgateway.toolkit.web.fm.entity.DgFeature;

public interface DgFmService {
    DgFeature getFeature(String featureName);
    Boolean hasFeature(String featureName);
    Boolean isFeatureEnabled(String featureName);
    Boolean isFeatureMandatory(String featureName);
}
