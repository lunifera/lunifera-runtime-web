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
package org.lunifera.runtime.web.vaadin.common.data;

import java.util.List;

import org.lunifera.dsl.dto.lib.services.IDTOService;
import org.lunifera.dsl.dto.lib.services.IQuery;
import org.lunifera.dsl.dto.lib.services.Query;
import org.lunifera.dsl.dto.lib.services.SortOrder;
import org.lunifera.dsl.dto.lib.services.filters.ILFilter;
import org.lunifera.runtime.web.vaadin.common.services.filter.LFilterConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.And;

@SuppressWarnings("restriction")
public class StatefulDelegatingDtoSearchService<BEAN> implements
		IBeanSearchService<BEAN> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(StatefulDelegatingDtoSearchService.class);

	private final LFilterConverter converter = new LFilterConverter();
	private final IDTOService<BEAN> delegate;
	private final Class<BEAN> type;

	public StatefulDelegatingDtoSearchService(IDTOService<BEAN> delegate,
			Class<BEAN> type) {
		this.delegate = delegate;
		this.type = type;

		if (delegate == null) {
			LOGGER.error("Delegate could not be found for " + type);
		}
	}

	private boolean checkDelegate() {
		if (delegate == null) {
			LOGGER.error("Delegate could not be found for " + type);
			return false;
		}
		return true;
	}

	@Override
	public int size(List<Filter> filters) {
		if (!checkDelegate()) {
			return 0;
		}

		return delegate.size(createQuery(filters));
	}

	/**
	 * Creats a query for the given filters.
	 * 
	 * @param filters
	 * @return
	 */
	private IQuery createQuery(List<Filter> filters) {
		And vAnd = new And(filters.toArray(new Filter[filters.size()]));
		ILFilter lAnd = converter.convert(vAnd);
		return new Query(lAnd);
	}

	/**
	 * Creates a query for the given filter and sortOrder.
	 * 
	 * @param filters
	 * @param sortOrder
	 * @return
	 */
	private IQuery createQuery(List<Filter> filters, SortOrder sortOrder) {
		And vAnd = new And(filters.toArray(new Filter[filters.size()]));
		ILFilter lAnd = converter.convert(vAnd);
		return new Query(lAnd, sortOrder);
	}

	@Override
	public boolean contains(BEAN bean, List<Filter> filters) {
		if (!checkDelegate()) {
			return false;
		}

		return delegate.contains(bean, createQuery(filters));
	}

	@Override
	public BEAN getNextBean(BEAN bean, List<Filter> filters, SortOrder sortOrder) {
		if (!checkDelegate()) {
			return null;
		}
		return delegate.getNext(bean, createQuery(filters, sortOrder));
	}

	@Override
	public BEAN getPreviousBean(BEAN bean, List<Filter> filters,
			SortOrder sortOrder) {
		if (!checkDelegate()) {
			return null;
		}
		return delegate.getPrevious(bean, createQuery(filters, sortOrder));
	}

	@Override
	public BEAN getFirstBean(List<Filter> filters, SortOrder sortOrder) {
		if (!checkDelegate()) {
			return null;
		}
		return delegate.getFirst(createQuery(filters, sortOrder));
	}

	@Override
	public BEAN getLastBean(List<Filter> filters, SortOrder sortOrder) {
		if (!checkDelegate()) {
			return null;
		}
		return delegate.getLast(createQuery(filters, sortOrder));
	}

	@Override
	public boolean isFirstBean(BEAN bean, List<Filter> filters,
			SortOrder sortOrder) {
		if (!checkDelegate()) {
			return false;
		}
		return delegate.isFirst(bean, createQuery(filters, sortOrder));
	}

	@Override
	public boolean isLastBean(BEAN bean, List<Filter> filters,
			SortOrder sortOrder) {
		if (!checkDelegate()) {
			return false;
		}
		return delegate.isLast(bean, createQuery(filters, sortOrder));
	}

	@Override
	public int indexOf(BEAN bean, List<Filter> filters, SortOrder sortOrder) {
		if (!checkDelegate()) {
			return 0;
		}
		return delegate.indexOf(bean, createQuery(filters, sortOrder));
	}

	@Override
	public BEAN getBeanByIndex(int index, List<Filter> filters,
			SortOrder sortOrder) {
		if (!checkDelegate()) {
			return null;
		}
		return delegate.getByIndex(index, createQuery(filters, sortOrder));
	}

	@Override
	public List<BEAN> getBeansByIndex(int startIndex, int numberOfItems,
			List<Filter> filters, SortOrder sortOrder) {
		if (!checkDelegate()) {
			return null;
		}
		return delegate.getByIndex(startIndex, numberOfItems,
				createQuery(filters, sortOrder));
	}
}
