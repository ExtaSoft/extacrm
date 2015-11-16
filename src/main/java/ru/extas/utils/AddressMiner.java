package ru.extas.utils;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.extas.model.common.Address;
import ru.extas.model.contacts.AddressInfo;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.model.contacts.Person;
import ru.extas.model.contacts.SalePoint;
import ru.extas.server.contacts.LegalEntityRepository;
import ru.extas.server.contacts.PersonRepository;
import ru.extas.server.contacts.SalePointRepository;
import ru.extas.web.commons.component.address.AddressAccessService;
import ru.extas.web.commons.component.address.AddressAccessServiceImpl;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Компонент преобразования старых адресов в новые.
 * TODO После того, как все адреса будут преобразованы, этот компонент надо выключить.
 *
 * @author sandarkin
 * @since 2.0
 * @version 2.0
 */

@Component
public class AddressMiner {

    @Autowired
    private BasicDataSource dataSource;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private LegalEntityRepository legalEntityRepository;

    @Autowired
    private SalePointRepository salePointRepository;

    private final AddressAccessService databaseAccessService = new AddressAccessServiceImpl();

    @PostConstruct
    public void init() {
        for (Person person : personRepository.findAll()) {
            minePersonAddresses(person);
        }
        for (LegalEntity legalEntity : legalEntityRepository.findAll()) {
            mineLegalEntityAddresses(legalEntity);
        }
        for (SalePoint salePoint : salePointRepository.findAll()) {
            mineSalePointAddresses(salePoint);
        }
    }

    private void minePersonAddresses(Person person) {
        if(person.getRegisterAddress() == null && person.getRegAddress() != null) {
            List<Address> addressList = databaseAccessService.filterAddresses(plainAddrInfo(person.getRegAddress()));
            person.setRegisterAddress(first(addressList));
        }

        if(person.getFactAddress() == null && person.getActualAddress() != null) {
            List<Address> addressList = databaseAccessService.filterAddresses(plainAddrInfo(person.getActualAddress()));
            person.setFactAddress(first(addressList));
        }

        if(person.getWorkAddress() == null && person.getEmployerAdress() != null) {
            List<Address> addressList = databaseAccessService.filterAddresses(person.getEmployerAdress());
            person.setWorkAddress(first(addressList));
        }

        if(person.getBusinessAddress() == null && person.getBusinessAdress() != null) {
            List<Address> addressList = databaseAccessService.filterAddresses(person.getBusinessAdress());
            person.setBusinessAddress(first(addressList));
        }

        personRepository.save(person);
    }

    private void mineLegalEntityAddresses(LegalEntity legalEntity) {
        if (legalEntity.getLegalAddress() == null && legalEntity.getRegAddress() != null) {
            List<Address> addressList = databaseAccessService.filterAddresses(plainAddrInfo(legalEntity.getRegAddress()));
            legalEntity.setLegalAddress(first(addressList));
        }
        legalEntityRepository.save(legalEntity);
    }

    private void mineSalePointAddresses(SalePoint salePoint) {
        if (salePoint.getPosAddress() == null && salePoint.getRegAddress() != null) {
            List<Address> addressList = databaseAccessService.filterAddresses(plainAddrInfo(salePoint.getRegAddress()));
            salePoint.setPosAddress(first(addressList));
        }
        salePointRepository.save(salePoint);
    }

    private String plainAddrInfo(AddressInfo info) {
        return info.getRegion() + " " + info.getCity() + " " + info.getStreetBld();
    }

    private Address first(List<Address> addressList) {
        if(addressList.isEmpty()) {
            return null;
        }
        return addressList.get(0);
    }


}
