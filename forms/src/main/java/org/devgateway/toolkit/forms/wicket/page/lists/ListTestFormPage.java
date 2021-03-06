/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.forms.wicket.page.lists;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.components.table.TextFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.components.table.filter.JpaFilterState;
import org.devgateway.toolkit.forms.wicket.components.table.filter.TestFormFilterState;
import org.devgateway.toolkit.forms.wicket.page.edit.EditTestFormPage;
import org.devgateway.toolkit.persistence.dao.TestForm;
import org.devgateway.toolkit.persistence.service.RoleService;
import org.devgateway.toolkit.persistence.service.TestFormService;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath(value = "/listTestForm")
public class ListTestFormPage extends AbstractListStatusEntityPage<TestForm> {
    private static final long serialVersionUID = -324298525712620234L;

    @SpringBean
    private TestFormService testFormService;

    @SpringBean
    private RoleService roleService;

    public ListTestFormPage(final PageParameters pageParameters) {
        super(pageParameters);
        this.jpaService = testFormService;
        this.editPageClass = EditTestFormPage.class;

        columns.add(new TextFilteredBootstrapPropertyColumn<>(new Model<>("Text Field"), "textField", "textField"));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // enable excel download
        excelForm.setVisibilityAllowed(true);
    }

    @Override
    public JpaFilterState<TestForm> newFilterState() {
        return new TestFormFilterState();
    }
}
