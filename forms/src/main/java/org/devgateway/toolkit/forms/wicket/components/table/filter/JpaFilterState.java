package org.devgateway.toolkit.forms.wicket.components.table.filter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity;
import org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity_;
import org.devgateway.toolkit.persistence.repository.SpecificationContext;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Octavian on 01.07.2016.
 */
public class JpaFilterState<T> implements Serializable {
    protected String status;

    @JsonIgnore
    public Specification<T> getSpecification() {
        return (root, query, cb) -> cb.and();
    }

    protected <T extends AbstractStatusAuditableEntity> void addStatus(
            List<Predicate> predicates, SpecificationContext<T> sc) {
        if (StringUtils.isNotBlank(status)) {
            predicates.add(sc.cb().like(sc.root().get(AbstractStatusAuditableEntity_.status), status));
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }
}
