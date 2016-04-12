#######################################################################################################################
# Заполняем Город, тип и т.п. для малых населенных пунктов

UPDATE ADDRESS
SET
  CITY_WITH_TYPE = SETTLEMENT_WITH_TYPE,
  CITY           = SETTLEMENT,
  CITY_TYPE      = SETTLEMENT_TYPE,
  CITY_TYPE_FULL = SETTLEMENT_TYPE_FULL
WHERE
  SETTLEMENT_WITH_TYPE IS NOT NULL
  AND CITY_WITH_TYPE IS NULL;
