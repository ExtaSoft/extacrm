package ru.extas.server.security;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.extas.model.security.UserProfile;

/**
 * <p>UserRegistry interface.</p>
 *
 * @author Valery Orlov
 *         Date: 19.12.13
 *         Time: 12:33
 * @version $Id: $Id
 * @since 0.3
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface UserRegistry extends JpaRepository<UserProfile, String> {

    /**
     * <p>findByLogin.</p>
     *
     * @param login a {@link java.lang.String} object.
     * @return a {@link ru.extas.model.security.UserProfile} object.
     */
    @Cacheable("userByLogin")
    UserProfile findByLogin(String login);

//    @CacheEvict(value = "userByLogin", allEntries=true)
//    @Override
//    UserProfile save(UserProfile entity);
}
