/**
 * 
 */
package ru.extas.server;

import com.google.inject.AbstractModule;

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
		bind(UserManagementService.class).to(UserManagementServiceJdo.class);
		// Служба управления страховками
		bind(InsuranceRepository.class).to(InsuranceRepositoryJdo.class);
		// Служба управления контактами
		bind(ContactService.class).to(ContactServiceJdo.class);
		// Служба - поставщик простых справочных данных
		bind(SupplementService.class).to(SupplementServiceImpl.class);

	}

}
