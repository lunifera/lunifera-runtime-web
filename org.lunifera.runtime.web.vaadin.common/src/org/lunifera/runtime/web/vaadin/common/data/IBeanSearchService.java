package org.lunifera.runtime.web.vaadin.common.data;

import java.util.List;

import org.lunifera.dsl.dto.lib.services.SortOrder;

import com.vaadin.data.Container.Filter;

public interface IBeanSearchService<BEAN> {

	/**
	 * Returns the count of all bean matching the filter.
	 * 
	 * @param filters
	 * @return
	 */
	int size(List<Filter> filters);

	/**
	 * Returns true, if the the bean exists for the given filter.
	 * 
	 * @param bean
	 * @param filters
	 * @return
	 */
	boolean contains(BEAN bean, List<Filter> filters);

	/**
	 * Returns the next bean for the given one using the filter and the
	 * sortOrder.
	 * 
	 * @param bean
	 * @param filters
	 * @param sortOrder
	 * @return
	 */
	BEAN getNextBean(BEAN bean, List<Filter> filters, SortOrder sortOrder);

	/**
	 * Returns the previous bean for the given one using the filter and the
	 * sortOrder.
	 * 
	 * @param bean
	 * @param filters
	 * @param sortOrder
	 * @return
	 */
	BEAN getPreviousBean(BEAN bean, List<Filter> filters,
			SortOrder sortOrder);

	/**
	 * Returns the first bean for the given filter and the sortOrder.
	 * 
	 * @param filters
	 * @param sortOrder
	 * @return
	 */
	BEAN getFirstBean(List<Filter> filters, SortOrder sortOrder);

	/**
	 * Returns the last bean for the given filter and the sortOrder.
	 * 
	 * @param filters
	 * @param sortOrder
	 * @return
	 */
	BEAN getLastBean(List<Filter> filters, SortOrder sortOrder);

	/**
	 * Returns true, if the given bean is the first one for the filter and
	 * sortOrder.
	 * 
	 * @param filters
	 * @param sortOrder
	 * @return
	 */
	boolean isFirstBean(BEAN bean, List<Filter> filters, SortOrder sortOrder);

	/**
	 * Returns true, if the given bean is the last one for the filter and
	 * sortOrder.
	 * 
	 * @param filters
	 * @param sortOrder
	 * @return
	 */
	boolean isLastBean(BEAN bean, List<Filter> filters, SortOrder sortOrder);

	/**
	 * Returns the index of the given bean for the filter and sortOrder.
	 * 
	 * @param filters
	 * @param sortOrder
	 * @return
	 */
	int indexOf(BEAN bean, List<Filter> filters, SortOrder sortOrder);

	/**
	 * Returns the bean for the given index, filter and sortOrder.
	 * 
	 * @param index
	 * @param filters
	 * @param sortOrder
	 * @return
	 */
	BEAN getBeanByIndex(int index, List<Filter> filters, SortOrder sortOrder);

	/**
	 * Returns a list of beans with size <= numberOfItems, starting from the
	 * startIndex in respect to the filter and sortOrder.
	 * 
	 * @param startIndex
	 * @param numberOfItems
	 * @param filters
	 * @param sortOrder
	 * @return
	 */
	List<BEAN> getBeansByIndex(int startIndex, int numberOfItems,
			List<Filter> filters, SortOrder sortOrder);

}
