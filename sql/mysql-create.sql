DROP TABLE item;
CREATE TABLE item (
    id INT NOT NULL AUTO_INCREMENT,
    timestamp BIGINT NOT NULL,
    bitfield VARBINARY(128) NOT NULL,
    code VARCHAR(4) NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE gold;
CREATE TABLE gold (
   balance BIGINT NOT NULL
);

INSERT INTO gold(balance) VALUES(0);
