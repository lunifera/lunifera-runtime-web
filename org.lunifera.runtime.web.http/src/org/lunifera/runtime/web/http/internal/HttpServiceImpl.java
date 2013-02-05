package org.lunifera.runtime.web.http.internal;

import java.util.Dictionary;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

public class HttpServiceImpl implements HttpService {

	@SuppressWarnings("rawtypes")
	@Override
	public void registerServlet(String alias, Servlet servlet,
			Dictionary initparams, HttpContext context)
			throws ServletException, NamespaceException {
		
	}

	@Override
	public void registerResources(String alias, String name, HttpContext context)
			throws NamespaceException {

	}

	@Override
	public void unregister(String alias) {

	}

	@Override
	public HttpContext createDefaultHttpContext() {
		return null;
	}

	public void destroy() {
		
	}

}
