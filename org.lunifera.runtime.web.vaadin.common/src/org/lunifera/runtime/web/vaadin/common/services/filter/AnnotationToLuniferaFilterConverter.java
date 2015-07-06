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

import org.lunifera.dsl.dto.lib.services.filters.ILFilter;
import org.lunifera.dsl.dto.lib.services.filters.LAnd;
import org.lunifera.dsl.dto.lib.services.filters.LCompare;
import org.lunifera.dsl.dto.lib.services.filters.LNot;
import org.lunifera.runtime.common.annotations.TargetEnumConstraint;
import org.lunifera.runtime.common.annotations.TargetEnumConstraints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converts filter annotations to lunifera filters.
 */
public class AnnotationToLuniferaFilterConverter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AnnotationToLuniferaFilterConverter.class);

	/**
	 * Creates zero or more filters for the given annotation. If more filters
	 * are being created, they will be put into an And-Filter.<br>
	 * Returns <code>null</code> if no filter have been created.
	 * 
	 * @param annotation
	 * @return
	 */
	public static ILFilter createFilter(TargetEnumConstraints annotation) {
		if (annotation != null) {
			Set<ILFilter> filters = new HashSet<ILFilter>();
			for (TargetEnumConstraint constraint : annotation.constraints()) {
				ILFilter filter = createFilterFor(constraint);
				if (filter != null) {
					filters.add(filter);
				}
			}
			if (filters.size() == 1) {
				return filters.iterator().next();
			} else if (filters.size() > 1) {
				return new LAnd(filters.toArray(new ILFilter[filters.size()]));
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
	public static ILFilter createFilterFor(TargetEnumConstraint constraint) {
		if (constraint == null) {
			return null;
		}
		Enum<?> enumLiteral = null;
		try {
			@SuppressWarnings("unchecked")
			Class<Enum<?>> enumClass = (Class<Enum<?>>) constraint.enumClass();
			for (Enum<?> enumx : enumClass.getEnumConstants()) {
				if (enumx.name().equals(enumLiteral)) {
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

		ILFilter result = null;
		switch (constraint.compareType()) {
		case EQUALS:
			result = new LCompare.Equal(constraint.targetProperty(),
					enumLiteral);
			break;
		case GREATER:
			result = new LCompare.Greater(constraint.targetProperty(),
					enumLiteral);
			break;
		case GREATER_EQ:
			result = new LCompare.GreaterOrEqual(constraint.targetProperty(),
					enumLiteral);
			break;
		case LOWER:
			result = new LCompare.Less(constraint.targetProperty(), enumLiteral);
			break;
		case LOWER_EQ:
			result = new LCompare.LessOrEqual(constraint.targetProperty(),
					enumLiteral);
			break;
		case NOT_EQ:
			result = new LNot(new LCompare.Equal(constraint.targetProperty(),
					enumLiteral));
			break;
		}
		return result;
	}

}
