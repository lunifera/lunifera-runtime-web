package org.lunifera.runtime.web.ecview.services.vaadin.impl;

import java.util.Map;

import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YGridLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.lunifera.dsl.semantic.common.types.LScalarType;
import org.lunifera.dsl.semantic.entity.LEntity;
import org.lunifera.dsl.semantic.entity.LEntityAttribute;
import org.lunifera.dsl.semantic.entity.LEntityFeature;
import org.lunifera.dsl.semantic.entity.LEntityReference;
import org.lunifera.runtime.web.ecview.services.vaadin.ILEntityRenderStrategy;
import org.osgi.service.component.annotations.Component;

@Component(properties = { "service.ranking=100" })
public class LEntityRenderStrategy implements ILEntityRenderStrategy {

	private SimpleExtensionModelFactory factory = new SimpleExtensionModelFactory();

	@Override
	public YView render(LEntity lEntity, Class<?> entityClass,
			Object entityInstance, Map<String, Object> beanSlots) {

		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yLayout.setColumns(2);
		yView.setContent(yLayout);

		for (LEntityFeature lFeature : lEntity.getAllFeatures()) {
			if (lFeature instanceof LEntityAttribute) {
				LScalarType lDatatype = ((LEntityAttribute) lFeature).getType();
				
			} else if (lFeature instanceof LEntityReference) {

			}
		}
		return null;
	}

}
