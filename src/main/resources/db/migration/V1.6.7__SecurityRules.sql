#######################################################################################################################
# Заполняем упущенные правила безопасности для торговых точек и юр.лиц и сотрудников

INSERT INTO SECURITY_RULE_COMPANY
(SECURITY_RULE_ID,
 COMPANY_ID)
  SELECT
    s.SECURITY_RULE_ID,
    s.COMPANY_ID
  FROM
    SALE_POINT s
  WHERE
    s.SECURITY_RULE_ID IS NOT NULL
    AND s.COMPANY_ID NOT IN (SELECT c.COMPANY_ID
                             FROM
                               SECURITY_RULE_COMPANY c
                             WHERE
                               s.SECURITY_RULE_ID = c.SECURITY_RULE_ID);

INSERT INTO SECURITY_RULE_COMPANY
(SECURITY_RULE_ID,
 COMPANY_ID)
  SELECT
    cl.SECURITY_RULE_ID,
    s.COMPANY_ID
  FROM
    LEGAL_ENTITY s JOIN CLIENT cl ON s.ID = cl.ID
  WHERE
    cl.SECURITY_RULE_ID IS NOT NULL
    AND s.COMPANY_ID NOT IN (SELECT c.COMPANY_ID
                             FROM
                               SECURITY_RULE_COMPANY c
                             WHERE
                               cl.SECURITY_RULE_ID = c.SECURITY_RULE_ID);

INSERT INTO SECURITY_RULE_COMPANY
(SECURITY_RULE_ID,
 COMPANY_ID)
  SELECT
    s.SECURITY_RULE_ID,
    s.COMPANY_ID
  FROM
    EMPLOYEE s
  WHERE
    s.SECURITY_RULE_ID IS NOT NULL
    AND s.COMPANY_ID NOT IN (SELECT c.COMPANY_ID
                             FROM
                               SECURITY_RULE_COMPANY c
                             WHERE
                               s.SECURITY_RULE_ID = c.SECURITY_RULE_ID);