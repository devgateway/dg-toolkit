package org.devgateway.toolkit.persistence.checks;

import org.devgateway.toolkit.persistence.repository.CacheableHibernateQueryResult;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Nadejda Mandrescu
 */
@Component
public class ValidateCacheableHibernateQueryResult {
    private static final String SCAN_PACKAGE = "org.devgateway";
    private static final String ERROR_MESSAGE = "Classes inherited from @CacheableHibernateQueryResult base class "
            + "must also be annotated with @CacheableHibernateQueryResult: ";

    private Set<String> validated = new HashSet<>();
    private Set<String> invalidClasses = new HashSet<>();

    @PostConstruct
    public void validate() {
        Reflections reflections = new Reflections(SCAN_PACKAGE);
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(CacheableHibernateQueryResult.class);
        annotatedClasses.forEach(annotatedBaseClass -> validateClass(null, annotatedBaseClass, reflections));
        if (!invalidClasses.isEmpty()) {
            throw new RuntimeException(ERROR_MESSAGE + String.join(", ", invalidClasses));
        }
    }

    private void validateClass(final Class<?> parent, final Class<?> clazz, final Reflections reflections) {
        boolean notYetValidated = validated.add(clazz.getCanonicalName());
        if (notYetValidated) {
            if (parent != null) {
                CacheableHibernateQueryResult annotation = clazz.getAnnotation(CacheableHibernateQueryResult.class);
                if (annotation == null) {
                    invalidClasses.add(clazz.getCanonicalName());
                }
            }
            reflections.getSubTypesOf(clazz).forEach(subClass -> validateClass(clazz, subClass, reflections));
        }
    }
}
