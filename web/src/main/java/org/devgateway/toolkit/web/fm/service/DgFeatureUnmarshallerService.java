package org.devgateway.toolkit.web.fm.service;

import org.devgateway.toolkit.web.fm.entity.UnchainedDgFeature;

import java.util.List;

public interface DgFeatureUnmarshallerService {
    List<UnchainedDgFeature> unmarshall(String resourceLocation);
    List<String> getResources();
}
