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
package org.lunifera.runtime.web.ecview.services.vaadin.impl;

import com.google.common.base.Objects;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import org.lunifera.dsl.semantic.entity.LBean;
import org.lunifera.dsl.semantic.entity.LBeanAttribute;
import org.lunifera.dsl.semantic.entity.LEntity;
import org.lunifera.dsl.semantic.entity.LEntityAttribute;
import org.lunifera.runtime.common.annotations.DomainDescription;
import org.lunifera.runtime.common.annotations.DomainKey;
import org.lunifera.runtime.web.ecview.services.vaadin.impl.OperationExtensions;

@SuppressWarnings("all")
public class BeanHelper {
  /**
   * Returns the caption property if it could be found. Null otherwise.
   */
  public static String findCaptionProperty(final Object bean) {
    boolean _equals = Objects.equal(bean, null);
    if (_equals) {
      return null;
    }
    if ((bean instanceof Class<?>)) {
      return BeanHelper.findCaptionProperty(((Class<?>) bean));
    } else {
      Class<?> _class = bean.getClass();
      return BeanHelper.findCaptionProperty(_class);
    }
  }
  
  /**
   * Returns the caption property if it could be found. Null otherwise.
   */
  public static String findCaptionProperty(final Class<?> beanClass) {
    boolean _equals = Objects.equal(beanClass, null);
    if (_equals) {
      return null;
    }
    Field[] _declaredFields = beanClass.getDeclaredFields();
    for (final Field field : _declaredFields) {
      boolean _isAnnotationPresent = field.isAnnotationPresent(DomainKey.class);
      if (_isAnnotationPresent) {
        return field.getName();
      }
    }
    Field[] _fields = beanClass.getFields();
    for (final Field field_1 : _fields) {
      boolean _isAnnotationPresent_1 = field_1.isAnnotationPresent(DomainKey.class);
      if (_isAnnotationPresent_1) {
        return field_1.getName();
      }
    }
    return null;
  }
  
  /**
   * Returns the caption property if it could be found. Null otherwise.
   */
  public static String findCaptionProperty(final LEntity lEntity) {
    String bestMatch = null;
    boolean _equals = Objects.equal(lEntity, null);
    if (_equals) {
      return null;
    }
    List<LEntityAttribute> _allAttributes = lEntity.getAllAttributes();
    for (final LEntityAttribute field : _allAttributes) {
      boolean _or = false;
      boolean _or_1 = false;
      boolean _or_2 = false;
      boolean _or_3 = false;
      boolean _isDomainKey = field.isDomainKey();
      if (_isDomainKey) {
        _or_3 = true;
      } else {
        String _name = field.getName();
        boolean _equalsIgnoreCase = _name.equalsIgnoreCase("Name");
        _or_3 = _equalsIgnoreCase;
      }
      if (_or_3) {
        _or_2 = true;
      } else {
        String _name_1 = field.getName();
        boolean _equalsIgnoreCase_1 = _name_1.equalsIgnoreCase("Number");
        _or_2 = _equalsIgnoreCase_1;
      }
      if (_or_2) {
        _or_1 = true;
      } else {
        String _name_2 = field.getName();
        boolean _equalsIgnoreCase_2 = _name_2.equalsIgnoreCase("Description");
        _or_1 = _equalsIgnoreCase_2;
      }
      if (_or_1) {
        _or = true;
      } else {
        boolean _isUuid = field.isUuid();
        _or = _isUuid;
      }
      if (_or) {
        String _name_3 = field.getName();
        bestMatch = _name_3;
      }
    }
    return bestMatch;
  }
  
  /**
   * Returns the caption property if it could be found. Null otherwise.
   */
  public static String findCaptionProperty(final LBean lBean) {
    String bestMatch = null;
    boolean _equals = Objects.equal(lBean, null);
    if (_equals) {
      return null;
    }
    List<LBeanAttribute> _allAttributes = lBean.getAllAttributes();
    for (final LBeanAttribute field : _allAttributes) {
      boolean _or = false;
      boolean _or_1 = false;
      boolean _or_2 = false;
      boolean _or_3 = false;
      boolean _isDomainKey = field.isDomainKey();
      if (_isDomainKey) {
        _or_3 = true;
      } else {
        String _name = field.getName();
        boolean _equalsIgnoreCase = _name.equalsIgnoreCase("Name");
        _or_3 = _equalsIgnoreCase;
      }
      if (_or_3) {
        _or_2 = true;
      } else {
        String _name_1 = field.getName();
        boolean _equalsIgnoreCase_1 = _name_1.equalsIgnoreCase("Number");
        _or_2 = _equalsIgnoreCase_1;
      }
      if (_or_2) {
        _or_1 = true;
      } else {
        String _name_2 = field.getName();
        boolean _equalsIgnoreCase_2 = _name_2.equalsIgnoreCase("Description");
        _or_1 = _equalsIgnoreCase_2;
      }
      if (_or_1) {
        _or = true;
      } else {
        boolean _isUuid = field.isUuid();
        _or = _isUuid;
      }
      if (_or) {
        String _name_3 = field.getName();
        bestMatch = _name_3;
      }
    }
    return bestMatch;
  }
  
  /**
   * Returns the description property if it could be found. Null otherwise.
   */
  public static String findDescriptionProperty(final Object bean) {
    boolean _equals = Objects.equal(bean, null);
    if (_equals) {
      return null;
    }
    if ((bean instanceof Class<?>)) {
      return BeanHelper.findDescriptionProperty(((Class<?>) bean));
    } else {
      Class<?> _class = bean.getClass();
      return BeanHelper.findDescriptionProperty(_class);
    }
  }
  
  /**
   * Returns the description property if it could be found. Null otherwise.
   */
  public static String findDescriptionProperty(final Class<?> beanClass) {
    boolean _equals = Objects.equal(beanClass, null);
    if (_equals) {
      return null;
    }
    Field[] _declaredFields = beanClass.getDeclaredFields();
    for (final Field field : _declaredFields) {
      boolean _isAnnotationPresent = field.isAnnotationPresent(DomainDescription.class);
      if (_isAnnotationPresent) {
        return field.getName();
      }
    }
    Method[] _declaredMethods = beanClass.getDeclaredMethods();
    for (final Method method : _declaredMethods) {
      boolean _isAnnotationPresent_1 = method.isAnnotationPresent(DomainDescription.class);
      if (_isAnnotationPresent_1) {
        String _name = method.getName();
        return OperationExtensions.toPropertyName(_name);
      }
    }
    Field[] _fields = beanClass.getFields();
    for (final Field field_1 : _fields) {
      boolean _isAnnotationPresent_2 = field_1.isAnnotationPresent(DomainDescription.class);
      if (_isAnnotationPresent_2) {
        return field_1.getName();
      }
    }
    Method[] _methods = beanClass.getMethods();
    for (final Method method_1 : _methods) {
      boolean _isAnnotationPresent_3 = method_1.isAnnotationPresent(DomainDescription.class);
      if (_isAnnotationPresent_3) {
        String _name_1 = method_1.getName();
        return OperationExtensions.toPropertyName(_name_1);
      }
    }
    return null;
  }
  
  /**
   * Returns the description property if it could be found. Null otherwise.
   */
  public static String findDescriptionProperty(final LEntity entity) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method annotationType is undefined for the type BeanHelper"
      + "\nThe method annotationType is undefined for the type BeanHelper"
      + "\nqualifiedName cannot be resolved"
      + "\nequals cannot be resolved"
      + "\nqualifiedName cannot be resolved"
      + "\nequals cannot be resolved");
  }
  
  /**
   * Returns the description property if it could be found. Null otherwise.
   */
  public static String findDescriptionProperty(final LBean bean) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method annotationType is undefined for the type BeanHelper"
      + "\nThe method annotationType is undefined for the type BeanHelper"
      + "\nqualifiedName cannot be resolved"
      + "\nequals cannot be resolved"
      + "\nqualifiedName cannot be resolved"
      + "\nequals cannot be resolved");
  }
}
