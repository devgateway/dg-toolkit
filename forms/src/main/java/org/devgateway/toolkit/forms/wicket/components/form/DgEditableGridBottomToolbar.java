package org.devgateway.toolkit.forms.wicket.components.form;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IFormSubmitter;
import org.apache.wicket.markup.html.form.IFormVisitorParticipant;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.model.PropertyModel;
import org.devgateway.toolkit.forms.wicket.components.FieldPanel;
import org.wicketstuff.egrid.component.EditableDataTable;
import org.wicketstuff.egrid.component.EditableGridSubmitLink;
import org.wicketstuff.egrid.toolbar.AbstractEditableGridToolbar;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Nadeem Mohammad, mpostelnicu
 *
 */
public abstract class DgEditableGridBottomToolbar<T, S> extends AbstractEditableGridToolbar
{

	private static final long serialVersionUID 	= 1L;
	private static final String CELL_ID 		= "cell";
	private static final String CELLS_ID 		= "cells";

	private T newRow							= null;
	
	protected abstract void onAdd(AjaxRequestTarget target, T newRow);

	public DgEditableGridBottomToolbar(EditableDataTable<?, ?> table, Class<T> clazz)
	{
		super(table);
		createNewInstance(clazz);
		AddToolBarForm addToolBarForm = new AddToolBarForm("addToolbarForm");
		addToolBarForm.add(newAddButton(addToolBarForm));
		add(addToolBarForm);
	}

	protected void onError(AjaxRequestTarget target) {	}

	//TODO: use Objenesis instead of the following

	private void createNewInstance(Class<T> clazz) 
	{
		try
		{
			newRow = (T) clazz.newInstance();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}
	
	private class AddToolBarForm extends Form<T> implements IFormVisitorParticipant
	{

		private static final long serialVersionUID = 1L;

		public AddToolBarForm(String id)
		{
			super(id);
			add(newEditorComponents());
		}
		public boolean processChildren()
		{
			IFormSubmitter submitter = getRootForm().findSubmittingButton();
            return submitter != null && submitter.getForm() == this;
		}		
	}

	private Component newAddButton(WebMarkupContainer encapsulatingContainer)
	{
		return new EditableGridSubmitLink("add", encapsulatingContainer) {

			private static final long serialVersionUID = 1L;		
			@SuppressWarnings("unchecked")
			@Override
			protected void onSuccess(AjaxRequestTarget target)
			{
				onAdd(target, newRow);
				createNewInstance((Class<T>) newRow.getClass());
				target.add(getTable());
				
			}
			@Override
			protected void onError(AjaxRequestTarget target)
			{				
				DgEditableGridBottomToolbar.this.onError(target);
			}
		};
	}
	
	private Loop newEditorComponents()
	{
		final List<AbstractDgEditablePropertyColumn<T, S, ?>> columns = getEditableColumns();
		return new Loop(CELLS_ID, columns.size())
		{

			private static final long serialVersionUID 	= 	1L;

			protected void populateItem(LoopItem item)
			{
				addEditorComponent(item, getEditorColumn(columns, item.getIndex()));
			}
		};
	}

	private void addEditorComponent(LoopItem item, AbstractDgEditablePropertyColumn<T, S, ?> toolBarCell)
	{
		item.add(newCell(toolBarCell));		
	}

	@SuppressWarnings("unchecked")
	private List<AbstractDgEditablePropertyColumn<T, S, ?>> getEditableColumns()
	{
		 List<AbstractDgEditablePropertyColumn<T, S, ?>> columns = new ArrayList<>();
		 for (IColumn<?, ?> column : getTable().getColumns()) {
			if (column instanceof AbstractDgEditablePropertyColumn)
			{
				columns.add((AbstractDgEditablePropertyColumn<T, S, ?>)column);
			}
			
		}
		 
		 return columns;
	}	

	private Component newCell(AbstractDgEditablePropertyColumn<T, S, ?> editableGridColumn)
	{
		FieldPanel<?> panel = editableGridColumn.getEditableCellPanel(CELL_ID,
				new PropertyModel<>(newRow , editableGridColumn.getPropertyExpression()));
		return panel;
	}

	private AbstractDgEditablePropertyColumn<T, S, ?> getEditorColumn(final List<AbstractDgEditablePropertyColumn<T, S, ?>> editorColumn, int index)
	{
		return editorColumn.get(index);
	}
}
