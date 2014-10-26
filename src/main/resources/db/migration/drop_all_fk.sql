USE `extacrm`;

DELIMITER ;

/* DROP_FOREIGN_KEY */
DROP PROCEDURE IF EXISTS DropForeignKey;

DELIMITER //

CREATE PROCEDURE DropForeignKey(
  IN param_schema          VARCHAR(100),
  IN param_table_name      VARCHAR(100),
  IN param_constraint_name VARCHAR(100)
)

  BEGIN
    IF EXISTS(
        SELECT
          NULL
        FROM information_schema.TABLE_CONSTRAINTS
        WHERE CONSTRAINT_NAME = param_constraint_name AND TABLE_NAME = param_table_name AND TABLE_SCHEMA = param_schema
    )
    THEN
      SET @paramTable = param_table_name;

      SET @ParamConstraintName = param_constraint_name;
      SET @ParamSchema = param_schema;
/* Create the full statement to execute */
      SET @StatementToExecute = concat('ALTER TABLE `', @ParamSchema, '`.`', @paramTable, '` DROP FOREIGN KEY `',
                                       @ParamConstraintName, '` ');
/* Prepare and execute the statement that was built */
      PREPARE DynamicStatement FROM @StatementToExecute;
      EXECUTE DynamicStatement;
/* Cleanup the prepared statement */
      DEALLOCATE PREPARE DynamicStatement;

    END IF;
  END //

DELIMITER ;

DROP PROCEDURE IF EXISTS DropConstraints;
# create a handy dandy stored procedure
DELIMITER $$
CREATE PROCEDURE DropConstraints(refschema VARCHAR(64))
  BEGIN
    DECLARE exit_loop BOOLEAN;
    DECLARE tbl_name VARCHAR(64);
    DECLARE cnstr_name VARCHAR(64);

    DECLARE fks_cursor CURSOR FOR
      SELECT
        TABLE_NAME,
        CONSTRAINT_NAME
      FROM
        information_schema.KEY_COLUMN_USAGE
      WHERE
        REFERENCED_TABLE_SCHEMA = refschema
        AND REFERENCED_TABLE_NAME <> 'schema_version'
        AND REFERENCED_TABLE_NAME NOT LIKE 'ACT_%';

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET exit_loop = TRUE;

    OPEN fks_cursor;

    fks_cursor_loop: LOOP

      FETCH fks_cursor
      INTO tbl_name, cnstr_name;
      IF exit_loop
      THEN
        LEAVE fks_cursor_loop;
      END IF;

      CALL DropForeignKey(refschema, tbl_name, cnstr_name);

    END LOOP fks_cursor_loop;

    CLOSE fks_cursor;

  END$$
DELIMITER ;

CALL DropConstraints('extacrm');

DROP PROCEDURE DropConstraints;
DROP PROCEDURE DropForeignKey;

