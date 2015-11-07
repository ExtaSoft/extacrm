package ru.extas.web.commons.component.address;

import ru.extas.model.common.Address;

import java.util.List;

/**
 * Created by sandarkin on 07/11/15.
 */
public interface AddressAccessService {

    List<Address> obtainSuggestedAddresses(String filterPrefix);

}
