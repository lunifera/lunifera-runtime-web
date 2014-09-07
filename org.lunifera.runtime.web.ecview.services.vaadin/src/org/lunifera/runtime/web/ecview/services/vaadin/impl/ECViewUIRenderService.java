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

import java.util.Map;

import org.lunifera.ecview.core.common.context.ContextException;
import org.lunifera.ecview.core.common.context.IViewContext;
import org.lunifera.ecview.core.common.model.core.YView;
import org.lunifera.dsl.semantic.dto.LDto;
import org.lunifera.dsl.semantic.entity.LEntity;
import org.lunifera.dsl.xtext.builder.participant.IDtoMetadataService;
import org.lunifera.dsl.xtext.builder.participant.IEntityMetadataService;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.services.vaadin.IECViewUIRenderService;
import org.lunifera.runtime.web.ecview.services.vaadin.ILDtoRenderStrategy;
import org.lunifera.runtime.web.ecview.services.vaadin.ILEntityRenderStrategy;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.vaadin.ui.ComponentContainer;

@Component
public class ECViewUIRenderService implements IECViewUIRenderService {

	private ILDtoRenderStrategy dtoStrategy;
	private ILEntityRenderStrategy entityStrategy;
	private IEntityMetadataService entityMetadataService;
	private IDtoMetadataService dtoMetadataService;

	@Override
	public IViewContext renderUIForDTO(Class<?> clazz,
			ComponentContainer component, Map<String, Object> beanSlots,
			Map<String, Object> renderingOptions) {
		if (dtoStrategy == null) {
			return null;
		}

		YView yView = dtoStrategy.render(findLDTO(clazz), clazz, null,
				beanSlots);
		VaadinRenderer renderer = new VaadinRenderer();
		IViewContext context = null;
		try {
			context = renderer.render(component, yView, renderingOptions);
		} catch (ContextException e) {
			throw new RuntimeException(e);
		}

		return context;
	}

	@Override
	public IViewContext renderUIForDTO(Object dto,
			ComponentContainer component, Map<String, Object> beanSlots,
			Map<String, Object> renderingOptions) {
		if (dtoStrategy == null) {
			return null;
		}

		YView yView = dtoStrategy.render(findLDTO(dto.getClass()),
				dto.getClass(), dto, beanSlots);
		VaadinRenderer renderer = new VaadinRenderer();
		IViewContext context = null;
		try {
			context = renderer.render(component, yView, renderingOptions);
			context.setBean(IViewContext.ROOTBEAN_SELECTOR, dto);
		} catch (ContextException e) {
			throw new RuntimeException(e);
		}

		return context;
	}

	@Override
	public IViewContext renderUIForEntity(Class<?> clazz,
			ComponentContainer component, Map<String, Object> beanSlots,
			Map<String, Object> renderingOptions) {
		if (entityStrategy == null) {
			return null;
		}

		YView yView = entityStrategy.render(findLEntity(clazz), clazz, null,
				beanSlots);
		VaadinRenderer renderer = new VaadinRenderer();
		IViewContext context = null;
		try {
			context = renderer.render(component, yView, renderingOptions);
		} catch (ContextException e) {
			throw new RuntimeException(e);
		}

		return context;

	}

	@Override
	public IViewContext renderUIForEntity(Object entity,
			ComponentContainer component, Map<String, Object> beanSlots,
			Map<String, Object> renderingOptions) {
		if (entityStrategy == null) {
			return null;
		}

		YView yView = entityStrategy.render(findLEntity(entity.getClass()),
				entity.getClass(), entity, beanSlots);
		VaadinRenderer renderer = new VaadinRenderer();
		IViewContext context = null;
		try {
			context = renderer.render(component, yView, renderingOptions);
			context.setBean(IViewContext.ROOTBEAN_SELECTOR, entity);
		} catch (ContextException e) {
			throw new RuntimeException(e);
		}

		return context;
	}

	/**
	 * Returns the dto for the given class.
	 * 
	 * @param clazz
	 * @return
	 */
	private LDto findLDTO(Class<?> clazz) {
		return dtoMetadataService.getMetadata(clazz);
	}

	/**
	 * Returns the entity for the given class.
	 * 
	 * @param clazz
	 * @return
	 */
	private LEntity findLEntity(Class<?> clazz) {
		return entityMetadataService.getMetadata(clazz);
	}

	@Reference(unbind = "unbindDtoRenderingStrategy", policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
	protected void bindDtoRenderingStrategy(ILDtoRenderStrategy dtoStrategy) {
		this.dtoStrategy = dtoStrategy;
	}

	protected void unbindDtoRenderingStrategy(ILDtoRenderStrategy dtoStrategy) {
		this.dtoStrategy = null;
	}

	@Reference(unbind = "unbindEntityRenderingStrategy", policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
	protected void bindEntityRenderingStrategy(
			ILEntityRenderStrategy entityStrategy) {
		this.entityStrategy = entityStrategy;
	}

	protected void unbindEntityRenderingStrategy(
			ILEntityRenderStrategy entityStrategy) {
		this.entityStrategy = null;
	}

	@Reference(unbind = "unbindEntityMetadataService", policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
	protected void bindEntityMetadataService(
			IEntityMetadataService entityMetadataService) {
		this.entityMetadataService = entityMetadataService;
	}

	protected void unbindEntityMetadataService(
			IEntityMetadataService entityMetadataService) {
		this.entityMetadataService = null;
	}

	@Reference(unbind = "unbindDtoMetadataService", policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
	protected void bindDtoMetadataService(IDtoMetadataService dtoMetadataService) {
		this.dtoMetadataService = dtoMetadataService;
	}

	protected void unbindDtoMetadataService(
			IDtoMetadataService dtoMetadataService) {
		this.dtoMetadataService = null;
	}

}
