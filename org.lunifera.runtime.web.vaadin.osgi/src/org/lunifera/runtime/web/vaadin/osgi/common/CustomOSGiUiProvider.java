package org.lunifera.runtime.web.vaadin.osgi.common;

import org.lunifera.runtime.web.vaadin.osgi.webapp.OSGiUIProvider;

import com.vaadin.ui.UI;

/**
 * The base class for customer ui provider implementation.
 */
public abstract class CustomOSGiUiProvider extends OSGiUIProvider {

	public CustomOSGiUiProvider(String vaadinApplication,
			Class<? extends UI> uiClass) {
		super(vaadinApplication, uiClass);
	}

}
