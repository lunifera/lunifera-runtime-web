package org.lunifera.runtime.web.http;

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;

/**
 * An application manager that manages all http allications.
 */
public interface IHttpApplicationManager {

	public static final String OSGI__PID = "org.lunifera.runtime.web.http.application.manager";
	

	/**
	 * Updates the http application with the given properties.
	 * 
	 * @param properties
	 */
	void updated(Dictionary<String, ?> properties)
			throws ConfigurationException;

	/**
	 * Returns the http application with the given id.
	 * 
	 * @param id
	 * @return
	 */
	IHttpApplication getApplication(String id);
}
