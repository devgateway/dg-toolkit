package org.devgateway.toolkit.forms.wicket.components.form;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.IValidator;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.PageableTablePanel;
import org.devgateway.toolkit.forms.wicket.components.table.AbstractBootstrapPagingNavigationWithError;
import org.devgateway.toolkit.forms.wicket.components.table.AjaxBootstrapNavigationToolbar;
import org.devgateway.toolkit.forms.wicket.components.table.AjaxFallbackBootstrapDataTable;
import org.devgateway.toolkit.forms.wicket.components.table.CustomUpdatableToolbar;
import org.devgateway.toolkit.forms.wicket.components.table.CustomUpdatableToolbar.CellDef;
import org.devgateway.toolkit.forms.wicket.components.table.EditableInputColumn;
import org.devgateway.toolkit.forms.wicket.components.table.PagingNavigationFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This panel can be used to edit small data grid tables.
 *
 * <p/> For many columns you need to use {@link org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel}.
 * For too many rows you should check
 * {@link org.devgateway.toolkit.forms.wicket.components.form.EditableGridFormComponent}.
 *
 * <p/>This class also tracks and updates computed columns (computed formula must be defined in the column model).
 *
 * <p>(Optional) You can use bottom toolbar for computed totals.
 *
 * @author Nadejda Mandrescu
 */
public class EditableTablePanel<T extends Serializable, PARENT extends Serializable>
        extends PageableTablePanel<T, PARENT> {
    private static final long serialVersionUID = 4277801542684359509L;

    /* <T> property that can be used as unique id among entries */
    protected String rowIdProperty;

    private List<String> computedColumnsProperties = new ArrayList<>();
    private Map<String, Integer> rowId2RowIndex = new HashMap<>();
    private Map<String, List<String>> rowId2ComputedColumnsComponentIds = new HashMap<>();

    protected boolean usePagingWithErrors = true;
    private LoopItem currentPaginatorLoopItem;

    protected CustomUpdatableToolbar customUpdatableToolbar;

    public EditableTablePanel(final String id, final IModel<PARENT> parentModel) {
        super(id, parentModel);
    }

    protected AjaxFallbackBootstrapDataTable buildDataTable() {
        AjaxFallbackBootstrapDataTable dataTable = super.buildDataTable();
        if (this.rowsPerPage != WebConstants.PAGE_SIZE_NO_LIMIT && usePagingWithErrors) {
            AjaxBootstrapNavigationToolbar navToolbar = dataTable.getNavigationToolbar();
            navToolbar.withPagingNavFactory(new PagingNavigationFactory(EditableTablePagination.class, this));
        }
        return dataTable;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        customUpdatableToolbar = getBottomToolbar(dataTable);
        if (customUpdatableToolbar != null) {
            dataTable.addBottomToolbar(customUpdatableToolbar);
        }
    }

    public String getRowIdProperty() {
        return rowIdProperty;
    }

    public void setRowIdProperty(final String rowIdProperty) {
        this.rowIdProperty = rowIdProperty;
    }

    public void addComputedColumn(final String resourceKey, final String property) {
        addComputedColumn(resourceKey, null, property, null);
    }

    public void addComputedColumn(final String resourceKey, final String sortProperty, final String property,
            final String css) {
        if (rowIdProperty == null) {
            throw new RuntimeException("No row id property configured.");
        }
        computedColumnsProperties.add(property);
        IColumn<T, String> computedColumn = new PropertyColumn<T, String>(
                new StringResourceModel(resourceKey), sortProperty, property) {
            private static final long serialVersionUID = -3578903469355827616L;
            @Override
            public void populateItem(final Item<ICellPopulator<T>> item, final String componentId,
                    final IModel<T> rowModel) {
                super.populateItem(item, componentId, rowModel);
                item.setOutputMarkupId(true);

                String rowId = getRowId(rowModel);
                rowId2RowIndex.putIfAbsent(rowId, rowId2RowIndex.size() + 1);
                rowId2ComputedColumnsComponentIds.computeIfAbsent(rowId, k -> new ArrayList<>()).add(item.getId());
            }

            @Override
            public String getCssClass() {
                return css;
            }
        };
        columns.add(computedColumn);
    }

    private String getRowId(final IModel<T> rowModel) {
        return new PropertyModel<>(rowModel, rowIdProperty).getObject().toString();
    }

    public void addIntegerColumn(final String resourceKey, final String property, final IValidator validator) {
        columns.add(new AjaxInputColumn(resourceKey, Integer.class, property).setValidator(validator));
    }

    public void addDoubleColumn(final String resourceKey, final String property, final IValidator validator) {
        columns.add(new AjaxInputColumn(resourceKey, Double.class, property).setValidator(validator));
    }

    public void addDecimalColumn(final String resourceKey, final String property, final IValidator validator) {
        columns.add(new AjaxInputColumn(resourceKey, BigDecimal.class, property).setValidator(validator));
    }

    protected void onUpdate(final AjaxRequestTarget target) {
    }

    protected boolean hasErrors(final long pageIndex) {
        // customize with your page validation logic
        return  false;
    }

    private void onRowEdit(final AjaxRequestTarget target, final IModel<T> rowModel) {
        if (!computedColumnsProperties.isEmpty()) {
            String rowId = getRowId(rowModel);
            Integer rowComponentId = rowId2RowIndex.get(rowId);
            rowId2ComputedColumnsComponentIds.get(rowId).forEach(computedColComponentId -> {
                String componentId = "table:body:rows:" + rowComponentId + ":cells:"
                        + computedColComponentId;
                Component computedCol = EditableTablePanel.this.get(componentId);
                target.add(computedCol);
            });
        }
        if (customUpdatableToolbar != null) {
            customUpdatableToolbar.onUpdate(target);
        }
        onUpdate(target);
    }

    protected CustomUpdatableToolbar getBottomToolbar(final DataTable dataTable) {
        return new CustomUpdatableToolbar(dataTable, getToolbarModel(), getToolbarDef());
    }

    protected List<CellDef> getToolbarDef() {
        return Collections.emptyList();
    }

    protected IModel<?> getToolbarModel() {
        return Model.of();
    }

    private class AjaxInputColumn extends EditableInputColumn<T> {
        private static final long serialVersionUID = 8423233281861649118L;

        AjaxInputColumn(final String resourceKey, final Class<?> fieldClass, final String property) {
            super(resourceKey, fieldClass, property);
        }

        @Override
        protected void onRowEdit(final AjaxRequestTarget target, final IModel<T> rowModel) {
            EditableTablePanel.this.onRowEdit(target, rowModel);
        }
    }

    public class EditableTablePagination extends AbstractBootstrapPagingNavigationWithError {
        private static final long serialVersionUID = -5015086509864523000L;

        public EditableTablePagination(final String id, final IPageable pageable,
                final IPagingLabelProvider labelProvider) {
            super(id, pageable, labelProvider);
        }

        @Override
        protected boolean hasErrors(final long pageIndex) {
            return EditableTablePanel.this.hasErrors(pageIndex);
        }

        @Override
        protected void populateItem(final LoopItem loopItem) {
            super.populateItem(loopItem);
            if (loopItem.getIndex() == this.pageable.getCurrentPage()) {
                currentPaginatorLoopItem = loopItem;
                currentPaginatorLoopItem.setOutputMarkupId(true);
            }
        }
    }

}
