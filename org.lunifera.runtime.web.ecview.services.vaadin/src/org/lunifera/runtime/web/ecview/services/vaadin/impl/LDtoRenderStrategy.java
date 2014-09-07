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

import org.lunifera.ecview.core.common.context.IViewContext;
import org.lunifera.ecview.core.common.model.core.YView;
import org.lunifera.ecview.core.extension.model.extension.YGridLayout;
import org.lunifera.ecview.core.extension.model.extension.util.SimpleExtensionModelFactory;
import org.lunifera.dsl.semantic.dto.LDto;
import org.lunifera.runtime.web.ecview.services.vaadin.ILDtoRenderStrategy;
import org.osgi.service.component.annotations.Component;

@Component(property = { "service.ranking=100" })
public class LDtoRenderStrategy implements ILDtoRenderStrategy {

	private SimpleExtensionModelFactory factory = new SimpleExtensionModelFactory();

	@Override
	public YView render(LDto lDto, Class<?> dtoClass, Object dtoInstance,
			Map<String, Object> beanSlots) {

		YView yView = factory.createView();
		// create a bean slot for the bindings
		yView.addBeanSlot(IViewContext.ROOTBEAN_SELECTOR, dtoClass);

		// create a main layout
		YGridLayout yLayout = factory.createGridLayout();
		yLayout.setColumns(4);
		yView.setContent(yLayout);

		// Use the dto visitor to build the UI
		DtoModelRenderer visitor = new DtoModelRenderer();
		visitor.render(lDto, yLayout, dtoClass);

		return yView;
	}
}
