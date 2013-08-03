package org.lunifera.runtime.web.vaadin.osgi.tests.context.helper;

import org.lunifera.runtime.web.vaadin.osgi.common.IOSGiUiProviderFactory;
import org.lunifera.runtime.web.vaadin.osgi.webapp.OSGiUIProvider;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

/**
 * Registered for vaadin application "providerFactoryTest".
 */
@SuppressWarnings({ "restriction", "serial" })
public class UI_WithProviderFactory extends UI {

	@Override
	protected void init(VaadinRequest request) {
		
	}

	/**
	 * An OSGi service that provides UiProvider.
	 */
	public static class ProviderFactory implements IOSGiUiProviderFactory {
		
		public ProviderFactory(){
			
		}
		
		@Override
		public OSGiUIProvider createUiProvider(String vaadinApplication,
				Class<? extends UI> uiClass) {
			return vaadinApplication.equals("providerFactoryTest") ? new UiProvider(
					vaadinApplication, uiClass) : null;
		}
	}

	/**
	 * The custom UIProvider.
	 */
	public static class UiProvider extends OSGiUIProvider {
		public UiProvider(String vaadinApplication, Class<? extends UI> uiClass) {
			super(vaadinApplication, uiClass);
		}

	}
}
