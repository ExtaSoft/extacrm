package ru.extas.web.insurance;

import ru.extas.web.commons.DataDeclMapping.PresentFlag;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.converters.PhoneConverter;
import ru.extas.web.contacts.ClientColumnGenerator;

import java.util.EnumSet;

/**
 * Опции отрбражения страховок
 *
 * @author Valery Orlov
 */
class InsuranceDataDecl extends GridDataDecl {

    /**
     * <p>Constructor for InsuranceDataDecl.</p>
     */
    public InsuranceDataDecl() {
        super();
        addMapping("regNum", "Номер полиса");
        addMapping("a7Num", "Квитанция А-7", EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("date", "Дата договора");
        addMapping("client.name", "Клиент", new ClientColumnGenerator());
        addMapping("client.phone", "Телефон", PhoneConverter.class);
        addMapping("beneficiary", "Выгодопреобретатель", EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("usedMotor", "Б/у", EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("motorType", "Тип техники");
        addMapping("motorBrand", "Марка техники");
        addMapping("motorModel", "Модель техники");
        addMapping("motorVin", "VIN", EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("saleNum", "№ дог.купли/продажи", EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("saleDate", "Дата дог.купли/продажи", EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("riskSum", "Страховая сумма");
        addMapping("coverTime", "Период страхования", EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("premium", "Страховая премия");
        addMapping("docComplete", "Комплект документов", EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("paymentDate", "Дата оплаты страховой премии", EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("startDate", "Дата начала срока действия договора", EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("endDate", "Дата окончания срока действия договора", EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("dealer.name", "Торговая точка");
        super.addDefaultMappings();
    }

}
