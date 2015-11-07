package ru.extas.web.commons.component.todelete;

import ru.extas.model.common.Address;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * In-memory fake implementation of the {@link DatabaseAccessService} interface.
 */
public class DatabaseAccessServiceImpl implements DatabaseAccessService {

  private List<Address> addressDatabaseTable;

  public DatabaseAccessServiceImpl() {
      addressDatabaseTable = new LinkedList<Address>();
    populateDatabaseTable();
  }

  @Override
  public List<Address> filterAddresses(String filterPrefix) {
    if ("".equals(filterPrefix) || filterPrefix == null) {
      return Collections.emptyList();
    }
    List<Address> result = new ArrayList<>();

    for (Address address : addressDatabaseTable) {
      ///if (country.getName().toLowerCase().startsWith(filterPrefix)) {
      if (address.getValue().toLowerCase().startsWith(filterPrefix)) {
        result.add(address);
      }
    }

    return result;
  }

  private void populateDatabaseTable() {
      addressDatabaseTable.addAll(Arrays.asList(
        Address.builder().value("Aaaaa").build(),
        Address.builder().value("Bbbbb").build(),
        Address.builder().value("Ccccc").build(),
        Address.builder().value("Ddddd").build(),
        Address.builder().value("Eeeee").build(),
        Address.builder().value("Fffff").build()
      ));
  }

/*

  private void populateDatabaseTable() {
    long id = 0;
    // @formatter:off
      addressDatabaseTable.addAll(Arrays.asList(
        new CountryBean(id++, "Afghanistan"),
        new CountryBean(id++, "Albania"),
        new CountryBean(id++, "Algeria"),
        new CountryBean(id++, "Andorra"),
        new CountryBean(id++, "Angola"),
        new CountryBean(id++, "Antigua and Barbuda"),
        new CountryBean(id++, "Argentina"),
        new CountryBean(id++, "Armenia"),
        new CountryBean(id++, "Australia"),
        new CountryBean(id++, "Austria"),
        new CountryBean(id++, "Azerbaijan"),
        new CountryBean(id++, "Bahamas"),
        new CountryBean(id++, "Bahrain"),
        new CountryBean(id++, "Bangladesh"),
        new CountryBean(id++, "Barbados"),
        new CountryBean(id++, "Belarus"),
        new CountryBean(id++, "Belgium"),
        new CountryBean(id++, "Belize"),
        new CountryBean(id++, "Benin"),
        new CountryBean(id++, "Bhutan"),
        new CountryBean(id++, "Bolivia"),
        new CountryBean(id++, "Bosnia and Herzegovina"),
        new CountryBean(id++, "Botswana"),
        new CountryBean(id++, "Brazil"),
        new CountryBean(id++, "Brunei Darussalam"),
        new CountryBean(id++, "Bulgaria"),
        new CountryBean(id++, "Burkina Faso"),
        new CountryBean(id++, "Burundi"),
        new CountryBean(id++, "Cabo Verde"),
        new CountryBean(id++, "Cambodia"),
        new CountryBean(id++, "Cameroon"),
        new CountryBean(id++, "Canada"),
        new CountryBean(id++, "Central African Republic"),
        new CountryBean(id++, "Chad"),
        new CountryBean(id++, "Chile"),
        new CountryBean(id++, "China"),
        new CountryBean(id++, "Colombia"),
        new CountryBean(id++, "Comoros"),
        new CountryBean(id++, "Congo"),
        new CountryBean(id++, "Democratic Republic of the Congo"),
        new CountryBean(id++, "Costa Rica"),
        new CountryBean(id++, "Côte d'Ivoire"),
        new CountryBean(id++, "Croatia"),
        new CountryBean(id++, "Cuba"),
        new CountryBean(id++, "Cyprus"),
        new CountryBean(id++, "Czech Republic"),
        new CountryBean(id++, "Denmark"),
        new CountryBean(id++, "Djibouti"),
        new CountryBean(id++, "Dominica"),
        new CountryBean(id++, "Dominican Republic"),
        new CountryBean(id++, "Ecuador"),
        new CountryBean(id++, "Egypt"),
        new CountryBean(id++, "El Salvador"),
        new CountryBean(id++, "Equatorial Guinea"),
        new CountryBean(id++, "Eritrea"),
        new CountryBean(id++, "Estonia"),
        new CountryBean(id++, "Ethiopia"),
        new CountryBean(id++, "Fiji"),
        new CountryBean(id++, "Finland"),
        new CountryBean(id++, "France"),
        new CountryBean(id++, "Gabon"),
        new CountryBean(id++, "Gambia"),
        new CountryBean(id++, "Georgia"),
        new CountryBean(id++, "Germany"),
        new CountryBean(id++, "Ghana"),
        new CountryBean(id++, "Greece"),
        new CountryBean(id++, "Grenada"),
        new CountryBean(id++, "Guatemala"),
        new CountryBean(id++, "Guinea"),
        new CountryBean(id++, "Guinea-Bissau"),
        new CountryBean(id++, "Guyana"),
        new CountryBean(id++, "Haiti"),
        new CountryBean(id++, "Honduras"),
        new CountryBean(id++, "Hungary"),
        new CountryBean(id++, "Iceland"),
        new CountryBean(id++, "India"),
        new CountryBean(id++, "Indonesia"),
        new CountryBean(id++, "Iran (Islamic Republic of)"),
        new CountryBean(id++, "Iraq"),
        new CountryBean(id++, "Ireland"),
        new CountryBean(id++, "Israel"),
        new CountryBean(id++, "Italy"),
        new CountryBean(id++, "Jamaica"),
        new CountryBean(id++, "Japan"),
        new CountryBean(id++, "Jordan"),
        new CountryBean(id++, "Kazakhstan"),
        new CountryBean(id++, "Kenya"),
        new CountryBean(id++, "Kiribati"),
        new CountryBean(id++, "Democratic People's Republic of Korea"),
        new CountryBean(id++, "Republic of Korea"),
        new CountryBean(id++, "Kuwait"),
        new CountryBean(id++, "Kyrgyzstan"),
        new CountryBean(id++, "Lao People's Democratic Republic"),
        new CountryBean(id++, "Latvia"),
        new CountryBean(id++, "Lebanon"),
        new CountryBean(id++, "Lesotho"),
        new CountryBean(id++, "Liberia"),
        new CountryBean(id++, "Libya"),
        new CountryBean(id++, "Liechtenstein"),
        new CountryBean(id++, "Lithuania"),
        new CountryBean(id++, "Luxembourg"),
        new CountryBean(id++, "The former Yugoslav Republic of Macedonia"),
        new CountryBean(id++, "Madagascar"),
        new CountryBean(id++, "Malawi"),
        new CountryBean(id++, "Malaysia"),
        new CountryBean(id++, "Maldives"),
        new CountryBean(id++, "Mali"),
        new CountryBean(id++, "Malta"),
        new CountryBean(id++, "Marshall Islands"),
        new CountryBean(id++, "Mauritania"),
        new CountryBean(id++, "Mauritius"),
        new CountryBean(id++, "Mexico"),
        new CountryBean(id++, "Micronesia (Federated States of)"),
        new CountryBean(id++, "Republic of Moldova"),
        new CountryBean(id++, "Monaco"),
        new CountryBean(id++, "Mongolia"),
        new CountryBean(id++, "Montenegro"),
        new CountryBean(id++, "Morocco"),
        new CountryBean(id++, "Mozambique"),
        new CountryBean(id++, "Myanmar"),
        new CountryBean(id++, "Namibia"),
        new CountryBean(id++, "Nauru"),
        new CountryBean(id++, "Nepal"),
        new CountryBean(id++, "Netherlands"),
        new CountryBean(id++, "New Zealand"),
        new CountryBean(id++, "Nicaragua"),
        new CountryBean(id++, "Niger"),
        new CountryBean(id++, "Nigeria"),
        new CountryBean(id++, "Norway"),
        new CountryBean(id++, "Oman"),
        new CountryBean(id++, "Pakistan"),
        new CountryBean(id++, "Palau"),
        new CountryBean(id++, "Panama"),
        new CountryBean(id++, "Papua New Guinea"),
        new CountryBean(id++, "Paraguay"),
        new CountryBean(id++, "Peru"),
        new CountryBean(id++, "Philippines"),
        new CountryBean(id++, "Poland"),
        new CountryBean(id++, "Portugal"),
        new CountryBean(id++, "Qatar"),
        new CountryBean(id++, "Romania"),
        new CountryBean(id++, "Russian Federation"),
        new CountryBean(id++, "Rwanda"),
        new CountryBean(id++, "Saint Kitts and Nevis"),
        new CountryBean(id++, "Saint Lucia"),
        new CountryBean(id++, "Saint Vincent and the Grenadines"),
        new CountryBean(id++, "Samoa"),
        new CountryBean(id++, "San Marino"),
        new CountryBean(id++, "Sao Tome and Principe"),
        new CountryBean(id++, "Saudi Arabia"),
        new CountryBean(id++, "Senegal"),
        new CountryBean(id++, "Serbia"),
        new CountryBean(id++, "Seychelles"),
        new CountryBean(id++, "Sierra Leone"),
        new CountryBean(id++, "Singapore"),
        new CountryBean(id++, "Slovakia"),
        new CountryBean(id++, "Slovenia"),
        new CountryBean(id++, "Solomon Islands"),
        new CountryBean(id++, "Somalia"),
        new CountryBean(id++, "South Africa"),
        new CountryBean(id++, "South Sudan"),
        new CountryBean(id++, "Spain"),
        new CountryBean(id++, "Sri Lanka"),
        new CountryBean(id++, "Sudan"),
        new CountryBean(id++, "Suriname"),
        new CountryBean(id++, "Swaziland"),
        new CountryBean(id++, "Sweden"),
        new CountryBean(id++, "Switzerland"),
        new CountryBean(id++, "Syrian Arab Republic"),
        new CountryBean(id++, "Tajikistan"),
        new CountryBean(id++, "United Republic of Tanzania"),
        new CountryBean(id++, "Thailand"),
        new CountryBean(id++, "Timor-Leste"),
        new CountryBean(id++, "Togo"),
        new CountryBean(id++, "Tonga"),
        new CountryBean(id++, "Trinidad and Tobago"),
        new CountryBean(id++, "Tunisia"),
        new CountryBean(id++, "Turkey"),
        new CountryBean(id++, "Turkmenistan"),
        new CountryBean(id++, "Tuvalu"),
        new CountryBean(id++, "Uganda"),
        new CountryBean(id++, "Ukraine"),
        new CountryBean(id++, "United Arab Emirates"),
        new CountryBean(id++, "United Kingdom of Great Britain and Northern Ireland"),
        new CountryBean(id++, "United States of America"),
        new CountryBean(id++, "Uruguay"),
        new CountryBean(id++, "Uzbekistan"),
        new CountryBean(id++, "Vanuatu"),
        new CountryBean(id++, "Venezuela (Bolivarian Republic of)"),
        new CountryBean(id++, "Viet Nam"),
        new CountryBean(id++, "Yemen"),
        new CountryBean(id++, "Zambia"),
        new CountryBean(id++, "Zimbabwe")
        ));
    // @formatter:on
  }
*/

}
