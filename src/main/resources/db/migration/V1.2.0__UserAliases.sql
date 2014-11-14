# Запрос для выявления дублей
# SELECT
#   *,
#   count(*)
# FROM
#   extacrm.user_profile
# GROUP BY login
# HAVING COUNT(*) > 1;

# Чистим базу
DELETE
FROM
  USER_GROUP_LINK
WHERE USER_ID = '81FF5A8B-CF10-4E85-9960-219B24E55B33';
DELETE
FROM
  USER_PROFILE
WHERE ID = '81FF5A8B-CF10-4E85-9960-219B24E55B33';

# Добавляем текущие логины пользователей в таблицу псевдонимов
INSERT INTO USER_LOGIN_ALIAS
(USER_PROFILE_ID,
 ALIAS)
  (SELECT
     u.ID,
     u.login
   FROM USER_PROFILE u
  );
