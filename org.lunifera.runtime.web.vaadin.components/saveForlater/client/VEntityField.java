package org.lunifera.runtime.web.vaadin.components.fields.client;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.StyleConstants;
import com.vaadin.client.ui.Field;
import com.vaadin.client.ui.Icon;

public class VEntityField extends Composite implements Field, KeyPressHandler,
		KeyDownHandler, Focusable {

	public static final String CLASSNAME = "l-entityfield";

	private final FlowPanel fp = new FlowPanel();

	private EntityFieldConnector connector;

	private VEntityTextField textField;
	private VEntityLink link;
	private Widget activeField;

	/** For internal use only. May be removed or replaced in the future. */
	public String id;

	/** For internal use only. May be removed or replaced in the future. */
	public boolean immediate;

	/** For internal use only. May be removed or replaced in the future. */
	public ApplicationConnection client;

	/** For internal use only. May be removed or replaced in the future. */
	public Element errorIndicatorElement;

	/** For internal use only. May be removed or replaced in the future. */
	public Icon icon;

	public String entityNumber;
	public String entityDescription;
	public Object entityId;

	public VEntityField() {
		super();
		setStyleName(CLASSNAME);
		addStyleName(StyleConstants.UI_LAYOUT);
		initWidget(fp);
	}

	public void init(EntityFieldConnector connector) {
		this.connector = connector;

		swapEditable(true);
	}

	// public HandlerRegistration addChangeHandler(ChangeHandler handler) {
	// return addHandler(handler, ChangeEvent.getType());
	// }
	//
	// public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
	// return addHandler(handler, KeyDownEvent.getType());
	// }
	//
	// public HandlerRegistration addFocusHandler(FocusHandler handler) {
	// return addHandler(handler, FocusEvent.getType());
	// }
	//
	// public HandlerRegistration addBlurHandler(BlurHandler handler) {
	// return addHandler(handler, BlurEvent.getType());
	// }
	//
	// public HandlerRegistration addClickHandler(ClickHandler handler) {
	// return addHandler(handler, ClickEvent.getType());
	// }

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
				textField.addChangeHandler(connector);
				textField.addFocusHandler(connector);
				textField.addBlurHandler(connector);
				textField.addKeyDownHandler(connector);
			}

			if (link != null) {
				fp.remove(link);
			}
			fp.add(textField);
		} else {
			if (link == null) {
				link = new VEntityLink();
				link.addMouseUpHandler(connector);
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
	public int getTabIndex() {
		return textField.getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
		textField.setAccessKey(key);
	}

	@Override
	public void setFocus(boolean focused) {
		/*
		 * Similar issue as with selectAll. Focusing must happen before possible
		 * selectall, so keep the timeout here lower.
		 */
		new Timer() {
			@Override
			public void run() {
				textField.setFocus(true);
			}
		}.schedule(300);
	}

	@Override
	public void setTabIndex(int index) {
		textField.setTabIndex(index);
	}

	public void setEntityNumber(String value) {
		if (textField.isAttached()) {
			textField.setValue(value);
		}
	}

	/**
	 * Get the value the text area
	 */
	public String getEntityNumber() {
		if (textField.isAttached()) {
			return textField.getValue();
		}
		return "";
	}

	@Override
	public void onKeyDown(KeyDownEvent event) {

	}

	@Override
	public void onKeyPress(KeyPressEvent event) {

	}

	// @Override
	// public void onKeyDown(KeyDownEvent event) {
	// if (isEditable()) {
	//
	// }
	// }
	//
	// @Override
	// public void onBlur(BlurEvent event) {
	// if (isEditable()) {
	//
	// }
	// }
	//
	// @Override
	// public void onFocus(FocusEvent event) {
	// if (isEditable()) {
	//
	// }
	// }
	//
	// @Override
	// public void onChange(ChangeEvent event) {
	// if (isEditable()) {
	//
	// }
	// }
	//
	// @Override
	// public void onMouseUp(MouseUpEvent event) {
	// if (!isEditable()) {
	//
	// }
	// }

}
