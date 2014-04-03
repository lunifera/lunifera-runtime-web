package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.model;

import java.util.Date;

public class ValueBean extends AbstractBean {

	private String value;
	private boolean boolValue;
	private Date dateValue;

	public ValueBean(String value) {
		super();
		this.value = value;
	}

	public ValueBean(boolean value) {
		super();
		this.boolValue = value;
	}
	
	public ValueBean(Date value) {
		super();
		this.dateValue = value;
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

	public boolean isBoolValue() {
		return boolValue;
	}

	public void setBoolValue(boolean boolValue) {
		firePropertyChanged("boolValue", this.boolValue,
				this.boolValue = boolValue);
	}

	public Date getDateValue() {
		return dateValue;
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

}