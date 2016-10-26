package ru.extas.web.info;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Or;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;
import com.vaadin.ui.UI;
import org.tepi.filtertable.FilterGenerator;
import org.vaadin.dialogs.ConfirmDialog;
import ru.extas.model.info.InfoFile;
import ru.extas.model.info.InfoFile_;
import ru.extas.model.security.*;
import ru.extas.server.info.InfoFileRepository;
import ru.extas.server.security.UserManagementService;
import ru.extas.web.commons.*;
import ru.extas.web.commons.container.ExtaDbContainer;
import ru.extas.web.motor.MotorBrandSelect;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Грид с информационными материалами
 * <p>
 * Created by valery on 25.10.16.
 */
public class InfoFilesGrid extends ExtaGrid<InfoFile> {

    /**
     * <p>Constructor for ExtaGrid.</p>
     */
    public InfoFilesGrid() {
        super(InfoFile.class);
        if (!lookup(UserManagementService.class).isPermitted(ExtaDomain.INFORMATION, SecureTarget.ALL, SecureAction.EDIT))
            setReadOnly(true);
    }

    @Override
    public ExtaEditForm<InfoFile> createEditForm(InfoFile infoFile, boolean isInsert) {
        final InfoFileEditForm form = new InfoFileEditForm(infoFile);
        return form;
    }

    @Override
    protected GridDataDecl createDataDecl() {
        return new InfoFileDataDecl();
    }

    @Override
    protected Container createContainer() {
        final ExtaDbContainer<InfoFile> dbContainer = new ExtaDbContainer<>(InfoFile.class);
        UserProfile user = lookup(UserManagementService.class).getCurrentUser();
        if (user.getRole() != UserRole.ADMIN) {
            final Set<String> permitBrands = newHashSet(user.getPermitBrands());
            final Set<UserGroup> groups = user.getGroupList();
            if (groups != null) {
                for (final UserGroup group : groups) {
                    permitBrands.addAll(group.getPermitBrands());
                }
            }
            if (!permitBrands.isEmpty()) {
                Container.Filter[] bFilters = new Container.Filter[permitBrands.size()];
                int i = 0;
                for (String brand : permitBrands) {
                    bFilters[i] = new Compare.Equal(InfoFile_.permitBrands.getName(), brand);
                    i++;
                }
                dbContainer.addContainerFilter(new Or(bFilters));
            }
        }
        return dbContainer;
    }

    @Override
    protected FilterGenerator createFilterGenerator() {
        return new CompositeFilterGenerator()
                .with(new AbstractFilterGenerator() {
                    @Override
                    public Container.Filter generateFilter(Object propertyId, Field<?> originatingField) {
                        return null;
                    }

                    @Override
                    public AbstractField<?> getCustomFilterComponent(final Object propertyId) {
                        if (propertyId.equals(InfoFile_.permitBrands.getName()))
                            return new MotorBrandSelect();
                        else
                            return null;
                    }
                });
    }

    @Override
    protected List<UIAction> createActions() {
        final List<UIAction> actions = newArrayList();

        final UserManagementService userManagementService = lookup(UserManagementService.class);

        if (userManagementService.isPermitted(ExtaDomain.INFORMATION, SecureTarget.ALL, SecureAction.INSERT))
            actions.add(new NewObjectAction("Новый файл", "Загрузка нового информационного файла"));

        if (userManagementService.isPermitted(ExtaDomain.INFORMATION, SecureTarget.ALL, SecureAction.EDIT))
            actions.add(new EditObjectAction("Изменить", "Редактировать запись об информационном файле"));
        else
            actions.add(new EditObjectAction("Посмотреть", "Просмотреть запись об информационном файле"));

        if (userManagementService.isPermitted(ExtaDomain.INFORMATION, SecureTarget.ALL, SecureAction.DELETE))
            actions.add(new ItemAction("Удалить", "Удалить запись об информационном файле", Fontello.TRASH) {
                @Override
                public void fire(Set itemIds) {
                    ConfirmDialog.show(UI.getCurrent(), "Удаление файлов...",
                            "Вы уверены что необходимо удалить выделенные файлы? Эту операцию нельзя отменить.",
                            "Удалить", "Оставить", dialog -> {
                                if (dialog.isConfirmed()) {
                                    lookup(InfoFileRepository.class)
                                            .delete(getSelectedEntities());
                                    NotificationUtil.showSuccess("Информационные материалы удалены");
                                    refreshContainer();
                                }
                            });
                }
            });

        return actions;
    }
}
