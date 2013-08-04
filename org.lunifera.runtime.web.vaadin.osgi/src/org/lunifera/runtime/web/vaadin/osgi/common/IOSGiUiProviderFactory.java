package org.lunifera.runtime.web.vaadin.osgi.common;

import com.vaadin.ui.UI;

/**
 * Is used to prepare customer ui provider for a UI.class or a vaadin
 * application.
 */
public interface IOSGiUiProviderFactory {

	/**
	 * Returns a new instance of the OSGI UI provider. May return null.
	 * 
	 * @param vaadinApplication
	 *            - the name of the UI provider
	 * @param uiClass
	 *            - the UI class that will be instantiated by the provider
	 * @return
	 */
	CustomOSGiUiProvider createUiProvider(String vaadinApplication,
			Class<? extends UI> uiClass);

}
