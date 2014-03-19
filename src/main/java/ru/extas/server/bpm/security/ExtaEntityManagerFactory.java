package ru.extas.server.bpm.security;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.UserIdentityManager;

/**
 * <p>ExtaEntityManagerFactory class.</p>
 *
 * @author Valery Orlov
 *         Date: 14.11.13
 *         Time: 18:51
 * @version $Id: $Id
 */
public class ExtaEntityManagerFactory implements SessionFactory {
    /** {@inheritDoc} */
    @Override
    public Class<?> getSessionType() {
        return UserIdentityManager.class;
    }

    /** {@inheritDoc} */
    @Override
    public Session openSession() {
        return (Session) new ExtaUserEntityManager();
    }
}
