package ru.extas.web.commons.component.todelete;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.extas.model.common.Address;

import java.util.List;

/**
 * @author sandarkin
 * @version 2.0
 * @since 2.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressSuggestions {
    List<Address> suggestions;
}
