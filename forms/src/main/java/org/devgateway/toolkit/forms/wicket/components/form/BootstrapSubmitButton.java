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
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;

/**
 * @author mpostelnicu
 *
 */
public abstract class BootstrapSubmitButton extends SpinnerAjaxButton {

    private static final long serialVersionUID = 8306451874943978003L;

    /**
     * @param id
     * @param model
     */
    public BootstrapSubmitButton(final String id, final IModel<String> model) {
        super(id, model, Buttons.Type.Primary);
        setIconType(FontAwesome5IconType.save_s);
    }

    public BootstrapSubmitButton(final String id, final Form<?> form, final IModel<String> model) {
        super(id, model, form, Buttons.Type.Primary);
        setIconType(FontAwesome5IconType.save_s);
    }

    @Override
    protected abstract void onSubmit(AjaxRequestTarget target);

    @Override
    protected void onInitialize() {
        super.onInitialize();

        if (ComponentUtil.isViewMode()) {
            setVisibilityAllowed(false);
        }
    }
}
