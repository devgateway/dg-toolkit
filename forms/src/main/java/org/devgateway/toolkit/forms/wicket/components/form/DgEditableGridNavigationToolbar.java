package org.devgateway.toolkit.forms.wicket.components.form;

import org.wicketstuff.egrid.component.EditableDataTable;
import org.wicketstuff.egrid.toolbar.EditableGridNavigationToolbar;

/**
 * @author mpostelnicu
 */
public class DgEditableGridNavigationToolbar extends EditableGridNavigationToolbar {

    public DgEditableGridNavigationToolbar(EditableDataTable<?, ?> table) {
        super(table);
    }
}
