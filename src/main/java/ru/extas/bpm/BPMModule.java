package ru.extas.bpm;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.activiti.engine.*;

/**
 * Модуль инъекций лдя всего что связано с бизнес процессом
 *
 * @author Valery Orlov
 *         Date: 29.10.13
 *         Time: 13:33
 */
public class BPMModule extends AbstractModule {
    @Override
    protected void configure() {
        // Инициализация BPM
        ProcessEngines.init();

    }

    @Provides
    public ProcessEngine provideProcessEngine() {
        return ProcessEngines.getDefaultProcessEngine();
    }

    @Provides
    public RuntimeService provideRuntimeService() {
        return provideProcessEngine().getRuntimeService();
    }

    @Provides
    public RepositoryService provideRepositoryService() {
        return provideProcessEngine().getRepositoryService();
    }

    @Provides
    public TaskService provideTaskService() {
        return provideProcessEngine().getTaskService();
    }

    @Provides
    public ManagementService provideManagementService() {
        return provideProcessEngine().getManagementService();
    }

    @Provides
    public IdentityService provideIdentityService() {
        return provideProcessEngine().getIdentityService();
    }

    @Provides
    public HistoryService provideHistoryService() {
        return provideProcessEngine().getHistoryService();
    }

    @Provides
    public FormService provideFormService() {
        return provideProcessEngine().getFormService();
    }

}
