/**
 * Copyright 2009-2013 Oy Vaadin Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * 
 * Contributor: Florian Pirchner - Extracted Interface and converted to OSGi-service
 */
package org.lunifera.runtime.web.vaadin.filter.jpa;

import java.util.List;

import org.lunifera.runtime.web.vaadin.common.IFilterProvider;
import org.lunifera.runtime.web.vaadin.filter.jpa.util.CollectionUtil;
import org.osgi.service.component.annotations.Component;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Between;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.filter.Compare.Greater;
import com.vaadin.data.util.filter.Compare.GreaterOrEqual;
import com.vaadin.data.util.filter.Compare.Less;
import com.vaadin.data.util.filter.Compare.LessOrEqual;
import com.vaadin.data.util.filter.IsNull;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Not;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;

/**
 * Utility class for creating filter instances.
 * 
 * @author Petter Holmström (Vaadin Ltd)
 * @since 1.0
 */
@Component(service = { IFilterProvider.class })
public final class Filters implements IFilterProvider {

	public Filters() {
		// To prevent applications from creating instances of this class.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lunifera.runtime.web.vaadin.filter.jpa.IFilterProvider#isNull(java
	 * .lang.Object)
	 */
	@Override
	public Filter isNull(Object propertyId) {
		return new IsNull(propertyId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lunifera.runtime.web.vaadin.filter.jpa.IFilterProvider#isNotNull(
	 * java.lang.Object)
	 */
	@Override
	public Filter isNotNull(Object propertyId) {
		return new Not(isNull(propertyId));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lunifera.runtime.web.vaadin.filter.jpa.IFilterProvider#isEmpty(java
	 * .lang.Object)
	 */
	@Override
	public Filter isEmpty(Object propertyId) {
		return new Equal(propertyId, "");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lunifera.runtime.web.vaadin.filter.jpa.IFilterProvider#isNotEmpty
	 * (java.lang.Object)
	 */
	@Override
	public Filter isNotEmpty(Object propertyId) {
		return new Not(isEmpty(propertyId));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lunifera.runtime.web.vaadin.filter.jpa.IFilterProvider#eq(java.lang
	 * .Object, java.lang.String, boolean)
	 */
	@Override
	public Filter eq(Object propertyId, String value, boolean caseSensitive) {
		return new SimpleStringFilter(propertyId, value, !caseSensitive, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lunifera.runtime.web.vaadin.filter.jpa.IFilterProvider#like(java.
	 * lang.Object, java.lang.String, boolean)
	 */
	@Override
	public Filter like(Object propertyId, String value, boolean caseSensitive) {
		return new Like(propertyId.toString(), value, caseSensitive);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lunifera.runtime.web.vaadin.filter.jpa.IFilterProvider#eq(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public Filter eq(Object propertyId, Object value) {
		return new Equal(propertyId, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lunifera.runtime.web.vaadin.filter.jpa.IFilterProvider#gteq(java.
	 * lang.Object, java.lang.Object)
	 */
	@Override
	public Filter gteq(Object propertyId, Object value) {
		return new GreaterOrEqual(propertyId, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lunifera.runtime.web.vaadin.filter.jpa.IFilterProvider#gt(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public Filter gt(Object propertyId, Object value) {
		return new Greater(propertyId, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lunifera.runtime.web.vaadin.filter.jpa.IFilterProvider#lteq(java.
	 * lang.Object, java.lang.Object)
	 */
	@Override
	public Filter lteq(Object propertyId, Object value) {
		return new LessOrEqual(propertyId, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lunifera.runtime.web.vaadin.filter.jpa.IFilterProvider#lt(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public Filter lt(Object propertyId, Object value) {
		return new Less(propertyId, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lunifera.runtime.web.vaadin.filter.jpa.IFilterProvider#between(java
	 * .lang.Object, java.lang.Comparable, java.lang.Comparable)
	 */
	@Override
	public Filter between(Object propertyId, Comparable<?> startingPoint,
			Comparable<?> endingPoint) {
		return new Between(propertyId, startingPoint, endingPoint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lunifera.runtime.web.vaadin.filter.jpa.IFilterProvider#between(java
	 * .lang.Object, java.lang.Object, java.lang.Object, boolean, boolean)
	 */
	@Override
	public Filter between(Object propertyId, Object startingPoint,
			Object endingPoint, boolean includeStartingPoint,
			boolean includeEndingPoint) {
		return new And((includeStartingPoint ? new GreaterOrEqual(propertyId,
				startingPoint) : new Greater(propertyId, startingPoint)),
				(includeEndingPoint ? new LessOrEqual(propertyId, endingPoint)
						: new Less(propertyId, endingPoint)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lunifera.runtime.web.vaadin.filter.jpa.IFilterProvider#betweenInclusive
	 * (java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public Filter betweenInclusive(Object propertyId, Object startingPoint,
			Object endingPoint) {
		return between(propertyId, startingPoint, endingPoint, true, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lunifera.runtime.web.vaadin.filter.jpa.IFilterProvider#betweenExlusive
	 * (java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public Filter betweenExlusive(Object propertyId, Object startingPoint,
			Object endingPoint) {
		return between(propertyId, startingPoint, endingPoint, false, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lunifera.runtime.web.vaadin.filter.jpa.IFilterProvider#outside(java
	 * .lang.Object, java.lang.Object, java.lang.Object, boolean, boolean)
	 */
	@Override
	public Filter outside(Object propertyId, Object startingPoint,
			Object endingPoint, boolean includeStartingPoint,
			boolean includeEndingPoint) {
		return new Or((includeStartingPoint ? new LessOrEqual(propertyId,
				startingPoint) : new Less(propertyId, startingPoint)),
				(includeEndingPoint ? new GreaterOrEqual(propertyId,
						endingPoint) : new Greater(propertyId, endingPoint)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lunifera.runtime.web.vaadin.filter.jpa.IFilterProvider#outsideInclusive
	 * (java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public Filter outsideInclusive(Object propertyId, Object startingPoint,
			Object endingPoint) {
		return outside(propertyId, startingPoint, endingPoint, true, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lunifera.runtime.web.vaadin.filter.jpa.IFilterProvider#outsideExclusive
	 * (java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public Filter outsideExclusive(Object propertyId, Object startingPoint,
			Object endingPoint) {
		return outside(propertyId, startingPoint, endingPoint, false, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lunifera.runtime.web.vaadin.filter.jpa.IFilterProvider#not(com.vaadin
	 * .data.Container.Filter)
	 */
	@Override
	public Filter not(Filter filter) {
		return new Not(filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lunifera.runtime.web.vaadin.filter.jpa.IFilterProvider#and(com.vaadin
	 * .data.Container.Filter)
	 */
	@Override
	public And and(Filter... filters) {
		return new And(filters);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lunifera.runtime.web.vaadin.filter.jpa.IFilterProvider#and(java.util
	 * .List)
	 */
	@Override
	public And and(List<Filter> filters) {
		return new And(CollectionUtil.toArray(Filter.class, filters));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lunifera.runtime.web.vaadin.filter.jpa.IFilterProvider#or(com.vaadin
	 * .data.Container.Filter)
	 */
	@Override
	public Or or(Filter... filters) {
		return new Or(filters);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lunifera.runtime.web.vaadin.filter.jpa.IFilterProvider#or(java.util
	 * .List)
	 */
	@Override
	public Or or(List<Filter> filters) {
		return new Or(CollectionUtil.toArray(Filter.class, filters));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lunifera.runtime.web.vaadin.filter.jpa.IFilterProvider#joinFilter
	 * (java.lang.String, com.vaadin.data.Container.Filter)
	 */
	public JoinFilter joinFilter(String joinProperty, Filter... filters) {
		return new JoinFilter(joinProperty, filters);
	}
}
