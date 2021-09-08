package org.devgateway.toolkit.forms.wicket.components.table;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.ChoiceFilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.wicketstuff.select2.ChoiceProvider;

import java.util.List;

/**
 * A ChoiceFilteredPropertyColumn that uses Select2ChoiceBootstrapFormComponent as a filter.
 *
 * @author idobre
 * @since 12/20/16
 */
public class SelectFilteredBootstrapPropertyColumn<T, Y, S> extends ChoiceFilteredPropertyColumn<T, Y, S>
    implements DataTableAware {
    private DataTable dataTable;
    private boolean disableFilter = false;
    private ChoiceProvider<Y> choiceProvider;

    private final String filterFormPropertyExpression;

    public SelectFilteredBootstrapPropertyColumn(final IModel<String> displayModel,
                                                 final S sortProperty,
                                                 final String propertyExpression,
                                                 final String filterFormPropertyExpression,
                                                 final ChoiceProvider<Y> choiceProvider,
                                                 final DataTable dataTable) {
        this(displayModel, sortProperty, propertyExpression, filterFormPropertyExpression, choiceProvider, dataTable,
                false);
    }

    public SelectFilteredBootstrapPropertyColumn(final IModel<String> displayModel,
                                                 final S sortProperty,
                                                 final String propertyExpression,
                                                 final String filterFormPropertyExpression,
                                                 final IModel<? extends List<? extends Y>> filterChoices) {
        super(displayModel, sortProperty, propertyExpression, filterChoices);
        this.filterFormPropertyExpression = filterFormPropertyExpression;
    }

    public SelectFilteredBootstrapPropertyColumn(final IModel<String> displayModel,
                                                 final S sortProperty,
                                                 final String propertyExpression,
                                                 final String filterFormPropertyExpression,
                                                 final ChoiceProvider<Y> choiceProvider,
                                                 final DataTable dataTable,
                                                 final boolean disableFilter) {
        super(displayModel, sortProperty, propertyExpression, null);
        this.disableFilter = disableFilter;
        this.dataTable = dataTable;
        this.choiceProvider = choiceProvider;
        this.filterFormPropertyExpression = filterFormPropertyExpression;
    }

    public SelectFilteredBootstrapPropertyColumn(final IModel<String> displayModel,
            final S sortProperty,
            final String propertyExpression,
            final String filterFormPropertyExpression,
            final IModel<? extends List<? extends Y>> filterChoices,
            final DataTable dataTable,
            final boolean disableFilter) {
        super(displayModel, sortProperty, propertyExpression, filterChoices);
        this.filterFormPropertyExpression = filterFormPropertyExpression;
        this.disableFilter = disableFilter;
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
        final Select2ChoiceBootstrapFormComponent<Y> selectorField =
                new Select2ChoiceBootstrapFormComponent<>(componentId, provider, getFilterModel(form));
        selectorField.hideLabel();
        if (disableFilter) {
            selectorField.setEnabled(false);
        }

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
    public void detach() {
        super.detach();
        if (choiceProvider != null) {
            choiceProvider.detach();
        }
    }

    @Override
    protected IModel<Y> getFilterModel(FilterForm<?> form) {
        return new PropertyModel<>(form.getDefaultModel(), filterFormPropertyExpression);
    }

    @Override
    public DataTable getDataTable() {
        return dataTable;
    }

    @Override
    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }
}
