package ru.extas.server.bpm.security;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.UserIdentityManager;

/**
 * @author Valery Orlov
 *         Date: 14.11.13
 *         Time: 18:51
 */
public class ExtaEntityManagerFactory implements SessionFactory {
    @Override
    public Class<?> getSessionType() {
        return UserIdentityManager.class;
    }

    @Override
    public Session openSession() {
        return (Session) new ExtaUserEntityManager();
    }
}
