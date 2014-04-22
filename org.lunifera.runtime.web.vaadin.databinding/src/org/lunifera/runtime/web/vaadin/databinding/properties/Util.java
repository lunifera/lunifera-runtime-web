package org.lunifera.runtime.web.vaadin.databinding.properties;

import com.vaadin.data.Property;

public class Util {

	/**
	 * Returns the property datasource if available, the source as a property or
	 * null.
	 * 
	 * @param source
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Property<Object> getProperty(Object source) {
		Property<Object> result = null;
		if (source instanceof Property.Viewer) {
			result = ((Property.Viewer) source).getPropertyDataSource();
		}

		if (result == null && source instanceof Property<?>) {
			result = (Property<Object>) source;
		}
		return result;
	}

}
