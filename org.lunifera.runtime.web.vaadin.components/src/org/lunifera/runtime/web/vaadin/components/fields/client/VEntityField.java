package org.lunifera.runtime.web.vaadin.components.fields.client;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.StyleConstants;
import com.vaadin.client.ui.Field;

public class VEntityField extends Composite implements Field, ChangeHandler,
		FocusHandler, BlurHandler, KeyDownHandler, MouseUpHandler {

	public static final String CLASSNAME = "l-entityfield";

	private final FlowPanel fp = new FlowPanel();

	private VEntityTextField textField;
	private VEntityLink link;
	private Widget activeField;

	private HandlerRegistration mouseUpHandler;

	public ApplicationConnection client;
	public String id;

	public VEntityField() {
		super();
		setStyleName(CLASSNAME);
		addStyleName(StyleConstants.UI_LAYOUT);

		swapEditable(true);

		initWidget(fp);
	}

	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return addHandler(handler, ChangeEvent.getType());
	}

	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return addHandler(handler, KeyDownEvent.getType());
	}

	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return addHandler(handler, FocusEvent.getType());
	}

	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return addHandler(handler, BlurEvent.getType());
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addHandler(handler, ClickEvent.getType());
	}

	/**
	 * If true, then the widget is editable. False otherwise.
	 * 
	 * @param editable
	 */
	public void setEditable(boolean editable) {
		swapEditable(editable);
	}

	private void swapEditable(boolean editable) {
		if (editable) {
			if (textField == null) {
				textField = new VEntityTextField();
				textField.addChangeHandler(this);
				textField.addFocusHandler(this);
				textField.addBlurHandler(this);
				textField.addKeyDownHandler(this);
			}

			if (link != null) {
				fp.remove(link);
			}
			fp.add(textField);
		} else {
			if (link == null) {
				link = new VEntityLink();
				link.addMouseUpHandler(this);
			}

			if (textField != null) {
				fp.remove(textField);
			}
			fp.add(link);
		}
	}

	/**
	 * Returns true, if the field is editable.
	 * 
	 * @return
	 */
	private boolean isEditable() {
		return textField != null && textField.isAttached();
	}

	@Override
	public void onKeyDown(KeyDownEvent event) {
		if (isEditable()) {

		}
	}

	@Override
	public void onBlur(BlurEvent event) {
		if (isEditable()) {

		}
	}

	@Override
	public void onFocus(FocusEvent event) {
		if (isEditable()) {

		}
	}

	@Override
	public void onChange(ChangeEvent event) {
		if (isEditable()) {

		}
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		if (!isEditable()) {

		}
	}

}
