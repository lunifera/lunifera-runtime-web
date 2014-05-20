package org.lunifera.runtime.web.vaadin.components.fields.client;

import com.vaadin.client.StyleConstants;
import com.vaadin.client.ui.VCustomComponent;

public class VEntityField extends VCustomComponent {

	public static final String CLASSNAME = "l-entityfield";

	public VEntityField() {
		super();
		setStyleName(CLASSNAME);
		addStyleName(StyleConstants.UI_LAYOUT);
	}

}
