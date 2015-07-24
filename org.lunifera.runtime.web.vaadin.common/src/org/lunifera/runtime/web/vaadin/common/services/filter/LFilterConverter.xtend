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
package org.lunifera.runtime.web.vaadin.common.services.filter

import com.vaadin.data.Container
import com.vaadin.data.util.filter.And
import com.vaadin.data.util.filter.Between
import com.vaadin.data.util.filter.Compare
import com.vaadin.data.util.filter.IsNull
import com.vaadin.data.util.filter.Like
import com.vaadin.data.util.filter.Not
import com.vaadin.data.util.filter.Or
import com.vaadin.data.util.filter.SimpleStringFilter
import org.lunifera.dsl.dto.lib.services.filters.ILFilter
import org.lunifera.dsl.dto.lib.services.filters.LAnd
import org.lunifera.dsl.dto.lib.services.filters.LBetween
import org.lunifera.dsl.dto.lib.services.filters.LCompare
import org.lunifera.dsl.dto.lib.services.filters.LIsNull
import org.lunifera.dsl.dto.lib.services.filters.LLike
import org.lunifera.dsl.dto.lib.services.filters.LNot
import org.lunifera.dsl.dto.lib.services.filters.LOr
import org.lunifera.dsl.dto.lib.services.filters.LSimpleStringFilter

/**
 * This converter will convert from Vaadin-Filters to Lunifera-Filters.
 * Lunifera-Filters are more common and used by the data services. For instance
 * JPAServices to access data.
 */
class LFilterConverter {

	def dispatch ILFilter convert(Container.Filter vFilter) {
		throw new UnsupportedOperationException("Not a valid type: " + vFilter)
	}

	def dispatch ILFilter convert(And vFilter) {

		val children = newArrayList()
		vFilter.filters.forEach [
			children += it.convert
		]

		val result = new LAnd(children)
		return result;
	}

	def dispatch ILFilter convert(Or vFilter) {

		val children = newArrayList()
		vFilter.filters.forEach [
			children += it.convert
		]

		val result = new LOr(children)
		return result;
	}

	def dispatch ILFilter convert(Between vFilter) {
		return new LBetween(vFilter.propertyId, vFilter.startValue, vFilter.endValue)
	}

	def dispatch ILFilter convert(Compare.Equal vFilter) {
		return new LCompare.Equal(vFilter.propertyId, vFilter.value)
	}

	def dispatch ILFilter convert(Compare.Greater vFilter) {
		return new LCompare.Greater(vFilter.propertyId, vFilter.value)
	}

	def dispatch ILFilter convert(Compare.GreaterOrEqual vFilter) {
		return new LCompare.GreaterOrEqual(vFilter.propertyId, vFilter.value)
	}

	def dispatch ILFilter convert(Compare.Less vFilter) {
		return new LCompare.Less(vFilter.propertyId, vFilter.value)
	}

	def dispatch ILFilter convert(Compare.LessOrEqual vFilter) {
		return new LCompare.LessOrEqual(vFilter.propertyId, vFilter.value)
	}

	def dispatch ILFilter convert(IsNull vFilter) {
		return new LIsNull(vFilter.propertyId)
	}

	def dispatch ILFilter convert(Like vFilter) {
		return new LLike(vFilter.propertyId, vFilter.value)
	}

	def dispatch ILFilter convert(Not vFilter) {
		return new LNot(vFilter.filter.convert)
	}

	def dispatch ILFilter convert(SimpleStringFilter vFilter) {
		return new LSimpleStringFilter(vFilter.propertyId, vFilter.filterString, vFilter.ignoreCase,
			vFilter.onlyMatchPrefix)
	}

}
