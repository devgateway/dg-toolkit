package org.devgateway.toolkit.forms.validators;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.service.UniquePropertyService;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Nadejda Mandrescu
 */
public class UniquePropertyValidator<T extends GenericPersistable & Serializable, V> implements IValidator<V> {
    private static final long serialVersionUID = -7185120038483258730L;

    protected final UniquePropertyService<T> uniquePropertyService;
    protected final Collection<Long> entityIdsToIgnore;
    protected final String propertyName;
    protected final IModel<String> propertyLabel;

    public UniquePropertyValidator(final UniquePropertyService<T> uniquePropertyService,
            final long entityIdToIgnore, final String propertyName, final Component component) {
        this(uniquePropertyService, Collections.singleton(entityIdToIgnore), propertyName, component);
    }

    public UniquePropertyValidator(final UniquePropertyService<T> uniquePropertyService,
            final Collection<Long> entityIdsToIgnore, final String propertyName, final Component component) {
        this(uniquePropertyService, entityIdsToIgnore, propertyName,
                new StringResourceModel(propertyName + ".label", component));
    }

    public UniquePropertyValidator(final UniquePropertyService<T> uniquePropertyService,
            final Collection<Long> entityIdsToIgnore, final String propertyName, final IModel<String> propertyLabel) {
        if (entityIdsToIgnore.isEmpty()) {
            entityIdsToIgnore.add(-1L);
        }
        this.uniquePropertyService = uniquePropertyService;
        this.entityIdsToIgnore = entityIdsToIgnore;
        this.propertyName = propertyName;
        this.propertyLabel = propertyLabel;
    }

    @Override
    public void validate(final IValidatable<V> validatable) {
        final V value = validatable.getValue();
        if (uniquePropertyService.existsByProperty(propertyName, value, entityIdsToIgnore)) {
            ValidationError error = new ValidationError(this);
            error.setVariable("label", propertyLabel.getObject());
            error.setVariable("input", value);
            validatable.error(error);
        }
    }

}
