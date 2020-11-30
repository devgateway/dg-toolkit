package org.devgateway.toolkit.forms.wicket.components.form;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;

/**
 * @author mpostelnicu
 * @param <T>
 */
public interface IDgEditableGridColumn<T>
{
	GenericBootstrapFormComponent<T, ? extends FormComponent<T>> getEditableCellPanel(String componentId, IModel<T> model);
}
