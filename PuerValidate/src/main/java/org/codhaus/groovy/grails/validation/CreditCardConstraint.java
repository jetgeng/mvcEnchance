package org.codhaus.groovy.grails.validation;

import com.qunar.flight.flagship.validate.ext.ConstrainedPropertyQunar;
import org.apache.commons.validator.CreditCardValidator;
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
	            rejectValue(target, errors, ConstrainedPropertyQunar.DEFAULT_INVALID_CREDIT_CARD_MESSAGE_CODE,
	                    ConstrainedPropertyQunar.CREDIT_CARD_CONSTRAINT + ConstrainedPropertyQunar.INVALID_SUFFIX, args);
	        }
	        
	    }

	    @Override
	    public void setParameter(Object constraintParameter) {
	        if (!(constraintParameter instanceof Boolean)) {
	            throw new IllegalArgumentException("Parameter for constraint [" +
	                    ConstrainedPropertyQunar.CREDIT_CARD_CONSTRAINT + "] of property [" +
	                    constraintPropertyName + "] of class [" +
	                    constraintOwningClass + "] must be a boolean value");
	        }

	        creditCard = (Boolean)constraintParameter;
	        super.setParameter(constraintParameter);
	    }

	    public String getName() {
	        return ConstrainedPropertyQunar.CREDIT_CARD_CONSTRAINT;
	    }

	    @SuppressWarnings("rawtypes")
	    public boolean supports(Class type) {
	        return type != null && String.class.isAssignableFrom(type);
	    }

}
