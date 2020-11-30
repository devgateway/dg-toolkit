package org.devgateway.toolkit.forms.wicket.components.form;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.wicketstuff.select2.ChoiceProvider;

import java.util.Collection;

/**
 * @author mpostelnicu
 * @param <T>
 * @param <S>
 * @param <Z>
 */
public class EditableMultiselectPropertyColumn<T, S, Z> extends AbstractDgEditablePropertyColumn<T, S, Collection<Z>> {

    private final ChoiceProvider<Z> choiceProvider;

    public EditableMultiselectPropertyColumn(IModel<String> displayModel, String propertyExpression,
                                             final ChoiceProvider<Z> choiceProvider) {
        super(displayModel, propertyExpression);
        this.choiceProvider = choiceProvider;
    }

    public Select2MultiChoiceBootstrapFormComponent<Z> postConfig(Select2MultiChoiceBootstrapFormComponent<Z>
                                                                          fieldPanel) {
        return fieldPanel;
    }

    @Override
    public GenericBootstrapFormComponent<Collection<Z>, ? extends FormComponent<Collection<Z>>> getEditableCellPanel(
            String componentId, IModel<Collection<Z>> model) {
        return postConfig(new Select2MultiChoiceBootstrapFormComponent<Z>(componentId, Model.of(), model,
                choiceProvider));
    }
}
