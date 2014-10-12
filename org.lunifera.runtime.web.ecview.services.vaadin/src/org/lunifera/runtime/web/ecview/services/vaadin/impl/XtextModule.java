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
package org.lunifera.runtime.web.ecview.services.vaadin.impl;

import org.eclipse.xtext.common.types.TypesFactory;
import org.eclipse.xtext.common.types.access.IJvmTypeProvider;
import org.eclipse.xtext.common.types.impl.TypesFactoryImpl;
import org.eclipse.xtext.common.types.util.TypeReferences;
import org.eclipse.xtext.common.types.xtext.AbstractTypeScopeProvider;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider;
import org.eclipse.xtext.resource.impl.ResourceSetBasedResourceDescriptions;
import org.eclipse.xtext.service.AbstractGenericModule;
import org.eclipse.xtext.service.SingletonBinding;
import org.lunifera.dsl.xtext.types.bundles.BundleSpaceTypeProviderFactory;
import org.lunifera.dsl.xtext.types.bundles.BundleSpaceTypeScopeProvider;

import com.google.inject.name.Names;

public class XtextModule extends AbstractGenericModule {

	public XtextModule() {
	}

	public void configureNamedProviderScope(com.google.inject.Binder binder) {
		binder.bind(IResourceDescriptions.class)
				.annotatedWith(
						Names.named(ResourceDescriptionsProvider.NAMED_BUILDER_SCOPE))
				.to(ResourceSetBasedResourceDescriptions.class);
	}

	public void configureLiveProviderScope(com.google.inject.Binder binder) {
		binder.bind(IResourceDescriptions.class)
				.annotatedWith(
						Names.named(ResourceDescriptionsProvider.LIVE_SCOPE))
				.to(ResourceSetBasedResourceDescriptions.class);
	}

	public void configurePeristedProviderScope(com.google.inject.Binder binder) {
		binder.bind(IResourceDescriptions.class)
				.annotatedWith(
						Names.named(ResourceDescriptionsProvider.PERSISTED_DESCRIPTIONS))
				.to(ResourceSetBasedResourceDescriptions.class);
	}

	@SingletonBinding
	public Class<? extends IResourceDescriptions> bindIResourceDescriptions() {
		return ResourceSetBasedResourceDescriptions.class;
	}

	@SingletonBinding
	public Class<? extends XtextResourceSet> bindXtextResourceSet() {
		return XtextResourceSet.class;
	}

	public Class<? extends TypeReferences> bindTypeReferences() {
		return TypeReferences.class;
	}
	
	public Class<? extends TypesFactory> bindTypesFactory() {
		return TypesFactoryImpl.class;
	}
	
	public Class<? extends AbstractTypeScopeProvider> bindAbstractTypeScopeProvider() {
		return BundleSpaceTypeScopeProvider.class;
	}

	public Class<? extends IJvmTypeProvider.Factory> bindbindIJvmTypeProvider$Factory() {
		return BundleSpaceTypeProviderFactory.class;
	}
	

}
