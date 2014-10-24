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
ALTER TABLE INSURANCE DROP COLUMN CLIENT_ID;

#######################################################################
# Обновляем компании после удаления наследования
#######################################################################
UPDATE COMPANY d
  INNER JOIN CONTACT c ON c.ID = d.ID
SET
  d.CITY             = c.CITY,
  d.CREATED_AT       = c.CREATED_AT,
  d.CREATED_BY       = c.CREATED_BY,
  d.EMAIL            = c.EMAIL,
  d.MODIFIED_AT      = c.MODIFIED_AT,
  d.MODIFIED_BY      = c.MODIFIED_BY,
  d.NAME             = c.NAME,
  d.REGION           = c.REGION,
  d.VERSION          = c.VERSION,
  d.WWW              = c.WWW,
  d.SECURITY_RULE_ID = c.SECURITY_RULE_ID;

#######################################################################
# Обновляем Торговые точки после удаления наследования
#######################################################################
UPDATE SALE_POINT s
  INNER JOIN CONTACT c ON c.ID = s.ID
SET
  s.CREATED_AT          = c.CREATED_AT,
  s.CREATED_BY          = c.CREATED_BY,
  s.EMAIL               = c.EMAIL,
  s.MODIFIED_AT         = c.MODIFIED_AT,
  s.MODIFIED_BY         = c.MODIFIED_BY,
  s.NAME                = c.NAME,
  s.CELL_PHONE          = c.CELL_PHONE,
  s.VERSION             = c.VERSION,
  s.WWW                 = c.WWW,
  s.CITY                = c.CITY,
  s.PERIOD_OF_RESIDENCE = c.PERIOD_OF_RESIDENCE,
  s.POST_INDEX          = c.POST_INDEX,
  s.REALTY_KIND         = c.REALTY_KIND,
  s.REGION              = c.REGION,
  s.STREET_BLD          = c.STREET_BLD,
  s.SECURITY_RULE_ID    = c.SECURITY_RULE_ID;

#######################################################################
# Обновляем Юр. лица после удаления наследования
#######################################################################
UPDATE LEGAL_ENTITY l
  INNER JOIN CONTACT c ON c.ID = l.ID
SET
  l.CREATED_AT          = c.CREATED_AT,
  l.CREATED_BY          = c.CREATED_BY,
  l.EMAIL               = c.EMAIL,
  l.MODIFIED_AT         = c.MODIFIED_AT,
  l.MODIFIED_BY         = c.MODIFIED_BY,
  l.NAME                = c.NAME,
  l.CELL_PHONE          = c.CELL_PHONE,
  l.VERSION             = c.VERSION,
  l.WWW                 = c.WWW,
  l.CITY                = c.CITY,
  l.PERIOD_OF_RESIDENCE = c.PERIOD_OF_RESIDENCE,
  l.POST_INDEX          = c.POST_INDEX,
  l.REALTY_KIND         = c.REALTY_KIND,
  l.REGION              = c.REGION,
  l.STREET_BLD          = c.STREET_BLD,
  l.SECURITY_RULE_ID    = c.SECURITY_RULE_ID;

#######################################################################
# Обновляем Физ. лица после удаления наследования
#######################################################################
UPDATE PERSON p
  INNER JOIN CONTACT c ON c.ID = p.ID
SET
  p.CREATED_AT          = c.CREATED_AT,
  p.CREATED_BY          = c.CREATED_BY,
  p.EMAIL               = c.EMAIL,
  p.MODIFIED_AT         = c.MODIFIED_AT,
  p.MODIFIED_BY         = c.MODIFIED_BY,
  p.NAME                = c.NAME,
  p.CELL_PHONE          = c.CELL_PHONE,
  p.VERSION             = c.VERSION,
  p.WWW                 = c.WWW,
  p.CITY                = c.CITY,
  p.PERIOD_OF_RESIDENCE = c.PERIOD_OF_RESIDENCE,
  p.POST_INDEX          = c.POST_INDEX,
  p.REALTY_KIND         = c.REALTY_KIND,
  p.REGION              = c.REGION,
  p.STREET_BLD          = c.STREET_BLD,
  p.SECURITY_RULE_ID    = c.SECURITY_RULE_ID;


#######################################################################
# Забираем часть Физ. лиц в сотрудники (оставляем только клиентов)
# Сотрудник, если:
# * Является пользователем
# * Является сотрудником торговой точки
# * Является сотрудником юр.лица
# * Является сотрудником компании
# * Является владельцем компании
#######################################################################
INSERT INTO EMPLOYEE
(ID,
 BIRTH_PLACE,
 BIRTHDAY,
 CREATED_AT,
 CREATED_BY,
 EMAIL,
 HOME_PHONE,
 JOB_DEPARTMENT,
 JOB_POSITION,
 MODIFIED_AT,
 MODIFIED_BY,
 NAME,
 PASS_ISSUE_DATE,
 PASS_ISSUED_BY,
 PASS_ISSUED_BY_NUM,
 PASS_NUM,
 CELL_PHONE,
 VERSION,
 WORK_PHONE,
 WWW,
 CITY,
 PERIOD_OF_RESIDENCE,
 POST_INDEX,
 REALTY_KIND,
 REGION,
 STREET_BLD,
 SECURITY_RULE_ID)
  SELECT DISTINCT
    p.ID,
    p.BIRTH_PLACE,
    p.BIRTHDAY,
    p.CREATED_AT,
    p.CREATED_BY,
    p.EMAIL,
    p.HOME_PHONE,
    p.JOB_DEPARTMENT,
    p.JOB_POSITION,
    p.MODIFIED_AT,
    p.MODIFIED_BY,
    p.NAME,
    p.PASS_ISSUE_DATE,
    p.PASS_ISSUED_BY,
    p.PASS_ISSUED_BY_NUM,
    p.PASS_NUM,
    p.CELL_PHONE,
    p.VERSION,
    p.WORK_PHONE,
    p.WWW,
    p.CITY,
    p.PERIOD_OF_RESIDENCE,
    p.POST_INDEX,
    p.REALTY_KIND,
    p.REGION,
    p.STREET_BLD,
    p.SECURITY_RULE_ID
  FROM PERSON p
# * Является пользователем
    LEFT JOIN USER_PROFILE u ON p.ID = u.CONTACT_ID
# * Является сотрудником торговой точки
# * Является сотрудником компании
    LEFT JOIN CONTACT_EMPLOYEE ce ON ce.EMPLOYEE_ID = p.ID
# * Является владельцем компании
    LEFT JOIN COMPANY_OWNER co ON co.OWNER_ID = p.ID
# * Является сотрудником юр.лица
    LEFT JOIN LEGAL_ENTITY led ON led.DIRECTOR_ID = p.ID
    LEFT JOIN LEGAL_ENTITY lea ON lea.ACCOUNTANT_ID = p.ID
  WHERE u.ID IS NOT NULL
        OR ce.CONTACT_ID IS NOT NULL
        OR co.COMPANY_ID IS NOT NULL
        OR led.ID IS NOT NULL
        OR lea.ID IS NOT NULL;

#######################################################################
# Обновляем Сотрудников: проставляем торговую точку
#######################################################################
INSERT INTO EMPLOYEE_FILE
(ID,
 CREATED_AT,
 CREATED_BY,
 DESCRIPTION,
 FILEDATA,
 FILESIZE,
 MIMETYPE,
 MODIFIED_AT,
 MODIFIED_BY,
 `NAME`,
 OWNER_ID,
 VERSION)
  SELECT
    p.ID,
    p.CREATED_AT,
    p.CREATED_BY,
    p.DESCRIPTION,
    p.FILEDATA,
    p.FILESIZE,
    p.MIMETYPE,
    p.MODIFIED_AT,
    p.MODIFIED_BY,
    p.`NAME`,
    p.OWNER_ID,
    p.VERSION
  FROM PERSON_FILE p JOIN EMPLOYEE e ON p.OWNER_ID = e.ID;


#######################################################################
# Обновляем Сотрудников: проставляем торговую точку
#######################################################################
UPDATE EMPLOYEE e
# * Является сотрудником торговой точки
  JOIN CONTACT_EMPLOYEE ce ON ce.EMPLOYEE_ID = e.ID
  JOIN SALE_POINT s ON ce.CONTACT_ID = s.ID
SET
  e.SALE_POINT_ID = s.ID,
  e.COMPANY_ID    = s.COMPANY_ID;


#######################################################################
# Обновляем Сотрудников: проставляем Компанию (для сотрудников компании)
#######################################################################
UPDATE EMPLOYEE e
# * Является сотрудником компании
  JOIN CONTACT_EMPLOYEE ce ON ce.EMPLOYEE_ID = e.ID
  JOIN COMPANY s ON ce.CONTACT_ID = s.ID
SET
  e.COMPANY_ID = s.ID;


#######################################################################
# Обновляем Сотрудников: проставляем Компанию (для владельцев компании)
#######################################################################
UPDATE EMPLOYEE e
# * Является сотрудником компании
  JOIN COMPANY_OWNER co ON co.OWNER_ID = e.ID
SET
  e.COMPANY_ID = co.COMPANY_ID;

UPDATE EMPLOYEE e
SET
  e.JOB_POSITION = NULL;

#######################################################################
# Обновляем Сотрудников: проставляем Компанию и Юр. лицо (для Директоров)
#######################################################################
UPDATE EMPLOYEE e
# * Является директором юр.лица
  JOIN LEGAL_ENTITY l ON l.DIRECTOR_ID = e.ID
SET
  e.LEGAL_ENTITY_ID = l.ID,
  e.JOB_POSITION    = "Генеральный директор",
  e.COMPANY_ID      = l.COMPANY_ID;


#######################################################################
# Обновляем Сотрудников: проставляем Компанию и Юр. лицо (для Бехгалтеров)
#######################################################################
UPDATE EMPLOYEE e
# * Является бухгалтером юр.лица
  JOIN LEGAL_ENTITY l ON l.ACCOUNTANT_ID = e.ID
SET
  e.LEGAL_ENTITY_ID = l.ID,
  e.JOB_POSITION    = "Главный бухгалтер",
  e.COMPANY_ID      = l.COMPANY_ID;


#######################################################################
# Обновляем Сотрудников: у кого нет компании считаем своими сотрудниками
#######################################################################
UPDATE EMPLOYEE e
SET
  e.COMPANY_ID = "598EB4D6-BC0A-4A2F-B445-52BEF27AC873"
WHERE COMPANY_ID IS NULL;

#######################################################################
# Обновляем связь сотрудника и пользователя
#######################################################################
UPDATE USER_PROFILE u
SET
  u.EMPLOYEE_ID = u.CONTACT_ID;


#######################################################################
# Приводим схему в порядок, чистим
#######################################################################
SET foreign_key_checks = 0;

# Меняем сущ. таблици
ALTER TABLE A7_FORM CHANGE ID ID VARCHAR(50);
ALTER TABLE COMPANY CHANGE ID ID VARCHAR(50);
ALTER TABLE CONTACT_CODE CHANGE ID ID VARCHAR(50);
ALTER TABLE FORM_TRANSFER CHANGE ID ID VARCHAR(50);
ALTER TABLE INSURANCE CHANGE ID ID VARCHAR(50);
ALTER TABLE INSURANCE CHANGE IS_USED_MOTOR IS_USED_MOTOR TINYINT;
ALTER TABLE INSURANCE CHANGE MOTOR_MODEL MOTOR_MODEL VARCHAR(100);
ALTER TABLE LEAD CHANGE CONTACT_EMAIL CONTACT_EMAIL VARCHAR(50);
ALTER TABLE LEAD CHANGE CONTACT_NAME CONTACT_NAME VARCHAR(50);
ALTER TABLE LEAD CHANGE CONTACT_PHONE CONTACT_PHONE VARCHAR(20);
ALTER TABLE LEAD CHANGE CONTACT_REGION CONTACT_REGION VARCHAR(50);
ALTER TABLE LEAD CHANGE MOTOR_BRAND MOTOR_BRAND VARCHAR(50);
ALTER TABLE LEAD CHANGE MOTOR_MODEL MOTOR_MODEL VARCHAR(100);
ALTER TABLE LEAD CHANGE MOTOR_TYPE MOTOR_TYPE VARCHAR(50);
ALTER TABLE LEAD CHANGE POINT_OF_SALE POINT_OF_SALE VARCHAR(200);
ALTER TABLE LEAD CHANGE PROCESS_ID PROCESS_ID VARCHAR(64);
ALTER TABLE LEAD CHANGE REGION REGION VARCHAR(50);
ALTER TABLE LEAD CHANGE RESULT RESULT VARCHAR(30);
ALTER TABLE LEAD CHANGE STATUS STATUS VARCHAR(30);
ALTER TABLE MOTOR_BRAND CHANGE NAME NAME VARCHAR(50);
ALTER TABLE MOTOR_MODEL CHANGE BRAND BRAND VARCHAR(50);
ALTER TABLE MOTOR_MODEL CHANGE CODE CODE VARCHAR(50);
ALTER TABLE MOTOR_MODEL CHANGE NAME NAME VARCHAR(100);
ALTER TABLE MOTOR_MODEL CHANGE TYPE TYPE VARCHAR(50);
ALTER TABLE MOTOR_TYPE CHANGE NAME NAME VARCHAR(50);
ALTER TABLE PAY_ACCOUNT CHANGE ID ID VARCHAR(50);
ALTER TABLE PERSON CHANGE ID ID VARCHAR(50);
ALTER TABLE PERSON DROP JOB_DEPARTMENT;
ALTER TABLE PERSON DROP JOB_POSITION;
ALTER TABLE PERSON DROP PASS_REG_ADRESS;
ALTER TABLE POLICY CHANGE ID ID VARCHAR(50);
ALTER TABLE PROD_CREDIT_DOC CHANGE IS_REQUIRED IS_REQUIRED TINYINT;
ALTER TABLE PRODUCT CHANGE IS_ACTIVE IS_ACTIVE TINYINT;
ALTER TABLE SALE CHANGE MOTOR_BRAND MOTOR_BRAND VARCHAR(50);
ALTER TABLE SALE CHANGE MOTOR_MODEL MOTOR_MODEL VARCHAR(100);
ALTER TABLE SALE CHANGE MOTOR_TYPE MOTOR_TYPE VARCHAR(50);
ALTER TABLE SALE CHANGE PROCESS_ID PROCESS_ID VARCHAR(64);
ALTER TABLE SALE CHANGE REGION REGION VARCHAR(50);
ALTER TABLE SALE CHANGE RESULT RESULT VARCHAR(30);
ALTER TABLE USER_PROFILE CHANGE BLOCKED BLOCKED TINYINT;
ALTER TABLE USER_PROFILE CHANGE CHANGE_PASSWORD CHANGE_PASSWORD TINYINT;
ALTER TABLE USER_PROFILE DROP CONTACT_ID;
ALTER TABLE USER_PROFILE CHANGE ID ID VARCHAR(50);

# Удаляем лишние таблици
DROP TABLE CONTACT_EMPLOYEE;
DROP TABLE CONTACT;

#######################################################################
# Удаляем сотрудников из физ.лиц
#######################################################################
DELETE p FROM PERSON_FILE p JOIN EMPLOYEE_FILE e ON p.ID = e.ID;
DELETE p FROM PERSON p JOIN EMPLOYEE e ON p.ID = e.ID;

SET foreign_key_checks = 1;
