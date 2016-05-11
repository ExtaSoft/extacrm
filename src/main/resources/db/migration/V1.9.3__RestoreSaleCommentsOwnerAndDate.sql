#######################################################################################################################
# Восстанавливаем владельца и дату комментария после квалификации (#CRM-379)

UPDATE SALE_COMMENT sc
  JOIN
  SALE s ON sc.OWNER_ID = s.ID
  JOIN
  LEAD_COMMENT lc ON lc.OWNER_ID = s.LEAD_ID
                     AND lc.TEXT = sc.TEXT
SET
  sc.CREATED_BY = lc.CREATED_BY,
  sc.CREATED_AT = lc.CREATED_AT
WHERE
  sc.CREATED_BY <> lc.CREATED_BY;