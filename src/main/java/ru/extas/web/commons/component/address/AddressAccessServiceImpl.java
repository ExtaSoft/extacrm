package ru.extas.web.commons.component.address;

import ru.extas.model.common.Address;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sandarkin
 * @since 2.0
 * @version 2.0
 */
public class AddressAccessServiceImpl implements AddressAccessService {

    @Override
    public List<Address> obtainSuggestedAddresses(String filterPrefix) {

        List<Address> addresses = new ArrayList<>();

        Address a1 = Address.builder().value("Aaaaaaaaa").build();
        Address a2 = Address.builder().value("Bbbbbbbbb").build();
        Address a3 = Address.builder().value("Ccccccccc").build();

        Collections.addAll(addresses, a1, a2, a3);

        return addresses;
    }

}
