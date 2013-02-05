package org.lunifera.runtime.web.http.internal;

import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.lunifera.runtime.web.http.IHttpApplication;

public class ServletContextHandler extends
		org.eclipse.jetty.servlet.ServletContextHandler {

	private final IHttpApplication application;
	private HttpApplicationScopeHandler applicationScopeHandler;

	public ServletContextHandler(IHttpApplication application) {
		this.application = application;
		setContextPath(application.getContextPath());
		setDisplayName(application.getId() + " - " + application.getName()
				+ " - " + application.getContextPath());
	}
	

	/**
	 * @return the applicationScopeHandler
	 */
	public HttpApplicationScopeHandler getApplicationScopeHandler() {
		return applicationScopeHandler;
	}



	@Override
	protected void startContext() throws Exception {
		// create remaining the handlers
		applicationScopeHandler = new HttpApplicationScopeHandler(application);
		SessionHandler sessionHandler = getSessionHandler();
		// setup the handler chain before super initialization
		// session -> application -> registered servlets/resources
		sessionHandler.setHandler(applicationScopeHandler);
		applicationScopeHandler.setHandler(_servletHandler);

		super.startContext();
	}

	/**
	 * Creates a new session handler.
	 * 
	 * @return the new session handler instance
	 */
	protected SessionHandler newSessionHandler() {
		final HashSessionManager sessionManager = new HashSessionManager();
		sessionManager.setMaxInactiveInterval(NumberUtils.toInt(
				getInitParameter("session.maxInactiveInterval"), 1800));
		return new SessionHandler(sessionManager);
	}

}
