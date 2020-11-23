package org.devgateway.toolkit.persistence.repository.norepository;

import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Nadejda Mandrescu
 */
@NoRepositoryBean
@Transactional
public interface UniquePropertyRepository<T extends GenericPersistable, ID extends Serializable>
        extends BaseJpaRepository<T, ID> {

    default Specification<T> getFindByPropertySpecification(final String propertyName, final Object propertyValue,
            final Collection<ID> ignoreIds) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Path<?> propertyPath = root.get(propertyName);
            if (String.class.isAssignableFrom(propertyPath.getJavaType())) {
                predicates.add(SpecificationUtils.equalIgnoreCaseValue(root, cb, propertyName, (String) propertyValue));
            } else {
                predicates.add(cb.equal(propertyPath, propertyValue));
            }
            predicates.add(root.get("id").in(ignoreIds).not());
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    };

}
