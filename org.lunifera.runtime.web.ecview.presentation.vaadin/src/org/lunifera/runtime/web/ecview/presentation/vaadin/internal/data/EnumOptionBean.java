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
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal.data;

public class EnumOptionBean {

	private final Enum<?> enumx;
	private final String description;
	private final String imagePath;

	public EnumOptionBean(Enum<?> enumx, String description, String imagePath) {
		super();
		this.enumx = enumx;
		this.description = description;
		this.imagePath = imagePath;
	}

	/**
	 * @return the enumx
	 */
	public Enum<?> getEnumx() {
		return enumx;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}

}
