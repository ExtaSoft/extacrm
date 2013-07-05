/**
 * 
 */
package ru.extas.server;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * Модуль инъекций для интерфейсов бизнес логики
 * 
 * @author Valery Orlov
 * 
 */
public class ExtasServicesModule extends AbstractModule {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {

		// Служба управления пользователями
		bind(UserManagementService.class).to(UserManagementServiceJdo.class).in(Scopes.SINGLETON);
		// Служба управления страховками
		bind(InsuranceRepository.class).to(InsuranceRepositoryJdo.class).in(Scopes.SINGLETON);
		// Служба управления контактами
		bind(ContactService.class).to(ContactServiceJdo.class).in(Scopes.SINGLETON);
		// Служба - поставщик простых справочных данных
		bind(SupplementService.class).to(SupplementServiceImpl.class).in(Scopes.SINGLETON);

	}

}
