/**
 * 
 */
package ru.extacrm.shiro.test;

import java.util.Date;

import ru.extas.model.UserProfile;
import ru.extas.shiro.UserRealm;

/**
 * @author Valery Orlov
 * 
 */
public class PasswordEncript {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		final String plainTextPassword = "123qwe";
		UserProfile user = new UserProfile();
		user.setPassword(plainTextPassword);
		UserRealm.securePassword(user);

		System.out.println(user.getPassword());
		System.out.println(user.getPasswordSalt());

		System.out.println(new Date());
	}

}
