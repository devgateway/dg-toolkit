package org.devgateway.toolkit.persistence.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * @author Nadejda Mandrescu
 */
public class SpecificationContext<T> {

    private final Root<T> root;

    private final CriteriaQuery<?> criteriaQuery;

    private final CriteriaBuilder criteriaBuilder;

    public SpecificationContext(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        this.root = root;
        this.criteriaQuery = criteriaQuery;
        this.criteriaBuilder = criteriaBuilder;
    }

    public Root<T> root() {
        return root;
    }

    public CriteriaQuery<?> cq() {
        return criteriaQuery;
    }

    public CriteriaBuilder cb() {
        return criteriaBuilder;
    }
}
