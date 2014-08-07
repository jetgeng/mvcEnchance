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

import groovy.lang.Range;
import org.codhaus.groovy.grails.common.GrailsClassUtils;
import org.codhaus.groovy.grails.validation.ext.ConstrainedPropertyGunn;
import org.springframework.validation.Errors;

/**
 * Validates a range.
 *
 * @author Graeme Rocher
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class RangeConstraint extends AbstractConstraint {

    Range range;

    /**
     * @return Returns the range.
     */
    public Range getRange() {
        return range;
    }

    /* (non-Javadoc)
     * @see org.codehaus.groovy.org.codhaus.groovy.grails.validation.Constraint#supports(java.lang.Class)
     */
    public boolean supports(Class type) {
        return type != null && (Comparable.class.isAssignableFrom(type) ||
                GrailsClassUtils.isAssignableOrConvertibleFrom(Number.class, type));
    }

    /* (non-Javadoc)
     * @see org.codehaus.groovy.org.codhaus.groovy.grails.validation.ConstrainedProperty.AbstractConstraint#setParameter(java.lang.Object)
     */
    @Override
    public void setParameter(Object constraintParameter) {
        if (!(constraintParameter instanceof Range)) {
                throw new IllegalArgumentException("Parameter for constraint [" +
                        ConstrainedPropertyGunn.RANGE_CONSTRAINT + "] of property [" +
                        constraintPropertyName + "] of class [" +
                        constraintOwningClass + "] must be a of type [groovy.lang.Range]");
        }

        range = (Range)constraintParameter;
        super.setParameter(constraintParameter);
    }

    public String getName() {
        return ConstrainedPropertyGunn.RANGE_CONSTRAINT;
    }

    @Override
    protected void processValidate(Object target, Object propertyValue, Errors errors) {
        if (range.contains(propertyValue)) {
            return;
        }

        Object[] args = new Object[] { constraintPropertyName, constraintOwningClass,
                propertyValue, range.getFrom(), range.getTo()};

        Comparable from = range.getFrom();
        Comparable to = range.getTo();

        if (from instanceof Number && propertyValue instanceof Number) {
            // Upgrade the numbers to Long, so all integer types can be compared.
            from = ((Number) from).longValue();
            to = ((Number) to).longValue();
            propertyValue = ((Number) propertyValue).longValue();
        }

        if (from.compareTo(propertyValue) > 0) {
            rejectValue(target, errors, ConstrainedPropertyGunn.DEFAULT_INVALID_RANGE_MESSAGE_CODE,
                    ConstrainedPropertyGunn.RANGE_CONSTRAINT + ConstrainedPropertyGunn.TOOSMALL_SUFFIX, args);
        }
        else if (to.compareTo(propertyValue) < 0) {
            rejectValue(target, errors, ConstrainedPropertyGunn.DEFAULT_INVALID_RANGE_MESSAGE_CODE,
                    ConstrainedPropertyGunn.RANGE_CONSTRAINT + ConstrainedPropertyGunn.TOOBIG_SUFFIX, args);
        }
    }
}
