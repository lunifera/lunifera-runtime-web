package org.lunifera.runtime.web.vaadin.common.resource.impl;

import org.lunifera.runtime.web.vaadin.common.resource.IResourceProvider;
import org.osgi.service.component.annotations.Component;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;

/**
 * Default implementation.
 */
@Component(service = IResourceProvider.class)
public class ThemeResourceProvider implements IResourceProvider {

	@Override
	public Resource getResource(String resourcePath) {
		return new ThemeResource(resourcePath);
	}

}
