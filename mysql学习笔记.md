## mysql命令

### select

- 选择一个列	

  ```mysql
  SELECT columnName FROM tableName;
  ```

- 选择多个列    

  ```mysql
  SELECT columnName1, columnName2 FROM tableName;
  ```

- 加 DISTINCT 在 SELECT 后面，可以使行的值只出现一遍

  ```mysql
  SELECT DISTINCT columnName1, columnName2 FROM tableName;
  ```

- 可以加 LIMIT 子句限制输出多少行

  ```mysql
  SELECT columnName FROM tableName LIMIT 5;
  ```

  ```mysql
  #从第五行开始输出，输出五行；行数从0算起。
  SELECT columnName FROM tableName LIMIT 5, 5;
  ```

  