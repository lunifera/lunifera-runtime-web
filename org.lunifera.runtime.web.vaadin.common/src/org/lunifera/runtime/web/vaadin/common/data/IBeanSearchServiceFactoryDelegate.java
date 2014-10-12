package org.lunifera.runtime.web.vaadin.common.data;

/**
 * Implementations are factories used by the {@link IBeanSearchServiceFactory}.
 * Delegates are hooked up by {@link IBeanSearchServiceFactory} and requests for
 * proper services are delegated to the delegates.
 */
public interface IBeanSearchServiceFactoryDelegate {

	/**
	 * Returns the {@link IBeanSearchService} for the given type. If no proper
	 * implementation could be found, an in memory service will be returned.
	 * 
	 * @param bean
	 * @return
	 */
	<BEAN> IBeanSearchService<BEAN> createService(Class<BEAN> bean);

}
