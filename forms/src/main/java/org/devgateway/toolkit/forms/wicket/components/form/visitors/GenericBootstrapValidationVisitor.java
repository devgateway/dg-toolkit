package org.devgateway.toolkit.forms.wicket.components.form.visitors;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.SummernoteBootstrapFormComponent;

/**
 * Traverses all fields and refreshes the ones that are not valid, so that
 * we can see the errors
 *
 * @author mpostelnicu
 */
public class GenericBootstrapValidationVisitor implements IVisitor<GenericBootstrapFormComponent<?, ?>, Void> {

    private AjaxRequestTarget target;

    private GenericBootstrapFormComponent<?, ?> lastInvalidVisitedObject;

    public GenericBootstrapValidationVisitor(final AjaxRequestTarget target) {
        this.target = target;
    }

    @Override
    public void component(final GenericBootstrapFormComponent<?, ?> object, final IVisit<Void> visit) {
        visit.dontGoDeeper();
        if (object instanceof SummernoteBootstrapFormComponent) {
            object.getField().processInput();
        }
        if (!(object instanceof SummernoteBootstrapFormComponent) && object.getField().isValid()) {
            return;
        }
        target.add(object.getBorder());

        if (ListItem.class.isAssignableFrom(object.getParent().getClass())) {
            Component parent = object.getParent().getParent();
            while (parent != null && !ListViewSectionPanel.class.isAssignableFrom(parent.getClass())) {
                parent = parent.getParent();
            }
            if (parent != null) {
                ListViewSectionPanel listViewSectionPanel = (ListViewSectionPanel) parent;
                listViewSectionPanel.expandEntry(target, (ListItem) object.getParent());
            }
        }

        // remember last invalid visited object, we used this later to
        // trigger the visibility of its parent container, if it is folded
        lastInvalidVisitedObject = object;

        // there's no point in visiting anything else, we already have a
        // section with error. This hugely improves speed of large forms
        // visit.stop();
    }

    public GenericBootstrapFormComponent<?, ?> getLastInvalidVisitedObject() {
        return lastInvalidVisitedObject;
    }

}
