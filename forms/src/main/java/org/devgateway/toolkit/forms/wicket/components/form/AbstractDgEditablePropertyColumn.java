package org.devgateway.toolkit.forms.wicket.components.form;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.wicketstuff.egrid.column.EditableGridActionsPanel;

/**
 * @author mpostelnicu
 * @param <T>
 * @param <S>
 * @param <Z>
 */
public abstract class AbstractDgEditablePropertyColumn<T, S, Z> extends PropertyColumn<T, S> implements IDgEditableGridColumn<Z>
{
	public AbstractDgEditablePropertyColumn(IModel<String> displayModel, String propertyExpression)
	{
		super(displayModel, propertyExpression);
	}


	@SuppressWarnings("unchecked")
	@Override
	public final void populateItem(final Item<ICellPopulator<T>> item, final String componentId, final IModel<T> rowModel)
	{

		final Item<T> rowItem = ((Item<T>) item.findParent(Item.class));
		
		if (inEditingMode(rowItem))
		{
			GenericBootstrapFormComponent<Z, ? extends FormComponent<Z>> provider =
					getEditableCellPanel(componentId, (IModel<Z>) getDataModel(rowModel));
			item.add(provider);
		}
		else 
		{
			super.populateItem(item, componentId, rowModel);
		}		
	}

	private boolean inEditingMode(Item<T> rowItem)
	{
		return rowItem.getMetaData(EditableGridActionsPanel.EDITING);
	}
	 
	protected void addBehaviors(FormComponent<T> editorComponent) 
	{

	}	
}
