/**
 *
 */
package ru.extas.web.contacts.salepoint;

import com.google.common.base.Joiner;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import ru.extas.model.contacts.SalePoint_;
import ru.extas.server.contacts.SalePointRepository;
import ru.extas.web.commons.*;
import ru.extas.web.commons.converters.PhoneConverter;

import java.util.EnumSet;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Опции отображения контактов в списке
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class SalePointsDataDecl extends GridDataDecl {

	public static final String SALEPOINT_BRANDS = SalePoint_.legalEntities.getName();//"salepoint_brands";

	/**
	 * <p>Constructor for SalePointsDataDecl.</p>
	 */
	public SalePointsDataDecl() {
		super();
		addMapping("name", "Имя");
        addMapping("company.name", "Компания", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
		addMapping(SALEPOINT_BRANDS, "Бренды", new GridColumnGenerator() {
			@Override
			public String generateCell(Object columnId, Item item, Object itemId) {
				return Joiner.on(", ").join(lookup(SalePointRepository.class).findSalePointBrands(GridItem.extractBean(item)));
			}

			@Override
			public Property getCellProperty(Object columnId, Item item) {
				return null;
			}

			@Override
			public Class<?> getType() {
				return String.class;
			}
		}, EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
		addMapping("phone", "Телефон", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED), PhoneConverter.class);
		addMapping("email", "E-Mail", new EmailLinkColumnGen(), EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
		addMapping("www", "WWW", new UrlLinkColumnGen(), EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
		addMapping("regAddress.region", "Регион");
		addMapping("regAddress.city", "Город", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
		addMapping("regAddress.streetBld", "Адрес");
		super.addDefaultMappings();
	}

}
