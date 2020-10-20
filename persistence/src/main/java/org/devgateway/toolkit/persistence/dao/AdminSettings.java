package org.devgateway.toolkit.persistence.dao;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author idobre
 * @since 6/22/16
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AdminSettings extends AbstractAuditableEntity {
    private static final long serialVersionUID = -1051140524022133178L;

    public static final Duration REBOOT_ALERT_DURATION = Duration.ofMinutes(10L);

    private Boolean rebootServer = false;

    private LocalDateTime rebootAlertSince;

    @Override
    public AbstractAuditableEntity getParent() {
        return null;
    }

    public Boolean getRebootServer() {
        return rebootServer;
    }

    public void setRebootServer(final Boolean rebootServer) {
        this.rebootServer = rebootServer;
    }

    public boolean isRebootServer() {
        return Boolean.TRUE.equals(getRebootServer());
    }

    public LocalDateTime getRebootAlertSince() {
        return rebootAlertSince;
    }

    public void setRebootAlertSince(final LocalDateTime rebootAlertSince) {
        this.rebootAlertSince = rebootAlertSince;
    }
}
