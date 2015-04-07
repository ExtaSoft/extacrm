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


