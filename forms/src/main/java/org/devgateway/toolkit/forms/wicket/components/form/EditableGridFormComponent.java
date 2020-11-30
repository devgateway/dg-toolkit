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
package org.devgateway.toolkit.forms.wicket.components.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.wicket.components.ComponentUtil;
import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;

import java.util.List;

/**
 * @author mpostelnicu
 * @param <T>
 * @param <PARENT>
 */
public abstract class EditableGridFormComponent<T extends AbstractAuditableEntity, PARENT extends AbstractAuditableEntity>
        extends GenericBootstrapFormComponent<List<T>,
        EditableGridFormComponentWrapper<T>> {

    private final Class<T> clazz;
    private final long pageSize;


    public EditableGridFormComponent(String id, Class<T> clazz, long pageSize) {
        super(id);
        this.clazz = clazz;
        this.pageSize = pageSize;
    }

    public IModel<PARENT> getParentObjectModel() {
        return (IModel<PARENT>) this.getParent().getDefaultModel();
    }


    public EditableGridFormComponent(String id, IModel<List<T>> model, Class<T> clazz, long pageSize) {
        super(id, model);
        this.clazz = clazz;
        this.pageSize = pageSize;
    }

    @Override
    protected void getAjaxFormComponentUpdatingBehavior() {
        // do nothing;
    }

    public EditableGridFormComponent(String id, IModel<String> labelModel, IModel<List<T>> model,
                                     Class<T> clazz, long pageSize) {
        super(id, labelModel, model);
        this.clazz = clazz;
        this.pageSize = pageSize;
    }

    @Override
    public void onEvent(final IEvent<?> event) {
        ComponentUtil.enableDisableEvent(this, event);
    }

    @Override
    protected void onInitialize() {
        getField().setClazz(clazz);
        getField().setPageSize(pageSize);
        super.onInitialize();
    }

    public void onError(AjaxRequestTarget target) {

    }

    public void onCancel(AjaxRequestTarget target) {

    }

    public void onSave(AjaxRequestTarget target) {

    }

    public void onDelete(AjaxRequestTarget target) {

    }



    public abstract void onAdd(AjaxRequestTarget target, T newRow);

    @Override
    protected EditableGridFormComponentWrapper<T> inputField(String id, IModel<List<T>> model) {
        EditableGridFormComponentWrapper<T> field = new EditableGridFormComponentWrapper<T>(id, initFieldModel()){
            @Override
            public void onAdd(AjaxRequestTarget target, T newRow) {
                EditableGridFormComponent.this.onAdd(target, newRow);
                super.onAdd(target, newRow);
            }

            @Override
            public void onSave(AjaxRequestTarget target) {
                EditableGridFormComponent.this.onSave(target);
                super.onSave(target);
            }

            @Override
            public void onCancel(AjaxRequestTarget target) {
                EditableGridFormComponent.this.onCancel(target);
                super.onCancel(target);
            }

            @Override
            public void onDelete(AjaxRequestTarget target) {
                EditableGridFormComponent.this.onDelete(target);
                super.onDelete(target);
            }

            @Override
            public void onError(AjaxRequestTarget target) {
                EditableGridFormComponent.this.onError(target);
                super.onError(target);
            }
        };
        return field;
    }
}