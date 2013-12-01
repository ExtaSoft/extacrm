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
import ru.extas.model.UserProfile;
import ru.extas.server.UserManagementService;

import java.util.List;
import java.util.Map;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 *         Date: 14.11.13
 *         Time: 18:58
 */
public class ExtaUserEntityManager extends UserEntityManager {

    public static Logger LOG = LoggerFactory.getLogger(ExtaUserEntityManager.class);

    @Override
    public UserEntity findUserById(String userId) {
        UserProfile user = lookup(UserManagementService.class).findUserByLogin(userId);
        UserEntity userEntity = new UserEntity(user.getLogin());
        userEntity.setEmail(user.getContact().getEmail());
        userEntity.setFirstName(user.getContact().getName());
        userEntity.setLastName(user.getContact().getName());
        // TODO: Добавить группы пользователей и роль
        return userEntity;
    }

    @Override
    public UserQuery createNewUserQuery() {
        ExtaUserQuery query = new ExtaUserQuery();
        return query;
    }

    @Override
    public Boolean checkPassword(String userId, String password) {
        return Boolean.TRUE;
    }

    @Override
    public void insertUser(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateUser(UserEntity updatedUser) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteUser(String userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void flush() {
        super.flush();
        LOG.error("flush");
    }

    @Override
    public void close() {
        super.close();
        LOG.error("close");
    }

    @Override
    public User createNewUser(String userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> findUserByQueryCriteria(UserQueryImpl query, Page page) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long findUserCountByQueryCriteria(UserQueryImpl query) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Group> findGroupsByUser(String userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IdentityInfoEntity findUserInfoByUserIdAndKey(String userId, String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> findUserInfoKeysByUserIdAndType(String userId, String type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> findPotentialStarterUsers(String proceDefId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> findUsersByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long findUserCountByNativeQuery(Map<String, Object> parameterMap) {
        throw new UnsupportedOperationException();
    }
}
