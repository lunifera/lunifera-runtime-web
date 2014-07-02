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

package org.lunifera.runtime.web.testbase.api;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = { TYPE })
public @interface Testable {
	/**
	 * Defines the URI fragment that is used to access the test component. For
	 * instance "TextFieldTest". It may be addressed by
	 * "localhost:8080/testapplication#TextFieldTest"
	 * 
	 * @return
	 */
	String uriFragment();

}
