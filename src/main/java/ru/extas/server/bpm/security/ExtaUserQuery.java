package ru.extas.server.bpm.security;

import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import ru.extas.model.security.UserProfile;
import ru.extas.server.security.UserManagementService;
import ru.extas.server.security.UserRegistry;

import java.util.List;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * <p>ExtaUserQuery class.</p>
 *
 * @author Valery Orlov
 *         Date: 14.11.13
 *         Time: 19:21
 * @version $Id: $Id
 * @since 0.3
 */
public class ExtaUserQuery implements UserQuery {

/** {@inheritDoc} */
@Override
public UserQuery asc() {
	throw new UnsupportedOperationException();
}

/** {@inheritDoc} */
@Override
public long count() {
	return lookup(UserRegistry.class).count();
}

/** {@inheritDoc} */
@Override
public User singleResult() {
	throw new UnsupportedOperationException();
}

/** {@inheritDoc} */
@Override
public List<User> list() {
	throw new UnsupportedOperationException();
}

/** {@inheritDoc} */
@Override
public List<User> listPage(final int firstResult, final int maxResults) {
	throw new UnsupportedOperationException();
}

/** {@inheritDoc} */
@Override
public UserQuery userId(final String id) {
	final UserProfile userProfile = lookup(UserManagementService.class).findUserByLogin(id);

	return new UserQuery() {

		@Override
		public User singleResult() {
			return new BpmUser(userProfile);
		}

		@Override
		public List<User> listPage(final int firstResult, final int maxResults) {
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
		public UserQuery userLastNameLike(final String lastNameLike) {
			throw new UnsupportedOperationException();
		}

		@Override
		public UserQuery userFullNameLike(final String fullNameLike) {
			throw new UnsupportedOperationException();
		}

		@Override
		public UserQuery userLastName(final String lastName) {
			throw new UnsupportedOperationException();
		}

		@Override
		public UserQuery userId(final String id) {
			throw new UnsupportedOperationException();
		}

		@Override
		public UserQuery userFirstNameLike(final String firstNameLike) {
			throw new UnsupportedOperationException();
		}

		@Override
		public UserQuery userFirstName(final String firstName) {
			throw new UnsupportedOperationException();
		}

		@Override
		public UserQuery userEmailLike(final String emailLike) {
			throw new UnsupportedOperationException();
		}

		@Override
		public UserQuery userEmail(final String email) {
			throw new UnsupportedOperationException();
		}

		@Override
		public UserQuery potentialStarter(final String procDefId) {
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
		public UserQuery memberOfGroup(final String groupId) {
			throw new UnsupportedOperationException();
		}

	};
}

/** {@inheritDoc} */
@Override
public UserQuery userFirstName(final String firstName) {
	throw new UnsupportedOperationException();
}

/** {@inheritDoc} */
@Override
public UserQuery userFirstNameLike(final String firstNameLike) {
	throw new UnsupportedOperationException();
}

/** {@inheritDoc} */
@Override
public UserQuery userLastName(final String lastName) {
	throw new UnsupportedOperationException();
}

/** {@inheritDoc} */
@Override
public UserQuery userLastNameLike(final String lastNameLike) {
	throw new UnsupportedOperationException();
}

/** {@inheritDoc} */
@Override
public UserQuery userFullNameLike(final String fullNameLike) {
	throw new UnsupportedOperationException();
}

/** {@inheritDoc} */
@Override
public UserQuery userEmail(final String email) {
	throw new UnsupportedOperationException();
}

/** {@inheritDoc} */
@Override
public UserQuery userEmailLike(final String emailLike) {
	throw new UnsupportedOperationException();
}

/** {@inheritDoc} */
@Override
public UserQuery memberOfGroup(final String groupId) {
	throw new UnsupportedOperationException();
}

/** {@inheritDoc} */
@Override
public UserQuery potentialStarter(final String procDefId) {
	throw new UnsupportedOperationException();
}

/** {@inheritDoc} */
@Override
public UserQuery orderByUserId() {
	throw new UnsupportedOperationException();
}

/** {@inheritDoc} */
@Override
public UserQuery orderByUserFirstName() {
	throw new UnsupportedOperationException();
}

/** {@inheritDoc} */
@Override
public UserQuery orderByUserLastName() {
	throw new UnsupportedOperationException();
}

/** {@inheritDoc} */
@Override
public UserQuery orderByUserEmail() {
	throw new UnsupportedOperationException();
}

/** {@inheritDoc} */
@Override
public UserQuery desc() {
	throw new UnsupportedOperationException();
}
}
