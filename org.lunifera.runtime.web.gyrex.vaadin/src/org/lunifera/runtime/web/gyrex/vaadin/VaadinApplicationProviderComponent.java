/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Florian Pirchner - initial API and implementation
 *    
 *******************************************************************************/
package org.lunifera.runtime.web.gyrex.vaadin;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.gyrex.context.IRuntimeContext;
import org.eclipse.gyrex.http.application.Application;
import org.eclipse.gyrex.http.application.provider.ApplicationProvider;
import org.lunifera.runtime.web.gyrex.vaadin.internal.VaadinDebug;
import org.osgi.service.component.ComponentConstants;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OSGi Declarative Service component class for providing
 * {@link VaadinApplication Vaadin applications} in Gyrex.
 * <p>
 * Essentially, this class is an {@link ApplicationProvider} which provides
 * {@link VaadinApplication Vaadin Applications}. It provides convenience
 * features which ease the integration for Vaadin into an OSGi environment.
 * </p>
 */
public class VaadinApplicationProviderComponent extends ApplicationProvider {

	private static final Logger LOG = LoggerFactory
			.getLogger(VaadinApplicationProviderComponent.class);

	/**
	 * A component property for a component configuration that contains the
	 * {@link ApplicationProvider#getId() application provider id} which shall
	 * be used for registering the application provider. The value of this
	 * property must be of type {@code String}. If no property is provided, a
	 * fallback will be attempted to the
	 * {@link ComponentConstants#COMPONENT_NAME} property.
	 */
	public static final String APPLICATION_PROVIDER_ID = "applicationProviderId";

	public void activate(final ComponentContext context) {
		if (VaadinDebug.debug) {
			LOG.debug(
					"VaadinApplicationProviderComponent activation triggered for component '{}' (bundle {})",
					context.getProperties().get(
							ComponentConstants.COMPONENT_NAME), context
							.getBundleContext().getBundle());
		}

		// initialize application id
		final String applicationProviderId = getApplicationProviderId(context);
		try {
			if (VaadinDebug.debug) {
				LOG.debug(
						"Using application provider id '{}' for component '{}' (bundle {})",
						new Object[] {
								applicationProviderId,
								context.getProperties().get(
										ComponentConstants.COMPONENT_NAME),
								context.getBundleContext().getBundle() });
			}
			setId(applicationProviderId);
		} catch (final IllegalStateException e) {
			// compare and only continue if match
			if (!applicationProviderId.equals(getId())) {
				throw new IllegalStateException(
						String.format(
								"The VaadinApplicationProviderComponent has already been initialized with an application provider id (%s) and cannot be initialized again with a different id (%s). Please check your component configuration!",
								getId(), applicationProviderId), e);
			}
		}
	}

	@Override
	public Application createApplication(final String applicationId,
			final IRuntimeContext context) throws CoreException {
		// return an application that scan the bundle
		return new VaadinApplication(applicationId, context);
	}

	public void deactivate(final ComponentContext context) {
		if (VaadinDebug.debug) {
			LOG.debug(
					"VaadinApplicationProviderComponent de-activation triggered for component '{}' (bundle {})",
					context.getProperties().get(
							ComponentConstants.COMPONENT_NAME), context
							.getBundleContext().getBundle());
		}
	}

	private String getApplicationProviderId(final ComponentContext context) {
		final Object applicationProviderIdValue = context.getProperties().get(
				APPLICATION_PROVIDER_ID);

		if (null == applicationProviderIdValue) {
			return (String) context.getProperties().get(
					ComponentConstants.COMPONENT_NAME);
		}

		if (!(applicationProviderIdValue instanceof String)) {
			throw new IllegalStateException(
					"The VaadinApplicationProviderComponent property 'applicationProviderId' must be of type String!");
		}
		return (String) applicationProviderIdValue;
	}

}
