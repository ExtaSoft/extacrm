/**
 *
 */

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

/**
 * <p>DebugDevServer class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.2.9
 */
public class DebugDevServer {

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) throws NumberParseException {

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        String country = "RU";
        Phonenumber.PhoneNumber phone = null;

        phone = phoneUtil.parse("89876543232, 123", country);

        System.out.printf("NATIONAL - %s\n", phoneUtil.format(phone, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
        System.out.printf("INTERNATIONAL - %s\n", phoneUtil.format(phone, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));
        System.out.printf("E164 - %s\n", phoneUtil.format(phone, PhoneNumberUtil.PhoneNumberFormat.E164));
        System.out.printf("RFC3966 - %s\n", phoneUtil.format(phone, PhoneNumberUtil.PhoneNumberFormat.RFC3966));
        System.out.printf("formatInOriginalFormat - %s\n", phoneUtil.formatInOriginalFormat(phone, country));
        System.out.printf("formatNumberForMobileDialing - %s\n", phoneUtil.formatNumberForMobileDialing(phone, country, true));

        // Running C:\jdk1.7\jre\bin\java
        // -javaagent:C:\Users\Valery\.m2\repository\com\google\appengine\appengine-java-sdk\1.8.1.1\appengine-java-sdk\appengine-java-sdk-1.8.1.1\lib\agent\appengine-agent.jar
        // -Xbootclasspath/p:C:\Users\Valery\.m2\repository\com\google\appengine\appengine-java-sdk\1.8.1.1\appengine-java-sdk\appengine-java-sdk-1.8.1.1\lib\override\appengine-dev-jdk-overrides.jar
        // -Dappengine.fullscan.seconds=-1 -classpath
        // C:\Users\Valery\.m2\repository\com\google\appengine\appengine-java-sdk\1.8.1.1\appengine-java-sdk\appengine-java-sdk-1.8.1.1\lib\appengine-tools-api.jar
        // -Xdebug
        // -agentlib:jdwp=transport=dt_socket,address=8989,server=y,suspend=n
        // com.google.appengine.tools.development.DevAppServerMain
        // --allow_remote_shutdown -p 9090 --disable_update_check
        // C:\Users\Valery\git\extacrm\target/extacrm-0.0.1-SNAPSHOT

        // com.google.appengine.tools.development.DevAppServerMain.main();
    }

}
