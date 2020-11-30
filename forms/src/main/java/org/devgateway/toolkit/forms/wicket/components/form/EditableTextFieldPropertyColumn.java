package org.devgateway.toolkit.forms.wicket.components.form;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;

/**
 * @author mpostelnicu
 * @param <T>
 * @param <S>
 * @param <Z>
 */
public class EditableTextFieldPropertyColumn<T, S, Z> extends AbstractDgEditablePropertyColumn<T, S, Z> {

    public EditableTextFieldPropertyColumn(IModel<String> displayModel, String propertyExpression) {
        super(displayModel, propertyExpression);
    }

    public TextFieldBootstrapFormComponent<Z> postConfig(TextFieldBootstrapFormComponent<Z> fieldPanel) {
        return fieldPanel;
    }


    @Override
    public GenericBootstrapFormComponent<Z, ? extends FormComponent<Z>> getEditableCellPanel(String componentId, IModel<Z> model) {
        TextFieldBootstrapFormComponent<Z> c = new TextFieldBootstrapFormComponent<>(componentId, getDisplayModel(), model);
        c.hideLabel();
        return postConfig(c);
    }
}
