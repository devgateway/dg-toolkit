package org.devgateway.toolkit.forms.wicket.page.edit;


import org.devgateway.toolkit.forms.security.SecurityConstants;

public interface DefaultValidatorRoleAssignable extends EditorValidatorRoleAssignable {

    @Override
    default String getUserRole() {
        return SecurityConstants.Roles.ROLE_USER;
    }

    @Override
    default String getValidatorRole() {
        return SecurityConstants.Roles.ROLE_VALIDATOR;
    }
}
