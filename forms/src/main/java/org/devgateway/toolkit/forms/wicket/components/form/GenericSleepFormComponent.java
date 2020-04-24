package org.devgateway.toolkit.forms.wicket.components.form;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.devgateway.toolkit.forms.models.ViewModeConverterModel;
import org.devgateway.toolkit.forms.wicket.components.FieldPanel;

/**
 * @author idobre
 * @since 2019-04-10
 */
public class GenericSleepFormComponent<T> extends FieldPanel<T> {
    private static final long serialVersionUID = -2724483357887901236L;

    private IModel<String> labelModel;

    public GenericSleepFormComponent(final String id) {
        super(id);
    }

    public GenericSleepFormComponent(final String id, final IModel<T> model) {
        super(id, model);
    }

    public GenericSleepFormComponent(final String id, final IModel<T> model, final IModel<String> labelModel) {
        super(id, model);
        this.labelModel = labelModel;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        if (labelModel == null) {
            labelModel = new ResourceModel(getId() + ".label");
        }
        add(new Label("label", labelModel));

        final Label value = new Label("value", new ViewModeConverterModel<>(getModel()));
        value.setEscapeModelStrings(false);
        add(value);
    }
}
