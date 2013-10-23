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
        bind(UserManagementService.class).to(UserManagementServiceJpa.class).in(Scopes.SINGLETON);
        // Служба управления страховками
        bind(InsuranceRepository.class).to(InsuranceRepositoryJpa.class).in(Scopes.SINGLETON);
        // Служба управления контактами
        bind(ContactService.class).to(ContactServiceJpa.class).in(Scopes.SINGLETON);
        // Служба - поставщик простых справочных данных
        bind(SupplementService.class).to(SupplementServiceImpl.class).in(Scopes.SINGLETON);
        // Полисы БСО
        bind(PolicyRegistry.class).to(PolicyRegistryJpa.class).in(Scopes.SINGLETON);
        // Страховой калькулятор
        bind(InsuranceCalculator.class).to(InsuranceCalculatorImpl.class).in(Scopes.SINGLETON);
        // Управление актами приема передачи БСО
        bind(FormTransferService.class).to(FormTransferServiceJpa.class).in(Scopes.SINGLETON);
        // Управление формами А-7
        bind(A7FormService.class).to(A7FormServiceJpa.class).in(Scopes.SINGLETON);
        // Управление лидами
        bind(LeadService.class).to(LeadServiceJpa.class).in(Scopes.SINGLETON);
    }

}
