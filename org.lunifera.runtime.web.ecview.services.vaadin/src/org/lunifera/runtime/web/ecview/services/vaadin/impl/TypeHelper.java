

/**
 * Copyright (c) 2011 - 2015, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *         Florian Pirchner - Initial implementation
 */

package org.lunifera.runtime.web.ecview.services.vaadin.impl;

import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.util.TypeReferences;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@SuppressWarnings("restriction")
@Singleton
public class TypeHelper {

	@Inject
	private TypeReferences typeReferences;

	/**
	 * Returns true, if the type is numeric
	 * 
	 * @param type
	 * @return 
	 */
	public boolean isNumber(JvmType type) {
		if (typeReferences.is(type, Byte.class)
				|| typeReferences.is(type, Byte.TYPE)
				|| typeReferences.is(type, Short.class)
				|| typeReferences.is(type, Short.TYPE)
				|| typeReferences.is(type, Integer.class)
				|| typeReferences.is(type, Integer.TYPE)
				|| typeReferences.is(type, Long.class)
				|| typeReferences.is(type, Long.TYPE)
				|| typeReferences.is(type, Float.class)
				|| typeReferences.is(type, Float.TYPE)
				|| typeReferences.is(type, Double.class)
				|| typeReferences.is(type, Double.TYPE)) {
			return true;
		}
		return false;
	}

	/**
	 * Returns true, if the type is a decimal number
	 * 
	 * @param type
	 * @return
	 */
	public boolean isDecimal(JvmType type) {
		if (typeReferences.is(type, Float.class)
				|| typeReferences.is(type, Float.TYPE)
				|| typeReferences.is(type, Double.class)
				|| typeReferences.is(type, Double.TYPE)) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the qualified name of the given numeric type. Throws exception,
	 * if the given type is not a valid number.
	 * 
	 * @param name
	 * @return
	 */
	public String toNumericQualifiedName(JvmType type) {
		return toNumericType(type).getName();
	}

	/**
	 * Returns the class file of the given numeric type. Throws exception, if
	 * the given type is not a valid number.
	 * 
	 * @param type
	 * @return
	 */
	public Class<? extends Number> toNumericType(JvmType type) {
		if (typeReferences.is(type, Byte.class)
				|| typeReferences.is(type, Byte.TYPE)) {
			return Byte.class;
		} else if (typeReferences.is(type, Short.class)
				|| typeReferences.is(type, Short.TYPE)) {
			return Short.class;
		} else if (typeReferences.is(type, Integer.class)
				|| typeReferences.is(type, Integer.TYPE)) {
			return Integer.class;
		} else if (typeReferences.is(type, Long.class)
				|| typeReferences.is(type, Long.TYPE)) {
			return Long.class;
		} else if (typeReferences.is(type, Float.class)
				|| typeReferences.is(type, Float.TYPE)) {
			return Float.class;
		} else if (typeReferences.is(type, Double.class)
				|| typeReferences.is(type, Double.TYPE)) {
			return Double.class;
		}
		throw new IllegalArgumentException(type + " is not a valid number type");
	}

	/**
	 * Returns true, if the type is boolean
	 * 
	 * @param type
	 * @return
	 */
	public boolean isBoolean(JvmType type) {
		if (typeReferences.is(type, Boolean.class)
				|| typeReferences.is(type, Boolean.TYPE)) {
			return true;
		}
		return false;
	}

	/**
	 * Returns true, if the type is boolean
	 * 
	 * @param type
	 * @return
	 */
	public boolean isString(JvmType type) {
		if (typeReferences.is(type, String.class)) {
			return true;
		}
		return false;
	}
}
