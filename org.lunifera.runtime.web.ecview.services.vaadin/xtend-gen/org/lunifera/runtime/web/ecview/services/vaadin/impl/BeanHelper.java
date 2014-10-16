package org.lunifera.runtime.web.ecview.services.vaadin.impl;

import com.google.common.base.Objects;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.xbase.annotations.xAnnotations.XAnnotation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.lunifera.dsl.semantic.common.types.LAnnotationDef;
import org.lunifera.dsl.semantic.common.types.LAnnotationTarget;
import org.lunifera.dsl.semantic.entity.LBean;
import org.lunifera.dsl.semantic.entity.LBeanAttribute;
import org.lunifera.dsl.semantic.entity.LBeanFeature;
import org.lunifera.dsl.semantic.entity.LEntity;
import org.lunifera.dsl.semantic.entity.LEntityAttribute;
import org.lunifera.dsl.semantic.entity.LEntityFeature;
import org.lunifera.dsl.semantic.entity.LOperation;
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
    boolean _equals = Objects.equal(entity, null);
    if (_equals) {
      return null;
    }
    List<LEntityAttribute> _attributes = entity.getAttributes();
    for (final LEntityAttribute field : _attributes) {
      boolean _isDomainDescription = field.isDomainDescription();
      if (_isDomainDescription) {
        return field.getName();
      }
    }
    List<LOperation> _operations = entity.getOperations();
    for (final LOperation method : _operations) {
      {
        EList<LAnnotationDef> _annotations = method.getAnnotations();
        final Function1<LAnnotationDef, Boolean> _function = new Function1<LAnnotationDef, Boolean>() {
          public Boolean apply(final LAnnotationDef it) {
            XAnnotation _annotation = it.getAnnotation();
            JvmType _annotationType = _annotation.getAnnotationType();
            String _qualifiedName = _annotationType.getQualifiedName();
            String _canonicalName = DomainDescription.class.getCanonicalName();
            return Boolean.valueOf(_qualifiedName.equals(_canonicalName));
          }
        };
        final LAnnotationDef def = IterableExtensions.<LAnnotationDef>findFirst(_annotations, _function);
        boolean _notEquals = (!Objects.equal(def, null));
        if (_notEquals) {
          String _name = method.getName();
          return OperationExtensions.toPropertyName(_name);
        }
      }
    }
    List<LEntityAttribute> _allAttributes = entity.getAllAttributes();
    for (final LEntityAttribute field_1 : _allAttributes) {
      boolean _isDomainDescription_1 = field_1.isDomainDescription();
      if (_isDomainDescription_1) {
        return field_1.getName();
      }
    }
    List<LEntityFeature> _allFeatures = entity.getAllFeatures();
    for (final LEntityFeature method_1 : _allFeatures) {
      if ((method_1 instanceof org.lunifera.dsl.semantic.common.types.LOperation)) {
        EList<LAnnotationDef> _annotations = ((LAnnotationTarget)method_1).getAnnotations();
        final Function1<LAnnotationDef, Boolean> _function = new Function1<LAnnotationDef, Boolean>() {
          public Boolean apply(final LAnnotationDef it) {
            XAnnotation _annotation = it.getAnnotation();
            JvmType _annotationType = _annotation.getAnnotationType();
            String _qualifiedName = _annotationType.getQualifiedName();
            String _canonicalName = DomainDescription.class.getCanonicalName();
            return Boolean.valueOf(_qualifiedName.equals(_canonicalName));
          }
        };
        final LAnnotationDef def = IterableExtensions.<LAnnotationDef>findFirst(_annotations, _function);
        boolean _notEquals = (!Objects.equal(def, null));
        if (_notEquals) {
          String _name = method_1.getName();
          return OperationExtensions.toPropertyName(_name);
        }
      }
    }
    return null;
  }
  
  /**
   * Returns the description property if it could be found. Null otherwise.
   */
  public static String findDescriptionProperty(final LBean bean) {
    boolean _equals = Objects.equal(bean, null);
    if (_equals) {
      return null;
    }
    List<LBeanAttribute> _attributes = bean.getAttributes();
    for (final LBeanAttribute field : _attributes) {
      boolean _isDomainDescription = field.isDomainDescription();
      if (_isDomainDescription) {
        return field.getName();
      }
    }
    List<LOperation> _operations = bean.getOperations();
    for (final LOperation method : _operations) {
      {
        EList<LAnnotationDef> _annotations = method.getAnnotations();
        final Function1<LAnnotationDef, Boolean> _function = new Function1<LAnnotationDef, Boolean>() {
          public Boolean apply(final LAnnotationDef it) {
            XAnnotation _annotation = it.getAnnotation();
            JvmType _annotationType = _annotation.getAnnotationType();
            String _qualifiedName = _annotationType.getQualifiedName();
            String _canonicalName = DomainDescription.class.getCanonicalName();
            return Boolean.valueOf(_qualifiedName.equals(_canonicalName));
          }
        };
        final LAnnotationDef def = IterableExtensions.<LAnnotationDef>findFirst(_annotations, _function);
        boolean _notEquals = (!Objects.equal(def, null));
        if (_notEquals) {
          String _name = method.getName();
          return OperationExtensions.toPropertyName(_name);
        }
      }
    }
    List<LBeanAttribute> _allAttributes = bean.getAllAttributes();
    for (final LBeanAttribute field_1 : _allAttributes) {
      boolean _isDomainDescription_1 = field_1.isDomainDescription();
      if (_isDomainDescription_1) {
        return field_1.getName();
      }
    }
    List<LBeanFeature> _allFeatures = bean.getAllFeatures();
    for (final LBeanFeature method_1 : _allFeatures) {
      if ((method_1 instanceof org.lunifera.dsl.semantic.common.types.LOperation)) {
        EList<LAnnotationDef> _annotations = ((LAnnotationTarget)method_1).getAnnotations();
        final Function1<LAnnotationDef, Boolean> _function = new Function1<LAnnotationDef, Boolean>() {
          public Boolean apply(final LAnnotationDef it) {
            XAnnotation _annotation = it.getAnnotation();
            JvmType _annotationType = _annotation.getAnnotationType();
            String _qualifiedName = _annotationType.getQualifiedName();
            String _canonicalName = DomainDescription.class.getCanonicalName();
            return Boolean.valueOf(_qualifiedName.equals(_canonicalName));
          }
        };
        final LAnnotationDef def = IterableExtensions.<LAnnotationDef>findFirst(_annotations, _function);
        boolean _notEquals = (!Objects.equal(def, null));
        if (_notEquals) {
          String _name = method_1.getName();
          return OperationExtensions.toPropertyName(_name);
        }
      }
    }
    return null;
  }
}
