package org.lunifera.runtime.web.testbase.api;

import com.vaadin.ui.Component;

/**
 * Vaadin UI components that should become tested by selenium must implement
 * that interface. It will become hooked by the TestbaseUI and new instances of
 * the test component are requested.
 * 
 * Subclasses that should become tested must also be annotated with the Testable
 * annotation.
 */
public interface ITestPart {

	/**
	 * Returns the vaadin component that will be displayed if the fragment is
	 * addressed. Fragment is specified in the Testable annotation.
	 * 
	 * @return
	 */
	Component getTestComponent();

}
