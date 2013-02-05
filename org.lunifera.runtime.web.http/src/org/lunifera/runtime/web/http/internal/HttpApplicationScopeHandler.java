package org.lunifera.runtime.web.http.internal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ScopedHandler;
import org.lunifera.runtime.web.http.IHttpApplication;

public class HttpApplicationScopeHandler extends ScopedHandler {
	
	private final IHttpApplication httpApplication;

	public HttpApplicationScopeHandler(IHttpApplication httpApplication) {
		super();
		this.httpApplication = httpApplication;
	}

	@Override
	public void doScope(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

	}
	
	@Override
	public void doHandle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

	}

}
