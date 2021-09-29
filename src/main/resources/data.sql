DROP TABLE IF EXISTS ACCOUNT;

CREATE TABLE ACCOUNT
(
    id        INT PRIMARY KEY,
    agencia   VARCHAR(4),
    conta     VARCHAR(10),
    saldo     NUMBER(10),
    status    VARCHAR(250),
    resultado INTEGER(250) DEFAULT NULL
);
