package org.devgateway.toolkit.persistence.repository;

import org.devgateway.toolkit.persistence.dao.AdminSettings;
import org.devgateway.toolkit.persistence.repository.norepository.CacheableQueryResultRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 6/22/16
 */
@Transactional
@CacheableHibernateQueryResult
public interface AdminSettingsRepository extends CacheableQueryResultRepository<AdminSettings, Long> {

}
