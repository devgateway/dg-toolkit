package org.devgateway.toolkit.forms.wicket.components.table;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.IValidator;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;

import java.math.BigDecimal;

/**
 * Editable column using an input field.
 *
 * @author Nadejda Mandrescu
 */
public class EditableInputColumn<T> extends AbstractColumn<T, String> {
    private static final long serialVersionUID = -8136444296756281133L;

    private final Class<?> fieldClass;
    private final String property;
    private IValidator validator;

    public EditableInputColumn(final String resourceKey, final Class<?> fieldClass, final String property) {
        super(new StringResourceModel(resourceKey));
        this.fieldClass = fieldClass;
        this.property = property;
    }

    @Override
    public void populateItem(final Item<ICellPopulator<T>> cellItem, final String componentId,
            final IModel<T> rowModel) {
        TextFieldBootstrapFormComponent<String> textField = new TextFieldBootstrapFormComponent<String>(
                componentId, new PropertyModel<>(rowModel, property)) {
            private static final long serialVersionUID = 8923177302548131093L;

            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                onRowEdit(target, rowModel);
            }
        };
        textField.hideLabel();
        if (validator != null) {
            textField.getField().add(validator);
        }
        if (Double.class.isAssignableFrom(fieldClass)) {
            textField.asDouble();
        } else if (BigDecimal.class.isAssignableFrom(fieldClass)) {
            textField.decimal();
        } else if (Integer.class.isAssignableFrom(fieldClass)) {
            textField.integer();
        }
        cellItem.add(textField);
    }

    public EditableInputColumn<T> setValidator(final IValidator validator) {
        this.validator = validator;
        return this;
    }

    protected void onRowEdit(final AjaxRequestTarget target, final IModel<T> rowModel) {
    }
}
