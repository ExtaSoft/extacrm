USE `extacrm`;

DROP PROCEDURE IF EXISTS DropIndexOfSchema;
DELIMITER $$
CREATE PROCEDURE DropIndexOfSchema(IN refschema VARCHAR(64))
  BEGIN
/**
 * @Created By: Vicky Thakor
 * @Created On: 6th March, 2014
 * This will delete all Indexes of table except Primary Key
        *
        * @Test: CALL usp_DropIndexOfTable('user_master');
 */
    DECLARE v_finished INTEGER DEFAULT 0;
    DECLARE v_index VARCHAR(64) DEFAULT "";
    DECLARE v_table VARCHAR(64) DEFAULT "";
    DECLARE index_cursor CURSOR FOR
/* In below query remove TABLE_NAME criteria to remove all indexes */
      SELECT DISTINCT
        INDEX_NAME,
        TABLE_NAME
      FROM information_schema.STATISTICS
      WHERE TABLE_SCHEMA = refschema
            AND INDEX_NAME != 'PRIMARY'
            AND INDEX_NAME != 'NUM'
            AND TABLE_NAME <> 'schema_version'
            AND TABLE_NAME NOT LIKE 'ACT_%';

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished = 1;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
-- ERROR
      SHOW ERRORS LIMIT 1;
      ROLLBACK;
    END;

    DECLARE EXIT HANDLER FOR SQLWARNING
    BEGIN
-- WARNING
      SHOW WARNINGS LIMIT 1;
      ROLLBACK;
    END;

    OPEN index_cursor;
    get_index: LOOP
      FETCH index_cursor
      INTO v_index, v_table;

      IF v_finished = 1
      THEN
        LEAVE get_index;
      END IF;

      SET @v_query = CONCAT('DROP INDEX `', v_index, '` ON `', v_table, '`');
      PREPARE stmt FROM @v_query;
      EXECUTE stmt;
      DEALLOCATE PREPARE stmt;
    END LOOP get_index;
    CLOSE index_cursor;
  END$$
DELIMITER ;

CALL DropIndexOfSchema('extacrm');

DROP PROCEDURE DropIndexOfSchema;



