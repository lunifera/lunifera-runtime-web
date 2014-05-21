package org.lunifera.runtime.web.vaadin.components.fields.client;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.StyleConstants;
import com.vaadin.client.ui.Field;
import com.vaadin.client.ui.VCustomComponent;
import com.vaadin.client.ui.VLink;
import com.vaadin.client.ui.VTextField;

public class VEntityField extends VCustomComponent implements Field,
		ChangeHandler, FocusHandler, BlurHandler, KeyDownHandler,
		MouseUpHandler {

	public static final String CLASSNAME = "l-entityfield";

	private VTextField textField;
	private VLink link;
	private Widget activeField;

	private HandlerRegistration mouseUpHandler;

	public VEntityField() {
		super();
		setStyleName(CLASSNAME);
		addStyleName(StyleConstants.UI_LAYOUT);

		setEditable(true);
	}

	private void setEditable(boolean editable) {
		if (editable) {

			link.removeFromParent();
			textField.addChangeHandler(this);
			textField.addFocusHandler(this);
			textField.addBlurHandler(this);
			textField.addKeyDownHandler(this);
			add(textField);
		} else {
			mouseUpHandler = link.addMouseUpHandler(this);
			textField.removeFromParent();
			add(link);
		}
	}

	@Override
	public void onKeyDown(KeyDownEvent event) {

	}

	@Override
	public void onBlur(BlurEvent event) {

	}

	@Override
	public void onFocus(FocusEvent event) {

	}

	@Override
	public void onChange(ChangeEvent event) {

	}

}
