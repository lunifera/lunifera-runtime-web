/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Based on ideas of org.eclipse.jface.databinding.swt (EPL)
 * 
 * Contributor:
 * 		Florian Pirchner - porting swt databinding to support vaadin
 * 
 *******************************************************************************/

package org.lunifera.runtime.web.vaadin.databinding.component.internal;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;
import org.lunifera.runtime.web.vaadin.databinding.properties.AbstractVaadinValueProperty;

/**
 * Uses reflection to access getter and setter method.
 */
public class SimpleAccessorProperty extends AbstractVaadinValueProperty {

	private Class<?> type;
	private Method getter;
	private Method setter;

	public SimpleAccessorProperty(Class<?> componentClass, String property) {
		super();

		PropertyDescriptor result = getPropertyDescriptor(componentClass,
				property);
		this.getter = PropertyUtils.getReadMethod(result);
		this.setter = PropertyUtils.getWriteMethod(result);
		this.type = getter.getReturnType();
	}

	/**
	 * Returns the property descriptor for the given class and the property.
	 * 
	 * @param componentClass
	 * @param property
	 * @return
	 */
	protected PropertyDescriptor getPropertyDescriptor(Class<?> componentClass,
			String property) {
		PropertyDescriptor result = null;
		for (PropertyDescriptor descriptor : PropertyUtils
				.getPropertyDescriptors(componentClass)) {
			if (descriptor.getName().equals(property)) {
				result = descriptor;
				break;
			}
		}
		return result;
	}

	public Object getValueType() {
		return type;
	}

	protected Object doGetValue(Object source) {
		try {
			return getter.invoke(source);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	protected void doSetValue(Object source, Object value) {
		if (setter == null) {
			throw new UnsupportedOperationException("Not supported!");
		}
		try {
			setter.invoke(source, value);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}