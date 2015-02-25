/**
 * Copyright (c) 2011 - 2014, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * 		Klemens Edler - Initial implementation
 */
package org.lunifera.runtime.web.ecview.services.vaadin.impl;

import com.google.common.base.Objects;
import java.util.List;
import javax.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.lunifera.dsl.semantic.common.types.LDataType;
import org.lunifera.dsl.semantic.common.types.LMultiplicity;
import org.lunifera.dsl.semantic.common.types.LReference;
import org.lunifera.dsl.semantic.common.types.LScalarType;
import org.lunifera.dsl.semantic.common.types.LType;
import org.lunifera.dsl.semantic.common.types.LTypedPackage;
import org.lunifera.dsl.semantic.common.types.LUpperBound;
import org.lunifera.dsl.semantic.entity.LBean;
import org.lunifera.dsl.semantic.entity.LBeanAttribute;
import org.lunifera.dsl.semantic.entity.LBeanReference;
import org.lunifera.dsl.semantic.entity.LEntity;
import org.lunifera.dsl.semantic.entity.LEntityAttribute;
import org.lunifera.dsl.semantic.entity.LEntityFeature;
import org.lunifera.dsl.semantic.entity.LEntityReference;
import org.lunifera.runtime.web.ecview.services.vaadin.impl.BeanHelper;
import org.lunifera.runtime.web.ecview.services.vaadin.impl.Bindings;
import org.lunifera.runtime.web.ecview.services.vaadin.impl.Counter;
import org.lunifera.runtime.web.ecview.services.vaadin.impl.TypeHelper;

/**
 * This generator automatically creates a generic .uimodel-file from a given entity.
 */
@SuppressWarnings("all")
public class EntityToUimodelFileGenerator {
  @Inject
  private TypeHelper typeHelper;
  
  private Bindings bindings = new Bindings("");
  
  private Counter counter = new Counter(0);
  
  public CharSequence getContent(final LEntity entity) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package ");
    String _toUimodelName = this.getToUimodelName(entity);
    _builder.append(_toUimodelName, "");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("import ");
    EObject _eContainer = entity.eContainer();
    String _name = ((LTypedPackage) _eContainer).getName();
    _builder.append(_name, "");
    _builder.append(".*");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("ideview ");
    String _name_1 = entity.getName();
    _builder.append(_name_1, "");
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("datasource ds: ");
    String _toEntityFQN = this.getToEntityFQN(entity);
    _builder.append(_toEntityFQN, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("horizontalLayout ");
    String _name_2 = entity.getName();
    _builder.append(_name_2, "\t");
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("form leftForm {");
    _builder.newLine();
    {
      EList<LEntityFeature> _features = entity.getFeatures();
      for(final LEntityFeature feature : _features) {
        {
          if (((this.counter.value % 2) == 0)) {
            {
              if ((feature instanceof LEntityAttribute)) {
                {
                  LScalarType _type = ((LEntityAttribute)feature).getType();
                  if ((_type instanceof LDataType)) {
                    _builder.append("\t\t\t");
                    String _toAttributeUiField = this.getToAttributeUiField(((LEntityAttribute)feature));
                    _builder.append(_toAttributeUiField, "\t\t\t");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t\t\t");
                    String _toBeanRefUiField = this.getToBeanRefUiField(((LEntityAttribute)feature));
                    _builder.append(_toBeanRefUiField, "\t\t\t");
                    _builder.newLineIfNotEmpty();
                  }
                }
              } else {
                if ((feature instanceof LReference)) {
                  _builder.append("\t\t\t");
                  String _toEntityRefUiField = this.getToEntityRefUiField(feature);
                  _builder.append(_toEntityRefUiField, "\t\t\t");
                  _builder.newLineIfNotEmpty();
                }
              }
            }
          }
        }
        _builder.append("\t\t\t");
        int _value = this.counter.value = (this.counter.value + 1);
        _builder.append(_value, "\t\t\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    int _value_1 = this.counter.value = 0;
    _builder.append(_value_1, "\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("form rightForm {");
    _builder.newLine();
    {
      EList<LEntityFeature> _features_1 = entity.getFeatures();
      for(final LEntityFeature feature_1 : _features_1) {
        {
          if (((this.counter.value % 2) == 1)) {
            {
              if ((feature_1 instanceof LEntityAttribute)) {
                {
                  LScalarType _type_1 = ((LEntityAttribute)feature_1).getType();
                  if ((_type_1 instanceof LDataType)) {
                    _builder.append("\t\t\t");
                    String _toAttributeUiField_1 = this.getToAttributeUiField(((LEntityAttribute)feature_1));
                    _builder.append(_toAttributeUiField_1, "\t\t\t");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t\t\t");
                    String _toBeanRefUiField_1 = this.getToBeanRefUiField(((LEntityAttribute)feature_1));
                    _builder.append(_toBeanRefUiField_1, "\t\t\t");
                    _builder.newLineIfNotEmpty();
                  }
                }
              } else {
                if ((feature_1 instanceof LReference)) {
                  _builder.append("\t\t\t");
                  String _toEntityRefUiField_1 = this.getToEntityRefUiField(feature_1);
                  _builder.append(_toEntityRefUiField_1, "\t\t\t");
                  _builder.newLineIfNotEmpty();
                }
              }
            }
          }
        }
        _builder.append("\t\t\t");
        int _value_2 = this.counter.value = (this.counter.value + 1);
        _builder.append(_value_2, "\t\t\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append(this.bindings.bindingList, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public String getToUimodelName(final LEntity entity) {
    EObject _eContainer = entity.eContainer();
    String _name = ((LTypedPackage) _eContainer).getName();
    return (_name + ".uimodel");
  }
  
  public String getToEntityFQN(final LEntity entity) {
    EObject _eContainer = entity.eContainer();
    String _name = ((LTypedPackage) _eContainer).getName();
    String _plus = (_name + ".");
    String _name_1 = entity.getName();
    return (_plus + _name_1);
  }
  
  public String getToAttributeUiField(final LEntityAttribute attribute) {
    LScalarType _type = attribute.getType();
    final LDataType datatype = ((LDataType) _type);
    boolean _isDate = datatype.isDate();
    if (_isDate) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("bind ds.");
      String _name = attribute.getName();
      _builder.append(_name, "");
      _builder.append(" <--> [this.");
      {
        if (((this.counter.value % 2) == 0)) {
          _builder.append("leftform");
        } else {
          _builder.append("rightform");
        }
      }
      _builder.append(".");
      String _name_1 = attribute.getName();
      _builder.append(_name_1, "");
      _builder.append("].value");
      this.bindings.add(_builder.toString());
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("datefield ");
      String _name_2 = attribute.getName();
      _builder_1.append(_name_2, "");
      _builder_1.newLineIfNotEmpty();
      return _builder_1.toString();
    } else {
      JvmTypeReference _jvmTypeReference = datatype.getJvmTypeReference();
      JvmType _type_1 = _jvmTypeReference.getType();
      boolean _isString = this.typeHelper.isString(_type_1);
      if (_isString) {
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("bind ds.");
        String _name_3 = attribute.getName();
        _builder_2.append(_name_3, "");
        _builder_2.append(" <--> [this.");
        {
          if (((this.counter.value % 2) == 0)) {
            _builder_2.append("leftform");
          } else {
            _builder_2.append("rightform");
          }
        }
        _builder_2.append(".");
        String _name_4 = attribute.getName();
        _builder_2.append(_name_4, "");
        _builder_2.append("].value");
        this.bindings.add(_builder_2.toString());
        StringConcatenation _builder_3 = new StringConcatenation();
        _builder_3.append("textfield ");
        String _name_5 = attribute.getName();
        _builder_3.append(_name_5, "");
        _builder_3.newLineIfNotEmpty();
        return _builder_3.toString();
      } else {
        JvmTypeReference _jvmTypeReference_1 = datatype.getJvmTypeReference();
        JvmType _type_2 = _jvmTypeReference_1.getType();
        boolean _isBoolean = this.typeHelper.isBoolean(_type_2);
        if (_isBoolean) {
          StringConcatenation _builder_4 = new StringConcatenation();
          _builder_4.append("bind ds.");
          String _name_6 = attribute.getName();
          _builder_4.append(_name_6, "");
          _builder_4.append(" <--> [this.");
          {
            if (((this.counter.value % 2) == 0)) {
              _builder_4.append("leftform");
            } else {
              _builder_4.append("rightform");
            }
          }
          _builder_4.append(".");
          String _name_7 = attribute.getName();
          _builder_4.append(_name_7, "");
          _builder_4.append("].value");
          this.bindings.add(_builder_4.toString());
          StringConcatenation _builder_5 = new StringConcatenation();
          _builder_5.append("checkbox ");
          String _name_8 = attribute.getName();
          _builder_5.append(_name_8, "");
          _builder_5.newLineIfNotEmpty();
          return _builder_5.toString();
        } else {
          JvmTypeReference _jvmTypeReference_2 = datatype.getJvmTypeReference();
          JvmType _type_3 = _jvmTypeReference_2.getType();
          boolean _isNumber = this.typeHelper.isNumber(_type_3);
          if (_isNumber) {
            JvmTypeReference _jvmTypeReference_3 = datatype.getJvmTypeReference();
            JvmType _type_4 = _jvmTypeReference_3.getType();
            boolean _isDecimal = this.typeHelper.isDecimal(_type_4);
            if (_isDecimal) {
              StringConcatenation _builder_6 = new StringConcatenation();
              _builder_6.append("bind ds.");
              String _name_9 = attribute.getName();
              _builder_6.append(_name_9, "");
              _builder_6.append(" <--> [this.");
              {
                if (((this.counter.value % 2) == 0)) {
                  _builder_6.append("leftform");
                } else {
                  _builder_6.append("rightform");
                }
              }
              _builder_6.append(".");
              String _name_10 = attribute.getName();
              _builder_6.append(_name_10, "");
              _builder_6.append("].value");
              this.bindings.add(_builder_6.toString());
              StringConcatenation _builder_7 = new StringConcatenation();
              _builder_7.append("decimalField ");
              String _name_11 = attribute.getName();
              _builder_7.append(_name_11, "");
              _builder_7.newLineIfNotEmpty();
              return _builder_7.toString();
            } else {
              StringConcatenation _builder_8 = new StringConcatenation();
              _builder_8.append("bind ds.");
              String _name_12 = attribute.getName();
              _builder_8.append(_name_12, "");
              _builder_8.append(" <--> [this.");
              {
                if (((this.counter.value % 2) == 0)) {
                  _builder_8.append("leftform");
                } else {
                  _builder_8.append("rightform");
                }
              }
              _builder_8.append(".");
              String _name_13 = attribute.getName();
              _builder_8.append(_name_13, "");
              _builder_8.append("].value");
              this.bindings.add(_builder_8.toString());
              StringConcatenation _builder_9 = new StringConcatenation();
              _builder_9.append("numericField ");
              String _name_14 = attribute.getName();
              _builder_9.append(_name_14, "");
              _builder_9.newLineIfNotEmpty();
              return _builder_9.toString();
            }
          }
        }
      }
    }
    return null;
  }
  
  public String getToEntityRefUiField(final LEntityFeature feature) {
    final LEntityReference ref = ((LEntityReference) feature);
    LMultiplicity _multiplicity = ref.getMultiplicity();
    LUpperBound _upper = _multiplicity.getUpper();
    boolean _equals = Objects.equal(_upper, LUpperBound.ONE);
    if (_equals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("bind ds.");
      String _name = feature.getName();
      _builder.append(_name, "");
      _builder.append(" <--> [this.");
      {
        if (((this.counter.value % 2) == 0)) {
          _builder.append("leftform");
        } else {
          _builder.append("rightform");
        }
      }
      _builder.append(".");
      String _name_1 = feature.getName();
      _builder.append(_name_1, "");
      _builder.append("].value");
      this.bindings.add(_builder.toString());
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("referenceField ");
      String _name_2 = feature.getName();
      _builder_1.append(_name_2, "");
      _builder_1.append(" {");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      _builder_1.append("type ");
      LEntity _type = ref.getType();
      String _name_3 = _type.getName();
      _builder_1.append(_name_3, "\t");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      _builder_1.append("captionField ");
      EObject _eContainer = feature.eContainer();
      String _findCaptionProperty = BeanHelper.findCaptionProperty(((LEntity) _eContainer));
      _builder_1.append(_findCaptionProperty, "\t");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("}");
      _builder_1.newLine();
      return _builder_1.toString();
    }
    LMultiplicity _multiplicity_1 = ref.getMultiplicity();
    LUpperBound _upper_1 = _multiplicity_1.getUpper();
    boolean _equals_1 = Objects.equal(_upper_1, LUpperBound.MANY);
    if (_equals_1) {
      StringConcatenation _builder_2 = new StringConcatenation();
      _builder_2.append("bind ds.");
      String _name_4 = feature.getName();
      _builder_2.append(_name_4, "");
      _builder_2.append(" <--> [this.");
      {
        if (((this.counter.value % 2) == 0)) {
          _builder_2.append("leftform");
        } else {
          _builder_2.append("rightform");
        }
      }
      _builder_2.append(".");
      String _name_5 = feature.getName();
      _builder_2.append(_name_5, "");
      _builder_2.append("].collection");
      this.bindings.add(_builder_2.toString());
      StringConcatenation _builder_3 = new StringConcatenation();
      _builder_3.append("table ");
      String _name_6 = feature.getName();
      _builder_3.append(_name_6, "");
      _builder_3.append(" {");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append("\t");
      _builder_3.append("type ");
      LEntity _type_1 = ref.getType();
      String _name_7 = _type_1.getName();
      _builder_3.append(_name_7, "\t");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append("\t");
      _builder_3.append("columns {");
      _builder_3.newLine();
      _builder_3.append("\t\t");
      _builder_3.append("column ");
      EObject _eContainer_1 = feature.eContainer();
      String _findCaptionProperty_1 = BeanHelper.findCaptionProperty(((LEntity) _eContainer_1));
      _builder_3.append(_findCaptionProperty_1, "\t\t");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append("\t\t");
      _builder_3.append("column ");
      EObject _eContainer_2 = feature.eContainer();
      String _findDescriptionProperty = BeanHelper.findDescriptionProperty(((LEntity) _eContainer_2));
      _builder_3.append(_findDescriptionProperty, "\t\t");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append("\t");
      _builder_3.append("}");
      _builder_3.newLine();
      _builder_3.append("}");
      _builder_3.newLine();
      return _builder_3.toString();
    }
    return null;
  }
  
  public String getToBeanRefUiField(final LEntityAttribute attribute) {
    final LScalarType type = attribute.getType();
    if ((type instanceof LBean)) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("panel ");
      String _name = attribute.getName();
      _builder.append(_name, "");
      _builder.append(" {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("content horizontalLayout {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("form {");
      _builder.newLine();
      {
        List<LBeanAttribute> _allAttributes = ((LBean)type).getAllAttributes();
        for(final LBeanAttribute feature : _allAttributes) {
          {
            if ((feature instanceof LBeanAttribute)) {
              {
                LScalarType _type = feature.getType();
                if ((_type instanceof LDataType)) {
                  _builder.append("\t\t\t");
                  String _toBeanAttributeUiField = this.getToBeanAttributeUiField(feature);
                  _builder.append(_toBeanAttributeUiField, "\t\t\t");
                  _builder.newLineIfNotEmpty();
                } else {
                  _builder.append("\t\t\t");
                  String _toBeantoBeanRefUiField = this.getToBeantoBeanRefUiField(feature);
                  _builder.append(_toBeantoBeanRefUiField, "\t\t\t");
                  _builder.newLineIfNotEmpty();
                }
              }
            } else {
              if ((feature instanceof LReference)) {
                _builder.append("\t\t\t");
                String _toBeanReferenceUiField = this.getToBeanReferenceUiField(feature);
                _builder.append(_toBeanReferenceUiField, "\t\t\t");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      return _builder.toString();
    }
    return null;
  }
  
  public String getToBeantoBeanRefUiField(final LBeanAttribute attribute) {
    final LBeanReference ref = ((LBeanReference) attribute);
    LMultiplicity _multiplicity = ref.getMultiplicity();
    LUpperBound _upper = _multiplicity.getUpper();
    boolean _equals = Objects.equal(_upper, LUpperBound.ONE);
    if (_equals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("bind ds.");
      String _name = attribute.getName();
      _builder.append(_name, "");
      _builder.append(" <--> [this.");
      {
        if (((this.counter.value % 2) == 0)) {
          _builder.append("leftform");
        } else {
          _builder.append("rightform");
        }
      }
      _builder.append(".");
      String _name_1 = attribute.getName();
      _builder.append(_name_1, "");
      _builder.append("].value");
      this.bindings.add(_builder.toString());
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("referenceField ");
      String _name_2 = attribute.getName();
      _builder_1.append(_name_2, "");
      _builder_1.append(" {");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      _builder_1.append("type ");
      LType _type = ref.getType();
      String _name_3 = _type.getName();
      _builder_1.append(_name_3, "\t");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      _builder_1.append("captionField ");
      EObject _eContainer = attribute.eContainer();
      String _findCaptionProperty = BeanHelper.findCaptionProperty(((LBean) _eContainer));
      _builder_1.append(_findCaptionProperty, "\t");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("}");
      _builder_1.newLine();
      return _builder_1.toString();
    }
    LMultiplicity _multiplicity_1 = ref.getMultiplicity();
    LUpperBound _upper_1 = _multiplicity_1.getUpper();
    boolean _equals_1 = Objects.equal(_upper_1, LUpperBound.MANY);
    if (_equals_1) {
      StringConcatenation _builder_2 = new StringConcatenation();
      _builder_2.append("bind ds.");
      String _name_4 = attribute.getName();
      _builder_2.append(_name_4, "");
      _builder_2.append(" <--> [this.");
      {
        if (((this.counter.value % 2) == 0)) {
          _builder_2.append("leftform");
        } else {
          _builder_2.append("rightform");
        }
      }
      _builder_2.append(".");
      String _name_5 = attribute.getName();
      _builder_2.append(_name_5, "");
      _builder_2.append("].collection");
      this.bindings.add(_builder_2.toString());
      StringConcatenation _builder_3 = new StringConcatenation();
      _builder_3.append("table ");
      String _name_6 = attribute.getName();
      _builder_3.append(_name_6, "");
      _builder_3.append(" {");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append("\t");
      _builder_3.append("type ");
      LType _type_1 = ref.getType();
      String _name_7 = _type_1.getName();
      _builder_3.append(_name_7, "\t");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append("\t");
      _builder_3.append("columns {");
      _builder_3.newLine();
      _builder_3.append("\t\t");
      _builder_3.append("column ");
      EObject _eContainer_1 = attribute.eContainer();
      String _findCaptionProperty_1 = BeanHelper.findCaptionProperty(((LBean) _eContainer_1));
      _builder_3.append(_findCaptionProperty_1, "\t\t");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append("\t\t");
      _builder_3.append("column ");
      EObject _eContainer_2 = attribute.eContainer();
      String _findDescriptionProperty = BeanHelper.findDescriptionProperty(((LBean) _eContainer_2));
      _builder_3.append(_findDescriptionProperty, "\t\t");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append("\t");
      _builder_3.append("}");
      _builder_3.newLine();
      _builder_3.append("}");
      _builder_3.newLine();
      return _builder_3.toString();
    }
    return null;
  }
  
  public String getToBeanAttributeUiField(final LBeanAttribute attribute) {
    LScalarType _type = attribute.getType();
    final LDataType datatype = ((LDataType) _type);
    EObject _eContainer = attribute.eContainer();
    String _name = ((LBean) _eContainer).getName();
    final String beanName = StringExtensions.toFirstLower(_name);
    boolean _isDate = datatype.isDate();
    if (_isDate) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("bind ds.");
      _builder.append(beanName, "");
      _builder.append(".");
      String _name_1 = attribute.getName();
      _builder.append(_name_1, "");
      _builder.append(" <--> [this.");
      {
        if (((this.counter.value % 2) == 0)) {
          _builder.append("leftform");
        } else {
          _builder.append("rightform");
        }
      }
      _builder.append(".");
      _builder.append(beanName, "");
      _builder.append(".");
      String _name_2 = attribute.getName();
      _builder.append(_name_2, "");
      _builder.append("].value");
      this.bindings.add(_builder.toString());
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("datefield ");
      String _name_3 = attribute.getName();
      _builder_1.append(_name_3, "");
      _builder_1.newLineIfNotEmpty();
      return _builder_1.toString();
    } else {
      JvmTypeReference _jvmTypeReference = datatype.getJvmTypeReference();
      JvmType _type_1 = _jvmTypeReference.getType();
      boolean _isString = this.typeHelper.isString(_type_1);
      if (_isString) {
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("bind ds.");
        _builder_2.append(beanName, "");
        _builder_2.append(".");
        String _name_4 = attribute.getName();
        _builder_2.append(_name_4, "");
        _builder_2.append(" <--> [this.");
        {
          if (((this.counter.value % 2) == 0)) {
            _builder_2.append("leftform");
          } else {
            _builder_2.append("rightform");
          }
        }
        _builder_2.append(".");
        _builder_2.append(beanName, "");
        _builder_2.append(".");
        String _name_5 = attribute.getName();
        _builder_2.append(_name_5, "");
        _builder_2.append("].value");
        this.bindings.add(_builder_2.toString());
        StringConcatenation _builder_3 = new StringConcatenation();
        _builder_3.append("textfield ");
        String _name_6 = attribute.getName();
        _builder_3.append(_name_6, "");
        _builder_3.newLineIfNotEmpty();
        return _builder_3.toString();
      } else {
        JvmTypeReference _jvmTypeReference_1 = datatype.getJvmTypeReference();
        JvmType _type_2 = _jvmTypeReference_1.getType();
        boolean _isBoolean = this.typeHelper.isBoolean(_type_2);
        if (_isBoolean) {
          StringConcatenation _builder_4 = new StringConcatenation();
          _builder_4.append("bind ds.");
          _builder_4.append(beanName, "");
          _builder_4.append(".");
          String _name_7 = attribute.getName();
          _builder_4.append(_name_7, "");
          _builder_4.append(" <--> [this.");
          {
            if (((this.counter.value % 2) == 0)) {
              _builder_4.append("leftform");
            } else {
              _builder_4.append("rightform");
            }
          }
          _builder_4.append(".");
          _builder_4.append(beanName, "");
          _builder_4.append(".");
          String _name_8 = attribute.getName();
          _builder_4.append(_name_8, "");
          _builder_4.append("].value");
          this.bindings.add(_builder_4.toString());
          StringConcatenation _builder_5 = new StringConcatenation();
          _builder_5.append("checkbox ");
          String _name_9 = attribute.getName();
          _builder_5.append(_name_9, "");
          _builder_5.newLineIfNotEmpty();
          return _builder_5.toString();
        } else {
          JvmTypeReference _jvmTypeReference_2 = datatype.getJvmTypeReference();
          JvmType _type_3 = _jvmTypeReference_2.getType();
          boolean _isNumber = this.typeHelper.isNumber(_type_3);
          if (_isNumber) {
            JvmTypeReference _jvmTypeReference_3 = datatype.getJvmTypeReference();
            JvmType _type_4 = _jvmTypeReference_3.getType();
            boolean _isDecimal = this.typeHelper.isDecimal(_type_4);
            if (_isDecimal) {
              StringConcatenation _builder_6 = new StringConcatenation();
              _builder_6.append("bind ds.");
              _builder_6.append(beanName, "");
              _builder_6.append(".");
              String _name_10 = attribute.getName();
              _builder_6.append(_name_10, "");
              _builder_6.append(" <--> [this.");
              {
                if (((this.counter.value % 2) == 0)) {
                  _builder_6.append("leftform");
                } else {
                  _builder_6.append("rightform");
                }
              }
              _builder_6.append(".");
              _builder_6.append(beanName, "");
              _builder_6.append(".");
              String _name_11 = attribute.getName();
              _builder_6.append(_name_11, "");
              _builder_6.append("].value");
              this.bindings.add(_builder_6.toString());
              StringConcatenation _builder_7 = new StringConcatenation();
              _builder_7.append("decimalField ");
              String _name_12 = attribute.getName();
              _builder_7.append(_name_12, "");
              _builder_7.newLineIfNotEmpty();
              return _builder_7.toString();
            } else {
              StringConcatenation _builder_8 = new StringConcatenation();
              _builder_8.append("bind ds.");
              _builder_8.append(beanName, "");
              _builder_8.append(".");
              String _name_13 = attribute.getName();
              _builder_8.append(_name_13, "");
              _builder_8.append(" <--> [this.");
              {
                if (((this.counter.value % 2) == 0)) {
                  _builder_8.append("leftform");
                } else {
                  _builder_8.append("rightform");
                }
              }
              _builder_8.append(".");
              _builder_8.append(beanName, "");
              _builder_8.append(".");
              String _name_14 = attribute.getName();
              _builder_8.append(_name_14, "");
              _builder_8.append("].value");
              this.bindings.add(_builder_8.toString());
              StringConcatenation _builder_9 = new StringConcatenation();
              _builder_9.append("numericField ");
              String _name_15 = attribute.getName();
              _builder_9.append(_name_15, "");
              _builder_9.newLineIfNotEmpty();
              return _builder_9.toString();
            }
          }
        }
      }
    }
    return null;
  }
  
  public String getToBeanReferenceUiField(final LBeanAttribute attribute) {
    final LBeanReference ref = ((LBeanReference) attribute);
    LMultiplicity _multiplicity = ref.getMultiplicity();
    LUpperBound _upper = _multiplicity.getUpper();
    boolean _equals = Objects.equal(_upper, LUpperBound.ONE);
    if (_equals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("referenceField ");
      String _name = attribute.getName();
      _builder.append(_name, "");
      _builder.append(" {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("type ");
      LType _type = ref.getType();
      String _name_1 = _type.getName();
      _builder.append(_name_1, "\t");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("captionField uuid");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      return _builder.toString();
    }
    LMultiplicity _multiplicity_1 = ref.getMultiplicity();
    LUpperBound _upper_1 = _multiplicity_1.getUpper();
    boolean _equals_1 = Objects.equal(_upper_1, LUpperBound.MANY);
    if (_equals_1) {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("table ");
      String _name_2 = attribute.getName();
      _builder_1.append(_name_2, "");
      _builder_1.append(" {");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      _builder_1.append("type ");
      LType _type_1 = ref.getType();
      String _name_3 = _type_1.getName();
      _builder_1.append(_name_3, "\t");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      _builder_1.append("columns {");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("column uuid");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      return _builder_1.toString();
    }
    return null;
  }
  
  public String toDocu(final EObject element) {
    return "";
  }
}
