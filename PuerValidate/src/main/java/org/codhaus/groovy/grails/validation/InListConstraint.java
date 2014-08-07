package org.codhaus.groovy.grails.validation;

import org.codhaus.groovy.grails.validation.ext.ConstrainedPropertyGunn;
import org.springframework.validation.Errors;

import java.util.List;

public class InListConstraint extends AbstractConstraint {

	List<?> list;

    /**
     * @return Returns the list.
     */
    public List<?> getList() {
        return list;
    }

    /* (non-Javadoc)
     * @see org.codehaus.groovy.org.codhaus.groovy.grails.validation.Constraint#supports(java.lang.Class)
     */
    @SuppressWarnings("rawtypes")
    public boolean supports(Class type) {
        return type != null;
    }

    /* (non-Javadoc)
     * @see org.codehaus.groovy.org.codhaus.groovy.grails.validation.ConstrainedProperty.AbstractConstraint#setParameter(java.lang.Object)
     */
    @Override
    public void setParameter(Object constraintParameter) {
        if (!(constraintParameter instanceof List<?>)) {
            throw new IllegalArgumentException("Parameter for constraint [" +
                    ConstrainedPropertyGunn.IN_LIST_CONSTRAINT + "] of property [" +
                    constraintPropertyName + "] of class [" + constraintOwningClass +
                    "] must implement the interface [java.util.List]");
        }

        list = (List<?>)constraintParameter;
        super.setParameter(constraintParameter);
    }

    public String getName() {
        return ConstrainedPropertyGunn.IN_LIST_CONSTRAINT;
    }

    @Override
    protected void processValidate(Object target, Object propertyValue, Errors errors) {
        // Check that the list contains the given value. If not, add an error.
        if (!list.contains(propertyValue)) {
            Object[] args = new Object[] { constraintPropertyName, constraintOwningClass, propertyValue, list };
            rejectValue(target, errors, ConstrainedPropertyGunn.DEFAULT_NOT_INLIST_MESSAGE_CODE,
                    ConstrainedPropertyGunn.NOT_PREFIX + ConstrainedPropertyGunn.IN_LIST_CONSTRAINT, args);
        }
    }

}
