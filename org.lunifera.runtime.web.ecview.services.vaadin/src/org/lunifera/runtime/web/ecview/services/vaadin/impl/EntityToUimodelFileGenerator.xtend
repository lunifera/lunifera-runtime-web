/**
 * Copyright (c) 2011 - 2014, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Klemens Edler - Initial implementation
 */
package org.lunifera.runtime.web.ecview.services.vaadin.impl

import javax.inject.Inject
import org.eclipse.emf.ecore.EObject
import org.lunifera.dsl.semantic.common.types.LDataType
import org.lunifera.dsl.semantic.common.types.LReference
import org.lunifera.dsl.semantic.common.types.LTypedPackage
import org.lunifera.dsl.semantic.common.types.LUpperBound
import org.lunifera.dsl.semantic.entity.LBean
import org.lunifera.dsl.semantic.entity.LBeanAttribute
import org.lunifera.dsl.semantic.entity.LBeanReference
import org.lunifera.dsl.semantic.entity.LEntity
import org.lunifera.dsl.semantic.entity.LEntityAttribute
import org.lunifera.dsl.semantic.entity.LEntityFeature
import org.lunifera.dsl.semantic.entity.LEntityReference

/**
 *  This generator automatically creates a generic .uimodel-file from a given entity.
 * 	
 */
class EntityToUimodelFileGenerator {

	@Inject
	private TypeHelper typeHelper
	
	Bindings bindings = new Bindings("")
	Counter counter = new Counter(0)
	 
	def getContent(LEntity entity) '''
«««		«entity.toDocu»
		package «entity.toUimodelName»
		
		import «(entity.eContainer as LTypedPackage).name».*
		
		ideview «entity.name» {
			
			datasource ds: «entity.toEntityFQN»
			
			horizontalLayout «entity.name» {
				
				form leftForm {
					«FOR LEntityFeature feature: entity.features»
«««					«feature.toDocu»
					«IF counter.value % 2 == 0»
					«IF feature instanceof LEntityAttribute»
					«IF feature.type instanceof LDataType»
					«feature.toAttributeUiField»
					«ELSE»
					«feature.toBeanRefUiField»
					«ENDIF»
					«ELSEIF feature instanceof LReference»
					«feature.toEntityRefUiField»
					«ENDIF»
					«ENDIF»
					«counter.value = counter.value + 1»
					«ENDFOR»
				}
				
				«counter.value = 0»
				form rightForm {
					«FOR LEntityFeature feature: entity.features»
«««					«feature.toDocu»
					«IF counter.value % 2 == 1»
					«IF feature instanceof LEntityAttribute»
					«IF feature.type instanceof LDataType»
					«feature.toAttributeUiField»
					«ELSE»
					«feature.toBeanRefUiField»
					«ENDIF»
					«ELSEIF feature instanceof LReference»
					«feature.toEntityRefUiField»
					«ENDIF»
					«ENDIF»
					«counter.value = counter.value + 1»
					«ENDFOR»
				}
			«bindings.bindingList»
			
			}
		}
	'''
	
	

	def String getToUimodelName(LEntity entity) {
		return (entity.eContainer as LTypedPackage).name + ".uimodel"
	}

	def String getToEntityFQN(LEntity entity) {
		return (entity.eContainer as LTypedPackage).name + "." + entity.name
	}

	def String getToAttributeUiField(LEntityAttribute attribute) {
		val LDataType datatype = attribute.type as LDataType
		if (datatype.date) {
			bindings.add('''bind ds.«attribute.name» <--> [this.«IF counter.value %2 == 0»leftform«ELSE»rightform«ENDIF».«attribute.name»].value''')
			return '''
			datefield «attribute.name»
			'''
		} else if(typeHelper.isString(datatype.jvmTypeReference.type)){
			bindings.add('''bind ds.«attribute.name» <--> [this.«IF counter.value %2 == 0»leftform«ELSE»rightform«ENDIF».«attribute.name»].value''')
			return '''
			textfield «attribute.name»
			'''
		} else if(typeHelper.isBoolean(datatype.jvmTypeReference.type)){
			bindings.add('''bind ds.«attribute.name» <--> [this.«IF counter.value %2 == 0»leftform«ELSE»rightform«ENDIF».«attribute.name»].value''')
			return '''
			checkbox «attribute.name»
			'''
		} else if(typeHelper.isNumber(datatype.jvmTypeReference.type)){
			if(typeHelper.isDecimal(datatype.jvmTypeReference.type)){
				bindings.add('''bind ds.«attribute.name» <--> [this.«IF counter.value %2 == 0»leftform«ELSE»rightform«ENDIF».«attribute.name»].value''')
				return '''
				decimalField «attribute.name»
				'''
			} else {
				bindings.add('''bind ds.«attribute.name» <--> [this.«IF counter.value %2 == 0»leftform«ELSE»rightform«ENDIF».«attribute.name»].value''')
				return '''
				numericField «attribute.name»
				'''
			}
		}
	}
	
	def String getToEntityRefUiField(LEntityFeature feature) {
		val ref = feature as LEntityReference
		
		if (ref.multiplicity.upper == LUpperBound.ONE) {
			bindings.add('''bind ds.«feature.name» <--> [this.«IF counter.value %2 == 0»leftform«ELSE»rightform«ENDIF».«feature.name»].value''')
			return '''
			referenceField «feature.name» {
				type «ref.type.name»
				captionField «BeanHelper.findCaptionProperty(feature.eContainer as LEntity)»
			}
			'''
		} 
		
		if (ref.multiplicity.upper == LUpperBound.MANY) {
			bindings.add('''bind ds.«feature.name» <--> [this.«IF counter.value %2 == 0»leftform«ELSE»rightform«ENDIF».«feature.name»].collection''')
			return '''
			table «feature.name» {
				type «ref.type.name»
				columns {
					column «BeanHelper.findCaptionProperty(feature.eContainer as LEntity)»
					column «BeanHelper.findDescriptionProperty(feature.eContainer as LEntity)»
				}
			}
			'''
		}
	}
	
	def String getToBeanRefUiField(LEntityAttribute attribute) {
		val type = attribute.type
		
		if(type instanceof LBean){
			return '''
			panel «attribute.name» {
				content horizontalLayout {
					form {
						«FOR LBeanAttribute feature: type.allAttributes»
«««						«feature.toDocu»
						«IF feature instanceof LBeanAttribute»
						«IF feature.type instanceof LDataType»
						«feature.toBeanAttributeUiField»
						«ELSE»
						«feature.toBeantoBeanRefUiField»
						«ENDIF»
						«ELSEIF feature instanceof LReference»
						«feature.toBeanReferenceUiField»
						«ENDIF»
						«ENDFOR»
					}
				}
			}
			'''
		}
	}
	
	def String getToBeantoBeanRefUiField(LBeanAttribute attribute) {
		val ref = attribute as LBeanReference
		
		if (ref.multiplicity.upper == LUpperBound.ONE) {
			bindings.add('''bind ds.«attribute.name» <--> [this.«IF counter.value %2 == 0»leftform«ELSE»rightform«ENDIF».«attribute.name»].value''')
			return '''
			referenceField «attribute.name» {
				type «ref.type.name»
				captionField «BeanHelper.findCaptionProperty(attribute.eContainer as LBean)»
			}
			'''
		} 
		
		if (ref.multiplicity.upper == LUpperBound.MANY) {
			bindings.add('''bind ds.«attribute.name» <--> [this.«IF counter.value %2 == 0»leftform«ELSE»rightform«ENDIF».«attribute.name»].collection''')
			return '''
			table «attribute.name» {
				type «ref.type.name»
				columns {
					column «BeanHelper.findCaptionProperty(attribute.eContainer as LBean)»
					column «BeanHelper.findDescriptionProperty(attribute.eContainer as LBean)»
				}
			}
			'''
		}
	}
	
	def String getToBeanAttributeUiField(LBeanAttribute attribute) {
		val LDataType datatype = attribute.type as LDataType
		val String beanName = (attribute.eContainer as LBean).name.toFirstLower
		if (datatype.date) {
			bindings.add('''bind ds.«beanName».«attribute.name» <--> [this.«IF counter.value %2 == 0»leftform«ELSE»rightform«ENDIF».«beanName».«attribute.name»].value''')
			return '''
			datefield «attribute.name»
			'''
		} else if(typeHelper.isString(datatype.jvmTypeReference.type)){
			bindings.add('''bind ds.«beanName».«attribute.name» <--> [this.«IF counter.value %2 == 0»leftform«ELSE»rightform«ENDIF».«beanName».«attribute.name»].value''')
			return '''
			textfield «attribute.name»
			'''
		} else if(typeHelper.isBoolean(datatype.jvmTypeReference.type)){
			bindings.add('''bind ds.«beanName».«attribute.name» <--> [this.«IF counter.value %2 == 0»leftform«ELSE»rightform«ENDIF».«beanName».«attribute.name»].value''')
			return '''
			checkbox «attribute.name»
			'''
		} else if(typeHelper.isNumber(datatype.jvmTypeReference.type)){
			if(typeHelper.isDecimal(datatype.jvmTypeReference.type)){
				bindings.add('''bind ds.«beanName».«attribute.name» <--> [this.«IF counter.value %2 == 0»leftform«ELSE»rightform«ENDIF».«beanName».«attribute.name»].value''')
				return '''
				decimalField «attribute.name»
				'''
			} else {
				bindings.add('''bind ds.«beanName».«attribute.name» <--> [this.«IF counter.value %2 == 0»leftform«ELSE»rightform«ENDIF».«beanName».«attribute.name»].value''')
				return '''
				numericField «attribute.name»
				'''
			}
		}
		
	}
	
	def String getToBeanReferenceUiField(LBeanAttribute attribute){
		val ref = attribute as LBeanReference
		
		if (ref.multiplicity.upper == LUpperBound.ONE) {
			return '''
			referenceField «attribute.name» {
				type «ref.type.name»
				captionField uuid
			}
			'''
		} 
		
		if (ref.multiplicity.upper == LUpperBound.MANY) {
			return '''
			table «attribute.name» {
				type «ref.type.name»
				columns {
					column uuid
				}
			}
			'''
		}
	}
	
	def toDocu(EObject element) {
//		if (element.documentation.nullOrEmpty) return ""
//		var docu = element.documentation
//		if (!docu.nullOrEmpty) {
//			var docus = docu.split('\n')
//			if (docus.length > 1) {
//				return '''
//				/**
//				«FOR line : docus»
//					«" * "»«line»
//				«ENDFOR»
//				 */'''.toString
//			} else if (docus.length == 1) {
//				return '''/** «docu» */'''.toString
//			}
//		}
		return ""
	}
}

class Counter {
	
	@Property
	var int value
	
	new(int value) {
		this.value = value
	}
	
}

class Bindings {
	
	@Property
	var String bindingList
	
	new(String bindingList) {
		this.bindingList = bindingList
	}
	
	def add(String string){
		bindingList = bindingList.concat("\n").concat(string)
	}
}


