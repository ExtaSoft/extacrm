package ru.extas.shiro;

import javax.servlet.ServletContext;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.guice.web.ShiroWebModule;

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
		// try {
		// bindRealm().toConstructor(IniRealm.class.getConstructor(Ini.class));
		// } catch (NoSuchMethodException e) {
		// addError(e);
		// }
		bindRealm().to(UserRealm.class);
		// addFilterChain("/**", AUTHC_BASIC);
		bind(CredentialsMatcher.class).to(HashedCredentialsMatcher.class);
		bind(HashedCredentialsMatcher.class);
		bind(CacheManager.class).to(MemcacheManager.class);
		bind(MemcacheManager.class);
		bindConstant().annotatedWith(Names.named("shiro.hashAlgorithmName")).to(Sha256Hash.ALGORITHM_NAME);
		bindConstant().annotatedWith(Names.named("shiro.hashIterations")).to(UserRealm.HASH_ITERATIONS);
		bindConstant().annotatedWith(Names.named("shiro.storedCredentialsHexEncoded")).to(false);
		bindConstant().annotatedWith(Names.named("shiro.globalSessionTimeout")).to(30000L);
		bindConstant().annotatedWith(Names.named("shiro.rememberMe")).to(true);
	}

	// /**
	// * @return
	// */
	// @Provides
	// Ini loadShiroIni() {
	// return Ini.fromResourcePath("./WEB-INF/shiro.ini");
	// }
}