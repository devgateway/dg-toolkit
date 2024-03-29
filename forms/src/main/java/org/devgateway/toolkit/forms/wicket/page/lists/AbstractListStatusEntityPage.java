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

import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipBehavior;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.wicket.components.table.SelectFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.page.edit.ResourceLockable;
import org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity;
import org.devgateway.toolkit.persistence.dao.DBConstants;

import java.util.List;

/**
 * This class can be use to display a list of Entities that have a status {@link AbstractStatusAuditableEntity}.
 *
 * @author idobre
 * @since 2019-04-01
 */
public abstract class AbstractListStatusEntityPage<T extends AbstractStatusAuditableEntity>
        extends AbstractListPage<T> {


    public AbstractListStatusEntityPage(final PageParameters parameters) {
        super(parameters);
    }


    public class LockableResourceActionPanel extends ActionPanel implements ResourceLockable {

        /**
         * @param id
         * @param model
         */
        public LockableResourceActionPanel(final String id, final IModel<T> model) {
            super(id, model);
            editItemPageLink.setEnabled(canEditLockableResource());
            if (getLockableResource().getCheckedOutUser() != null) {
                add(new TooltipBehavior(Model.of("Checked out to user " + getCheckedOutUsername())));
            }
        }

        @Override
        public AbstractStatusAuditableEntity getLockableResource() {
            return getModelObject();
        }
    }

    @Override
    public ActionPanel getActionPanel(final String id, final IModel<T> model) {
        return new LockableResourceActionPanel(id, model);
    }


    @Override
    protected void onInitialize() {
        addStatusColumn();

        super.onInitialize();
    }

    private List<String> getStatusDropdownValues() {
        return DBConstants.Status.ALL_LIST;
    }

    private void addStatusColumn() {
        columns.add(1, new SelectFilteredBootstrapPropertyColumn<>(new Model<>("Status"),
                "status", "status", "status", new ListModel<>(getStatusDropdownValues())));
    }
}
