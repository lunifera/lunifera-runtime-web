package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.model;

import java.util.Date;

public class ValueBean extends AbstractBean {

	private String value;
	private boolean boolValue;
	private Date dateValue;
	private int intValue;
	private long longValue;
	private double doubleValue;

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

	public ValueBean(int value) {
		super();
		this.intValue = value;
	}

	public ValueBean(long value) {
		super();
		this.longValue = value;
	}

	public ValueBean(double value) {
		super();
		this.doubleValue = value;
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
		firePropertyChanged("dateValue", this.dateValue,
				this.dateValue = dateValue);
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int intValue) {
		firePropertyChanged("intValue", this.intValue, this.intValue = intValue);
	}

	public long getLongValue() {
		return longValue;
	}

	public void setLongValue(long longValue) {
		firePropertyChanged("longValue", this.longValue,
				this.longValue = longValue);
	}

	public double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(double doubleValue) {
		firePropertyChanged("doubleValue", this.doubleValue,
				this.doubleValue = doubleValue);
	}

}