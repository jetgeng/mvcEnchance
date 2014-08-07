package org.codhaus.groovy.grails.validation;

import org.apache.commons.validator.CreditCardValidator;
import org.codhaus.groovy.grails.validation.ext.ConstrainedPropertyGunn;
import org.springframework.validation.Errors;

public class CreditCardConstraint extends AbstractConstraint {
	 private boolean creditCard;

	    @Override
	    protected void processValidate(Object target, Object propertyValue, Errors errors) {
	        if (!creditCard) {
	            return;
	        }
	        
	         //TODO 暂时不
	        CreditCardValidator validator = new CreditCardValidator();
	        if (!validator.isValid(propertyValue.toString())) {
	            Object[] args = new Object[] { constraintPropertyName, constraintOwningClass, propertyValue };
	            rejectValue(target, errors, ConstrainedPropertyGunn.DEFAULT_INVALID_CREDIT_CARD_MESSAGE_CODE,
                        ConstrainedPropertyGunn.CREDIT_CARD_CONSTRAINT + ConstrainedPropertyGunn.INVALID_SUFFIX, args);
	        }
	        
	    }

	    @Override
	    public void setParameter(Object constraintParameter) {
	        if (!(constraintParameter instanceof Boolean)) {
	            throw new IllegalArgumentException("Parameter for constraint [" +
                        ConstrainedPropertyGunn.CREDIT_CARD_CONSTRAINT + "] of property [" +
	                    constraintPropertyName + "] of class [" +
	                    constraintOwningClass + "] must be a boolean value");
	        }

	        creditCard = (Boolean)constraintParameter;
	        super.setParameter(constraintParameter);
	    }

	    public String getName() {
	        return ConstrainedPropertyGunn.CREDIT_CARD_CONSTRAINT;
	    }

	    @SuppressWarnings("rawtypes")
	    public boolean supports(Class type) {
	        return type != null && String.class.isAssignableFrom(type);
	    }

}
