CREATE TABLE employees
(
    id         SERIAL PRIMARY KEY,
    first_name CHAR(250) NOT NULL,
    last_name  CHAR(250) NOT NULL,
    email      CHAR(250) DEFAULT NULL
);
