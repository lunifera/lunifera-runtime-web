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
import java.util.Arrays;

/**
 * This converter will convert from Vaadin-Filters to Lunifera-Filters.
 * Lunifera-Filters are more common and used by the data services. For instance
 * JPAServices to access data.
 */
@SuppressWarnings("all")
public class LFilterConverter {
  protected /* ILFilter */Object _convert(final Container.Filter vFilter) {
    throw new UnsupportedOperationException(("Not a valid type: " + vFilter));
  }
  
  protected /* ILFilter */Object _convert(final And vFilter) {
    throw new Error("Unresolved compilation problems:"
      + "\nLAnd cannot be resolved.");
  }
  
  protected /* ILFilter */Object _convert(final Or vFilter) {
    throw new Error("Unresolved compilation problems:"
      + "\nLOr cannot be resolved.");
  }
  
  protected /* ILFilter */Object _convert(final Between vFilter) {
    throw new Error("Unresolved compilation problems:"
      + "\nLBetween cannot be resolved.");
  }
  
  protected /* ILFilter */Object _convert(final Compare.Equal vFilter) {
    throw new Error("Unresolved compilation problems:"
      + "\nLCompare.Equal cannot be resolved.");
  }
  
  protected /* ILFilter */Object _convert(final Compare.Greater vFilter) {
    throw new Error("Unresolved compilation problems:"
      + "\nLCompare.Greater cannot be resolved.");
  }
  
  protected /* ILFilter */Object _convert(final Compare.GreaterOrEqual vFilter) {
    throw new Error("Unresolved compilation problems:"
      + "\nLCompare.GreaterOrEqual cannot be resolved.");
  }
  
  protected /* ILFilter */Object _convert(final Compare.Less vFilter) {
    throw new Error("Unresolved compilation problems:"
      + "\nLCompare.Less cannot be resolved.");
  }
  
  protected /* ILFilter */Object _convert(final Compare.LessOrEqual vFilter) {
    throw new Error("Unresolved compilation problems:"
      + "\nLCompare.LessOrEqual cannot be resolved.");
  }
  
  protected /* ILFilter */Object _convert(final IsNull vFilter) {
    throw new Error("Unresolved compilation problems:"
      + "\nLIsNull cannot be resolved.");
  }
  
  protected /* ILFilter */Object _convert(final Like vFilter) {
    throw new Error("Unresolved compilation problems:"
      + "\nLLike cannot be resolved.");
  }
  
  protected /* ILFilter */Object _convert(final Not vFilter) {
    throw new Error("Unresolved compilation problems:"
      + "\nLNot cannot be resolved.");
  }
  
  protected /* ILFilter */Object _convert(final SimpleStringFilter vFilter) {
    throw new Error("Unresolved compilation problems:"
      + "\nLSimpleStringFilter cannot be resolved.");
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
