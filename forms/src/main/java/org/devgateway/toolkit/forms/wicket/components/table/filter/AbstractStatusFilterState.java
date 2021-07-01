package org.devgateway.toolkit.forms.wicket.components.table.filter;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity;
import org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author Nadejda Mandrescu
 */
public class AbstractStatusFilterState<T extends AbstractStatusAuditableEntity> extends JpaFilterState<T> {

    protected String status;

    protected void addStatus(List<Predicate> predicates, Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (StringUtils.isNotBlank(status)) {
            predicates.add(cb.like(root.get(AbstractStatusAuditableEntity_.status), status));
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }
}
