package org.devgateway.toolkit.forms.wicket.components.table;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;

/**
 * By implementing this interface some form elements can receive the DataTable reference post initialization.
 *
 * Current use case is for list table filter columns: filter columns like {@link SelectFilteredBootstrapPropertyColumn}
 * rely upon DataTable, which is available only after AbstractListPage.onInitialize(). However,
 * AbstractListPage.onInitialize() also detects registered filter columns to decide if to show filter toolbar or not.
 * Therefore, a "circular" dependency arise between such filter columns, data table and adding the toolbar. To bypass,
 * filter columns need to be defined before AbstractListPage.onInitialize(). Then to avoid manually setting up their
 * DataTable once it becomes available, this interface helps to automatically set it within AbstractListPage.
 *
 * @author Nadejda Mandrescu
 */
public interface DataTableAware {

    DataTable getDataTable();
    void setDataTable(DataTable dataTable);
}
