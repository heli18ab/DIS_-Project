DROP DATABASE IF EXISTS dis_exam_henriette_lillehagen ;
CREATE DATABASE dis_exam_henriette_lillehagen;
Use dis_exam_henriette_lillehagen;


CREATE TABLE IF NOT EXISTS customers (
  customer_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(40) NOT NULL,
  username VARCHAR(40) NOT NULL,
password VARCHAR(200) NOT NULL,
typeCustomer INT NOT NULL,
  PRIMARY KEY (customer_id)
);



CREATE TABLE IF NOT EXISTS accounts (
  account_id INT NOT NULL AUTO_INCREMENT,
  customer_id INT NOT NULL,
  balance DOUBLE NOT NULL,
  created_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (account_id),
  FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);


INSERT INTO customers(name, username,password, typeCustomer) VALUES ("user", "user", "59F26B69F7B3A71F2530F6B92B7FB9F7236D5F7111D91CD0053044ED828F126F", "1");
INSERT INTO customers(name, username,password, typeCustomer) VALUES ("teller", "teller", "59F26B69F7B3A71F2530F6B92B7FB9F7236D5F7111D91CD0053044ED828F126F", "2");
INSERT INTO accounts(customer_id, balance) VALUES (1, 1000);
INSERT INTO accounts(customer_id, balance) VALUES (1, 200);