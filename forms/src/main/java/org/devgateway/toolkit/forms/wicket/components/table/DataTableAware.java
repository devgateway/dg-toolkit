package org.devgateway.toolkit.forms.wicket.components.table;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;

/**
 * @author Nadejda Mandrescu
 */
public interface DataTableAware {

    DataTable getDataTable();
    void setDataTable(DataTable dataTable);
}
