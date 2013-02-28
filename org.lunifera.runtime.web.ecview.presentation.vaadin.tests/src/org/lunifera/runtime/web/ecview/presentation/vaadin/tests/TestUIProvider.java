package org.lunifera.runtime.web.ecview.presentation.vaadin.tests;

import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class TestUIProvider extends UIProvider {

	private UI ui;
	
	public TestUIProvider (UI ui){
		this.ui = ui;
	}
	
	@Override
	public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
		return ui.getClass();
	}
	
	@Override
    public UI createInstance(UICreateEvent event) {
    	return ui;
    }

}
