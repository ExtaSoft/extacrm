#######################################################################################################################
# Увеличиваем размер полей кодов торговой точки

#  - Код Альфа банка
ALTER TABLE SALE_POINT
CHANGE COLUMN ALPHA_CODE ALPHA_CODE VARCHAR(50) NULL DEFAULT NULL;

#  - Код Экстрим Ассистанс
ALTER TABLE SALE_POINT
CHANGE COLUMN EXTA_CODE EXTA_CODE VARCHAR(50) NULL DEFAULT NULL;

#  - Код HomeCredit Банка
ALTER TABLE SALE_POINT
CHANGE COLUMN HOME_CODE HOME_CODE VARCHAR(50) NULL DEFAULT NULL;

#  - Код Банка СЕТЕЛЕМ
ALTER TABLE SALE_POINT
CHANGE COLUMN SETELEM_CODE SETELEM_CODE VARCHAR(50) NULL DEFAULT NULL;

# Статусы для продуктов в продаже
#  - На рассмотрении
UPDATE PRODUCT_IN_SALE p JOIN SALE s ON p.SALE_ID = s.ID
SET
  STATE = 0
WHERE s.STATUS = 'NEW';
#  - Одобренные
UPDATE PRODUCT_IN_SALE p JOIN SALE s ON p.SALE_ID = s.ID
SET
  STATE = 1
WHERE s.STATUS = 'FINISHED';
#  - Отвергнутые
UPDATE PRODUCT_IN_SALE p JOIN SALE s ON p.SALE_ID = s.ID
SET
  STATE = 2
WHERE s.STATUS = 'CANCELED';
