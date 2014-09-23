/**
 * Copyright (c) 2011 - 2014, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 		Florian Pirchner - Initial implementation
 */
package org.lunifera.runtime.web.vaadin.osgi.webapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletException;

import org.apache.shiro.web.servlet.IniShiroFilter;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.http.servlet.ExtendedHttpService;
import org.lunifera.runtime.web.vaadin.osgi.Activator;
import org.lunifera.runtime.web.vaadin.osgi.common.IOSGiUiProviderFactory;
import org.lunifera.runtime.web.vaadin.osgi.common.IVaadinApplication;
import org.lunifera.runtime.web.vaadin.osgi.common.VaadinConstants;
import org.lunifera.runtime.web.vaadin.osgi.common.VaadinStatusCodes;
import org.lunifera.runtime.web.vaadin.osgi.servlet.VaadinOSGiServlet;
import org.lunifera.runtime.web.vaadin.osgi.servlet.WebResourcesHttpContext;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.http.NamespaceException;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;

/** Default implementation of {@link IVaadinApplication}. */
@SuppressWarnings("deprecation")
public class VaadinApplication implements IVaadinApplication {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(VaadinApplication.class);

    private final String RESOURCE_BASE = "/VAADIN";

    // OSGi
    private ComponentContext context;
    private HttpServiceTracker serviceTracker;

    // properties
    private String id;
    private IStatus status;
    private String name = VaadinConstants.DEFAULT_APPLICATION_NAME;
    private String uiAlias = VaadinConstants.DEFAULT_UI_ALIAS;
    private String widgetset = "";
    private boolean productionMode;
    private String httpApplication = "";

    private List<OSGiUIProvider> uiProviders = new ArrayList<OSGiUIProvider>(1);
    private ExtendedHttpService httpService;

    private final List<IOSGiUiProviderFactory> providerFactories = new CopyOnWriteArrayList<IOSGiUiProviderFactory>();
    private final List<UiFactoryWrapper> uiFactories = new CopyOnWriteArrayList<UiFactoryWrapper>();

    // lifecycle
    private boolean started;
    private final Lock accessLock = new ReentrantLock();

    // web
    private IniShiroFilter filter;
    private VaadinOSGiServlet servlet;
    private WebResourcesHttpContext defaultContext;
    private String servletAlias;

    /** Called by OSGi-DS.
     * 
     * @param context
     * @param properties */
    protected void activate(final ComponentContext context,
            final Map<String, Object> properties) {
        LOGGER.debug("{} started", getName());
        this.context = context;
        this.id = UUID.randomUUID().toString();

        modified(context, properties);
    }

    /** Called by OSGi-DS.
     * 
     * @param context
     * @param properties */
    protected void deactivate(final ComponentContext context,
            final Map<String, Object> properties) {

        destroy();

        this.context = null;

        LOGGER.debug("{} stopped", getName());
    }

    /** Called by OSGi-DS if service properties became changed.
     * 
     * @param context
     * @param properties */
    protected void modified(final ComponentContext context,
            final Map<String, Object> properties) {

        System.out.println("Update");
        LOGGER.debug("Calling update");

        try {
            accessLock.lock();

            stop();

            id = "NOT SPECIFIED";
            if (properties.get(VaadinConstants.EXTERNAL_PID) != null) {
                id = (String) properties.get(VaadinConstants.EXTERNAL_PID);
            }

            String name = VaadinConstants.DEFAULT_APPLICATION_NAME;
            if (properties.get(VaadinConstants.APPLICATION_NAME) != null) {
                name = (String) properties
                        .get(VaadinConstants.APPLICATION_NAME);
            }

            String httpApplicationName = null;
            if (properties.get(VaadinConstants.HTTP_APPLICATION_NAME) != null) {
                httpApplicationName = (String) properties
                        .get(VaadinConstants.HTTP_APPLICATION_NAME);
            }

            String widgetsetName = null;
            if (properties.get(VaadinConstants.WIDGETSET) != null) {
                widgetsetName = (String) properties
                        .get(VaadinConstants.WIDGETSET);
            }

            String uialias = null;
            if (properties.get(VaadinConstants.UI_ALIAS) != null) {
                uialias = (String) properties.get(VaadinConstants.UI_ALIAS);
            }

            boolean productionMode = false;
            if (properties.get(VaadinConstants.PRODUCTIONMODE) != null) {
                productionMode = Boolean.valueOf((String) properties
                        .get(VaadinConstants.PRODUCTIONMODE));
            }

            setName(name);
            setWidgetSetName(widgetsetName);
            setHttpApplication(httpApplicationName);
            setUIAlias(uialias);
            setProductionMode(productionMode);

            // create and set the UI provider to the application
            final OSGiUIProvider provider = createUIProvider();
            if (provider != null) {
                addUIProvider(provider);
            }

            // properties.put(VaadinConstants.APPLICATION_ID, getId());

            // start the application again
            //
            start();

            LOGGER.debug(
                    "New IVaadinApplication {} deployed on 'http application' {}",
                    getName(), getHttpApplication());

        } finally {
            accessLock.unlock();
        }
    }

    /** Creates the UI provider for the given application name.
     * 
     * @param applicationName
     * @return */
    protected OSGiUIProvider createUIProvider() {

        // iterate all the factories with match application name filter
        OSGiUIProvider provider = null;
        for (final UiFactoryWrapper factory : uiFactories) {
            if (name.equals(factory.vaadinApplicationFilter)) {
                provider = factory.createProvider(name);
                break;
            }
        }

        // if no UI provider was found, try to find a factory with empty
        // application name filter
        if (provider == null) {
            for (final UiFactoryWrapper factory : uiFactories) {
                if (factory.vaadinApplicationFilter == null) {
                    provider = factory.createProvider(name);
                    break;
                }
            }
        }
        return provider;
    }

    @Override
    public IStatus getStatus() {
        return status != null ? status : Status.OK_STATUS;
    }

    @Override
    public String getId() {
        return id;
    }

    /** Returns the name of that http application.
     * 
     * @return */
    @Override
    public String getName() {
        return name;
    }

    /** Sets the name of that application.
     * 
     * @param name */
    protected void setName(final String name) {
        this.name = isStringValid(name) ? name
                : VaadinConstants.DEFAULT_APPLICATION_NAME;
    }

    @Override
    public String getUIAlias() {
        return uiAlias;
    }

    /** Sets the alias that is used to access the vaadin UI.
     * 
     * @param uiAlias */
    protected void setUIAlias(final String uiAlias) {
        this.uiAlias = isStringValid(uiAlias) ? uiAlias
                : VaadinConstants.DEFAULT_UI_ALIAS;
    }

    @Override
    public String getHttpApplication() {
        return httpApplication;
    }

    /** Sets the name of the {@link IHttpApplication} that vaadin application should be deployed at.
     * 
     * @param httpApplication the httpApplication to set */
    protected void setHttpApplication(final String httpApplication) {
        this.httpApplication = isStringValid(httpApplication) ? httpApplication
                : "";
    }

    @Override
    public String getWidgetSetName() {
        return widgetset;
    }

    /** Sets the name of the widgetset
     * 
     * @param widgetset */
    protected void setWidgetSetName(final String widgetset) {
        this.widgetset = isStringValid(widgetset) ? widgetset : "";
    }

    @Override
    public boolean isProductionMode() {
        return productionMode;
    }

    /** @param productionMode the productionMode to set */
    protected void setProductionMode(final boolean productionMode) {
        this.productionMode = productionMode;
    }

    /** Returns true if the string is not <code>null</code> and not empty string.
     * 
     * @param value
     * @return */
    private boolean isStringValid(final String value) {
        return value != null && !value.trim().equals("");
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public boolean isDeployed() {
        return httpService != null;
    }

    /** Is called to start the application. All resources should be registered and the http service is registered. */
    @Override
    public void start() {
        if (started) {
            LOGGER.debug("HttpApplication {} is already started", getName());
            return;
        }

        // ensure that only one threas manipulates the contents
        accessLock.lock();
        try {
            // start the tracker to observe handler
            try {
                serviceTracker = new HttpServiceTracker(
                        context.getBundleContext(), this);
                serviceTracker.open();
            } catch (final InvalidSyntaxException e) {
                LOGGER.error("{}", e);
                setStatus(VaadinStatusCodes.createHttpServiceTracker(e));

                // stop the application
                stop(true);
                return;
            }

            // add all yet existing handler from the tracker
            final ExtendedHttpService httpService = serviceTracker.getService();
            if (httpService != null) {
                try {
                    setHttpService(httpService);
                } catch (final AppException e) {
                    LOGGER.error(String
                            .format("Stopping vaadin application %s since setting http service caused a problem.",
                                    getName()));
                    setStatus(VaadinStatusCodes.createSettingHttpService(e));

                    // stop the application
                    stop(true);
                    return;
                }
            }

            started = true;
        } finally {
            accessLock.unlock();
        }

        setStatus(VaadinStatusCodes.OK_STATUS);
    }

    /** Is called by the service tracker to add a new http service.
     * 
     * @param httpService */
    protected void httpServiceAdded(final ExtendedHttpService httpService) {
        if (!isStarted()) {
            return;
        }

        // ensure that only one threads manipulates the contents
        accessLock.lock();
        try {
            setHttpService(httpService);
        } catch (final AppException e) {
            LOGGER.error(String
                    .format("Stopping vaadin application %s since setting http service caused a problem.",
                            getName()));
            setStatus(VaadinStatusCodes.createSettingHttpService(e));

            // stop the application
            stop();
            return;

        } finally {
            accessLock.unlock();
        }

        setStatus(VaadinStatusCodes.OK_STATUS);
    }

    /** Is called by the service tracker to remove a registered http service.
     * 
     * @param httpService */
    protected void httpServiceRemoved(final ExtendedHttpService httpService) {
        if (!isStarted()) {
            return;
        }

        // ensure that only one threas manipulates the contents
        accessLock.lock();
        try {
            unsetHttpService(httpService);
        } finally {
            accessLock.unlock();
        }
    }

    /** Sets the given http service to the internal cache.
     * 
     * @param httpService
     * @throws AppException */
    protected void setHttpService(final ExtendedHttpService httpService)
            throws AppException {
        if (this.httpService != null) {
            LOGGER.error("HttpService already present. Abort operation!");
            return;
        }

        this.httpService = httpService;

        registerWebArtifacts();
    }

    /** Registers servlets, filters and resources.
     * 
     * @throws AppException */
    protected void registerWebArtifacts() throws AppException {
        final Hashtable<String, String> properties = new Hashtable<String, String>();
        properties.put(VaadinConstants.EXTERNAL_PID, getId());
        properties.put(VaadinConstants.APPLICATION_NAME, getName());
        properties.put(VaadinConstants.WIDGETSET, getWidgetSetName());
        properties.put("widgetset", getWidgetSetName());
        properties.put("productionMode", Boolean.toString(isProductionMode()));

        servlet = new VaadinOSGiServlet(this);
        servletAlias = String.format("/%s", getUIAlias());
        defaultContext = new WebResourcesHttpContext(Activator
                .getBundleContext().getBundle());
        filter = new IniShiroFilter();
        try {
            try {
                httpService.registerFilter("/", filter, properties,
                        defaultContext);
            } catch (final Exception e) {
                LOGGER.error("{}", e);
                throw new AppException(e);
            }
            if (!ResourceRegistrationCounter.INSTANCE.hasRegistrations()) {
                httpService.registerResources(RESOURCE_BASE, RESOURCE_BASE,
                        defaultContext);
            }
            ResourceRegistrationCounter.INSTANCE.increment();
            httpService.registerServlet(servletAlias, servlet, properties,
                    defaultContext);
        } catch (final ServletException e) {
            LOGGER.error("{}", e);
            throw new AppException(e);
        } catch (final NamespaceException e) {
            LOGGER.error("{}", e);
            throw new AppException(e);
        }
    }

    /** Sets the current status.
     * 
     * @param status */
    private void setStatus(final IStatus status) {
        this.status = status;

        if (status != null && status != VaadinStatusCodes.OK_STATUS) {
            LOGGER.warn("Status was set to vaadin application {}: {}",
                    getName(), status);
        }
    }

    /** Unregisters all registered servlets, filters and resources. */
    protected void unregisterWebArtifacts() {
        try {
            if (servletAlias != null) {
                httpService.unregister(servletAlias);
            }
        } catch (final Exception e) {
            // May throw exception if http service was stopped by
            // tracker.close()
            LOGGER.info("{}", e.getMessage());
        } finally {
            servletAlias = null;
            servlet = null;
        }

        try {
            if (filter != null) {
                httpService.unregisterFilter(filter);
            }
        } catch (final Exception e) {
            LOGGER.info("{}", e.getMessage());
        } finally {
            filter = null;
        }

        try {
            httpService.unregister(RESOURCE_BASE);
            ResourceRegistrationCounter.INSTANCE.decrement();
        } catch (final Exception e) {
            LOGGER.info("{}", e.getMessage());
        } finally {
        }
    }

    /** Removes the given http service from the internal cache.
     * 
     * @param httpService */
    protected void unsetHttpService(final ExtendedHttpService httpService) {
        if (this.httpService == null) {
            LOGGER.error("HttpService can not be unset! No instance set yet!");
            return;
        }

        if (this.httpService != httpService) {
            LOGGER.error("Tries to unset different http service. Operation aborted.");
            return;
        }

        // unregister all registered artifacts
        unregisterWebArtifacts();

        this.httpService = null;
    }

    /** Is called to stop the application. All resources should be unregistered and the http service will become
     * disposed. */
    @Override
    public void stop() {
        stop(false);
    }

    /** Stops the application. If true, then the started state will no be checked.
     * 
     * @param force */
    protected void stop(final boolean force) {
        if (!force && !started) {
            LOGGER.debug("HttpApplication {} not started", getName());
            return;
        }

        // ensure that only one threads manipulates the contents
        accessLock.lock();
        try {
            if (serviceTracker != null) {
                serviceTracker.close();
                serviceTracker = null;
            }

        } finally {
            accessLock.unlock();
            started = false;
        }
    }

    /** Destroys the http application. */
    public void destroy() {
        try {
            accessLock.lock();
            if (isStarted()) {
                stop();
            }

            uiProviders = null;

        } finally {
            accessLock.unlock();
        }
    }

    /** Adds a new {@link UIProvider} to the web application.
     * 
     * @param provider */
    protected void addUIProvider(final OSGiUIProvider provider) {
        if (!uiProviders.contains(provider)) {
            uiProviders.add(provider);
        }
    }

    /** Removes an {@link UIProvider} from the web application.
     * 
     * @param provider */
    protected void removeUIProvider(final OSGiUIProvider provider) {
        if (!isActive()) {
            // component deactivated
            return;
        }

        uiProviders.remove(provider);
    }

    /** Removes an {@link UIProvider} for the given component factory that is used created UI instances.
     * 
     * @param uiFactory */
    protected void removeUIProviderForFactory(final ComponentFactory uiFactory) {
        if (!isActive()) {
            return;
        }

        for (final Iterator<OSGiUIProvider> iterator = uiProviders.iterator(); iterator
                .hasNext();) {
            final OSGiUIProvider provider = iterator.next();
            if (provider.getUIFactory() == uiFactory) {
                iterator.remove();
            }
        }
    }

    /** Returns true if the component is active.
     * 
     * @return */
    private boolean isActive() {
        return context != null;
    }

    @Override
    public List<OSGiUIProvider> getUiProviders() {
        return Collections.unmodifiableList(uiProviders);
    }

    /** Creates a filter string.
     * 
     * @param httpApplication
     * @return */
    private String createHttpServiceFilterString(final String httpApplication) {
        String filterString = null;
        if (httpApplication != null && !httpApplication.equals("")) {
            filterString = String
                    .format("(&(objectClass=org.eclipse.equinox.http.servlet.ExtendedHttpService)(lunifera.http.name=%s))",
                            httpApplication);
        } else {
            filterString = String
                    .format("(objectClass=org.eclipse.equinox.http.servlet.ExtendedHttpService)",
                            httpApplication);
        }
        return filterString;
    }

    /** Adds the OSGi UI provider factory.
     * 
     * @param providerFactory */
    protected void addUIProviderFactory(final IOSGiUiProviderFactory providerFactory) {
        providerFactories.add(providerFactory);
    }

    /** Removes the OSGi UI provider factory.
     * 
     * @param providerFactory */
    protected void removeUIProviderFactory(
            final IOSGiUiProviderFactory providerFactory) {
        if (!isActive()) {
            // component deactivated
            return;
        }
        providerFactories.remove(providerFactory);
    }

    /** Called by OSGi-DS and adds a component factory that is used to create instances of UIs.
     * 
     * @param reference */
    protected void addUIFactory(final ServiceReference<ComponentFactory> reference) {
        try {
            accessLock.lock();

            LOGGER.debug("Adding ui factory");

            // parse the target of service
            //
            final String name = (String) reference.getProperty("component.factory");
            final String[] tokens = name.split("/");
            if (tokens.length != 2) {
                LOGGER.error(
                        "UiFactory {} does not meet syntax. (org.lunifera.web.vaadin.UI/[uiclass]@[vaadinApp]) Eg: org.lunifera.web.vaadin.UI/org.lunifera.examples.runtime.web.vaadin.standalone.Vaadin7StandaloneDemoUI@StandaloneDemo",
                        name);
                return;
            }

            String uiClassName;
            String vaadinApplication;
            final String[] innerTokens = tokens[1].split("@");
            if (innerTokens.length == 1) {
                uiClassName = innerTokens[0];
                vaadinApplication = null;
            } else {
                uiClassName = innerTokens[0];
                vaadinApplication = innerTokens[1];
            }

            // register factory internally
            //
            uiFactories.add(new UiFactoryWrapper(vaadinApplication,
                    uiClassName, reference));

        } finally {
            accessLock.unlock();
        }
    }

    /** Called by OSGi-DS and removes a component factory that is used to create instances of UIs.
     * 
     * @param factory */
    protected void removeUIFactory(final ComponentFactory factory,
            final Map<String, Object> properties) {
        try {
            accessLock.lock();
            removeUIProviderForFactory(factory);
        } finally {
            accessLock.unlock();
        }
    }

    /** Update the web application with the new UI provider.
     * 
     * @param uiProvider */
    private void uiProviderAdded(final OSGiUIProvider uiProvider) {
        try {
            accessLock.lock();
            if (uiProvider.getVaadinApplication() == null) {
                addUIProvider(uiProvider);
            } else {
                if (uiProvider.getVaadinApplication().equals(getName())) {
                    addUIProvider(uiProvider);
                }
            }
        } finally {
            accessLock.unlock();
        }
    }

    /** A wrapper class to keep information about pending factory. Pending means that the service was not activated yet. */
    private class UiFactoryWrapper {

        private final String vaadinApplicationFilter;
        private final ServiceReference<ComponentFactory> reference;
        private final String uiClassName;

        public UiFactoryWrapper(final String vaadinApplicationFilter,
                final String uiClassName, final ServiceReference<ComponentFactory> reference) {
            super();
            this.vaadinApplicationFilter = vaadinApplicationFilter;
            this.uiClassName = uiClassName;
            this.reference = reference;
        }

        @SuppressWarnings("unchecked")
        public OSGiUIProvider createProvider(final String vaadinApplication) {
            if (vaadinApplicationFilter != null
                    && !vaadinApplicationFilter.equals(vaadinApplication)) {
                return null;
            }

            final ComponentFactory uiFactory = reference.getBundle()
                    .getBundleContext().getService(reference);

            Class<? extends UI> uiClass = null;
            try {
                uiClass = (Class<? extends UI>) reference.getBundle()
                        .loadClass(uiClassName);
            } catch (final ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }

            // iterates all UI provider factories to find a proper UI provider
            OSGiUIProvider uiProvider = null;
            for (final IOSGiUiProviderFactory providerFactory : providerFactories) {
                uiProvider = providerFactory.createUiProvider(
                        vaadinApplication, uiClass);
                if (uiProvider == null) {
                    continue;
                }
                uiProvider.setUIFactory(uiFactory);
                break;
            }

            // If no provider was found for the given vaadin application, then
            // add a default one
            if (uiProvider == null) {
                uiProvider = new OSGiUIProvider(vaadinApplication, uiClass);
                uiProvider.setUIFactory(uiFactory);
            }

            // handle added UI provider
            uiProviderAdded(uiProvider);

            return uiProvider;
        }
    }

    public class HttpServiceTracker extends
            ServiceTracker<ExtendedHttpService, ExtendedHttpService> {

        @SuppressWarnings("unused")
        private final Logger logger = LoggerFactory
                .getLogger(HttpServiceTracker.class);

        public HttpServiceTracker(final BundleContext ctx,
                final IVaadinApplication webApplication)
                throws InvalidSyntaxException {
            super(ctx, ctx
                    .createFilter(createHttpServiceFilterString(webApplication
                            .getHttpApplication())), null);
        }

        @Override
        public ExtendedHttpService addingService(
                final ServiceReference<ExtendedHttpService> reference) {
            final ExtendedHttpService service = super.addingService(reference);

            VaadinApplication.this.httpServiceAdded(service);

            return service;
        }

        @Override
        public void removedService(
                final ServiceReference<ExtendedHttpService> reference,
                final ExtendedHttpService service) {
            super.removedService(reference, service);

            VaadinApplication.this.httpServiceRemoved(service);
        }
    }

}
