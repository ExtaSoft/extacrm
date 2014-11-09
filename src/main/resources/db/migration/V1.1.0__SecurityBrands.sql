# Всем компаниям назначаем категорию
INSERT INTO COMPANY_CATEGORY
(COMPANY_ID,
 CATEGORY)
  (SELECT
     c.ID,
     'Дилер'
   FROM COMPANY c
   WHERE c.NAME NOT LIKE '%Альфа Страхование%'
         AND c.NAME NOT LIKE '%Альфа-Банк%'
         AND c.NAME NOT LIKE '%ОТП Банк%'
         AND c.NAME NOT LIKE '%ХоумКредит%'
         AND c.NAME NOT LIKE '%CETELEM%'
  );
INSERT INTO COMPANY_CATEGORY
(COMPANY_ID,
 CATEGORY)
  (SELECT
     c.ID,
     'Страховая компания'
   FROM COMPANY c
   WHERE c.NAME LIKE '%Альфа Страхование%'
  );
INSERT INTO COMPANY_CATEGORY
(COMPANY_ID,
 CATEGORY)
  (SELECT
     c.ID,
     'Банк'
   FROM COMPANY c
   WHERE c.NAME LIKE '%Альфа-Банк%'
         OR c.NAME LIKE '%ОТП Банк%'
         OR c.NAME LIKE '%ХоумКредит%'
         OR c.NAME LIKE '%CETELEM%'
  );

#
