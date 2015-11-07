package ru.extas.web.commons.component.address;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import ru.extas.model.common.Address;

import java.util.List;

/**
 * @author sandarkin
 * @since 2.0
 * @version 2.0
 */
public class AddressSuggestingContainer extends BeanItemContainer<Address> {

    private AddressAccessService accessService;

    public AddressSuggestingContainer(AddressAccessService accessService) throws IllegalArgumentException {
        super(Address.class);
        this.accessService = accessService;
    }

    @Override
    protected void addFilter(Filter filter) throws UnsupportedFilterException {
        SuggestionFilter suggestionFilter = (SuggestionFilter) filter;
        filterItems(suggestionFilter.getFilterString());
    }

    private void filterItems(String filterString) {
        if ("".equals(filterString)) {
/*            if (defaultCountry != null) {
                // if "nothing" has been selected from the dropdown list and a default value is defined, add this default
                // value to this container so that it can be selected as the current value.
                addBean(defaultCountry);
            }
            return;*/
        }

        removeAllItems();
        List<Address> addresses = this.accessService.obtainSuggestedAddresses(filterString);
        addAll(addresses);
    }

    public void setSelectedAddress(Address address) {
        removeAllItems();
        addBean(address);
    }

    /**
     * The sole purpose of this {@link Filter} implementation is to transport the
     * current filterString (which is a private property of ComboBox) to our
     * custom container implementation {@link SuggestingContainer}. Our container
     * needs that filterString in order to fetch a filtered country list from the
     * database.
     */
    public static class SuggestionFilter implements Container.Filter {

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
