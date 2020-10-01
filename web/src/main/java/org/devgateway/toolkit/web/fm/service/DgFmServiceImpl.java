package org.devgateway.toolkit.web.fm.service;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.beanutils.BeanUtils;
import org.devgateway.toolkit.web.fm.entity.DgFeature;
import org.devgateway.toolkit.web.fm.entity.UnchainedDgFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class DgFmServiceImpl implements DgFmService {

    private static final Logger logger = LoggerFactory.getLogger(DgFmServiceImpl.class);

    @Autowired
    private DgFeatureUnmarshallerService featureUnmarshallerService;

    @Autowired
    private DgFeatureMarshallerService dgFeatureMarshallerService;

    private Map<String, UnchainedDgFeature> unchainedFeatures;

    private Map<String, DgFeature> features;

    private Set<String> featuresInUse;

    @Value("${fm.emitProjected:#{false}}")
    private Boolean emitProjectedFm;

    @Value("${fm.printFeaturesInUseOnExit:#{false}}")
    private Boolean printFeaturesInUseOnExit;


    @PostConstruct
    public void init() {
        featuresInUse = ConcurrentHashMap.newKeySet();
        hydrateUnchainedFeatures();
        Map<String, DgFeature> mutableChainedFeatures = new ConcurrentHashMap<>();
        chainFeatures(mutableChainedFeatures);
        projectProperties(mutableChainedFeatures);
        features = ImmutableMap.copyOf(mutableChainedFeatures);
        if (emitProjectedFm) {
            dgFeatureMarshallerService.marshall("_projectedFm.yml", features.values());
        }
    }

    @PreDestroy
    public void decommission() {
        if (printFeaturesInUseOnExit) {
            logger.info(String.format("FM: Features that have been used during this session: %s ",
                    featuresInUse.toString()));
        }
    }

    @Override
    public DgFeature getFeature(String featureName) {
        logger.debug(String.format("FM: Querying feature %s", featureName));
        DgFeature dgFeature = features.get(featureName);
        if (dgFeature == null) {
            throw new RuntimeException(String.format("Unknown feature with name %s", featureName));
        }
        featuresInUse.add(featureName);
        return dgFeature;
    }

    @Override
    public Boolean hasFeature(String featureName) {
        logger.debug(String.format("FM: Checking existence of feature %s", featureName));
        return features.containsKey(featureName);
    }

    @Override
    public Boolean isFeatureEnabled(String featureName) {
        return getFeature(featureName).getEnabled();
    }

    @Override
    public Boolean isFeatureMandatory(String featureName) {
        return getFeature(featureName).getMandatory();
    }

    private void projectProperties(Map<String, DgFeature> features) {
        logger.debug("FM: Projecting properties for all features");
        features.values().forEach(this::projectProperties);
        logger.debug("FM: Projected properties for all features");
    }

    private void projectProperties(DgFeature feature) {
        logger.debug(String.format("FM: Projecting properties for feature %s", feature));
        projectChainedMixins(feature);
        projectChainedHardDeps(feature);
        projectChainedSoftDeps(feature);
        projectEnabled(feature);
        projectMandatory(feature);
        logger.debug(String.format("FM: Projected properties for feature %s", feature));
    }

    private void projectChainedMixins(DgFeature feature) {
        logger.debug(String.format("FM: Projecting chained mixins for feature %s", feature));
        feature.getChainedMixins().addAll(getHierarchyChainedMixins(feature));
        logger.debug(String.format("FM: projected chained mixins for feature %s", feature));
    }

    private void projectEnabled(DgFeature feature) {
        logger.debug(String.format("FM: Projecting enabled for feature %s", feature));
        feature.setEnabled(feature.getEnabled() && feature
                .getChainedMixins().stream().map(DgFeature::getEnabled).reduce(Boolean::logicalAnd)
                .orElse(true));
        logger.debug(String.format("FM: Projected enabled for feature %s", feature));
    }

    private void projectMandatory(DgFeature feature) {
        logger.debug(String.format("FM: Projecting mandatory for feature %s", feature));
        feature.setMandatory(feature.getMandatory() && feature
                .getChainedMixins().stream().map(DgFeature::getMandatory).reduce(Boolean::logicalAnd)
                .orElse(true));
        logger.debug(String.format("FM: Projected mandatory for feature %s", feature));
    }

    private void projectChainedHardDeps(DgFeature feature) {
        logger.debug(String.format("FM: Projecting chained hard deps for feature %s", feature));
        feature.getChainedHardDeps().addAll(getHierarchyChainedHardDeps(feature));
        logger.debug(String.format("FM: Projected chained hard deps for feature %s", feature));
    }

    private void projectChainedSoftDeps(DgFeature feature) {
        logger.debug(String.format("FM: Projecting chained soft deps for feature %s", feature));
        feature.getChainedSoftDeps().addAll(getHierarchyChainedSoftDeps(feature));
        logger.debug(String.format("FM: Projected chained soft deps for feature %s", feature));
    }

    private Set<DgFeature> getHierarchyChainedMixins(DgFeature feature) {
        return feature.getChainedMixins().stream().flatMap(f -> getHierarchyChainedMixins(f).stream())
                .collect(Collectors.toSet());
    }

    public Set<DgFeature> getHierarchyChainedHardDeps(DgFeature feature) {
        return feature.getChainedHardDeps().stream().flatMap(f -> getHierarchyChainedHardDeps(f).stream())
                .collect(Collectors.toSet());
    }

    private Set<DgFeature> getHierarchyChainedSoftDeps(DgFeature feature) {
        return feature.getChainedSoftDeps().stream().flatMap(f -> getHierarchyChainedSoftDeps(f).stream())
                .collect(Collectors.toSet());
    }

    private void hydrateUnchainedFeatures() {
        logger.debug("FM: Hydrating unchained features");
        Map<String, UnchainedDgFeature> mutableUnchainedFeatures = new ConcurrentHashMap<>();
        featureUnmarshallerService.getResources().stream()
                .map(featureUnmarshallerService::unmarshall).flatMap(Collection::stream).forEach(f -> {
            UnchainedDgFeature existingFeature = mutableUnchainedFeatures.get(f.getName());
            if (!Objects.isNull(existingFeature)) {
                throw new RuntimeException(String.format("Feature %s from resource %s cannot be loaded. Another "
                                + "feature with same name was previously loaded from resource %s", f.getName(),
                        f.getResourceLocation(), existingFeature.getResourceLocation()));
            }
            mutableUnchainedFeatures.put(f.getName(), f);
        });
        unchainedFeatures = ImmutableMap.copyOf(mutableUnchainedFeatures);
        logger.debug("FM: Hydrated unchained features");
    }

    private void chainReferences(Map<String, DgFeature> features,
                                 String fName, Supplier<Set<String>> referenceSupplier,
                                 Consumer<DgFeature> referenceConsumer, String refName) {
        logger.debug(String.format("FM: Chaining references for field %s and reference %s", fName, refName));
        referenceSupplier.get().stream().map(r -> {
            UnchainedDgFeature dgFeature = unchainedFeatures.get(r);
            if (Objects.isNull(dgFeature)) {
                throw new RuntimeException(String.format("Unknown %s %s in feature %s", refName, r, fName));
            }
            return dgFeature;
        }).map(f-> chainFeature(features, f)).forEach(referenceConsumer);
        logger.debug(String.format("FM: Chained references for field %s and reference %s", fName, refName));
    }

    private void chainFeatures(Map<String, DgFeature> features) {
        logger.debug("FM: Chaining features");
        unchainedFeatures.values().forEach(f->chainFeature(features, f));
        logger.debug("FM: Chained features");
    }

    private DgFeature chainFeature(Map<String, DgFeature> features, UnchainedDgFeature unchained) {
        logger.debug(String.format("FM: Chaining feature %s", unchained));
        if (features.containsKey(unchained.getName())) {
            return features.get(unchained.getName());
        }
        DgFeature chained = new DgFeature();
        try {
            BeanUtils.copyProperties(chained, unchained);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        chainReferences(features, chained);
        features.put(chained.getName(), chained);
        logger.debug(String.format("FM: Chained feature %s", unchained));
        return chained;
    }

    private void chainReferences(Map<String, DgFeature> features, DgFeature f) {
        logger.debug(String.format("FM: Chaining references for feature %s", f));
        chainReferences(features, f.getName(), f::getHardDeps, f.getChainedHardDeps()::add, "hard deps");
        chainReferences(features, f.getName(), f::getSoftDeps, f.getChainedSoftDeps()::add, "soft deps");
        chainReferences(features, f.getName(), f::getMixins, f.getChainedMixins()::add, "mixins");
        logger.debug(String.format("FM: Chained references for feature %s", f));
    }

}