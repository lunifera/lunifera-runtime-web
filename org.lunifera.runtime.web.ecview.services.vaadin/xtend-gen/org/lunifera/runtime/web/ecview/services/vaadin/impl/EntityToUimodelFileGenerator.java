/**
 * Copyright (c) 2011 - 2015, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
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
        _builder.append(this.counter.value = (this.counter.value + 1), "\t\t\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append(this.counter.value = 0, "\t\t");
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
        _builder.append(this.counter.value = (this.counter.value + 1), "\t\t\t");
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
    throw new Error("Unresolved compilation problems:"
      + "\nThe method type is undefined for the type EntityToUimodelFileGenerator"
      + "\nThe method type is undefined for the type EntityToUimodelFileGenerator"
      + "\nThe method type is undefined for the type EntityToUimodelFileGenerator"
      + "\nThe method type is undefined for the type EntityToUimodelFileGenerator");
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
    throw new Error("Unresolved compilation problems:"
      + "\nThe method type is undefined for the type EntityToUimodelFileGenerator"
      + "\nThe method type is undefined for the type EntityToUimodelFileGenerator"
      + "\nThe method type is undefined for the type EntityToUimodelFileGenerator"
      + "\nThe method type is undefined for the type EntityToUimodelFileGenerator");
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
