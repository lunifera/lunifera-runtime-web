package org.lunifera.runtime.web.vaadin.common.data;

/**
 * This factory is exposed as an OSGi service and will return new bean search
 * service instances for the given bean type.
 */
public interface IBeanSearchServiceFactory {

	/**
	 * Returns the {@link IBeanSearchService} for the given type. If no proper
	 * implementation could be found, an in memory service will be returned.
	 * 
	 * @param type
	 * @return
	 */
	<BEAN> IBeanSearchService<BEAN> createService(Class<BEAN> type);

}
