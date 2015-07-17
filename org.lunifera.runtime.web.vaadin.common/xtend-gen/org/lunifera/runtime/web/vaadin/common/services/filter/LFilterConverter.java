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

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Between;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.IsNull;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Not;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.lunifera.dsl.dto.lib.services.filters.ILFilter;
import org.lunifera.dsl.dto.lib.services.filters.LAnd;
import org.lunifera.dsl.dto.lib.services.filters.LBetween;
import org.lunifera.dsl.dto.lib.services.filters.LCompare;
import org.lunifera.dsl.dto.lib.services.filters.LIsNull;
import org.lunifera.dsl.dto.lib.services.filters.LLike;
import org.lunifera.dsl.dto.lib.services.filters.LNot;
import org.lunifera.dsl.dto.lib.services.filters.LOr;
import org.lunifera.dsl.dto.lib.services.filters.LSimpleStringFilter;

/**
 * This converter will convert from Vaadin-Filters to Lunifera-Filters.
 * Lunifera-Filters are more common and used by the data services. For instance
 * JPAServices to access data.
 */
@SuppressWarnings("all")
public class LFilterConverter {
  protected ILFilter _convert(final Container.Filter vFilter) {
    throw new UnsupportedOperationException(("Not a valid type: " + vFilter));
  }
  
  protected ILFilter _convert(final And vFilter) {
    final ArrayList<ILFilter> children = CollectionLiterals.<ILFilter>newArrayList();
    Collection<Container.Filter> _filters = vFilter.getFilters();
    final Procedure1<Container.Filter> _function = new Procedure1<Container.Filter>() {
      public void apply(final Container.Filter it) {
        ILFilter _convert = LFilterConverter.this.convert(it);
        children.add(_convert);
      }
    };
    IterableExtensions.<Container.Filter>forEach(_filters, _function);
    final LAnd result = new LAnd(((ILFilter[])Conversions.unwrapArray(children, ILFilter.class)));
    return result;
  }
  
  protected ILFilter _convert(final Or vFilter) {
    final ArrayList<ILFilter> children = CollectionLiterals.<ILFilter>newArrayList();
    Collection<Container.Filter> _filters = vFilter.getFilters();
    final Procedure1<Container.Filter> _function = new Procedure1<Container.Filter>() {
      public void apply(final Container.Filter it) {
        ILFilter _convert = LFilterConverter.this.convert(it);
        children.add(_convert);
      }
    };
    IterableExtensions.<Container.Filter>forEach(_filters, _function);
    final LOr result = new LOr(((ILFilter[])Conversions.unwrapArray(children, ILFilter.class)));
    return result;
  }
  
  protected ILFilter _convert(final Between vFilter) {
    Object _propertyId = vFilter.getPropertyId();
    Comparable<?> _startValue = vFilter.getStartValue();
    Comparable<?> _endValue = vFilter.getEndValue();
    return new LBetween(_propertyId, _startValue, _endValue);
  }
  
  protected ILFilter _convert(final Compare.Equal vFilter) {
    Object _propertyId = vFilter.getPropertyId();
    Object _value = vFilter.getValue();
    return new LCompare.Equal(_propertyId, _value);
  }
  
  protected ILFilter _convert(final Compare.Greater vFilter) {
    Object _propertyId = vFilter.getPropertyId();
    Object _value = vFilter.getValue();
    return new LCompare.Greater(_propertyId, _value);
  }
  
  protected ILFilter _convert(final Compare.GreaterOrEqual vFilter) {
    Object _propertyId = vFilter.getPropertyId();
    Object _value = vFilter.getValue();
    return new LCompare.GreaterOrEqual(_propertyId, _value);
  }
  
  protected ILFilter _convert(final Compare.Less vFilter) {
    Object _propertyId = vFilter.getPropertyId();
    Object _value = vFilter.getValue();
    return new LCompare.Less(_propertyId, _value);
  }
  
  protected ILFilter _convert(final Compare.LessOrEqual vFilter) {
    Object _propertyId = vFilter.getPropertyId();
    Object _value = vFilter.getValue();
    return new LCompare.LessOrEqual(_propertyId, _value);
  }
  
  protected ILFilter _convert(final IsNull vFilter) {
    Object _propertyId = vFilter.getPropertyId();
    return new LIsNull(_propertyId);
  }
  
  protected ILFilter _convert(final Like vFilter) {
    Object _propertyId = vFilter.getPropertyId();
    String _value = vFilter.getValue();
    return new LLike(_propertyId, _value);
  }
  
  protected ILFilter _convert(final Not vFilter) {
    Container.Filter _filter = vFilter.getFilter();
    ILFilter _convert = this.convert(_filter);
    return new LNot(_convert);
  }
  
  protected ILFilter _convert(final SimpleStringFilter vFilter) {
    Object _propertyId = vFilter.getPropertyId();
    String _filterString = vFilter.getFilterString();
    boolean _isIgnoreCase = vFilter.isIgnoreCase();
    boolean _isOnlyMatchPrefix = vFilter.isOnlyMatchPrefix();
    return new LSimpleStringFilter(_propertyId, _filterString, _isIgnoreCase, _isOnlyMatchPrefix);
  }
  
  public ILFilter convert(final Container.Filter vFilter) {
    if (vFilter instanceof And) {
      return _convert((And)vFilter);
    } else if (vFilter instanceof Compare.Equal) {
      return _convert((Compare.Equal)vFilter);
    } else if (vFilter instanceof Compare.Greater) {
      return _convert((Compare.Greater)vFilter);
    } else if (vFilter instanceof Compare.GreaterOrEqual) {
      return _convert((Compare.GreaterOrEqual)vFilter);
    } else if (vFilter instanceof Compare.Less) {
      return _convert((Compare.Less)vFilter);
    } else if (vFilter instanceof Compare.LessOrEqual) {
      return _convert((Compare.LessOrEqual)vFilter);
    } else if (vFilter instanceof Or) {
      return _convert((Or)vFilter);
    } else if (vFilter instanceof Between) {
      return _convert((Between)vFilter);
    } else if (vFilter instanceof IsNull) {
      return _convert((IsNull)vFilter);
    } else if (vFilter instanceof Like) {
      return _convert((Like)vFilter);
    } else if (vFilter instanceof Not) {
      return _convert((Not)vFilter);
    } else if (vFilter instanceof SimpleStringFilter) {
      return _convert((SimpleStringFilter)vFilter);
    } else if (vFilter != null) {
      return _convert(vFilter);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(vFilter).toString());
    }
  }
}
