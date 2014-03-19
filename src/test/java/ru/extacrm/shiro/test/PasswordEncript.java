/**
 *
 */
package ru.extacrm.shiro.test;

import ru.extas.model.UserProfile;
import ru.extas.shiro.UserRealm;

import java.util.Date;

/**
 * <p>PasswordEncript class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.2.9
 */
public class PasswordEncript {

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
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
