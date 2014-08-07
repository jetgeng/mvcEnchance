package org.gunn.puer.validate


class SpringValidatorConfigureationDelegate {
	//可以直接将Context 放到ValidatorRuleBuilder中就可以了。
	//出来的东西就ok了。
	 def rulesBySite = [:]
	 Object rules(param , Closure closure = null) {
		if(closure != null){
			closure = (Closure)closure.clone();
			def binding = new Binding()
			param.each { key, value -> binding.setVariable(key, value)}
			ValidatorConfigurationBuilder eb = new ValidatorConfigurationBuilder()
			closure.setDelegate(eb)
			closure.setBinding(binding)
			closure.setResolveStrategy(Closure.DELEGATE_FIRST)
		 	closure.call()
			def validatorRules = eb.rules
			validatorRules.each { key,value -> value.siteFilter = param["site"] }
			rulesBySite[param["site"]] = validatorRules
			return rulesBySite
		}
	}
}
