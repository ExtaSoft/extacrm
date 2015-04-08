#######################################################################################################################
# Заполняем новое поле "причина отмены продажи"

UPDATE SALE s
SET
  CANCEL_REASON = 'VENDOR_REJECTED'
WHERE
  s.STATUS = 'CANCELED'
  AND (s.RESULT = 'VENDOR_REJECTED'
       OR s.RESULT IS NULL);

UPDATE SALE s
SET
  CANCEL_REASON = 'PERCENT_TO_BIG'
WHERE
  s.STATUS = 'CANCELED'
  AND s.RESULT = 'CLIENT_REJECTED';

ALTER TABLE SALE DROP COLUMN RESULT;


#######################################################################################################################
# Упразняем раздел лидов

# удалить сущ. права доступа
DELETE a.* FROM ACCESS_PERMISSION_ACTION a
  JOIN ACCESS_PERMISSION p ON a.ExtaPermission_ID = p.ID
WHERE
  p.DOMAIN IN ('leads/qualified', 'leads/closed');

DELETE FROM ACCESS_PERMISSION
WHERE
  DOMAIN IN ('leads/qualified', 'leads/closed');

# обновить раздел новых лидов
UPDATE ACCESS_PERMISSION
SET
  DOMAIN = 'sales/leads'
WHERE DOMAIN = 'leads/new';
