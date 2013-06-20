package ru.extas.shiro;

import javax.servlet.ServletContext;

import org.apache.shiro.config.Ini;
import org.apache.shiro.guice.web.ShiroWebModule;
import org.apache.shiro.realm.text.IniRealm;

import com.google.inject.Provides;
import com.google.inject.name.Names;

/**
 * Модуль инъекциц для системы безопасности
 * 
 * @author Valery Orlov
 * 
 */
public class ExtasShiroWebModule extends ShiroWebModule {

	public ExtasShiroWebModule(ServletContext sc) {
		super(sc);
	}

	@Override
	protected void configureShiroWeb() {
		try {
			bindRealm().toConstructor(IniRealm.class.getConstructor(Ini.class));
		} catch (NoSuchMethodException e) {
			addError(e);
		}
		bindRealm().to(UserRealm.class);
		// addFilterChain("/**", AUTHC_BASIC);
		// bind(CredentialsMatcher.class).to(HashedCredentialsMatcher.class);
		// bind(HashedCredentialsMatcher.class);
		// bindConstant().annotatedWith(Names.named("shiro.hashAlgorithmName")).to(Md5Hash.ALGORITHM_NAME);
		bindConstant().annotatedWith(Names.named("shiro.globalSessionTimeout")).to(30000L);
	}

	/**
	 * @return
	 */
	@Provides
	Ini loadShiroIni() {
		return Ini.fromResourcePath("./WEB-INF/shiro.ini");
	}
}