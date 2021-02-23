package org.devgateway.toolkit.persistence.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author mihai
 * <p>
 * Assigned to objects that provide a status, in our case, objects derived from
 * {@link AbstractStatusAuditableEntity}
 */
public interface Statusable {

    String getStatus();

}
