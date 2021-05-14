package org.devgateway.toolkit.forms.wicket.components.table;

import org.apache.wicket.ClassAttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.io.Serializable;
import java.util.List;

/**
 * @author Nadejda Mandrescu
 */
public class CustomUpdatableToolbar extends AbstractToolbar {
    private static final long serialVersionUID = -3962323106906355689L;

    private WebMarkupContainer toolbarRow;
    private RepeatingView repeatingView;

    private final IModel<?> rowModel;
    private final List<CellDef> cellDefs;

    public CustomUpdatableToolbar(final DataTable<?, ?> table, final IModel<?> rowModel,
            final List<CellDef> cellDefs) {
        super(rowModel, table);

        this.rowModel = rowModel;
        this.cellDefs = cellDefs;

        toolbarRow = new WebMarkupContainer("toolbarRow");
        toolbarRow.setOutputMarkupId(true);
        add(toolbarRow);

        addOrReplaceToolbarRow();
    }

    private void addOrReplaceToolbarRow() {
        repeatingView = new RepeatingView("td");
        /* Using row data object instead of its model to avoid the object to be recomputed each time its properties
        values are requested. E.g. This can be a total computed on the fly at getObject() request.
        */
        Object rowData = rowModel.getObject();
        cellDefs.forEach(cellDef -> addCell(rowData, cellDef));
        toolbarRow.addOrReplace(repeatingView);
    }

    private void addCell(final Object rowData, final CellDef cellDef) {
        WebMarkupContainer td = new WebMarkupContainer(repeatingView.newChildId());
        if (cellDef.css != null) {
            td.add(ClassAttributeModifier.replace("class", cellDef.css));
        }
        repeatingView.add(td);

        Label label = new Label("cellItem", new PropertyModel<>(rowData, cellDef.property));
        td.add(label);
    }

    public void onUpdate(final AjaxRequestTarget target) {
        addOrReplaceToolbarRow();
        target.add(toolbarRow);
    }

    public static class CellDef implements Serializable {
        private static final long serialVersionUID = -1627640537060391602L;

        private String property;
        private String css;

        public CellDef(final String property) {
            this(property, null);
        }

        public CellDef(final String property, final String css) {
            this.property = property;
            this.css = css;
        }
    }
}
