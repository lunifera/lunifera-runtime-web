





package org.lunifera.runtime.web.ecview.services.vaadin.impl

import org.lunifera.runtime.common.annotations.DomainDescription
import org.lunifera.runtime.common.annotations.DomainKey
import org.eclipse.xtext.common.types.JvmDeclaredType
import org.lunifera.dsl.semantic.entity.LEntity
import org.lunifera.dsl.semantic.entity.LBean
import org.lunifera.dsl.semantic.common.types.LAnnotationDef
import org.lunifera.dsl.semantic.common.types.LOperation

class BeanHelper {

	/**
	 * Returns the caption property if it could be found. Null otherwise.
	 */
	def static String findCaptionProperty(Object bean) {
		if (bean == null) {
			return null
		}

		if (bean instanceof Class<?>) {
			return findCaptionProperty(bean as Class<?>)
		} else {
			return findCaptionProperty(bean.class)
		}
	}

	/**
	 * Returns the caption property if it could be found. Null otherwise.
	 */
	def static String findCaptionProperty(Class<?> beanClass) {
		if (beanClass == null) {
			return null
		}
 
		// try to find annotation in class
		for (field : beanClass.declaredFields) {
			if (field.isAnnotationPresent(typeof(DomainKey))) {
				return field.name
			}
		}

		// include super classes too
		for (field : beanClass.fields) {
			if (field.isAnnotationPresent(typeof(DomainKey))) {
				return field.name
			}
		}

		return null
	}
	
	/**
	 * Returns the caption property if it could be found. Null otherwise.
	 */
	def static String findCaptionProperty(LEntity lEntity) {
		var String bestMatch = null
		
		if (lEntity == null) {
			return null
		}

		for (field : lEntity.allAttributes) {
			if (field.domainKey
				|| field.name.equalsIgnoreCase("Name") 
				|| field.name.equalsIgnoreCase("Number") 
				|| field.name.equalsIgnoreCase("Description")
				|| field.uuid
				) {
				bestMatch = field.name
			} 
		}
		return bestMatch

//		// include super classes too
//		for (field : beanClass.fields) {
//			if (field.isAnnotationPresent(typeof(DomainKey))) {
//				return field.name
//			}
//		}

	}
	
	/**
	 * Returns the caption property if it could be found. Null otherwise.
	 */
	def static String findCaptionProperty(LBean lBean) {
		var String bestMatch = null
		
		if (lBean == null) {
			return null
		}

		for (field : lBean.allAttributes) {
			if (field.domainKey
				|| field.name.equalsIgnoreCase("Name") 
				|| field.name.equalsIgnoreCase("Number") 
				|| field.name.equalsIgnoreCase("Description")
				|| field.uuid
				) {
				bestMatch = field.name
			} 
		}
		return bestMatch

//		// include super classes too
//		for (field : beanClass.fields) {
//			if (field.isAnnotationPresent(typeof(DomainKey))) {
//				return field.name
//			}
//		}
	}

	/**
	 * Returns the description property if it could be found. Null otherwise.
	 */
	def static String findDescriptionProperty(Object bean) {
		if (bean == null) {
			return null
		}

		if (bean instanceof Class<?>) {
			return findDescriptionProperty(bean as Class<?>)
		} else {
			return findDescriptionProperty(bean.class)
		}
	}

	/**
	 * Returns the description property if it could be found. Null otherwise.
	 */
	def static String findDescriptionProperty(Class<?> beanClass) {
		if (beanClass == null) {
			return null
		}

		// try to find annotation in fields of class
		for (field : beanClass.declaredFields) {
			if (field.isAnnotationPresent(typeof(DomainDescription))) {
				return field.name
			}
		}

		// try to find annotation in methods of class
		for (method : beanClass.declaredMethods) {
			if (method.isAnnotationPresent(typeof(DomainDescription))) {
				return OperationExtensions.toPropertyName(method.name)
			}
		}

		// include super classes too
		for (field : beanClass.fields) {
			if (field.isAnnotationPresent(typeof(DomainDescription))) {
				return field.name
			}
		}

		// include super class too
		for (method : beanClass.methods) {
			if (method.isAnnotationPresent(typeof(DomainDescription))) {
				return OperationExtensions.toPropertyName(method.name)
			}
		}

		return null
	}

	/**
	 * Returns the description property if it could be found. Null otherwise.
	 */
	def static String findDescriptionProperty(LEntity entity) {
		if (entity == null) {
			return null
		}

		// try to find annotation in fields of class
		for (field : entity.attributes) {
			if (field.domainDescription) {
				return field.name
			}
		}

		// try to find annotation in methods of class
		for (method : entity.operations) {
			val LAnnotationDef def = method.annotations.findFirst[
				it.annotation.annotationType.qualifiedName.equals((typeof(DomainDescription)).canonicalName)
			]; 
			if(def != null){
				return OperationExtensions.toPropertyName(method.name)
			}
		}

		// include super classes too
		for (field : entity.allAttributes) {
			if (field.domainDescription) {
				return field.name
			}
		}

		// include super class too
		for (method : entity.allFeatures) {
			if (method instanceof LOperation){
				val LAnnotationDef def = method.annotations.findFirst[
					it.annotation.annotationType.qualifiedName.equals((typeof(DomainDescription)).canonicalName)
				]; 
				if(def != null){
					return OperationExtensions.toPropertyName(method.name)
				}
			}
		}

		return null
	}
	
		/**
	 * Returns the description property if it could be found. Null otherwise.
	 */
	def static String findDescriptionProperty(LBean bean) {
		if (bean == null) {
			return null
		}

		// try to find annotation in fields of class
		for (field : bean.attributes) {
			if (field.domainDescription) {
				return field.name
			}
		}

		// try to find annotation in methods of class
		for (method : bean.operations) {
			val LAnnotationDef def = method.annotations.findFirst[
				it.annotation.annotationType.qualifiedName.equals((typeof(DomainDescription)).canonicalName)
			]; 
			if(def != null){
				return OperationExtensions.toPropertyName(method.name)
			}
		}

		// include super classes too
		for (field : bean.allAttributes) {
			if (field.domainDescription) {
				return field.name
			}
		}

		// include super class too
		for (method : bean.allFeatures) {
			if (method instanceof LOperation){
				val LAnnotationDef def = method.annotations.findFirst[
					it.annotation.annotationType.qualifiedName.equals((typeof(DomainDescription)).canonicalName)
				]; 
				if(def != null){
					return OperationExtensions.toPropertyName(method.name)
				}
			}
		}

		return null
	}

	

}

