package org.codhaus.groovy.grails.validation;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.codhaus.groovy.grails.validation.ext.ConstrainedPropertyGunn;
import org.springframework.validation.Errors;

public class EmailConstraint extends AbstractConstraint {

	 private boolean email;

	    /* (non-Javadoc)
	     * @see org.codehaus.groovy.org.codhaus.groovy.grails.validation.Constraint#supports(java.lang.Class)
	     */
	    @SuppressWarnings("rawtypes")
	    public boolean supports(Class type) {
	        return type != null && String.class.isAssignableFrom(type);
	    }

	    /* (non-Javadoc)
	     * @see org.codehaus.groovy.org.codhaus.groovy.grails.validation.ConstrainedProperty.AbstractConstraint#setParameter(java.lang.Object)
	     */
	    @Override
	    public void setParameter(Object constraintParameter) {
	        if (!(constraintParameter instanceof Boolean)) {
	            throw new IllegalArgumentException("Parameter for constraint [" +
                        ConstrainedPropertyGunn.EMAIL_CONSTRAINT + "] of property [" +
	                    constraintPropertyName + "] of class [" + constraintOwningClass +
	                    "] must be a boolean value");
	        }

	        email = (Boolean)constraintParameter;
	        super.setParameter(constraintParameter);
	    }

	    public String getName() {
	        return ConstrainedPropertyGunn.EMAIL_CONSTRAINT;
	    }

	    @Override
	    protected void processValidate(Object target, Object propertyValue, Errors errors) {
	        if (!email) {
	            return;
	        }

	        EmailValidator emailValidator = EmailValidator.getInstance();
	        Object[] args = new Object[] { constraintPropertyName, constraintOwningClass, propertyValue };
	        String value = propertyValue.toString();
	        if (StringUtils.isBlank(value)) {
	            return;
	        }

	        if (!emailValidator.isValid(value)) {
	            rejectValue(target, errors, ConstrainedPropertyGunn.DEFAULT_INVALID_EMAIL_MESSAGE_CODE,
                        ConstrainedPropertyGunn.EMAIL_CONSTRAINT + ConstrainedPropertyGunn.INVALID_SUFFIX, args);
	        }
	    }

}
