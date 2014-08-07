package org.gunn.puer.validate


import org.codhaus.groovy.grails.validation.ext.ConstrainedPropertyGunn
import org.springframework.beans.BeanWrapperImpl
import org.springframework.validation.Errors;
import org.springframework.validation.Validator
import org.apache.commons.lang.StringUtils

class ValidateGroup implements Validator {

	String pojoClassName
	String name
	
	Map<String, ConstrainedPropertyGunn> constrainedProperties = new LinkedHashMap<String, ConstrainedPropertyGunn>()
	@Override
	public boolean supports(Class<?> clazz) {
		return StringUtils.equalsIgnoreCase(pojoClassName, clazz.getName())	
	}

	def void validate(Object target, Errors errors){
		BeanWrapperImpl bean = new BeanWrapperImpl(target)
		constrainedProperties.each{key, value ->
			value.validate(target, bean.getPropertyValue(key), errors)
		}
	}
	
	def addConstrained(ConstrainedPropertyGunn propertyRule){
		constrainedProperties[propertyRule.propertyName] = propertyRule
	}
	
	
}
