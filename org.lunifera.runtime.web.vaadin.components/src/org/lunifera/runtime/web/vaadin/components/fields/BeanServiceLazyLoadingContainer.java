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
package org.lunifera.runtime.web.vaadin.components.fields;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.lunifera.dsl.dto.lib.services.SortBy;
import org.lunifera.dsl.dto.lib.services.SortOrder;
import org.lunifera.runtime.common.state.ISharedStateContext;
import org.lunifera.runtime.common.state.SharedStateUnitOfWork;
import org.lunifera.runtime.web.vaadin.common.data.DeepResolvingBeanItem;
import org.lunifera.runtime.web.vaadin.common.data.IBeanSearchService;
import org.lunifera.runtime.web.vaadin.common.data.StatefulInMemoryBeanSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Container.SimpleFilterable;
import com.vaadin.data.Container.Sortable;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.AbstractContainer;
import com.vaadin.data.util.VaadinPropertyDescriptor;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.filter.UnsupportedFilterException;

@SuppressWarnings("serial")
public class BeanServiceLazyLoadingContainer<BEANTYPE> extends
		AbstractContainer implements Filterable, Indexed, SimpleFilterable,
		Sortable, ValueChangeListener, Container.ItemSetChangeNotifier,
		Container.PropertySetChangeNotifier, IClearable {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BeanServiceLazyLoadingContainer.class);

	private IBeanSearchService<BEANTYPE> service;
	private SortOrder sortOrder = new SortOrder();
	private List<Filter> filters = new ArrayList<Filter>();
	@SuppressWarnings("unused")
	private Class<BEANTYPE> type;

	private LinkedHashMap<String, VaadinPropertyDescriptor<BEANTYPE>> model;

	private Map<Object, DeepResolvingBeanItem<BEANTYPE>> cache = new HashMap<Object, DeepResolvingBeanItem<BEANTYPE>>();
	// new records that have been added from outside -> new records
	private Map<Object, DeepResolvingBeanItem<BEANTYPE>> externalCache = new HashMap<Object, DeepResolvingBeanItem<BEANTYPE>>();

	private final ISharedStateContext sharedState;

	public BeanServiceLazyLoadingContainer(
			IBeanSearchService<BEANTYPE> service, Class<BEANTYPE> type,
			ISharedStateContext sharedState) {
		this.service = service != null ? service
				: new StatefulInMemoryBeanSearchService<BEANTYPE>(type);
		this.type = type;
		model = DeepResolvingBeanItem
				.getPropertyDescriptors((Class<BEANTYPE>) type);
		this.sharedState = sharedState;
	}

	@Override
	public void addListener(PropertySetChangeListener listener) {
		super.addPropertySetChangeListener(listener);
	}

	@Override
	public void removeListener(PropertySetChangeListener listener) {
		super.removePropertySetChangeListener(listener);
	}

	@Override
	public void addListener(ItemSetChangeListener listener) {
		super.addItemSetChangeListener(listener);
	}

	@Override
	public void removeListener(ItemSetChangeListener listener) {
		super.removeItemSetChangeListener(listener);
	}

	@Override
	public void addPropertySetChangeListener(PropertySetChangeListener listener) {
		super.addPropertySetChangeListener(listener);
	}

	@Override
	public void removePropertySetChangeListener(
			PropertySetChangeListener listener) {
		super.removePropertySetChangeListener(listener);
	}

	@Override
	public void addItemSetChangeListener(ItemSetChangeListener listener) {
		super.addItemSetChangeListener(listener);
	}

	@Override
	public void removeItemSetChangeListener(ItemSetChangeListener listener) {
		super.removeItemSetChangeListener(listener);
	}

	protected DeepResolvingBeanItem<BEANTYPE> addItem(Object itemId,
			BEANTYPE bean) {
		return addBean(bean);
	}

	protected DeepResolvingBeanItem<BEANTYPE> internalAddItemAtEnd(
			Object newItemId, DeepResolvingBeanItem<BEANTYPE> item) {
		if (externalCache.containsKey(newItemId)) {
			cache.put(newItemId, externalCache.get(newItemId));
		} else {
			cache.put(newItemId, item);
		}
		return item;
	}

	protected DeepResolvingBeanItem<BEANTYPE> createBeanItem(BEANTYPE bean) {
		return bean == null ? null : new DeepResolvingBeanItem<BEANTYPE>(bean,
				model);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object nextItemId(final Object itemId) {
		if (sharedState != null) {
			return new SharedStateUnitOfWork<Object>() {
				@Override
				protected Object doExecute() {
					return service.getNextBean((BEANTYPE) itemId, filters,
							sortOrder);
				}
			}.execute(sharedState);
		} else {
			return service.getNextBean((BEANTYPE) itemId, filters, sortOrder);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object prevItemId(final Object itemId) {
		if (sharedState != null) {
			return new SharedStateUnitOfWork<Object>() {
				@Override
				protected Object doExecute() {
					return service.getPreviousBean((BEANTYPE) itemId, filters,
							sortOrder);
				}
			}.execute(sharedState);
		} else {
			return service.getPreviousBean((BEANTYPE) itemId, filters,
					sortOrder);
		}
	}

	@Override
	public Object firstItemId() {
		if (sharedState != null) {
			return new SharedStateUnitOfWork<Object>() {
				@Override
				protected Object doExecute() {
					return service.getFirstBean(filters, sortOrder);
				}
			}.execute(sharedState);
		} else {
			return service.getFirstBean(filters, sortOrder);
		}
	}

	@Override
	public Object lastItemId() {
		if (sharedState != null) {
			return new SharedStateUnitOfWork<Object>() {
				@Override
				protected Object doExecute() {
					return service.getLastBean(filters, sortOrder);
				}
			}.execute(sharedState);
		} else {
			return service.getLastBean(filters, sortOrder);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isFirstId(final Object itemId) {
		if (sharedState != null) {
			return new SharedStateUnitOfWork<Boolean>() {
				@Override
				protected Boolean doExecute() {
					return service.isFirstBean((BEANTYPE) itemId, filters,
							sortOrder);
				}
			}.execute(sharedState);
		} else {
			return service.isFirstBean((BEANTYPE) itemId, filters, sortOrder);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isLastId(final Object itemId) {
		if (sharedState != null) {
			return new SharedStateUnitOfWork<Boolean>() {
				@Override
				protected Boolean doExecute() {
					return service.isLastBean((BEANTYPE) itemId, filters,
							sortOrder);
				}
			}.execute(sharedState);
		} else {
			return service.isLastBean((BEANTYPE) itemId, filters, sortOrder);
		}
	}

	@Override
	public Object addItemAfter(Object previousItemId)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Item addItemAfter(Object previousItemId, Object newItemId)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Item getItem(Object itemId) {
		if (cache.containsKey(itemId)) {
			return cache.get(itemId);
		} else if (externalCache.containsKey(itemId)) {
			return externalCache.get(itemId);
		}
		return addBean((BEANTYPE) itemId);
	}

	@Override
	public Collection<?> getContainerPropertyIds() {
		return model.keySet();
	}

	@Override
	public Collection<?> getItemIds() {
		List<Object> itemIds = new ArrayList<Object>();
		itemIds.addAll(cache.keySet());
		itemIds.addAll(externalCache.keySet());
		return itemIds;
	}

	@Override
	public Property<?> getContainerProperty(Object itemId, Object propertyId) {
		Item item = getItem(itemId);
		if (item == null) {
			return null;
		}
		return item.getItemProperty(propertyId);
	}

	@Override
	public Class<?> getType(Object propertyId) {
		return model.get(propertyId).getPropertyType();
	}

	@Override
	public int size() {
		if (sharedState != null) {
			return new SharedStateUnitOfWork<Integer>() {
				@Override
				protected Integer doExecute() {
					return service.size(filters) + externalCache.size();
				}
			}.execute(sharedState);
		} else {
			return service.size(filters) + externalCache.size();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean containsId(final Object itemId) {
		if (cache.containsKey(itemId) || externalCache.containsKey(itemId)) {
			return true;
		}
		if (sharedState != null) {
			return new SharedStateUnitOfWork<Boolean>() {
				@Override
				protected Boolean doExecute() {
					return service.contains((BEANTYPE) itemId, filters);
				}
			}.execute(sharedState);
		} else {
			return service.contains((BEANTYPE) itemId, filters);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Item addItem(Object itemId) throws UnsupportedOperationException {
		// external data -> new record
		return addBean((BEANTYPE) itemId, true);
	}

	@Override
	public Object addItem() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeItem(Object itemId)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addContainerProperty(Object propertyId, Class<?> type,
			Object defaultValue) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeContainerProperty(Object propertyId)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAllItems() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void valueChange(ValueChangeEvent event) {

	}

	@Override
	public void sort(Object[] propertyIds, boolean[] ascending) {
		sortOrder.clear();
		for (int i = 0; i < propertyIds.length; i++) {
			sortOrder.add(new SortBy((String) propertyIds[i], ascending[i]));
		}
		clearCache();
	}

	@Override
	public Collection<?> getSortableContainerPropertyIds() {
		return getContainerPropertyIds();
	}

	@Override
	public void addContainerFilter(Object propertyId, String filterString,
			boolean ignoreCase, boolean onlyMatchPrefix) {
		addContainerFilter(new SimpleStringFilter(propertyId, filterString,
				ignoreCase, onlyMatchPrefix));
	}

	@Override
	public void removeContainerFilters(Object propertyId) {
		if (getFilters().isEmpty() || propertyId == null) {
			return;
		}
		boolean result = false;
		for (Iterator<Filter> iterator = getFilters().iterator(); iterator
				.hasNext();) {
			Filter f = iterator.next();
			if (f.appliesToProperty(propertyId)) {
				iterator.remove();
				result = true;
			}
		}

		if (result) {
			clearCache();
		}
	}

	protected List<Filter> getFilters() {
		return filters;
	}

	@Override
	public void addContainerFilter(Filter filter)
			throws UnsupportedFilterException {
		if (!filters.contains(filter)) {
			filters.add(filter);
			clearCache();
		}

		fireItemSetChange();
	}

	protected void clearCache() {
		cache.clear();
	}

	@Override
	public void removeContainerFilter(Filter filter) {
		if (filters.remove(filter)) {
			clearCache();
		}
	}

	@Override
	public void removeAllContainerFilters() {
		filters.clear();
		clearCache();
	}

	@Override
	public Collection<Filter> getContainerFilters() {
		return Collections.unmodifiableList(filters);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int indexOfId(final Object itemId) {
		if (sharedState != null) {
			return new SharedStateUnitOfWork<Integer>() {
				@Override
				protected Integer doExecute() {
					return service.indexOf((BEANTYPE) itemId, filters,
							sortOrder);
				}
			}.execute(sharedState);
		} else {
			return service.indexOf((BEANTYPE) itemId, filters, sortOrder);
		}
	}

	@Override
	public Object getIdByIndex(final int index) {
		if (sharedState != null) {
			return new SharedStateUnitOfWork<BEANTYPE>() {
				@Override
				protected BEANTYPE doExecute() {
					BEANTYPE result = null;
					result = (BEANTYPE) service.getBeanByIndex(index, filters,
							sortOrder);
					addBean(result);
					return result;
				}
			}.execute(sharedState);
		} else {
			BEANTYPE result = null;
			result = (BEANTYPE) service.getBeanByIndex(index, filters,
					sortOrder);
			addBean(result);
			return result;
		}
	}

	protected DeepResolvingBeanItem<BEANTYPE> addBean(BEANTYPE result) {
		return addBean(result, false);
	}

	protected DeepResolvingBeanItem<BEANTYPE> addBean(BEANTYPE result,
			boolean toExternalCache) {
		return addBean(result, result, toExternalCache);
	}

	protected DeepResolvingBeanItem<BEANTYPE> addBean(Object itemId,
			BEANTYPE result, boolean toExternalCache) {

		DeepResolvingBeanItem<BEANTYPE> item = createBeanItem(result);
		if (toExternalCache) {
			externalCache.put(itemId, item);
		}

		return internalAddItemAtEnd(itemId, item);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BEANTYPE> getItemIds(final int startIndex,
			final int numberOfItems) {
		if (sharedState != null) {
			return new SharedStateUnitOfWork<List<BEANTYPE>>() {
				@Override
				protected List<BEANTYPE> doExecute() {
					List<BEANTYPE> beans = new ArrayList<BEANTYPE>();
					beans.addAll((Collection<? extends BEANTYPE>) externalCache
							.keySet());
					beans.addAll((List<BEANTYPE>) service.getBeansByIndex(
							startIndex, numberOfItems, filters, sortOrder));
					for (BEANTYPE bean : beans) {
						addBean(bean);
					}
					return beans;
				}
			}.execute(sharedState);
		} else {
			List<BEANTYPE> beans = new ArrayList<BEANTYPE>();
			beans.addAll((Collection<? extends BEANTYPE>) externalCache
					.keySet());
			beans.addAll((List<BEANTYPE>) service.getBeansByIndex(startIndex,
					numberOfItems, filters, sortOrder));
			for (BEANTYPE bean : beans) {
				addBean(bean);
			}
			return beans;
		}
	}

	@Override
	public Object addItemAt(int index) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Item addItemAt(int index, Object newItemId)
			throws UnsupportedOperationException {

		// external data -> new record
		return addBean((BEANTYPE) newItemId, true);
	}

	@Override
	public void clear() {
		cache.clear();
		externalCache.clear();
	}
}
