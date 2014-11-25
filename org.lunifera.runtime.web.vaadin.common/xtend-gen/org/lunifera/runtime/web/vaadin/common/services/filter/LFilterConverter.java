package org.lunifera.runtime.web.vaadin.common.services.filter;

import java.util.Arrays;
import org.lunifera.dsl.dto.lib.services.filters.ILFilter;

/**
 * This converter will convert from Vaadin-Filters to Lunifera-Filters.
 * Lunifera-Filters are more common and used by the data services. For instance
 * JPAServices to access data.
 */
@SuppressWarnings("all")
public class LFilterConverter {
  protected ILFilter _convert(final /* Container.Filter */Object vFilter) {
    String _plus = ("Not a valid type: " + vFilter);
    throw new UnsupportedOperationException(_plus);
  }
  
  protected ILFilter _convert(final /* And */Object vFilter) {
    throw new Error("Unresolved compilation problems:"
      + "\nfilters cannot be resolved"
      + "\nforEach cannot be resolved");
  }
  
  protected ILFilter _convert(final /* Or */Object vFilter) {
    throw new Error("Unresolved compilation problems:"
      + "\nfilters cannot be resolved"
      + "\nforEach cannot be resolved");
  }
  
  protected ILFilter _convert(final /* Between */Object vFilter) {
    throw new Error("Unresolved compilation problems:"
      + "\npropertyId cannot be resolved"
      + "\nstartValue cannot be resolved"
      + "\nendValue cannot be resolved");
  }
  
  protected ILFilter _convert(final /* Compare.Equal */Object vFilter) {
    throw new Error("Unresolved compilation problems:"
      + "\npropertyId cannot be resolved"
      + "\nvalue cannot be resolved");
  }
  
  protected ILFilter _convert(final /* Compare.Greater */Object vFilter) {
    throw new Error("Unresolved compilation problems:"
      + "\npropertyId cannot be resolved"
      + "\nvalue cannot be resolved");
  }
  
  protected ILFilter _convert(final /* Compare.GreaterOrEqual */Object vFilter) {
    throw new Error("Unresolved compilation problems:"
      + "\npropertyId cannot be resolved"
      + "\nvalue cannot be resolved");
  }
  
  protected ILFilter _convert(final /* Compare.Less */Object vFilter) {
    throw new Error("Unresolved compilation problems:"
      + "\npropertyId cannot be resolved"
      + "\nvalue cannot be resolved");
  }
  
  protected ILFilter _convert(final /* Compare.LessOrEqual */Object vFilter) {
    throw new Error("Unresolved compilation problems:"
      + "\npropertyId cannot be resolved"
      + "\nvalue cannot be resolved");
  }
  
  protected ILFilter _convert(final /* IsNull */Object vFilter) {
    throw new Error("Unresolved compilation problems:"
      + "\npropertyId cannot be resolved");
  }
  
  protected ILFilter _convert(final /* Like */Object vFilter) {
    throw new Error("Unresolved compilation problems:"
      + "\npropertyId cannot be resolved"
      + "\nvalue cannot be resolved");
  }
  
  protected ILFilter _convert(final /* Not */Object vFilter) {
    throw new Error("Unresolved compilation problems:"
      + "\nfilter cannot be resolved"
      + "\nconvert cannot be resolved");
  }
  
  protected ILFilter _convert(final /* SimpleStringFilter */Object vFilter) {
    throw new Error("Unresolved compilation problems:"
      + "\npropertyId cannot be resolved"
      + "\nfilterString cannot be resolved"
      + "\nignoreCase cannot be resolved"
      + "\nonlyMatchPrefix cannot be resolved");
  }
  
  public ILFilter convert(final Filter vFilter) {
    if (vFilter != null) {
      return _convert(vFilter); else {
        throw new IllegalArgumentException("Unhandled parameter types: " +
          Arrays.<Object>asList(vFilter).toString());
      }
    }
  }
  