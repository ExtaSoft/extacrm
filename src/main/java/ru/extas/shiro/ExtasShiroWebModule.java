package ru.extas.shiro;

import javax.servlet.ServletContext;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.config.Ini;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.guice.web.ShiroWebModule;
import org.apache.shiro.realm.text.IniRealm;

import com.google.inject.Provides;
import com.google.inject.name.Names;

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
		//addFilterChain("/**", AUTHC_BASIC);
		bind(CredentialsMatcher.class).to(HashedCredentialsMatcher.class);
		bind(HashedCredentialsMatcher.class);
		bindConstant().annotatedWith(Names.named("shiro.hashAlgorithmName")).to(Md5Hash.ALGORITHM_NAME);
		bindConstant().annotatedWith(Names.named("shiro.globalSessionTimeout")).to(30000L);
	}

	@Provides
	Ini loadShiroIni() {
		return Ini.fromResourcePath("./WEB-INF/shiro.ini");
	}
}