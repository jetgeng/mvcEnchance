package org.codhaus.groovy.grails.validation;

import org.apache.commons.lang.StringUtils;
import org.codhaus.groovy.grails.validation.ext.ConstrainedPropertyGunn;
import org.springframework.validation.Errors;

public class BlankConstraint extends AbstractVetoingConstraint {

	private boolean blank;

    /* (non-Javadoc)
     * @see org.codehaus.groovy.org.codhaus.groovy.grails.validation.Constraint#supports(java.lang.Class)
     */
    @SuppressWarnings("rawtypes")
    public boolean supports(Class type) {
        return type != null && String.class.isAssignableFrom(type);
    }

    @Override
    public Object getParameter() {
        return blank;
    }

    public boolean isBlank() {
        return blank;
    }

    /* (non-Javadoc)
     * @see org.codehaus.groovy.org.codhaus.groovy.grails.validation.ConstrainedProperty.AbstractConstraint#setParameter(java.lang.Object)
     */
    @Override
    public void setParameter(Object constraintParameter) {
        if (!(constraintParameter instanceof Boolean)) {
            throw new IllegalArgumentException("Parameter for constraint [" +
                    ConstrainedPropertyGunn.BLANK_CONSTRAINT + "] of property [" +
                    constraintPropertyName + "] of class [" + constraintOwningClass +
                    "] must be a boolean value");
        }

        blank = (Boolean)constraintParameter;
        super.setParameter(constraintParameter);
    }

    public String getName() {
        return ConstrainedPropertyGunn.BLANK_CONSTRAINT;
    }

    @Override
    protected boolean skipBlankValues() {
        return false;
    }

    @Override
    protected boolean processValidateWithVetoing(Object target, Object propertyValue, Errors errors) {
        if (propertyValue instanceof String && StringUtils.isBlank((String)propertyValue)) {
            if (!blank) {
                Object[] args = new Object[] { constraintPropertyName, constraintOwningClass };
                rejectValue(target, errors, ConstrainedPropertyGunn.DEFAULT_BLANK_MESSAGE_CODE,
                        ConstrainedPropertyGunn.BLANK_CONSTRAINT, args);
                // empty string is caught by 'blank' constraint, no addition validation needed
                return true;
            }
        }
        return false;
    }
}
