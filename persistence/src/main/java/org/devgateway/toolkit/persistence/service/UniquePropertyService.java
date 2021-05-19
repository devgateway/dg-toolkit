package org.devgateway.toolkit.persistence.service;

import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.repository.norepository.UniquePropertyRepository;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Nadejda Mandrescu
 */
public interface UniquePropertyService<T extends GenericPersistable & Serializable> {
    UniquePropertyRepository<T, Long> uniquePropertyRepository();

    default T findByProperty(final String propertyName, final Object propertyValue, final Collection<Long> ignoreIds) {
        final Specification<T> spec =
                uniquePropertyRepository().getFindByPropertySpecification(propertyName, propertyValue, ignoreIds);

        return uniquePropertyRepository().findOne(spec).orElse(null);
    }

    default boolean existsByProperty(final String propertyName, final Object propertyValue,
            final Collection<Long> ignoreIds) {
        final Specification<T> spec =
                uniquePropertyRepository().getFindByPropertySpecification(propertyName, propertyValue, ignoreIds);

        return uniquePropertyRepository().count(spec) > 0;
    }

}
