package org.devgateway.toolkit.persistence.repository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation helps to guarantee that once query result caching is used,
 * then all methods across the repo hierarchy explicitly use the caching or not.
 *
 * Done with:
 * 1. Checkstyle CachableQueryAnnotationCheck: validates that such class methods
 * explicitly declare @CacheHibernateQueryResult or @NoCacheHibernateQueryResult.
 *
 * 2. ValidateCacheableHibernateQueryResult: checks at app startup that
 * all subclasses of a class annotated with @CacheableHibernateQueryResult
 * are also annotated with @CacheableHibernateQueryResult.
 * This will guarantee the checkstyle validation for the entire hierarchy.
 */
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheableHibernateQueryResult {
}
