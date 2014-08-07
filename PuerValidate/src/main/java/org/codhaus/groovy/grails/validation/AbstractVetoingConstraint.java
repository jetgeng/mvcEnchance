package org.codhaus.groovy.grails.validation;

import org.springframework.validation.Errors;

public abstract class AbstractVetoingConstraint extends AbstractConstraint implements VetoingConstraint {

	public boolean validateWithVetoing(Object target, Object propertyValue, Errors errors) {
        checkState();
        if (propertyValue == null && skipNullValues()) {
            return false;
        }

        return processValidateWithVetoing(target, propertyValue, errors);
    }

    @Override
    protected void processValidate(Object target, Object propertyValue, Errors errors) {
        processValidateWithVetoing(target, propertyValue, errors);
    }

    protected abstract boolean processValidateWithVetoing(Object target, Object propertyValue, Errors errors);

}
