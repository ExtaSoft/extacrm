package ru.extas.server.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.common.ArchivedObject;
import ru.extas.model.common.IdentifiedObject;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Имплементация службы архивации объектов
 *
 * @author Valery Orlov
 *         Date: 23.11.2014
 *         Time: 18:57
 */
@Component
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class ArchiveServiceImpl implements ArchiveService {
    private final static Logger logger = LoggerFactory.getLogger(ArchiveServiceImpl.class);

    @Inject
    private EntityManager em;

    @Transactional
    @Override
    public <TEntity extends ArchivedObject> TEntity archive(final TEntity entity) {
        return setArchivedFlag(entity, true);
    }

    @Transactional
    @Override
    public <TEntity extends ArchivedObject> Set<TEntity> archive(final Set<TEntity> entities) {
        return setArchivedFlag(entities, true);
    }

    @Transactional
    @Override
    public <TEntity extends ArchivedObject> TEntity extract(final TEntity entity) {
        return setArchivedFlag(entity, false);
    }

    @Transactional
    @Override
    public <TEntity extends ArchivedObject> Set<TEntity> extract(final Set<TEntity> entities) {
        return setArchivedFlag(entities, false);
    }

    private <TEntity extends ArchivedObject> TEntity setArchivedFlag(TEntity entity, final boolean toArchive) {
        checkNotNull(entity);
        checkArgument(entity instanceof IdentifiedObject);
        checkArgument(!((IdentifiedObject) entity).isNew());

        if (toArchive != entity.isArchived()) {
            if(!em.contains(entity)) {
                entity = em.find((Class<TEntity>) entity.getClass(), ((IdentifiedObject)entity).getId());
            }
            em.refresh(entity);
            entity.setArchived(toArchive);
            entity = em.merge(entity);
        }

        return entity;
    }

    private <TEntity extends ArchivedObject> Set<TEntity> setArchivedFlag(final Set<TEntity> entities, final boolean toArchive) {
        checkNotNull(entities);

        final Set<TEntity> res = new LinkedHashSet<>(entities.size());
        for (final TEntity entity : entities) {
            res.add(setArchivedFlag(entity, toArchive));
        }

        return res;
    }

}
