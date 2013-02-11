package org.lunifera.runtime.web.http;

public interface Constants extends org.osgi.framework.Constants {

	public static final String OSGI__FACTORY_PID = "org.lunifera.runtime.web.http.application.factory";
	
	public static final String OSGI__APPLICATION_ID = "lunifera.http.id";
	public static final String OSGI__APPLICATION_NAME = "lunifera.http.name";
	public static final String OSGI__APPLICATION_CONTEXT_PATH = "lunifera.http.contextPath";

	public static final String DEFAULT_APPLICATION_NAME = "defaultapplication";
	public static final String DEFAULT_APPLICATION_CONTEXT_PATH = "/";
}
