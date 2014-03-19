/**
 *
 */
package ru.extas.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.FormTransfer;

import javax.inject.Inject;

/**
 * <p>FormTransferServiceImpl class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 */
@Component
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class FormTransferServiceImpl implements FormTransferService {

private final static Logger logger = LoggerFactory.getLogger(FormTransferServiceImpl.class);

@Inject
private FormTransferRepository transferRepository;
@Inject
private A7FormService a7FormService;

/*
 * (non-Javadoc)
 *
 * @see ru.extas.server.FormTransferRepository#saveAndChangeOwner(ru.extas.model.FormTransfer)
 */
/** {@inheritDoc} */
@Transactional
@Override
public void saveAndChangeOwner(final FormTransfer tf) {
	logger.debug("Persisting FormTransfer");
	a7FormService.changeOwner(tf.getFormNums(), tf.getToContact());
	transferRepository.save(tf);
}

}
