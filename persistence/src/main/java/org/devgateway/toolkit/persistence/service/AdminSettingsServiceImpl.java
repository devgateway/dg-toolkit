package org.devgateway.toolkit.persistence.service;

import org.devgateway.toolkit.persistence.dao.AdminSettings;
import org.devgateway.toolkit.persistence.repository.AdminSettingsRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author idobre
 * @since 2019-03-04
 */
@Service
@Transactional(readOnly = true)
public class AdminSettingsServiceImpl extends BaseJpaServiceImpl<AdminSettings> implements AdminSettingsService {
    @Autowired
    private AdminSettingsRepository adminSettingsRepository;

    @Override
    protected BaseJpaRepository<AdminSettings, Long> repository() {
        return adminSettingsRepository;
    }

    @Override
    public AdminSettings newInstance() {
        return new AdminSettings();
    }

    @Override
    @Transactional
    public <S extends AdminSettings> S save(final S entity) {
        preProcessRebootAlert(entity);
        return repository().save(entity);
    }

    @Override
    @Transactional
    public <S extends AdminSettings> S saveAndFlush(final S entity) {
        preProcessRebootAlert(entity);
        return repository().saveAndFlush(entity);
    }

    private <S extends AdminSettings> void preProcessRebootAlert(final S as) {
        if (!as.isRebootServer()) {
            as.setRebootAlertSince(null);
        } else if (as.getRebootAlertSince() == null) {
            as.setRebootAlertSince(LocalDateTime.now());
        }
    }

    @Override
    public AdminSettings get() {
        List<AdminSettings> entries = repository().findAll();
        return entries.isEmpty() ? null :  entries.get(0);
    }

    private AdminSettings getOrDefault() {
        List<AdminSettings> entries = repository().findAll();
        return entries.isEmpty() ? new AdminSettings() :  entries.get(0);
    }

    @Override
    public Integer getAutosaveTime() {
        return getOrDefault().getAutosaveTime();
    }

}
