package org.codhaus.groovy.grails.validation;

import com.qunar.flight.flagship.validate.ValidatorConfigManager;
import com.qunar.flight.flagship.validate.ext.ConstrainedPropertyQunar;
import com.qunar.org.codhaus.groovy.grails.common.FlagshipMDC;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户处理Cascade
 * @author yichaogeng
 *
 */
@SuppressWarnings("rawtypes")
public class CascadeConstraint extends AbstractConstraint {

	private boolean cascade;

	@Override
	public boolean supports(Class type) {

		return !type.isPrimitive();
	}

	@Override
	public String getName() {
		return ConstrainedPropertyQunar.CASCADE_CONSTRAINT;
	}

	@Override
	public void setParameter(Object constraintParameter) {
		if (!(constraintParameter instanceof Boolean)) {
			throw new IllegalArgumentException("Parameter for constraint ["
					+ ConstrainedPropertyQunar.CASCADE_CONSTRAINT + "] of property [" + constraintPropertyName
					+ "] of class [" + constraintOwningClass + "] must Boolean");
		}
		cascade = (Boolean) constraintParameter;
		//list = (List<?>)constraintParameter;
		super.setParameter(constraintParameter);
	}

	@Override
	protected void processValidate(Object target, Object propertyValue, Errors errors) {
		if (!cascade) {
			return;
		}
		ValidatorConfigManager rootValidator = (ValidatorConfigManager) FlagshipMDC.get(FlagshipMDC.ROOT_VALIDATOR);
		String[] currentGroups = (String[]) FlagshipMDC.get(FlagshipMDC.CURRENT_GROUP);
		if (rootValidator == null) {
			return;
		}
		Object[] children = new Object[] {};
		if (propertyValue instanceof List) {
			children = ((List) propertyValue).toArray();
		} else if (propertyValue instanceof Set) {
			children = ((Set) propertyValue).toArray();
		} else if (propertyValue instanceof Map) {
			children = ((Map) propertyValue).values().toArray();
		} else if (!propertyValue.getClass().isPrimitive()) {
			children = new Object[] { propertyValue };

		}
		for (int i = 0; i < children.length; i++) {
			if (currentGroups == null) {
				rootValidator.validate(children[i], errors);
			} else {
				rootValidator.validate(children[i], errors, currentGroups);
			}
		}
	}

}
