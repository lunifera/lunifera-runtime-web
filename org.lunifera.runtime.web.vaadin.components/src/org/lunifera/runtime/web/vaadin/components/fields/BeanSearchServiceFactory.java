package org.lunifera.runtime.web.vaadin.components.fields;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lunifera.runtime.web.vaadin.common.data.IBeanSearchService;
import org.lunifera.runtime.web.vaadin.common.data.IBeanSearchServiceFactory;
import org.lunifera.runtime.web.vaadin.common.data.IBeanSearchServiceFactoryDelegate;
import org.lunifera.runtime.web.vaadin.common.data.StatefulInMemoryBeanSearchService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(immediate = true, enabled = true)
public class BeanSearchServiceFactory implements IBeanSearchServiceFactory {

	private List<IBeanSearchServiceFactoryDelegate> delegates = Collections
			.synchronizedList(new ArrayList<IBeanSearchServiceFactoryDelegate>());

	@Override
	public <BEAN> IBeanSearchService<BEAN> createService(Class<BEAN> bean) {

		synchronized (delegates) {
			for (IBeanSearchServiceFactoryDelegate delegate : delegates) {
				IBeanSearchService<BEAN> service = delegate.createService(bean);
				if (service != null) {
					return service;
				}
			}
		}

		return new StatefulInMemoryBeanSearchService<BEAN>(bean);
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC, unbind = "removeDelegate")
	protected void addDelegate(IBeanSearchServiceFactoryDelegate delegate) {
		if (!delegates.contains(delegate)) {
			delegates.add(delegate);
		}
	}

	protected void removeDelegate(IBeanSearchServiceFactoryDelegate delegate) {
		delegates.remove(delegate);
	}

}
