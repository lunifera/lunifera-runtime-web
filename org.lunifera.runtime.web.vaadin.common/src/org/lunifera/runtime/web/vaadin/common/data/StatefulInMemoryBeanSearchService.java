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

import java.util.Collection;
import java.util.List;

import org.lunifera.dsl.dto.lib.services.SortOrder;

import com.vaadin.data.Container.Filter;

public class StatefulInMemoryBeanSearchService<BEAN> implements
		IBeanSearchService<BEAN> {

	private DeepResolvingBeanItemContainer<BEAN> delegate;

	public StatefulInMemoryBeanSearchService(Class<BEAN> type) {
		delegate = new DeepResolvingBeanItemContainer<BEAN>(type);
	}

	public void addBean(BEAN bean) {
		delegate.addBean(bean);
	}

	public void addBeans(Collection<BEAN> beans) {
		delegate.addAll(beans);
	}

	@Override
	public int size(List<Filter> filters) {
		setupContainer(filters);
		return delegate.size();
	}

	@Override
	public boolean contains(BEAN bean, List<Filter> filters) {
		setupContainer(filters);
		return delegate.containsId(bean);
	}

	protected void setupContainer(List<Filter> filters) {
		delegate.removeAllContainerFilters();
		for (Filter filter : filters) {
			delegate.addContainerFilter(filter);
		}
	}

	@Override
	public BEAN getNextBean(BEAN bean, List<Filter> filters,
			SortOrder sortOrder) {
		setupContainer(filters);
		return delegate.nextItemId(bean);
	}

	@Override
	public BEAN getPreviousBean(BEAN bean, List<Filter> filters,
			SortOrder sortOrder) {
		setupContainer(filters);
		return delegate.prevItemId(bean);
	}

	@Override
	public BEAN getFirstBean(List<Filter> filters, SortOrder sortOrder) {
		setupContainer(filters);
		return delegate.firstItemId();
	}

	@Override
	public BEAN getLastBean(List<Filter> filters, SortOrder sortOrder) {
		setupContainer(filters);
		return delegate.lastItemId();
	}

	@Override
	public boolean isFirstBean(BEAN bean, List<Filter> filters,
			SortOrder sortOrder) {
		setupContainer(filters);
		return delegate.isFirstId(bean);
	}

	@Override
	public boolean isLastBean(BEAN bean, List<Filter> filters,
			SortOrder sortOrder) {
		return delegate.isLastId(bean);
	}

	@Override
	public int indexOf(BEAN bean, List<Filter> filters, SortOrder sortOrder) {
		setupContainer(filters);
		return delegate.indexOfId(bean);
	}

	@Override
	public BEAN getBeanByIndex(int index, List<Filter> filters,
			SortOrder sortOrder) {
		setupContainer(filters);
		return delegate.getIdByIndex(index);
	}

	@Override
	public List<BEAN> getBeansByIndex(int startIndex, int numberOfItems,
			List<Filter> filters, SortOrder sortOrder) {
		setupContainer(filters);
		return delegate.getItemIds(startIndex, numberOfItems);
	}
}
