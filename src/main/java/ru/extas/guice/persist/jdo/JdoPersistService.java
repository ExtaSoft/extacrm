package ru.extas.guice.persist.jdo;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Valery Orlov
 */
@Singleton
class JdoPersistService implements Provider<PersistenceManager>, UnitOfWork, PersistService {
    private final ThreadLocal<PersistenceManager> persistenceManager = new ThreadLocal<>();
    private final ThreadLocal<AtomicInteger> uowCount = new InheritableThreadLocal<>(); // Track UoW enclosing

    private volatile Provider<PersistenceManagerFactory> pmFactory;

    @Inject
    JdoPersistService(final Provider<PersistenceManagerFactory> pmFactory) {
        this.pmFactory = pmFactory;
    }

    public PersistenceManager get() {
        if (!isWorking()) {
            begin();
        }

        PersistenceManager pm = persistenceManager.get();
        Preconditions.checkState(null != pm, "Requested PersistenceManager outside work unit. "
                + "Try calling UnitOfWork.begin() first, or use a PersistFilter if you "
                + "are inside a servlet environment.");

        return pm;
    }

    public boolean isWorking() {
        return persistenceManager.get() != null;
    }

    public void begin() {
//        Preconditions.checkState(null == persistenceManager.get(),
//                "Work already begun on this thread. Looks like you have called UnitOfWork.begin() twice"
//                        + " without a balancing call to end() in between.");

        if (persistenceManager.get() == null) {
            persistenceManager.set(pmFactory.get().getPersistenceManager());
            uowCount.set(new AtomicInteger(1));
        } else
            uowCount.get().incrementAndGet();
    }

    public void end() {
        PersistenceManager pm = persistenceManager.get();

        // Let's not penalize users for calling end() multiple times.
        if (null == pm && uowCount.get() == null) {
            return;
        }

        if (uowCount.get().decrementAndGet() == 0) {
            pm.close();
            persistenceManager.remove();
            uowCount.remove();
        }
    }

    public synchronized void start() {
        this.pmFactory.get();
    }

    public synchronized void stop() {
        Preconditions.checkState(!pmFactory.get().isClosed(), "Persistence service was already shut down.");
        pmFactory.get().close();
    }

    @Singleton
    public static class PersistenceManagerFactoryProvider implements Provider<PersistenceManagerFactory> {
        private final String persistenceUnitName;

        @Inject
        public PersistenceManagerFactoryProvider(@Jdo final String persistenceUnitName) {
            this.persistenceUnitName = persistenceUnitName;
        }

        public PersistenceManagerFactory get() {
            return JDOHelper.getPersistenceManagerFactory(persistenceUnitName);
        }
    }

}
