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
package org.lunifera.runtime.web.vaadin.common.services.filter;

import java.util.HashSet;
import java.util.Set;

import org.lunifera.runtime.common.annotations.TargetEnumConstraint;
import org.lunifera.runtime.common.annotations.TargetEnumConstraints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Not;

/**
 * Converts filter annotations to vaadin filters.
 */
public class AnnotationToVaadinFilterConverter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AnnotationToVaadinFilterConverter.class);

	/**
	 * Creates zero or more filters for the given annotation. If more filters
	 * are being created, they will be put into an And-Filter.<br>
	 * Returns <code>null</code> if no filter have been created.
	 * 
	 * @param annotation
	 * @return
	 */
	public static Filter createFilter(TargetEnumConstraints annotation) {
		if (annotation != null) {
			Set<Filter> filters = new HashSet<Filter>();
			for (TargetEnumConstraint constraint : annotation.constraints()) {
				Filter filter = createFilterFor(constraint);
				if (filter != null) {
					filters.add(filter);
				}
			}
			if (filters.size() == 1) {
				return filters.iterator().next();
			} else if (filters.size() > 1) {
				return new And(filters.toArray(new Filter[filters.size()]));
			}
		}

		return null;
	}

	/**
	 * Creates one filter for the given annotation.
	 * 
	 * @param annotation
	 * @return
	 */
	public static Filter createFilterFor(TargetEnumConstraint constraint) {
		if (constraint == null) {
			return null;
		}
		Enum<?> enumLiteral = null;
		try {
			@SuppressWarnings("unchecked")
			Class<Enum<?>> enumClass = (Class<Enum<?>>) constraint.enumClass();
			for (Enum<?> enumx : enumClass.getEnumConstants()) {
				if (enumx.name().equals(constraint.enumLiteral())) {
					enumLiteral = enumx;
					break;
				}
			}
		} catch (Exception e) {
			LOGGER.error("{}", e);
		}

		if (enumLiteral == null) {
			return null;
		}

		Filter result = null;
		switch (constraint.compareType()) {
		case EQUALS:
			result = new Compare.Equal(constraint.targetProperty(), enumLiteral);
			break;
		case GREATER:
			result = new Compare.Greater(constraint.targetProperty(),
					enumLiteral);
			break;
		case GREATER_EQ:
			result = new Compare.GreaterOrEqual(constraint.targetProperty(),
					enumLiteral);
			break;
		case LOWER:
			result = new Compare.Less(constraint.targetProperty(), enumLiteral);
			break;
		case LOWER_EQ:
			result = new Compare.LessOrEqual(constraint.targetProperty(),
					enumLiteral);
			break;
		case NOT_EQ:
			result = new Not(new Compare.Equal(constraint.targetProperty(),
					enumLiteral));
			break;
		}
		return result;
	}

}
