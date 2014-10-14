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
public class Counter {
  private int _value;
  
  public int getValue() {
    return this._value;
  }
  
  public void setValue(final int value) {
    this._value = value;
  }
  
  public Counter(final int value) {
    this.setValue(value);
  }
}
