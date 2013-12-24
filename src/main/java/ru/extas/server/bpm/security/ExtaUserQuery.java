package ru.extas.server.bpm.security;

import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import ru.extas.model.UserProfile;
import ru.extas.server.UserManagementService;
import ru.extas.server.UserRegistry;

import java.util.List;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 *         Date: 14.11.13
 *         Time: 19:21
 */
public class ExtaUserQuery implements UserQuery {

@Override
public UserQuery asc() {
	throw new UnsupportedOperationException();
}

@Override
public long count() {
	return lookup(UserRegistry.class).count();
}

@Override
public User singleResult() {
	throw new UnsupportedOperationException();
}

@Override
public List<User> list() {
	throw new UnsupportedOperationException();
}

@Override
public List<User> listPage(int firstResult, int maxResults) {
	throw new UnsupportedOperationException();
}

@Override
public UserQuery userId(String id) {
	final UserProfile userProfile = lookup(UserManagementService.class).findUserByLogin(id);

	return new UserQuery() {

		@Override
		public User singleResult() {
			return new BpmUser(userProfile);
		}

		@Override
		public List<User> listPage(int firstResult, int maxResults) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<User> list() {
			throw new UnsupportedOperationException();
		}

		@Override
		public UserQuery desc() {
			throw new UnsupportedOperationException();
		}

		@Override
		public long count() {
			throw new UnsupportedOperationException();
		}

		@Override
		public UserQuery asc() {
			throw new UnsupportedOperationException();
		}

		@Override
		public UserQuery userLastNameLike(String lastNameLike) {
			throw new UnsupportedOperationException();
		}

		@Override
		public UserQuery userFullNameLike(String fullNameLike) {
			throw new UnsupportedOperationException();
		}

		@Override
		public UserQuery userLastName(String lastName) {
			throw new UnsupportedOperationException();
		}

		@Override
		public UserQuery userId(String id) {
			throw new UnsupportedOperationException();
		}

		@Override
		public UserQuery userFirstNameLike(String firstNameLike) {
			throw new UnsupportedOperationException();
		}

		@Override
		public UserQuery userFirstName(String firstName) {
			throw new UnsupportedOperationException();
		}

		@Override
		public UserQuery userEmailLike(String emailLike) {
			throw new UnsupportedOperationException();
		}

		@Override
		public UserQuery userEmail(String email) {
			throw new UnsupportedOperationException();
		}

		@Override
		public UserQuery potentialStarter(String procDefId) {
			throw new UnsupportedOperationException();
		}

		@Override
		public UserQuery orderByUserLastName() {
			throw new UnsupportedOperationException();
		}

		@Override
		public UserQuery orderByUserId() {
			throw new UnsupportedOperationException();
		}

		@Override
		public UserQuery orderByUserFirstName() {
			throw new UnsupportedOperationException();
		}

		@Override
		public UserQuery orderByUserEmail() {
			throw new UnsupportedOperationException();
		}

		@Override
		public UserQuery memberOfGroup(String groupId) {
			throw new UnsupportedOperationException();
		}

	};
}

@Override
public UserQuery userFirstName(String firstName) {
	throw new UnsupportedOperationException();
}

@Override
public UserQuery userFirstNameLike(String firstNameLike) {
	throw new UnsupportedOperationException();
}

@Override
public UserQuery userLastName(String lastName) {
	throw new UnsupportedOperationException();
}

@Override
public UserQuery userLastNameLike(String lastNameLike) {
	throw new UnsupportedOperationException();
}

@Override
public UserQuery userFullNameLike(String fullNameLike) {
	throw new UnsupportedOperationException();
}

@Override
public UserQuery userEmail(String email) {
	throw new UnsupportedOperationException();
}

@Override
public UserQuery userEmailLike(String emailLike) {
	throw new UnsupportedOperationException();
}

@Override
public UserQuery memberOfGroup(String groupId) {
	throw new UnsupportedOperationException();
}

@Override
public UserQuery potentialStarter(String procDefId) {
	throw new UnsupportedOperationException();
}

@Override
public UserQuery orderByUserId() {
	throw new UnsupportedOperationException();
}

@Override
public UserQuery orderByUserFirstName() {
	throw new UnsupportedOperationException();
}

@Override
public UserQuery orderByUserLastName() {
	throw new UnsupportedOperationException();
}

@Override
public UserQuery orderByUserEmail() {
	throw new UnsupportedOperationException();
}

@Override
public UserQuery desc() {
	throw new UnsupportedOperationException();
}
}
