/* Copyright 2004-2005 Graeme Rocher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codhaus.groovy.grails.validation;

import com.qunar.flight.flagship.validate.ext.ConstrainedPropertyQunar;
import org.springframework.validation.Errors;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * Validates maximum size or length of the property, for strings and arrays
 * this is the length and collections the size.
 *
 * @author Graeme Rocher
 * @since 0.4
 */
public class MaxSizeConstraint extends AbstractConstraint {

    private int maxSize;

    /**
     * @return Returns the maxSize.
     */
    public int getMaxSize() {
        return maxSize;
    }

    /* (non-Javadoc)
     * @see org.codehaus.groovy.org.codhaus.groovy.grails.validation.ConstrainedProperty.AbstractConstraint#setParameter(java.lang.Object)
     */
    @Override
    public void setParameter(Object constraintParameter) {
        if (!(constraintParameter instanceof Integer)) {
            throw new IllegalArgumentException("Parameter for constraint [" +
                    ConstrainedPropertyQunar.MAX_SIZE_CONSTRAINT + "] of property [" +
                    constraintPropertyName + "] of class [" + constraintOwningClass +
                    "] must be a of type [java.lang.Integer]");
        }

        maxSize = ((Integer)constraintParameter).intValue();
        super.setParameter(constraintParameter);
    }

    public String getName() {
        return ConstrainedPropertyQunar.MAX_SIZE_CONSTRAINT;
    }

    /* (non-Javadoc)
     * @see org.codehaus.groovy.org.codhaus.groovy.grails.validation.Constraint#supports(java.lang.Class)
     */
    @SuppressWarnings("rawtypes")
    public boolean supports(Class type) {
        return type != null && (
                String.class.isAssignableFrom(type) ||
                Collection.class.isAssignableFrom(type) ||
                type.isArray());
    }

    @Override
    protected void processValidate(Object target, Object propertyValue, Errors errors) {
        int length;
        if (propertyValue.getClass().isArray()) {
            length = Array.getLength(propertyValue);
        }
        else if (propertyValue instanceof Collection<?>) {
            length = ((Collection<?>)propertyValue).size();
        }
        else { // String
            length = ((String)propertyValue).length();
        }

        if (length > maxSize) {
            Object[] args = { constraintPropertyName, constraintOwningClass, propertyValue, maxSize};
            rejectValue(target, errors, ConstrainedPropertyQunar.DEFAULT_INVALID_MAX_SIZE_MESSAGE_CODE,
            		ConstrainedPropertyQunar.MAX_SIZE_CONSTRAINT + ConstrainedPropertyQunar.EXCEEDED_SUFFIX, args);
        }
    }
}
