package org.lunifera.runtime.web.vaadin.components.fields.shared;

import com.vaadin.shared.AbstractFieldState;
import com.vaadin.shared.annotations.DelegateToWidget;

@SuppressWarnings("serial")
public class EntityFieldState extends AbstractFieldState {
	{
		primaryStyleName = "l-entityfield";
	}

	/**
	 * The object representation of the entity id.
	 */
	@DelegateToWidget
	public Object entityId;

	/**
	 * True if the search icon should be visible.
	 */
	@DelegateToWidget
	public boolean searchEnabled;

}