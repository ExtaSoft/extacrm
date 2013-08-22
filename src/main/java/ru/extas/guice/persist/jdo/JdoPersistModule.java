/**
 * Copyright (C) 2010 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.extas.guice.persist.jdo;

import com.google.common.base.Preconditions;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistModule;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;
import org.aopalliance.intercept.MethodInterceptor;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;


/**
 * JDO provider for guice persist.
 *
 * @author Valery Orlov
 */
@SuppressWarnings("UnusedDeclaration")
public final class JdoPersistModule extends PersistModule {
    private final String jdoUnit;
    private MethodInterceptor transactionInterceptor;

    public JdoPersistModule(String jdoUnit) {
        Preconditions.checkArgument(null != jdoUnit && jdoUnit.length() > 0,
                "JDO unit name must be a non-empty string.");
        this.jdoUnit = jdoUnit;
    }

    @Override
    protected void configurePersistence() {
        bindConstant().annotatedWith(Jdo.class).to(jdoUnit);

        bind(JdoPersistService.class).in(Singleton.class);

        bind(PersistService.class).to(JdoPersistService.class);
        bind(UnitOfWork.class).to(JdoPersistService.class);
        bind(PersistenceManager.class).toProvider(JdoPersistService.class);
        bind(PersistenceManagerFactory.class)
                .toProvider(JdoPersistService.PersistenceManagerFactoryProvider.class).in(Singleton.class);

        transactionInterceptor = new JdoLocalTxnInterceptor();
        requestInjection(transactionInterceptor);

    }

    @Override
    protected MethodInterceptor getTransactionInterceptor() {
        return transactionInterceptor;
    }

}
