/**
 * Copyright (c) 2011 - 2015, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *         Florian Pirchner - Initial implementation
 */
package org.lunifera.runtime.web.vaadin.components.container;

import org.lunifera.runtime.common.datasource.IDataSourceService;
import org.lunifera.runtime.web.vaadin.common.data.IBeanSearchService;
import org.lunifera.runtime.web.vaadin.common.data.IBeanSearchServiceFactoryDelegate;
import org.lunifera.runtime.web.vaadin.common.data.StatefulInMemoryBeanSearchService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Returns an inmemory datasource service. Contents are loaded eagerly.
 */
@Component(service = IBeanSearchServiceFactoryDelegate.class)
public class DatasourceServiceFactoryDelegate implements
		IBeanSearchServiceFactoryDelegate {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DatasourceServiceFactoryDelegate.class);

	@SuppressWarnings("unchecked")
	@Override
	public <DataSourceInfo> IBeanSearchService<DataSourceInfo> createService(
			Class<DataSourceInfo> type) {
		if (type != IDataSourceService.DataSourceInfo.class) {
			return null;
		}

		StatefulInMemoryBeanSearchService<DataSourceInfo> service = new StatefulInMemoryBeanSearchService<DataSourceInfo>(
				type);
		BundleContext context = FrameworkUtil.getBundle(
				DatasourceServiceFactoryDelegate.class).getBundleContext();

		// find all datasource infos
		ServiceReference<IDataSourceService> ref = context
				.getServiceReference(IDataSourceService.class);
		if (ref != null) {
			IDataSourceService dsService = context.getService(ref);
			dsService.getDataSourcInfos(null).stream().forEach(t -> {
				// add them to the service
					service.addBean((DataSourceInfo) t);
				});
		} else {
			LOGGER.error("No datasource service available!");
		}

		return service;
	}
}
