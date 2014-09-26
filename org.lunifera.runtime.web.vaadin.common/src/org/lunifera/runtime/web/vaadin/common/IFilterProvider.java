package org.lunifera.runtime.web.vaadin.common;

import java.util.List;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Or;

public interface IFilterProvider {

	/**
	 * Creates a new filter that accepts all items whose value of
	 * <code>propertyId</code> is null.
	 */
	public abstract Filter isNull(Object propertyId);

	/**
	 * Creates a new filter that accepts all items whose value of
	 * <code>propertyId</code> is not null.
	 */
	public abstract Filter isNotNull(Object propertyId);

	/**
	 * Creates a new filter that accepts all items whose value of
	 * <code>propertyId</code> is empty.
	 */
	public abstract Filter isEmpty(Object propertyId);

	/**
	 * Creates a new filter that accepts all items whose value of
	 * <code>propertyId</code> is not empty.
	 */
	public abstract Filter isNotEmpty(Object propertyId);

	/**
	 * Creates a new filter that accepts all items whose value of
	 * <code>propertyId</code> is equal to <code>value</code>.
	 */
	public abstract Filter eq(Object propertyId, String value,
			boolean caseSensitive);

	/**
	 * Creates a new filter that accepts all items whose value of
	 * <code>propertyId</code> matches <code>value</code>. The precent-sign (%)
	 * may be used as wildcard.
	 */
	public abstract Filter like(Object propertyId, String value,
			boolean caseSensitive);

	/**
	 * Creates a new filter that accepts all items whose value of
	 * <code>propertyId</code> is equal to <code>value</code>.
	 */
	public abstract Filter eq(Object propertyId, Object value);

	/**
	 * Creates a new filter that accepts all items whose value of
	 * <code>propertyId</code> is greater than or equal to <code>value</code>.
	 */
	public abstract Filter gteq(Object propertyId, Object value);

	/**
	 * Creates a new filter that accepts all items whose value of
	 * <code>propertyId</code> is greater than <code>value</code>.
	 */
	public abstract Filter gt(Object propertyId, Object value);

	/**
	 * Creates a new filter that accepts all items whose value of
	 * <code>propertyId</code> is less than or equal to <code>value</code>.
	 */
	public abstract Filter lteq(Object propertyId, Object value);

	/**
	 * Creates a new filter that accepts all items whose value of
	 * <code>propertyId</code> is less than <code>value</code>.
	 */
	public abstract Filter lt(Object propertyId, Object value);

	/**
	 * Creates a new filter that accepts all items whose value of
	 * <code>propertyId</code> is between <code>startingPoint</code> and
	 * <code>endingPoint</code>.
	 */
	public abstract Filter between(Object propertyId,
			Comparable<?> startingPoint, Comparable<?> endingPoint);

	public abstract Filter between(Object propertyId, Object startingPoint,
			Object endingPoint, boolean includeStartingPoint,
			boolean includeEndingPoint);

	/**
	 * Creates a new filter that accepts all items whose value of
	 * <code>propertyId</code> is between <code>startingPoint</code> (inclusive)
	 * and <code>endingPoint</code> (inclusive).
	 */
	public abstract Filter betweenInclusive(Object propertyId,
			Object startingPoint, Object endingPoint);

	/**
	 * Creates a new filter that accepts all items whose value of
	 * <code>propertyId</code> is between <code>startingPoint</code> (exclusive)
	 * and <code>endingPoint</code> (exclusive).
	 */
	public abstract Filter betweenExlusive(Object propertyId,
			Object startingPoint, Object endingPoint);

	/**
	 * Creates a new filter that accepts all items whose value of
	 * <code>propertyId</code> is outside <code>startingPoint</code> and
	 * <code>endingPoint</code>.
	 */
	public abstract Filter outside(Object propertyId, Object startingPoint,
			Object endingPoint, boolean includeStartingPoint,
			boolean includeEndingPoint);

	/**
	 * Creates a new filter that accepts all items whose value of
	 * <code>propertyId</code> is outside <code>startingPoint</code> (inclusive)
	 * and <code>endingPoint</code> (inclusive).
	 */
	public abstract Filter outsideInclusive(Object propertyId,
			Object startingPoint, Object endingPoint);

	/**
	 * Creates a new filter that accepts all items whose value of
	 * <code>propertyId</code> is outside <code>startingPoint</code> (exclusive)
	 * and <code>endingPoint</code> (exclusive).
	 */
	public abstract Filter outsideExclusive(Object propertyId,
			Object startingPoint, Object endingPoint);

	/**
	 * Creates a filter that negates <code>filter</code>.
	 */
	public abstract Filter not(Filter filter);

	/**
	 * Creates a filter that groups <code>filters</code> together in a single
	 * conjunction.
	 */
	public abstract And and(Filter... filters);

	/**
	 * Creates a filter that groups <code>filters</code> together in a single
	 * conjunction.
	 */
	public abstract And and(List<Filter> filters);

	/**
	 * Creates a filter that groups <code>filters</code> together in a single
	 * disjunction.
	 */
	public abstract Or or(Filter... filters);

	/**
	 * Creates a filter that groups <code>filters</code> together in a single
	 * disjunction.
	 */
	public abstract Or or(List<Filter> filters);

}