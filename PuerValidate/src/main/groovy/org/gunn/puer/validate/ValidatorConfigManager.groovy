package org.gunn.puer.validate

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.validation.Errors
import org.springframework.validation.Validator


class ValidatorConfigManager implements Validator{

	def validateRuleContainer = [:]

	Logger logger = LoggerFactory.getLogger(ValidatorConfigManager)

	def init(){
		logger.info("init the validator config manager with validator.properties")
		def url = getClass().getClassLoader().getResource("validator.groovy")
		String configContext
		new File(url.getFile()).each{ it ->  configContext = configContext + "\n" + it}
		logger.info(configContext)
		configValidateRules(configContext)
	}

	def configValidateRules(String configContext, boolean isAppend = false){
		logger.info("begin config validate")
		def validatorRules = runDSL(configContext)
		if(isAppend){
			validatorRules.each{ key , value ->
				def oldRules = validateRuleContainer[key]
				oldRules = oldRules + value
				validateRuleContainer[key] = oldRules
			}
		} else {
			validateRuleContainer = validatorRules
		}
	}

	/**
	 * 通过运行DSL，获取配置对象。
	 * @return
	 */
	def runDSL(configContext){
		Script dslScript = new GroovyShell().parse(configContext);
		dslScript.metaClass.mixin(SpringValidatorConfigureationDelegate)
		return dslScript.run();
	}

	boolean supports(Class<?> clazz){
		if(validateRuleContainer.size() == 0){
			init()
		}
		return getValidateRule(clazz) != null
	}

	void validate(Object target, Errors errors){
		POJOValidatorRule rule = getValidateRule(target.getClass())
        def beforeValidateMethod = target.getMetaClass().getMetaMethod("beforeValidate")
        if(beforeValidateMethod != null){
            beforeValidateMethod.invoke(target, null)
        }
		rule?.validate(target, errors)
		target.properties.each {key,val ->
			[Collection, Object[]].any(){
				if(it.isAssignableFrom(val.getClass())){
					val.each{ validate(it, errors) }
				}
			}
		}
	}

	void validate(Object target, Errors errors , String...groups){
		POJOValidatorRule rule = getValidateRule(target.getClass())
		rule?.validate(target, errors, groups)
	}


	def getValidateRule(Class<?> clazz){
		def rule = null
		logger.debug("the all rule is:" + validateRuleContainer)
		return rule?:validateRuleContainer["*"]?.get(clazz.getName())
	}
}
