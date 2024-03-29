package org.devgateway.toolkit.forms.wicket.components.table;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.ChoiceFilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.wicket.components.form.Select2MultiChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.wicketstuff.select2.ChoiceProvider;

import java.util.Collection;
import java.util.List;

/**
 * @author idobre
 * @since 2019-03-11
 *
 * A ChoiceFilteredPropertyColumn that uses Select2MultiChoiceBootstrapFormComponent as a filter.
 */
public class SelectMultiFilteredBootstrapPropertyColumn<T, Y, S> extends ChoiceFilteredPropertyColumn<T, Y, S>
    implements DataTableAware {
    private DataTable dataTable;
    private boolean disableFilter = false;
    private ChoiceProvider<Y> choiceProvider;

    public SelectMultiFilteredBootstrapPropertyColumn(final IModel<String> displayModel,
                                                 final S sortProperty,
                                                 final String propertyExpression,
                                                 final ChoiceProvider<Y> choiceProvider,
                                                 final DataTable dataTable) {
        this(displayModel, sortProperty, propertyExpression, choiceProvider, dataTable, false);
    }

    public SelectMultiFilteredBootstrapPropertyColumn(final IModel<String> displayModel,
                                                 final S sortProperty,
                                                 final String propertyExpression,
                                                 final ChoiceProvider<Y> choiceProvider,
                                                 final DataTable dataTable,
                                                 final boolean disableFilter) {
        super(displayModel, sortProperty, propertyExpression, null);
        this.disableFilter = disableFilter;
        this.dataTable = dataTable;
        this.choiceProvider = choiceProvider;
    }

    public SelectMultiFilteredBootstrapPropertyColumn(final IModel<String> displayModel,
                                                      final S sortProperty,
                                                      final String propertyExpression,
                                                      final IModel<? extends List<? extends Y>> filterChoices,
                                                      final DataTable dataTable) {
        super(displayModel, sortProperty, propertyExpression, filterChoices);

        this.dataTable = dataTable;
    }

    public SelectMultiFilteredBootstrapPropertyColumn(final IModel<String> displayModel,
                                                      final String propertyExpression,
                                                      final IModel<? extends List<? extends Y>> filterChoices,
                                                      final DataTable dataTable) {
        super(displayModel, propertyExpression, filterChoices);

        this.dataTable = dataTable;
    }

    @Override
    public Component getFilter(final String componentId, final FilterForm<?> form) {
        ChoiceProvider<Y> provider;

        if (choiceProvider != null) {
            provider = choiceProvider;
        } else {
            provider = new GenericChoiceProvider<>((List<Y>) getFilterChoices().getObject());
        }


        final Select2MultiChoiceBootstrapFormComponent<Y> selectorField =
                new Select2MultiChoiceBootstrapFormComponent<>(componentId,
                        provider,
                        (IModel<Collection<Y>>) getFilterModel(form));
        selectorField.hideLabel();

        selectorField.getField().add(new AjaxComponentUpdatingBehavior(form, "change"));

        return selectorField;
    }

    private class AjaxComponentUpdatingBehavior extends AjaxFormComponentUpdatingBehavior {
        private final FilterForm<?> form;

        AjaxComponentUpdatingBehavior(final FilterForm<?> form, final String event) {
            super(event);
            this.form = form;
        }

        @Override
        protected void onUpdate(final AjaxRequestTarget target) {
            // update the table component
            dataTable.setCurrentPage(0);
            target.add(dataTable);
        }
    }


    @Override
    public DataTable getDataTable() {
        return dataTable;
    }

    @Override
    public void setDataTable(final DataTable dataTable) {
        this.dataTable = dataTable;
    }

}
