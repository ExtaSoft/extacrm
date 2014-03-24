package ru.extas.model.common;

import ru.extas.security.EntitySecurityManager;

import javax.persistence.MappedSuperclass;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

import static ru.extas.security.SecurityUtils.getSecurityManagerByClass;

/**
 * Базовый класс для всех сущностей.
 * Имплементирует ведение журналов безопасности.
 *
 * @author Valery Orlov
 *         Date: 22.03.2014
 *         Time: 17:40
 */
@MappedSuperclass
public class SecuredObject extends ChangeMarkedObject {

    @PostPersist
    @PostUpdate
    private void logOwnerPrivileges() {
        EntitySecurityManager securityManager = getSecurityManagerByClass(this.getClass());
        securityManager.addOwnerPrivileges(this);
    }
}
