package ru.extas.web.commons.component.address;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import ru.extas.model.common.Address;

import java.util.List;

/**
 * This is a specialized {@link BeanItemContainer} which redefines the filtering
 * functionality by overwriting method
 * {@link com.vaadin.data.util.AbstractInMemoryContainer#addFilter(Filter)}.
 * This method is called internally by the filtering code of a ComboBox.
 */
public class AddressSuggestingContainer extends BeanItemContainer<Address> {

  private AddressAccessService service;

  public AddressSuggestingContainer(AddressAccessService service) throws IllegalArgumentException {
    super(Address.class);
    this.service = service;
  }

  /**
   * This method will be called by ComboBox each time the user has entered a new
   * value into the text field of the ComboBox. For our custom ComboBox class
   * {@link AddressSuggestingComboBox} it is assured by
   * {@link AddressSuggestingComboBox#buildFilter(String, com.vaadin.shared.ui.combobox.FilteringMode)}
   * that only instances of {@link SuggestionFilter} are passed into this
   * method. We can therefore safely cast the filter to this class. Then we
   * simply get the filterString from this filter and call the database service
   * with this filterString. The database then returns a list of country objects
   * whose country names begin with the filterString. After having removed all
   * existing items from the container we add the new list of freshly filtered
   * country objects.
   */
  @Override
  protected void addFilter(Filter filter) throws UnsupportedFilterException {
    SuggestionFilter suggestionFilter = (SuggestionFilter) filter;
    filterItems(suggestionFilter.getFilterString());
  }

  private void filterItems(String filterString) {
    if ("".equals(filterString)) {
      return;
    }

    removeAllItems();
    List<Address> countries = service.filterAddresses(filterString);
    addAll(countries);
  }

  /**
   * This method makes sure that the selected value is the only value shown in the dropdown list of the ComboBox when
   * this is explicitly opened with the arrow icon. If such a method is omitted, the dropdown list will contain the
   * most recently suggested items.
   */
  public void setSelectedAddress(Address country) {
    removeAllItems();
    addBean(country);
  }

  /**
   * The sole purpose of this {@link Filter} implementation is to transport the
   * current filterString (which is a private property of ComboBox) to our
   * custom container implementation {@link AddressSuggestingContainer}. Our container
   * needs that filterString in order to fetch a filtered country list from the
   * database.
   */
  public static class SuggestionFilter implements Filter {

    private String filterString;

    public SuggestionFilter(String filterString) {
      this.filterString = filterString;
    }

    public String getFilterString() {
      return filterString;
    }

    @Override
    public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
      // will never be used and can hence always return false
      return false;
    }

    @Override
    public boolean appliesToProperty(Object propertyId) {
      // will never be used and can hence always return false
      return false;
    }
  }
}
