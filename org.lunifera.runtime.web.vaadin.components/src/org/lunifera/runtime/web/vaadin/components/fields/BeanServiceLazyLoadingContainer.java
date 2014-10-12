/**
 * Copyright (c) 2011 - 2014, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Florian Pirchner - Initial implementation
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
import org.lunifera.runtime.web.vaadin.common.data.DeepResolvingBeanItem;
import org.lunifera.runtime.web.vaadin.common.data.IBeanSearchService;
import org.lunifera.runtime.web.vaadin.common.data.StatefulInMemoryBeanSearchService;

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
		Sortable, ValueChangeListener {

	private IBeanSearchService<BEANTYPE> service;
	private SortOrder sortOrder = new SortOrder();
	private List<Filter> filters = new ArrayList<Filter>();
	@SuppressWarnings("unused")
	private Class<BEANTYPE> type;

	private LinkedHashMap<String, VaadinPropertyDescriptor<BEANTYPE>> model;

	private Map<Object, DeepResolvingBeanItem<BEANTYPE>> cache = new HashMap<Object, DeepResolvingBeanItem<BEANTYPE>>();

	public BeanServiceLazyLoadingContainer(
			IBeanSearchService<BEANTYPE> service, Class<BEANTYPE> type) {
		this.service = service != null ? service
				: new StatefulInMemoryBeanSearchService<BEANTYPE>(type);
		this.type = type;
		model = DeepResolvingBeanItem
				.getPropertyDescriptors((Class<BEANTYPE>) type);
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
		cache.put(newItemId, item);
		return item;
	}

	protected DeepResolvingBeanItem<BEANTYPE> createBeanItem(BEANTYPE bean) {
		return bean == null ? null : new DeepResolvingBeanItem<BEANTYPE>(bean,
				model);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object nextItemId(Object itemId) {
		return service.getNextBean((BEANTYPE) itemId, filters, sortOrder);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object prevItemId(Object itemId) {
		return service.getPreviousBean((BEANTYPE) itemId, filters, sortOrder);
	}

	@Override
	public Object firstItemId() {
		return service.getFirstBean(filters, sortOrder);
	}

	@Override
	public Object lastItemId() {
		return service.getLastBean(filters, sortOrder);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isFirstId(Object itemId) {
		return service.isFirstBean((BEANTYPE) itemId, filters, sortOrder);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isLastId(Object itemId) {
		return service.isLastBean((BEANTYPE) itemId, filters, sortOrder);
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
		}
		return addBean((BEANTYPE) itemId);
	}

	@Override
	public Collection<?> getContainerPropertyIds() {
		return model.keySet();
	}

	@Override
	public Collection<?> getItemIds() {
		return cache.keySet();
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
		return service.size(filters);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean containsId(Object itemId) {
		if (cache.containsKey(itemId)) {
			return true;
		}

		return service.contains((BEANTYPE) itemId, filters);
	}

	@Override
	public Item addItem(Object itemId) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
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
			sortOrder.add(new SortBy((String) propertyIds[i],
					ascending[i]));
		}
		clearCache();
	}

	@Override
	public Collection<?> getSortableContainerPropertyIds() {
		return null;
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
	public int indexOfId(Object itemId) {
		return service.indexOf((BEANTYPE) itemId, filters, sortOrder);
	}

	@Override
	public Object getIdByIndex(int index) {
		BEANTYPE result = (BEANTYPE) service.getBeanByIndex(index, filters,
				sortOrder);
		addBean(result);
		return result;
	}

	protected DeepResolvingBeanItem<BEANTYPE> addBean(BEANTYPE result) {
		return addBean(result, result);
	}

	protected DeepResolvingBeanItem<BEANTYPE> addBean(Object itemId,
			BEANTYPE result) {
		return internalAddItemAtEnd(itemId, createBeanItem(result));
	}

	@Override
	public List<BEANTYPE> getItemIds(int startIndex, int numberOfItems) {
		List<BEANTYPE> beans = (List<BEANTYPE>) service.getBeansByIndex(
				startIndex, numberOfItems, filters, sortOrder);
		for (BEANTYPE bean : beans) {
			addBean(bean);
		}
		return beans;
	}

	@Override
	public Object addItemAt(int index) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Item addItemAt(int index, Object newItemId)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

}
