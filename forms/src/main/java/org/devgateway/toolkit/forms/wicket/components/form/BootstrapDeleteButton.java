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
/**
 *
 */
package org.devgateway.toolkit.forms.wicket.components.form;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.spinner.SpinnerAjaxButton;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnEventHeaderItem;
import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.events.EditingDisabledEvent;

/**
 * @author mpostelnicu
 *
 */
public abstract class BootstrapDeleteButton extends SpinnerAjaxButton {

    private static final long serialVersionUID = 8306451874943978003L;

    /**
     * @param id
     * @param model
     */
    public BootstrapDeleteButton(final String id, final IModel<String> model) {
        super(id, model, Buttons.Type.Danger);
    }

    public BootstrapDeleteButton(final String id) {
        super(id, Buttons.Type.Danger);
    }

    @Override
    protected abstract void onSubmit(AjaxRequestTarget target);

    @Override
    protected void onInitialize() {
        super.onInitialize();
        setDefaultFormProcessing(false);
        setIconType(FontAwesome5IconType.trash_s);

        if (ComponentUtil.isViewMode()) {
            setVisibilityAllowed(false);
        }
    }

    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(OnEventHeaderItem.forComponent(this, "onclick", "window.onbeforeunload = null;"));
    }

    @Override
    public void onEvent(final IEvent<?> event) {
        if (event.getPayload() instanceof EditingDisabledEvent) {
            this.setEnabled(false);
        }
    }

    @Override
    protected void updateAjaxAttributes(final AjaxRequestAttributes attributes) {

        super.updateAjaxAttributes(attributes);
        AjaxCallListener ajaxCallListener = new AjaxCallListener();
        ajaxCallListener.onPrecondition("return confirm('Confirm Delete?');");
        attributes.getAjaxCallListeners().add(ajaxCallListener);
    }

}
