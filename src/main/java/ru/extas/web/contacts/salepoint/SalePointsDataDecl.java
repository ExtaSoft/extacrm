/**
 *
 */
package ru.extas.web.contacts.salepoint;

import com.google.common.base.Joiner;
import com.vaadin.data.Item;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.contacts.SalePoint_;
import ru.extas.server.contacts.SalePointRepository;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.GridItem;
import ru.extas.web.commons.UrlLinkColumnGen;
import ru.extas.web.commons.converters.PhoneConverter;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Опции отображения контактов в списке
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class SalePointsDataDecl extends GridDataDecl {

    public static final String SALEPOINT_BRANDS_COLUMN = SalePoint_.alphaCode.getName();
    public static final String SALEPOINT_LE_COLUMN = SalePoint_.homeCode.getName();
    public static final String SALEPOINT_INN_COLUMN = SalePoint_.setelemCode.getName();

    /**
     * <p>Constructor for SalePointsDataDecl.</p>
     */
    public SalePointsDataDecl() {
        super();
        addMapping("company.name", "Компания");
        addMapping("name", "Имя торговой точки");
        addMapping("posAddress.regionWithType", "Регион");
        addMapping("posAddress.cityWithType", "Город");
        addMapping("posAddress.value", "Адрес");
        addMapping("phone", "Телефон", PhoneConverter.class);
        addMapping("www", "WWW", new UrlLinkColumnGen());
        addMapping(SALEPOINT_LE_COLUMN, "Юридические лица", new ComponentColumnGenerator() {
            @Override
            public String generateCell(final Object columnId, final Item item, final Object itemId) {
                final SalePoint salePoint = GridItem.extractBean(item);
                return Joiner.on(", ")
                        .join(salePoint.getLegalEntities()
                                .stream()
                                .map(l -> l.getName())
                                .filter(i -> i != null)
                                .toArray());
            }
        });
        addMapping(SALEPOINT_INN_COLUMN, "ИНН", new ComponentColumnGenerator() {
            @Override
            public String generateCell(final Object columnId, final Item item, final Object itemId) {
                final SalePoint salePoint = GridItem.extractBean(item);
                return Joiner.on(", ")
                        .join(salePoint.getLegalEntities()
                                .stream()
                                .map(l -> l.getInn())
                                .filter(i -> i != null)
                                .toArray());
            }
        });
        addMapping(SALEPOINT_BRANDS_COLUMN, "Бренды", new ComponentColumnGenerator() {
            @Override
            public String generateCell(final Object columnId, final Item item, final Object itemId) {
                return Joiner.on(", ").join(lookup(SalePointRepository.class).findSalePointBrands(GridItem.extractBean(item)));
            }
        });
        super.addDefaultMappings();
    }

}
