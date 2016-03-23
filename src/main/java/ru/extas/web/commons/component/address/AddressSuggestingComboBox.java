package ru.extas.web.commons.component.address;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import ru.extas.model.common.Address;
import ru.extas.server.common.AddressAccessService;
import ru.extas.server.common.AddressAccessServiceImpl;
import ru.extas.web.commons.container.AddressSuggestingContainer;

public class AddressSuggestingComboBox extends ComboBox {

  public AddressSuggestingComboBox(String caption) {
    super(caption);

    final AddressAccessService databaseAccessService = new AddressAccessServiceImpl();
    final AddressSuggestingContainer container = new AddressSuggestingContainer(databaseAccessService);

    setItemCaptionMode(ItemCaptionMode.PROPERTY);
    setItemCaptionPropertyId("value");
    setImmediate(true);
    addValueChangeListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        container.setSelectedAddress((Address) event.getProperty().getValue());
      }
    });
    setContainerDataSource(container);
  }

  public AddressSuggestingComboBox() {
    this("Адрес");
  }

  /**
   * Overwrite the protected method
   * {@link ComboBox#buildFilter(String, FilteringMode)} to return a custom
   * {@link AddressSuggestingContainer.SuggestionFilter} which is only needed to pass the given
   * filterString on to the {@link AddressSuggestingContainer}.
   */
  @Override
  protected Filter buildFilter(String filterString, FilteringMode filteringMode) {
    return new AddressSuggestingContainer.SuggestionFilter(filterString);
  }


}
