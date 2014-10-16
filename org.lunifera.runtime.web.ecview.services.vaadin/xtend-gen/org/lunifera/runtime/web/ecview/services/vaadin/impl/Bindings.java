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

@SuppressWarnings("all")
public class Bindings {
  /* @Property
   */private String bindingList;
  
  public Bindings(final String bindingList) {
    this.bindingList = bindingList;
  }
  
  public String add(final String string) {
    String _concat = this.bindingList.concat("\n");
    String _concat_1 = _concat.concat(string);
    return this.bindingList = _concat_1;
  }
}
