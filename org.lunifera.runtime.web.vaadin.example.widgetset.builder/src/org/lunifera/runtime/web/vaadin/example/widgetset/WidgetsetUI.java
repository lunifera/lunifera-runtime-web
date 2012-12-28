package org.lunifera.runtime.web.vaadin.example.widgetset;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;

public class WidgetsetUI extends UI {
	@Override
	public void init(VaadinRequest request) {
		Label label = new Label("Hello Vaadin user");
		setContent(label);
	}

}
