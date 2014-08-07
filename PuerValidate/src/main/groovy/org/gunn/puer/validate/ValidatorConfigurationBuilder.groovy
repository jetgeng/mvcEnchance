package org.gunn.puer.validate

import org.codhaus.groovy.grails.validation.ext.ConstrainedPropertyGunn
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.BeanWrapper
import org.springframework.beans.BeanWrapperImpl




class ValidatorConfigurationBuilder extends BuilderSupport {
	Logger logger = LoggerFactory.getLogger(ValidatorConfigurationBuilder)
	def rules = [:]
	private POJOValidatorRule rule
	@Override
	protected void setParent(Object parent, Object child) {

		if(parent != null && child != null &&
		(parent instanceof POJOValidatorRule)
		) {
			if(child instanceof ValidateGroup && child.hasProperty("name")  && child.name != null){
				parent.addGroup(child.name, child)
			}
			if(child instanceof ConstrainedPropertyGunn){
				parent.addConstrained(child)
			}
		}
	}

	@Override
	protected Object postNodeCompletion(Object parent, Object node) {
		// 当组添加完成后，对全局做其他处理。
		if(node instanceof ValidateGroup){
			setCurrent(parent)
		}
	}

	@Override
	protected Object createNode(Object name) {
		//add group here
		ValidateGroup currnetGroup = new ValidateGroup(name:name,pojoClassName:getCurrent().pojoClassName)
		//setCurrent(currnetGroup);
		return currnetGroup;
	}

	@Override
	protected Object createNode(Object name, Object value) {
		return null;
	}

	@Override
	protected Object createNode(Object name, Map attributes) {

		if(attributes.containsKey("package") && rules["${attributes['package']}.${name}"] == null){
			rule = new POJOValidatorRule(pojoClassName: "${attributes['package']}.${name}")
			rules["${attributes['package']}.${name}"] = rule
			setCurrent(rule)
			return rule
		}else{

			if(getCurrent() == null){
				return null
			}
			Class targetClass = Class.forName(getCurrent().pojoClassName);
			//TODO 有可能属性不存在。需要check一下
			BeanWrapper bean = new BeanWrapperImpl(targetClass);
            ConstrainedPropertyGunn cp = new ConstrainedPropertyGunn(targetClass,name,bean.getPropertyType(name))
			getCurrent().addConstrained(cp)
			cp.setMessageSource(null);
			for (Object o : attributes.keySet()) {
				String constraintName = (String) o;
				final Object value = attributes.get(constraintName);
				try{
					if (cp.supportsContraint(constraintName)) {
						cp.applyConstraint(constraintName, value);
					}
					else {
						if (ConstrainedPropertyGunn.hasRegisteredConstraint(constraintName)) {
							// constraint is registered but doesn't support this property's type
						}
						else {
							// in the case where the constraint is not supported we still retain meta data
							// about the constraint in case its needed for other things
							cp.addMetaConstraint(constraintName, value);
						}
					}
				}catch(RuntimeException e){
					//分析其中一个规则的时候出错。
					//ConstrainedPropertyQuery 已经log出错误。此处忽略。
				}
			}
			return cp
		}
	}

	@Override
	protected Object createNode(Object name, Map attributes, Object value) {
		return null;
	}
}
