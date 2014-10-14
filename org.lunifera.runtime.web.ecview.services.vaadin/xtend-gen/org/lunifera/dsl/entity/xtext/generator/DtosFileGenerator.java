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
package org.lunifera.dsl.entity.xtext.generator;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import java.util.Arrays;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.lunifera.dsl.semantic.common.types.LAttribute;
import org.lunifera.dsl.semantic.common.types.LDataType;
import org.lunifera.dsl.semantic.common.types.LDateType;
import org.lunifera.dsl.semantic.common.types.LEnum;
import org.lunifera.dsl.semantic.common.types.LEnumLiteral;
import org.lunifera.dsl.semantic.common.types.LFeature;
import org.lunifera.dsl.semantic.common.types.LImport;
import org.lunifera.dsl.semantic.common.types.LReference;
import org.lunifera.dsl.semantic.common.types.LType;
import org.lunifera.dsl.semantic.common.types.LTypedPackage;
import org.lunifera.dsl.semantic.entity.LBean;
import org.lunifera.dsl.semantic.entity.LBeanFeature;
import org.lunifera.dsl.semantic.entity.LEntity;
import org.lunifera.dsl.semantic.entity.LEntityFeature;
import org.lunifera.dsl.semantic.entity.LEntityReference;

/**
 * This generator automatically creates a generic .dtos-file from a given entity model.
 * 	Imports, datatype declarations, enums and comments are carried over to the .dtos file,
 * 	while bean and entity declarations are translated to DTO declarations with the
 *  appropriate inheritance information. Attributes and references are defined as inherited
 *  in this default conversion.
 */
@SuppressWarnings("all")
public class DtosFileGenerator {
  @Inject
  @Extension
  private JvmTypesBuilder _jvmTypesBuilder;
  
  public CharSequence getContent(final LTypedPackage pkg) {
    StringConcatenation _builder = new StringConcatenation();
    String _docu = this.toDocu(pkg);
    _builder.append(_docu, "");
    _builder.newLineIfNotEmpty();
    _builder.append("package ");
    String _toDtoName = this.getToDtoName(pkg);
    _builder.append(_toDtoName, "");
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("/* Imports the original entity package */");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("import ");
    String _name = pkg.getName();
    _builder.append(_name, "\t");
    _builder.append(".*;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    {
      EList<LImport> _imports = pkg.getImports();
      for(final LImport lImport : _imports) {
        _builder.append("\t");
        String _docu_1 = this.toDocu(lImport);
        _builder.append(_docu_1, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("import ");
        String _importedNamespace = lImport.getImportedNamespace();
        _builder.append(_importedNamespace, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    {
      Iterable<LDataType> _datatypes = this.datatypes(pkg);
      for(final LDataType lDatatype : _datatypes) {
        _builder.append("\t");
        String _docu_2 = this.toDocu(lDatatype);
        _builder.append(_docu_2, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        CharSequence _dataType = this.toDataType(lDatatype);
        _builder.append(_dataType, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    {
      Iterable<LEntity> _entities = this.entities(pkg);
      for(final LEntity lEntity : _entities) {
        _builder.append("\t");
        String _docu_3 = this.toDocu(lEntity);
        _builder.append(_docu_3, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        String _entityDeclaration = this.toEntityDeclaration(lEntity);
        _builder.append(_entityDeclaration, "\t");
        _builder.newLineIfNotEmpty();
        {
          EList<LEntityFeature> _features = lEntity.getFeatures();
          final Function1<LEntityFeature, Boolean> _function = new Function1<LEntityFeature, Boolean>() {
            public Boolean apply(final LEntityFeature it) {
              boolean _or = false;
              if ((it instanceof LAttribute)) {
                _or = true;
              } else {
                _or = (it instanceof LReference);
              }
              return Boolean.valueOf(_or);
            }
          };
          Iterable<LEntityFeature> _filter = IterableExtensions.<LEntityFeature>filter(_features, _function);
          for(final LEntityFeature feature : _filter) {
            _builder.append("\t");
            _builder.append("\t");
            CharSequence _feature = this.toFeature(feature);
            _builder.append(_feature, "\t\t");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
      }
    }
    {
      Iterable<LBean> _beans = this.beans(pkg);
      for(final LBean lBean : _beans) {
        _builder.append("\t");
        String _docu_4 = this.toDocu(lBean);
        _builder.append(_docu_4, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        String _beanDeclaration = this.toBeanDeclaration(lBean);
        _builder.append(_beanDeclaration, "\t");
        _builder.newLineIfNotEmpty();
        {
          EList<LBeanFeature> _features_1 = lBean.getFeatures();
          final Function1<LBeanFeature, Boolean> _function_1 = new Function1<LBeanFeature, Boolean>() {
            public Boolean apply(final LBeanFeature it) {
              boolean _or = false;
              if ((it instanceof LAttribute)) {
                _or = true;
              } else {
                _or = (it instanceof LReference);
              }
              return Boolean.valueOf(_or);
            }
          };
          Iterable<LBeanFeature> _filter_1 = IterableExtensions.<LBeanFeature>filter(_features_1, _function_1);
          for(final LBeanFeature feature_1 : _filter_1) {
            _builder.append("\t");
            _builder.append("\t");
            CharSequence _feature_1 = this.toFeature(feature_1);
            _builder.append(_feature_1, "\t\t");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
      }
    }
    {
      Iterable<LEnum> _enums = this.enums(pkg);
      for(final LEnum lEnum : _enums) {
        _builder.append("\t");
        _builder.append("enum ");
        String _name_1 = lEnum.getName();
        _builder.append(_name_1, "\t");
        _builder.append(" {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        String _literals = this.toLiterals(lEnum);
        _builder.append(_literals, "\t\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public String getToDtoName(final LTypedPackage pkg) {
    String _name = pkg.getName();
    return (_name + ".dtos");
  }
  
  public String toEntityDeclaration(final LEntity lEntity) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("dto ");
    String _name = lEntity.getName();
    _builder.append(_name, "");
    _builder.append("Dto ");
    {
      LEntity _superType = lEntity.getSuperType();
      boolean _notEquals = (!Objects.equal(_superType, null));
      if (_notEquals) {
        _builder.append("extends ");
        LEntity _superType_1 = lEntity.getSuperType();
        String _name_1 = _superType_1.getName();
        _builder.append(_name_1, "");
        _builder.append("Dto ");
      }
    }
    _builder.append("wraps ");
    String _name_2 = lEntity.getName();
    _builder.append(_name_2, "");
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  public String toBeanDeclaration(final LBean lBean) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("dto ");
    String _name = lBean.getName();
    _builder.append(_name, "");
    _builder.append("Dto ");
    {
      LBean _superType = lBean.getSuperType();
      boolean _notEquals = (!Objects.equal(_superType, null));
      if (_notEquals) {
        _builder.append("extends ");
        LBean _superType_1 = lBean.getSuperType();
        String _name_1 = _superType_1.getName();
        _builder.append(_name_1, "");
        _builder.append("Dto ");
      }
    }
    _builder.append("wraps ");
    String _name_2 = lBean.getName();
    _builder.append(_name_2, "");
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  public String toDocu(final EObject element) {
    String docu = this._jvmTypesBuilder.getDocumentation(element);
    boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(docu);
    boolean _not = (!_isNullOrEmpty);
    if (_not) {
      String[] docus = docu.split("\n");
      int _length = docus.length;
      boolean _greaterThan = (_length > 1);
      if (_greaterThan) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("/**");
        _builder.newLine();
        {
          for(final String line : docus) {
            _builder.append(" * ", "");
            _builder.append(line, "");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append(" ");
        _builder.append("*/");
        return _builder.toString();
      } else {
        int _length_1 = docus.length;
        boolean _equals = (_length_1 == 1);
        if (_equals) {
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("/** ");
          _builder_1.append(docu, "");
          _builder_1.append(" */");
          return _builder_1.toString();
        }
      }
    }
    return "";
  }
  
  protected CharSequence _toFeature(final LAttribute att) {
    StringConcatenation _builder = new StringConcatenation();
    String _docu = this.toDocu(att);
    _builder.append(_docu, "");
    _builder.newLineIfNotEmpty();
    _builder.append("inherit var ");
    String _name = att.getName();
    _builder.append(_name, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _toFeature(final LReference att) {
    StringConcatenation _builder = new StringConcatenation();
    String _docu = this.toDocu(att);
    _builder.append(_docu, "");
    _builder.newLineIfNotEmpty();
    _builder.append("inherit ref ");
    String _name = att.getName();
    _builder.append(_name, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _toFeature(final LEntityReference att) {
    StringConcatenation _builder = new StringConcatenation();
    String _docu = this.toDocu(att);
    _builder.append(_docu, "");
    _builder.newLineIfNotEmpty();
    _builder.append("inherit ref ");
    String _name = att.getName();
    _builder.append(_name, "");
    _builder.append(" mapto ");
    LEntity _type = att.getType();
    String _name_1 = _type.getName();
    _builder.append(_name_1, "");
    _builder.append("Dto;");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public String toLiterals(final LEnum lEnum) {
    StringBuilder result = new StringBuilder();
    EList<LEnumLiteral> _literals = lEnum.getLiterals();
    for (final LEnumLiteral lit : _literals) {
      {
        String _name = lit.getName();
        result.append(_name);
        result.append(", ");
      }
    }
    int _length = result.length();
    int _minus = (_length - 2);
    return result.substring(0, _minus);
  }
  
  public CharSequence toDataType(final LDataType lDatatype) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("datatype ");
    String _name = lDatatype.getName();
    _builder.append(_name, "");
    _builder.append(" ");
    {
      boolean _isDate = lDatatype.isDate();
      if (_isDate) {
        _builder.append("dateType ");
        {
          LDateType _dateType = lDatatype.getDateType();
          boolean _equals = Objects.equal(_dateType, LDateType.DATE);
          if (_equals) {
            _builder.append("date");
          } else {
            LDateType _dateType_1 = lDatatype.getDateType();
            boolean _equals_1 = Objects.equal(_dateType_1, LDateType.TIME);
            if (_equals_1) {
              _builder.append("time");
            } else {
              _builder.append("timestamp");
            }
          }
        }
      } else {
        boolean _isAsBlob = lDatatype.isAsBlob();
        if (_isAsBlob) {
          _builder.append("as blob");
        } else {
          _builder.append("jvmType ");
          JvmTypeReference _jvmTypeReference = lDatatype.getJvmTypeReference();
          JvmType _type = _jvmTypeReference.getType();
          String _qualifiedName = _type.getQualifiedName();
          _builder.append(_qualifiedName, "");
          {
            boolean _isAsPrimitive = lDatatype.isAsPrimitive();
            if (_isAsPrimitive) {
              _builder.append(" as primitive");
            }
          }
        }
      }
    }
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public String toPackageName(final LTypedPackage pkg) {
    String _name = pkg.getName();
    return (_name + ".dtos");
  }
  
  public Iterable<LDataType> datatypes(final LTypedPackage pkg) {
    EList<LType> _types = pkg.getTypes();
    final Function1<LType, Boolean> _function = new Function1<LType, Boolean>() {
      public Boolean apply(final LType it) {
        return Boolean.valueOf((it instanceof LDataType));
      }
    };
    Iterable<LType> _filter = IterableExtensions.<LType>filter(_types, _function);
    final Function1<LType, LDataType> _function_1 = new Function1<LType, LDataType>() {
      public LDataType apply(final LType it) {
        return ((LDataType) it);
      }
    };
    return IterableExtensions.<LType, LDataType>map(_filter, _function_1);
  }
  
  public Iterable<LEntity> entities(final LTypedPackage pkg) {
    EList<LType> _types = pkg.getTypes();
    final Function1<LType, Boolean> _function = new Function1<LType, Boolean>() {
      public Boolean apply(final LType it) {
        return Boolean.valueOf((it instanceof LEntity));
      }
    };
    Iterable<LType> _filter = IterableExtensions.<LType>filter(_types, _function);
    final Function1<LType, LEntity> _function_1 = new Function1<LType, LEntity>() {
      public LEntity apply(final LType it) {
        return ((LEntity) it);
      }
    };
    return IterableExtensions.<LType, LEntity>map(_filter, _function_1);
  }
  
  public Iterable<LBean> beans(final LTypedPackage pkg) {
    EList<LType> _types = pkg.getTypes();
    final Function1<LType, Boolean> _function = new Function1<LType, Boolean>() {
      public Boolean apply(final LType it) {
        return Boolean.valueOf((it instanceof LBean));
      }
    };
    Iterable<LType> _filter = IterableExtensions.<LType>filter(_types, _function);
    final Function1<LType, LBean> _function_1 = new Function1<LType, LBean>() {
      public LBean apply(final LType it) {
        return ((LBean) it);
      }
    };
    return IterableExtensions.<LType, LBean>map(_filter, _function_1);
  }
  
  public Iterable<LEnum> enums(final LTypedPackage pkg) {
    EList<LType> _types = pkg.getTypes();
    final Function1<LType, Boolean> _function = new Function1<LType, Boolean>() {
      public Boolean apply(final LType it) {
        return Boolean.valueOf((it instanceof LEnum));
      }
    };
    Iterable<LType> _filter = IterableExtensions.<LType>filter(_types, _function);
    final Function1<LType, LEnum> _function_1 = new Function1<LType, LEnum>() {
      public LEnum apply(final LType it) {
        return ((LEnum) it);
      }
    };
    return IterableExtensions.<LType, LEnum>map(_filter, _function_1);
  }
  
  public CharSequence toFeature(final LFeature att) {
    if (att instanceof LEntityReference) {
      return _toFeature((LEntityReference)att);
    } else if (att instanceof LAttribute) {
      return _toFeature((LAttribute)att);
    } else if (att instanceof LReference) {
      return _toFeature((LReference)att);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(att).toString());
    }
  }
}
