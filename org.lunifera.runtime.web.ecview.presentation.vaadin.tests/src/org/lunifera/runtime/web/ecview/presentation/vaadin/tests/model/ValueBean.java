package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.model;

public class ValueBean extends AbstractBean {

	private String value;

	public ValueBean(String value) {
		super();
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		firePropertyChanged("value", this.value, this.value = value);
	}

}