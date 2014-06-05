package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.ui.samples;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;

public class CheckBoxSample extends CustomComponent {

	private CssLayout layout;

	public CheckBoxSample() {
		layout = new CssLayout();
		setCompositionRoot(layout);

		init();
	}

	protected void init() {
		CheckBox checkbox = new CheckBox();
		layout.addComponent(checkbox);
	}
}
