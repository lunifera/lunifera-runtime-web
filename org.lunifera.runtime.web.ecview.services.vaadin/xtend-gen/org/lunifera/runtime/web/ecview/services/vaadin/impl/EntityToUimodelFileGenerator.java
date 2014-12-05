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

import javax.inject.Inject;
import org.eclipse.emf.ecore.EObject;
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
  
  public CharSequence getContent(final /* LEntity */Object entity) {
    throw new Error("Unresolved compilation problems:"
      + "\nLTypedPackage cannot be resolved to a type."
      + "\nLEntityFeature cannot be resolved to a type."
      + "\nLEntityAttribute cannot be resolved to a type."
      + "\nLDataType cannot be resolved to a type."
      + "\nLReference cannot be resolved to a type."
      + "\nLEntityFeature cannot be resolved to a type."
      + "\nLEntityAttribute cannot be resolved to a type."
      + "\nLDataType cannot be resolved to a type."
      + "\nLReference cannot be resolved to a type."
      + "\ntoUimodelName cannot be resolved"
      + "\neContainer cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\ntoEntityFQN cannot be resolved"
      + "\nname cannot be resolved"
      + "\nfeatures cannot be resolved"
      + "\ntype cannot be resolved"
      + "\ntoAttributeUiField cannot be resolved"
      + "\ntoBeanRefUiField cannot be resolved"
      + "\ntoEntityRefUiField cannot be resolved"
      + "\nfeatures cannot be resolved"
      + "\ntype cannot be resolved"
      + "\ntoAttributeUiField cannot be resolved"
      + "\ntoBeanRefUiField cannot be resolved"
      + "\ntoEntityRefUiField cannot be resolved");
  }
  
  public String getToUimodelName(final /* LEntity */Object entity) {
    throw new Error("Unresolved compilation problems:"
      + "\nLTypedPackage cannot be resolved to a type."
      + "\neContainer cannot be resolved"
      + "\nname cannot be resolved"
      + "\n+ cannot be resolved");
  }
  
  public String getToEntityFQN(final /* LEntity */Object entity) {
    throw new Error("Unresolved compilation problems:"
      + "\nLTypedPackage cannot be resolved to a type."
      + "\neContainer cannot be resolved"
      + "\nname cannot be resolved"
      + "\n+ cannot be resolved"
      + "\n+ cannot be resolved"
      + "\nname cannot be resolved");
  }
  
  public String getToAttributeUiField(final /* LEntityAttribute */Object attribute) {
    throw new Error("Unresolved compilation problems:"
      + "\nLDataType cannot be resolved to a type."
      + "\nLDataType cannot be resolved to a type."
      + "\nThe method isString is undefined for the type EntityToUimodelFileGenerator"
      + "\nThe method isBoolean is undefined for the type EntityToUimodelFileGenerator"
      + "\nThe method isNumber is undefined for the type EntityToUimodelFileGenerator"
      + "\nThe method isDecimal is undefined for the type EntityToUimodelFileGenerator"
      + "\ntype cannot be resolved"
      + "\ndate cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\njvmTypeReference cannot be resolved"
      + "\ntype cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\njvmTypeReference cannot be resolved"
      + "\ntype cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\njvmTypeReference cannot be resolved"
      + "\ntype cannot be resolved"
      + "\njvmTypeReference cannot be resolved"
      + "\ntype cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved");
  }
  
  public String getToEntityRefUiField(final /* LEntityFeature */Object feature) {
    throw new Error("Unresolved compilation problems:"
      + "\nLEntityReference cannot be resolved to a type."
      + "\nLEntity cannot be resolved to a type."
      + "\nLEntity cannot be resolved to a type."
      + "\nLEntity cannot be resolved to a type."
      + "\nThe method or field LUpperBound is undefined for the type EntityToUimodelFileGenerator"
      + "\nThe method or field LUpperBound is undefined for the type EntityToUimodelFileGenerator"
      + "\nmultiplicity cannot be resolved"
      + "\nupper cannot be resolved"
      + "\n== cannot be resolved"
      + "\nONE cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\ntype cannot be resolved"
      + "\nname cannot be resolved"
      + "\neContainer cannot be resolved"
      + "\nmultiplicity cannot be resolved"
      + "\nupper cannot be resolved"
      + "\n== cannot be resolved"
      + "\nMANY cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\ntype cannot be resolved"
      + "\nname cannot be resolved"
      + "\neContainer cannot be resolved"
      + "\neContainer cannot be resolved");
  }
  
  public String getToBeanRefUiField(final /* LEntityAttribute */Object attribute) {
    throw new Error("Unresolved compilation problems:"
      + "\nLBean cannot be resolved to a type."
      + "\nLBeanAttribute cannot be resolved to a type."
      + "\nLBeanAttribute cannot be resolved to a type."
      + "\nLDataType cannot be resolved to a type."
      + "\nLReference cannot be resolved to a type."
      + "\ntype cannot be resolved"
      + "\nname cannot be resolved"
      + "\nallAttributes cannot be resolved"
      + "\ntype cannot be resolved"
      + "\ntoBeanAttributeUiField cannot be resolved"
      + "\ntoBeantoBeanRefUiField cannot be resolved"
      + "\ntoBeanReferenceUiField cannot be resolved");
  }
  
  public String getToBeantoBeanRefUiField(final /* LBeanAttribute */Object attribute) {
    throw new Error("Unresolved compilation problems:"
      + "\nLBeanReference cannot be resolved to a type."
      + "\nLBean cannot be resolved to a type."
      + "\nLBean cannot be resolved to a type."
      + "\nLBean cannot be resolved to a type."
      + "\nThe method or field LUpperBound is undefined for the type EntityToUimodelFileGenerator"
      + "\nThe method or field LUpperBound is undefined for the type EntityToUimodelFileGenerator"
      + "\nmultiplicity cannot be resolved"
      + "\nupper cannot be resolved"
      + "\n== cannot be resolved"
      + "\nONE cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\ntype cannot be resolved"
      + "\nname cannot be resolved"
      + "\neContainer cannot be resolved"
      + "\nmultiplicity cannot be resolved"
      + "\nupper cannot be resolved"
      + "\n== cannot be resolved"
      + "\nMANY cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\ntype cannot be resolved"
      + "\nname cannot be resolved"
      + "\neContainer cannot be resolved"
      + "\neContainer cannot be resolved");
  }
  
  public String getToBeanAttributeUiField(final /* LBeanAttribute */Object attribute) {
    throw new Error("Unresolved compilation problems:"
      + "\nLDataType cannot be resolved to a type."
      + "\nLDataType cannot be resolved to a type."
      + "\nLBean cannot be resolved to a type."
      + "\nThe method isString is undefined for the type EntityToUimodelFileGenerator"
      + "\nThe method isBoolean is undefined for the type EntityToUimodelFileGenerator"
      + "\nThe method isNumber is undefined for the type EntityToUimodelFileGenerator"
      + "\nThe method isDecimal is undefined for the type EntityToUimodelFileGenerator"
      + "\ntype cannot be resolved"
      + "\neContainer cannot be resolved"
      + "\nname cannot be resolved"
      + "\ntoFirstLower cannot be resolved"
      + "\ndate cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\njvmTypeReference cannot be resolved"
      + "\ntype cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\njvmTypeReference cannot be resolved"
      + "\ntype cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\njvmTypeReference cannot be resolved"
      + "\ntype cannot be resolved"
      + "\njvmTypeReference cannot be resolved"
      + "\ntype cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved");
  }
  
  public String getToBeanReferenceUiField(final /* LBeanAttribute */Object attribute) {
    throw new Error("Unresolved compilation problems:"
      + "\nLBeanReference cannot be resolved to a type."
      + "\nThe method or field LUpperBound is undefined for the type EntityToUimodelFileGenerator"
      + "\nThe method or field LUpperBound is undefined for the type EntityToUimodelFileGenerator"
      + "\nmultiplicity cannot be resolved"
      + "\nupper cannot be resolved"
      + "\n== cannot be resolved"
      + "\nONE cannot be resolved"
      + "\nname cannot be resolved"
      + "\ntype cannot be resolved"
      + "\nname cannot be resolved"
      + "\nmultiplicity cannot be resolved"
      + "\nupper cannot be resolved"
      + "\n== cannot be resolved"
      + "\nMANY cannot be resolved"
      + "\nname cannot be resolved"
      + "\ntype cannot be resolved"
      + "\nname cannot be resolved");
  }
  
  public String toDocu(final EObject element) {
    return "";
  }
}
