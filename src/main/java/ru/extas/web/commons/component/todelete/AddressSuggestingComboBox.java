package ru.extas.web.commons.component.todelete;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;

import ru.extas.web.commons.component.todelete.AddressSuggestingContainer.SuggestionFilter;

public class AddressSuggestingComboBox extends ComboBox {

  public AddressSuggestingComboBox(String caption) {
    super(caption);
    setItemCaptionMode(ItemCaptionMode.PROPERTY);
    setItemCaptionPropertyId("value");
  }

  public AddressSuggestingComboBox() {
    this(null);
  }

  /**
   * Overwrite the protected method
   * {@link ComboBox#buildFilter(String, FilteringMode)} to return a custom
   * {@link SuggestionFilter} which is only needed to pass the given
   * filterString on to the {@link AddressSuggestingContainer}.
   */
  @Override
  protected Filter buildFilter(String filterString, FilteringMode filteringMode) {
    return new AddressSuggestingContainer.SuggestionFilter(filterString);
  }
}
