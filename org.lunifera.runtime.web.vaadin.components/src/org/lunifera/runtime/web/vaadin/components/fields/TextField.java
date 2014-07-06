/**
 * Copyright (c) 2011 - 2014, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Florian Pirchner - Initial implementation
 * 		Hans Georg Glöckler - Initial implementation
 */
package org.lunifera.runtime.web.vaadin.components.fields;

import com.vaadin.data.Property;

/**
 * A textfield specific for redvoodo.
 */
@SuppressWarnings("serial")
public class TextField extends com.vaadin.ui.TextField {

	public TextField() {
		super();
	}

	public TextField(Property<String> dataSource) {
		super(dataSource);
	}

	public TextField(String caption, Property<String> dataSource) {
		super(caption, dataSource);
	}

	public TextField(String caption, String value) {
		super(caption, value);
	}

	public TextField(String caption) {
		super(caption);
	}

}
