/**
 *
 */

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
    public static void main(String[] args) {
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
