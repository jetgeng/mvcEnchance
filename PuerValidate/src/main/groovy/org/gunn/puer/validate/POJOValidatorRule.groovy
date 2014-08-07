package org.gunn.puer.validate

import org.springframework.validation.Errors


class POJOValidatorRule extends ValidateGroup {
	String siteFilter
	Map<String, ValidateGroup> groups = [:]

	static DEFAULT_GROUP = "default_group"

	def void addGroup(String name,ValidateGroup group){
		groups[name] = group
	}

	void validate(Object target, Errors errors , String...groupNames){
		//验证有规则存在的
		for(String group : groupNames){
			if(group == DEFAULT_GROUP){
				validate(target, errors)
			}else{
				groups[group]?.validate(target, errors);
			}
		}
	}

	@Override
	public String toString() {
		return "POJOValidatorRule [pojoClassName=" + pojoClassName + ", siteFilter=" + siteFilter
		+ ", constrainedProperties=" + constrainedProperties + "]";
	}

}
