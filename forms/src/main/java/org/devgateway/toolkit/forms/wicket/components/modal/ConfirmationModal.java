package org.devgateway.toolkit.forms.wicket.components.modal;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapCancelButton;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapSubmitButton;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.visitors.GenericBootstrapValidationVisitor;

/**
 * @author Viorel Chihai
 */
public class ConfirmationModal<T> extends Modal<T> {

    protected Form<T> form;

    protected Label description;

    protected BootstrapSubmitButton submitButton;

    public ConfirmationModal(String markupId, IModel<T> model) {
        super(markupId, model);

        header(new StringResourceModel("header", this, model));
        size(Size.Default);
        // workaround for Bootstrap bug that fade in stays on on modal close
        setFadeIn(false);

        form = new Form("confirmationForm");
        description = new Label("description", new StringResourceModel("description"));
        form.add(description);
        form.add(getSubmitButton());
        form.add(getCancelButton());

        add(form);
    }

    protected BootstrapSubmitButton getSubmitButton() {
        submitButton = new BootstrapSubmitButton("submit", form, new StringResourceModel("submit")) {
            @Override
            public void onSubmit(final AjaxRequestTarget target) {
                ConfirmationModal.this.onSubmit(target);
                ConfirmationModal.this.close(target);
            }

            @Override
            protected void onError(final AjaxRequestTarget target) {
                form.visitChildren(GenericBootstrapFormComponent.class, new GenericBootstrapValidationVisitor(target));
            }
        };
        submitButton.setType(Buttons.Type.Success);
        submitButton.setIconType(FontAwesomeIconType.check);

        return submitButton;
    }

    protected BootstrapCancelButton getCancelButton() {
        return new BootstrapCancelButton("cancel", new StringResourceModel("cancel")) {
            @Override
            public void onSubmit(final AjaxRequestTarget target) {
                ConfirmationModal.this.onCancel(target);
                ConfirmationModal.this.close(target);
            }
        };
    }

    protected void onCancel(final AjaxRequestTarget target) {
    }

    protected void onSubmit(final AjaxRequestTarget target) {
    }
}
