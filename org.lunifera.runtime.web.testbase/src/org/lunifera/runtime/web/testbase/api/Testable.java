package org.lunifera.runtime.web.testbase.api;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = { CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE,
		PARAMETER, TYPE })
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
