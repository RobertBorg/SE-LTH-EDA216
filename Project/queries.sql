-- Delete theg tables if they exist. Set foreign_key_checks = 0 to
-- disable foreign key checks, so the tables may be dropped in
-- arbitrary order.

SET foreign_key_checks = 0;

DROP TABLE IF EXISTS RawMaterials;

DROP TABLE IF EXISTS Recipes;


DROP TABLE IF EXISTS Pallets;


DROP TABLE IF EXISTS Orders;


DROP TABLE IF EXISTS Customers;


DROP TABLE IF EXISTS Ingredients;

DROP TABLE IF EXISTS Shipments;

-- Create the tables.
create table RawMaterials (
name char(40) PRIMARY KEY NOT NULL
);

create table Recipes (
name char(100) PRIMARY KEY NOT NULL
);

create table Ingredients (
quantity int NOT NULL,
rawMaterialName char(40) NOT NULL,
recipeName char(100) NOT NULL,
FOREIGN KEY(rawMaterialName)
  REFERENCES RawMaterials(name),
FOREIGN KEY(recipeName)
  REFERENCES Recipes(name),
CONSTRAINT uniqueNames UNIQUE (rawMaterialName,recipeName)
);

create table Pallets (
id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
orderId int,
creationDateAndTime datetime NOT NULL,
recipeName char(100) NOT NULL,
shipmentId int,
FOREIGN KEY (orderId)
  REFERENCES Orders(id),
FOREIGN KEY (recipeName)
  REFERENCES Recipes(name),
FOREIGN KEY (shipmentId)
  REFERENCES Shipments(id)
);

create table Orders (
id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
requestedDeliveryDate date NOT NULL,
customerName char(100) NOT NULL,
FOREIGN KEY (customerName)
  REFERENCES Customers(name)
);

create table Customers (
name char(100) PRIMARY KEY NOT NULL UNIQUE,
address char(100) NOT NULL
);

create table Shipments (
id int PRIMARY KEY NOT NULL AUTO_INCREMENT
);

SET foreign_key_checks = 1;

-- Insert data into the tables.