#######################################################################
# Разделяем клиентов в страховке на юр. лиц и физ. лиц
#######################################################################
# Юр. лица
UPDATE INSURANCE i
SET i.CLIENT_LE = i.CLIENT_ID
WHERE EXISTS(SELECT
               ID
             FROM LEGAL_ENTITY l
             WHERE l.ID = i.CLIENT_ID);
# Физ лица
UPDATE INSURANCE i
SET i.CLIENT_PP = i.CLIENT_ID
WHERE EXISTS(SELECT
               ID
             FROM PERSON p
             WHERE p.ID = i.CLIENT_ID);

# Удаляем старое поле
ALTER TABLE INSURANCE DROP FOREIGN KEY FK_INSURANCE_CLIENT_ID;
ALTER TABLE INSURANCE
  DROP COLUMN CLIENT_ID,
  DROP INDEX IX_INSURANCE_FK_INSURANCE_CLIENT_ID,
  DROP INDEX FK_INSURANCE_CLIENT_ID;
