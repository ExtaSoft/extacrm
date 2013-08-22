package ru.extas.guice.persist.jdo;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.UnitOfWork;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import java.lang.reflect.Method;

/**
 * @author Valery Orlov
 */
class JdoLocalTxnInterceptor implements MethodInterceptor {

    @Inject
    private final JdoPersistService pmProvider = null;
    @Inject
    private final UnitOfWork unitOfWork = null;
    // Tracks if the unit of work was begun implicitly by this transaction.
    private final ThreadLocal<Boolean> didWeStartWork = new ThreadLocal<>();

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        // Should we start a unit of work?
        if (!pmProvider.isWorking()) {
            pmProvider.begin();
            didWeStartWork.set(true);
        }

        Transactional transactional = readTransactionMetadata(methodInvocation);
        PersistenceManager pm = this.pmProvider.get();

        // Allow 'joining' of transactions if there is an enclosing @Transactional method.
        if (pm.currentTransaction().isActive()) {
            return methodInvocation.proceed();
        }

        Transaction txn = pm.currentTransaction();
        txn.begin();

        Object result;
        try {
            result = methodInvocation.proceed();

        } catch (Exception e) {
            //commit transaction only if rollback didnt occur
            if (rollbackIfNecessary(transactional, e, txn)) {
                txn.commit();
            }

            //propagate whatever exception is thrown anyway
            throw e;
        } finally {
            // Close the pm if necessary (guarded so this code doesn't run unless catch fired).
            if (null != didWeStartWork.get() && !txn.isActive()) {
                didWeStartWork.remove();
                unitOfWork.end();
            }
        }

        //everything was normal so commit the txn (do not move into try block above as it
        //  interferes with the advised method's throwing semantics)
        try {
            txn.commit();
        } finally {
            //close the pm if necessary
            if (null != didWeStartWork.get()) {
                didWeStartWork.remove();
                unitOfWork.end();
            }
        }

        //or return result
        return result;
    }

    // TODO(dhanji): Cache this method's results.
    private Transactional readTransactionMetadata(MethodInvocation methodInvocation) {
        Transactional transactional;
        Method method = methodInvocation.getMethod();
        Class<?> targetClass = methodInvocation.getThis().getClass();

        transactional = method.getAnnotation(Transactional.class);
        if (null == transactional) {
            // If none on method, try the class.
            transactional = targetClass.getAnnotation(Transactional.class);
        }
        if (null == transactional) {
            // If there is no transactional annotation present, use the default
            transactional = Internal.class.getAnnotation(Transactional.class);
        }

        return transactional;
    }

    /**
     * Returns True if rollback DID NOT HAPPEN (i.e. if commit should continue).
     *
     * @param transactional The metadata annotation of the method
     * @param e             The exception to test for rollback
     * @param txn           A JDO Transaction to issue rollbacks on
     */
    private boolean rollbackIfNecessary(Transactional transactional, Exception e,
                                        Transaction txn) {
        boolean commit = true;

        //check rollback clauses
        for (Class<? extends Exception> rollBackOn : transactional.rollbackOn()) {

            //if one matched, try to perform a rollback
            if (rollBackOn.isInstance(e)) {
                commit = false;

                //check ignore clauses (supercedes rollback clause)
                for (Class<? extends Exception> exceptOn : transactional.ignore()) {
                    //An exception to the rollback clause was found, DON'T rollback
                    // (i.e. commit and throw anyway)
                    if (exceptOn.isInstance(e)) {
                        commit = true;
                        break;
                    }
                }

                //rollback only if nothing matched the ignore check
                if (!commit) {
                    txn.rollback();
                }
                //otherwise continue to commit

                break;
            }
        }

        return commit;
    }

    @Transactional
    private static class Internal {
    }
}
