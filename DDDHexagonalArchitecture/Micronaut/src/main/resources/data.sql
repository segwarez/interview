DROP TABLE IF EXISTS employees;

CREATE TABLE employees (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  email VARCHAR(250) DEFAULT NULL
);

INSERT INTO employees(first_name, last_name, email) VALUES
  ('Lokesh', 'Gupta', 'howtodoinjava@gmail.com'),
  ('Alex', 'Kolenchiskey', 'abc@gmail.com'),
  ('David', 'Kameron', 'titanic@gmail.com');