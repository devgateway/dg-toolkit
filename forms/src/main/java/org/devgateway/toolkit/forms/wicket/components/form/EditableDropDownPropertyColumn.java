package org.devgateway.toolkit.forms.wicket.components.form;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.wicketstuff.select2.ChoiceProvider;

/**
 * @author mpostelnicu
 * @param <T>
 * @param <S>
 * @param <Z>
 */
public class EditableDropDownPropertyColumn<T, S, Z> extends AbstractDgEditablePropertyColumn<T, S, Z> {

    private final ChoiceProvider<Z> choiceProvider;

    public EditableDropDownPropertyColumn(IModel<String> displayModel, String propertyExpression,
                                          final ChoiceProvider<Z> choiceProvider) {
        super(displayModel, propertyExpression);
        this.choiceProvider = choiceProvider;
    }

    public Select2ChoiceBootstrapFormComponent<Z> postConfig(Select2ChoiceBootstrapFormComponent<Z> fieldPanel) {
        return fieldPanel;
    }

    @Override
    public GenericBootstrapFormComponent<Z, ? extends FormComponent<Z>> getEditableCellPanel(String componentId, IModel<Z> model) {
     return postConfig(new Select2ChoiceBootstrapFormComponent<Z>(componentId, Model.of(), model, choiceProvider));
    }
}
