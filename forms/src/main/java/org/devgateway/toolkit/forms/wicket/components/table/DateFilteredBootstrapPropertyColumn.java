package org.devgateway.toolkit.forms.wicket.components.table;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.wicket.components.form.DateFieldBootstrapFormComponent;

import java.util.Date;

/**
 * A TextFilteredPropertyColumn that uses DateFieldBootstrapFormComponent as a
 * filter.
 * <p>
 * Created by mpostelnicu
 */
public class DateFilteredBootstrapPropertyColumn<T, S> extends TextFilteredPropertyColumn<T, Date, S> {
    private static final long serialVersionUID = -7916572904120503939L;

    public DateFilteredBootstrapPropertyColumn(final IModel<String> displayModel, final S sortProperty,
                                               final String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);
    }

    public DateFilteredBootstrapPropertyColumn(final IModel<String> displayModel, final String propertyExpression) {
        super(displayModel, propertyExpression);
    }

    @Override
    public Component getFilter(final String componentId, final FilterForm<?> form) {
        final DateFieldBootstrapFormComponent dateField =
                new DateFieldBootstrapFormComponent(componentId, getFilterModel(form));
        dateField.hideLabel();
        dateField.getField().add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                target.add(form);
            }
        });
        return dateField;
    }
}
