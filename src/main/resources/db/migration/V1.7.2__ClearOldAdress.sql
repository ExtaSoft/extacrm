######################################################################################################################
# Удаляем все старые адреса


# Прежде всего надо удалить поля в таблицах наследуемых от Contact
# Что удаляем: region, city, POST_INDEX, STREET_BLD, REALTY_KIND, PERIOD_OF_RESIDENCE
# Откуда удаляем: EMPLOYEE, SALE_POINT
# PERSON: ACT_REGION,ACT_CITY,ACT_POST_INDEX,ACT_STREET_BLD,ACT_REALTY_KIND,ACT_PERIOD_OF_RESIDENCE
# LEGAL_ENTITY: PST_REGION,PST_CITY,PST_POST_INDEX,PST_STREET_BLD,PST_REALTY_KIND,PST_PERIOD_OF_RESIDENCE


# Увеличить размер полей: LEAD.REGION, LEAD.CONTACT_REGION, SALE.REGION, COMPANY.region, COMPANY.city
#
#
#
# Удаляемые свойства: regAddress,actualAddress

