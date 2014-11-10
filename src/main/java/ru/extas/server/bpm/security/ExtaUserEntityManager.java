package ru.extas.server.bpm.security;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.persistence.entity.IdentityInfoEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.security.UserProfile;
import ru.extas.server.security.UserManagementService;

import java.util.List;
import java.util.Map;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * <p>ExtaUserEntityManager class.</p>
 *
 * @author Valery Orlov
 *         Date: 14.11.13
 *         Time: 18:58
 * @version $Id: $Id
 * @since 0.3
 */
public class ExtaUserEntityManager extends UserEntityManager {

    /** Constant <code>LOG</code> */
    public static final Logger LOG = LoggerFactory.getLogger(ExtaUserEntityManager.class);

    /** {@inheritDoc} */
    @Override
    public UserEntity findUserById(final String userId) {
        final UserProfile user = lookup(UserManagementService.class).findUserByLogin(userId);
        final UserEntity userEntity = new UserEntity(user.getLogin());
        userEntity.setEmail(user.getEmployee().getEmail());
        userEntity.setFirstName(user.getEmployee().getName());
        userEntity.setLastName(user.getEmployee().getName());
        // TODO: Добавить группы пользователей и роль
        return userEntity;
    }

    /** {@inheritDoc} */
    @Override
    public UserQuery createNewUserQuery() {
        final ExtaUserQuery query = new ExtaUserQuery();
        return query;
    }

    /** {@inheritDoc} */
    @Override
    public Boolean checkPassword(final String userId, final String password) {
        return Boolean.TRUE;
    }

    /** {@inheritDoc} */
    @Override
    public void insertUser(final User user) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public void updateUser(final User updatedUser) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public void deleteUser(final String userId) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public void flush() {
        super.flush();
        LOG.error("flush");
    }

    /** {@inheritDoc} */
    @Override
    public void close() {
        super.close();
        LOG.error("close");
    }

    /** {@inheritDoc} */
    @Override
    public User createNewUser(final String userId) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public List<User> findUserByQueryCriteria(final UserQueryImpl query, final Page page) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public long findUserCountByQueryCriteria(final UserQueryImpl query) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public List<Group> findGroupsByUser(final String userId) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public IdentityInfoEntity findUserInfoByUserIdAndKey(final String userId, final String key) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public List<String> findUserInfoKeysByUserIdAndType(final String userId, final String type) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public List<User> findPotentialStarterUsers(final String proceDefId) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public List<User> findUsersByNativeQuery(final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public long findUserCountByNativeQuery(final Map<String, Object> parameterMap) {
        throw new UnsupportedOperationException();
    }
}
