package org.devgateway.toolkit.persistence.repository.norepository;

import org.devgateway.toolkit.persistence.repository.CacheHibernateQueryResult;
import org.devgateway.toolkit.persistence.repository.CacheableHibernateQueryResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * @author Octavian Ciubotaru
 */
@NoRepositoryBean
@CacheableHibernateQueryResult
public interface CacheableQueryResultRepository<T, ID extends Serializable> extends BaseJpaRepository<T, ID> {

    @Override
    @CacheHibernateQueryResult
    List<T> findAll();

    @Override
    @CacheHibernateQueryResult
    List<T> findAll(Sort sort);

    @Override
    @CacheHibernateQueryResult
    List<T> findAll(Specification<T> spec);

    @Override
    @CacheHibernateQueryResult
    Page<T> findAll(Specification<T> spec, Pageable pageable);

    @Override
    @CacheHibernateQueryResult
    Page<T> findAll(Pageable pageable);

    @Override
    @CacheHibernateQueryResult
    List<T> findAll(Specification<T> spec, Sort sort);

    @Override
    @CacheHibernateQueryResult
    Optional<T> findOne(@Nullable Specification<T> spec);

    @Override
    @CacheHibernateQueryResult
    long count(Specification<T> spec);

    @Override
    @CacheHibernateQueryResult
    long count();

    @Override
    @CacheHibernateQueryResult
    Optional<T> findById(ID id);

}
