package org.lunifera.runtime.web.ecview.presentation.vaadin.services.internal;

import java.util.Map;

import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.common.editpart.emf.ElementEditpart;
import org.lunifera.ecview.core.common.model.core.YElement;
import org.lunifera.ecview.core.common.services.AbstractWidgetAssocationsService;

import com.vaadin.ui.Component;

@SuppressWarnings("restriction")
public class WidgetAssocationsService extends
		AbstractWidgetAssocationsService<Component, YElement> {

	@Override
	public YElement getModelElement(String id) {
		synchronized (associations) {
			for (Map.Entry<Component, YElement> entry : associations.entrySet()) {
				YElement yElement = entry.getValue();
				if (yElement.getId() != null && yElement.getId().equals(id)) {
					return yElement;
				}
			}
		}
		return null;
	}

	@Override
	public IElementEditpart getEditpart(String id) {
		YElement yElement = getModelElement(id);
		return yElement != null ? ElementEditpart.getEditpart(yElement) : null;
	}

}
