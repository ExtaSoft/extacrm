/**
 * 
 */
package ru.extas.web.insurance;

import static ru.extas.ListUtil.emptyIfNull;
import static ru.extas.server.ServiceLocator.lookup;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.vaadin.addons.lazyquerycontainer.AbstractBeanQuery;
import org.vaadin.addons.lazyquerycontainer.BeanQueryFactory;
import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;

import ru.extas.model.Policy;
import ru.extas.server.PolicyRegistry;

/**
 * Контейнер с ленивой загрузкой для бланков полисов
 * 
 * @author Valery Orlov
 * 
 */
public class PolicyContainer extends LazyQueryContainer {

	private static final long serialVersionUID = -25234229510658767L;

	public static class PolicyBeanQuery extends AbstractBeanQuery<Policy> implements Serializable {

		private static final long serialVersionUID = -25234229510658767L;

		private transient PolicyRegistry policyRepository;

		/**
		 * @param queryDefinition
		 * @param queryConfiguration
		 * @param sortPropertyIds
		 * @param sortStates
		 */
		public PolicyBeanQuery(QueryDefinition queryDefinition, Map<String, Object> queryConfiguration, Object[] sortPropertyIds, boolean[] sortStates) {
			super(queryDefinition, queryConfiguration, sortPropertyIds, sortStates);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.vaadin.addons.lazyquerycontainer.AbstractBeanQuery#constructBean
		 * ()
		 */
		@Override
		protected Policy constructBean() {
			return new Policy();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.vaadin.addons.lazyquerycontainer.AbstractBeanQuery#loadBeans(int,
		 * int)
		 */
		@Override
		protected List<Policy> loadBeans(int startIndex, int count) {
			PolicyRegistry service = getPolicyRepository();
			return emptyIfNull(service.loadAll(startIndex, count, this.getSortPropertyIds(), this.getSortStates()));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.vaadin.addons.lazyquerycontainer.AbstractBeanQuery#saveBeans(java
		 * .util.List, java.util.List, java.util.List)
		 */
		@Override
		protected void saveBeans(List<Policy> addedBeans, List<Policy> modifiedBeans, List<Policy> removedBeans) {
			throw new UnsupportedOperationException();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.vaadin.addons.lazyquerycontainer.AbstractBeanQuery#size()
		 */
		@Override
		public int size() {
			PolicyRegistry service = getPolicyRepository();
			return service.queryPoliciesCount();
		}

		/**
		 * @return the policyRepository
		 */
		public PolicyRegistry getPolicyRepository() {
			if (policyRepository == null)
				policyRepository = lookup(PolicyRegistry.class);
			return policyRepository;
		}
	}

	/**
	 */
	public PolicyContainer() {
		super(new BeanQueryFactory<PolicyBeanQuery>(PolicyBeanQuery.class), null, 50, false);
	}

}
