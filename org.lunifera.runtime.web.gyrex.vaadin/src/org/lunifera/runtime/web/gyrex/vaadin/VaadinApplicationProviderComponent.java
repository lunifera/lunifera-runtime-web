/**
 * Copyright (c) 2012 Gunnar Wagenknecht and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 */
package org.lunifera.runtime.web.gyrex.vaadin;

import org.eclipse.gyrex.context.IRuntimeContext;
import org.eclipse.gyrex.http.application.Application;
import org.eclipse.gyrex.http.application.provider.ApplicationProvider;
import org.eclipse.gyrex.http.jaxrs.internal.JaxRsDebug;
import org.eclipse.gyrex.http.jaxrs.internal.ScanningJaxRsApplication;

import org.eclipse.core.runtime.CoreException;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentConstants;
import org.osgi.service.component.ComponentContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OSGi Declarative Service component class for providing
 * {@link VaadinApplication JAX-RS applications} in Gyrex.
 * <p>
 * Essentially, this class is an {@link ApplicationProvider} which provides
 * {@link VaadinApplication JAX-RS Applications}. It provides convenience
 * features which ease the integration for JAX-RS into an OSGi environment. For
 * example, JAX-RS Resources and Providers will be automatically discovered in
 * the bundle which defines the actual component by scanning the bundle for
 * annotated class files.
 * </p>
 * <p>
 * Clients which want to provider a set of JAX-RS resources as an HTTP
 * {@link Application application} typically create an OSGi Declarative Service
 * component definition XML within the bundle containing the JAX-RS resources.
 * The should also provide the component property
 * {@link #APPLICATION_PROVIDER_ID} (value String) with a unique id identifying
 * the JAX-RS application provider.
 * </p>
 * <p>
 * The component definition XML can be as simple as the following XML. However,
 * the <code>activate</code> and <code>deactivate</code> attributes <strong>must
 * be set</strong> as specified below as well as the <code>provide</code>
 * element.
 * 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;
 * &lt;scr:component xmlns:scr="http://www.osgi.org/xmlns/scr/v1.1.0" activate="activate" deactivate="deactivate" name="hello.jaxrs"&gt;
 *    &lt;implementation class="org.eclipse.gyrex.http.jaxrs.JaxRsApplicationProviderComponent"/&gt;
 *    &lt;service&gt;
 *       &lt;provide interface="org.eclipse.gyrex.http.application.provider.ApplicationProvider"/&gt;
 *    &lt;/service&gt;
 * &lt;/scr:component&gt;
 * </pre>
 * 
 * </p>
 */
public class VaadinApplicationProviderComponent extends ApplicationProvider {

	private static final Logger LOG = LoggerFactory.getLogger(VaadinApplicationProviderComponent.class);

	/**
	 * A component property for a component configuration that contains the
	 * {@link ApplicationProvider#getId() application provider id} which shall
	 * be used for registering the application provider. The value of this
	 * property must be of type {@code String}. If no property is provided, a
	 * fallback will be attempted to the
	 * {@link ComponentConstants#COMPONENT_NAME} property.
	 */
	public static final String APPLICATION_PROVIDER_ID = "applicationProviderId";

	private BundleContext bundleContext;

	public void activate(final ComponentContext context) {
		if (JaxRsDebug.debug) {
			LOG.debug("JaxRsApplicationProviderComponent activation triggered for component '{}' (bundle {})", context.getProperties().get(ComponentConstants.COMPONENT_NAME), context.getBundleContext().getBundle());
		}

		// initialize application id
		final String applicationProviderId = getApplicationProviderId(context);
		try {
			if (JaxRsDebug.debug) {
				LOG.debug("Using application provider id '{}' for component '{}' (bundle {})", new Object[] { applicationProviderId, context.getProperties().get(ComponentConstants.COMPONENT_NAME), context.getBundleContext().getBundle() });
			}
			setId(applicationProviderId);
		} catch (final IllegalStateException e) {
			// compare and only continue if match
			if (!applicationProviderId.equals(getId())) {
				throw new IllegalStateException(String.format("The JaxRsApplicationProviderComponent has already been initialized with an application provider id (%s) and cannot be initialized again with a different id (%s). Please check your component configuration!", getId(), applicationProviderId), e);
			}
		}

		// remember bundle for later use
		bundleContext = context.getBundleContext();
	}

	@Override
	public Application createApplication(final String applicationId, final IRuntimeContext context) throws CoreException {
		// return an application that scan the bundle
		return new ScanningJaxRsApplication(applicationId, context, bundleContext.getBundle());
	}

	public void deactivate(final ComponentContext context) {
		if (JaxRsDebug.debug) {
			LOG.debug("JaxRsApplicationProviderComponent de-activation triggered for component '{}' (bundle {})", context.getProperties().get(ComponentConstants.COMPONENT_NAME), context.getBundleContext().getBundle());
		}
		bundleContext = null;
	}

	private String getApplicationProviderId(final ComponentContext context) {
		final Object applicationProviderIdValue = context.getProperties().get(APPLICATION_PROVIDER_ID);

		if (null == applicationProviderIdValue) {
			return (String) context.getProperties().get(ComponentConstants.COMPONENT_NAME);
		}

		if (!(applicationProviderIdValue instanceof String)) {
			throw new IllegalStateException("The JaxRsApplicationProviderComponent property 'applicationProviderId' must be of type String!");
		}
		return (String) applicationProviderIdValue;
	}

}
