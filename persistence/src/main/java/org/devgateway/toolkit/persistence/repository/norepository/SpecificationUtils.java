package org.devgateway.toolkit.persistence.repository.norepository;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author Nadejda Mandrescu
 */
public final class SpecificationUtils {
    private SpecificationUtils() {
    }

    public static Expression<String> ignoreCaseLikeValue(final CriteriaBuilder cb, final String value) {
        return cb.lower(cb.concat(cb.concat(cb.literal("%"), value), "%"));
    }

    public static Predicate equalIgnoreCaseValue(final Root<?> root, final CriteriaBuilder cb,
            final String propertyName, final String value) {
        return cb.equal(cb.lower(root.get(propertyName)), StringUtils.lowerCase(value));
    }

}
