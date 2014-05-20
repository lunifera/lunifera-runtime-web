package org.lunifera.runtime.web.vaadin.components.fields.shared;

import com.vaadin.shared.AbstractFieldState;
import com.vaadin.shared.annotations.DelegateToWidget;
import com.vaadin.shared.ui.csslayout.CssLayoutState;

@SuppressWarnings("serial")
public class EntityFieldState extends AbstractFieldState {
	{
		primaryStyleName = "l-entityfield";
	}

	/**
	 * True if the search icon should be visible.
	 */
	@DelegateToWidget
	public boolean searchEnabled;

	/**
	 * True, if the field should act as a link.
	 */
	@DelegateToWidget
	public boolean businesslinkEnabled = true;

}