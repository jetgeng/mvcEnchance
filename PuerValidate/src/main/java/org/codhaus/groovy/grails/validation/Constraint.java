package org.codhaus.groovy.grails.validation;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.validation.Errors;

/**
 * Defines a validatable constraint.
 *
 * @author Graeme Rocher
 */
public interface Constraint extends MessageSourceAware {

    /**
     * Returns whether the constraint supports being applied against the specified type;
     *
     * @param type The type to support
     * @return true if the constraint can be applied against the specified type
     */
    @SuppressWarnings("rawtypes")
    boolean supports(Class type);

    /**
     * Return whether the constraint is valid for the owning class
     *
     * @return true if it is
     */
    boolean isValid();

    /**
     * Validate this constraint against a property value. If implementation is vetoing (isVetoing() method
     * returns true), then it could return 'true' to stop further validation.
     *
     * @param target
     * @param propertyValue The property value to validate
     * @param errors The errors instance to record errors against
     */
    void validate(Object target, Object propertyValue, Errors errors);

    /**
     * The parameter which the constraint is validated against.
     *
     * @param parameter
     */
    void setParameter(Object parameter);

    Object getParameter();

    /**
     * The class the constraint applies to
     *
     * @param owningClass
     */
    @SuppressWarnings("rawtypes")
    void setOwningClass(Class owningClass);

    /**
     * The name of the property the constraint applies to
     *
     * @param propertyName
     */
    void setPropertyName(String propertyName);

    /**
     * @return The name of the constraint
     */
    String getName();

    /**
     * @return The property name of the constraint
     */
    String getPropertyName();

    /**
     * The message source to evaluate the default messages from
     *
     * @param source
     */
    void setMessageSource(MessageSource source);
}
