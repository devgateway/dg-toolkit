package org.devgateway.toolkit.forms.wicket.page.edit;


import org.apache.commons.lang3.BooleanUtils;
import org.devgateway.toolkit.forms.security.SecurityUtil;
import org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.Person;

public interface ResourceLockable {

    AbstractStatusAuditableEntity getLockableResource();

    default boolean checkoutResource() {
        if (!SecurityUtil.isCurrentUserAdmin() && isResourceCheckedOut() && !isCurrentUserLockOwner()) {
            return false;
        }
        getLockableResource().setCheckedOutUser(SecurityUtil.getCurrentAuthenticatedPerson());
        return true;
    }

    default boolean canEditLockableResource() {
        return SecurityUtil.isCurrentUserAdmin() || getLockableResource().getCheckedOutUser() == null
                || isCurrentUserLockOwner();
    }

    default String getCheckedOutUsername() {
        if (getLockableResource().getCheckedOutUser() == null) {
            return null;
        }
        return getLockableResource().getCheckedOutUser().getUsername();
    }

    default boolean beforeSaveLockableResource() {
        if (BooleanUtils.isTrue(getLockableResource().getRemoveLock())) {
            return checkinResource();
        }
        if (!DBConstants.Status.DRAFT.equals(getLockableResource().getStatus())) {
            return checkinResource();
        }
        if (DBConstants.Status.DRAFT.equals(getLockableResource().getStatus()) && !isCurrentUserLockOwner()) {
            return checkoutResource();
        }
        return false;
    }

    default boolean isCurrentUserLockOwner() {
        Person currentAuthenticatedPerson = SecurityUtil.getCurrentAuthenticatedPerson();
        Person checkedOutUser = getLockableResource().getCheckedOutUser();
        return checkedOutUser != null && checkedOutUser.equals(currentAuthenticatedPerson);
    }

    default boolean isResourceCheckedOut() {
        return getLockableResource().getCheckedOutUser() != null;
    }

    default boolean checkinResource() {
        if (!isResourceCheckedOut()) {
            return true;
        } else {
            if (isCurrentUserLockOwner() || SecurityUtil.isCurrentUserAdmin()) {
                getLockableResource().setCheckedOutUser(null);
                return true;
            }
            return false;
        }
    }
}
