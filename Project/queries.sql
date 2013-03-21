-- Delete the tables if they exist. Set foreign_key_checks = 0 to
-- disable foreign key checks, so the tables may be dropped in
-- arbitrary order.

SET foreign_key_checks = 0;

DROP TABLE IF EXISTS RawMaterials;

DROP TABLE IF EXISTS Recipes;


DROP TABLE IF EXISTS Pallets;


DROP TABLE IF EXISTS Orders;


DROP TABLE IF EXISTS Customers;


DROP TABLE IF EXISTS Ingredients;

-- Create the tables.
create table RawMaterials (
name char(20) PRIMARY KEY NOT NULL
);

create table Recipes (
name char(20) PRIMARY KEY NOT NULL
);

create table Ingredients (
quantity int NOT NULL,
rawMaterialName char(20) NOT NULL,
recipeName char(20) NOT NULL,
FOREIGN KEY(rawMaterialName)
  REFERENCES RawMaterials(name),
FOREIGN KEY(recipeName)
  REFERENCES Recipes(name),
CONSTRAINT uniqueNames UNIQUE (rawMaterialName,recipeName)
);

SET foreign_key_checks = 1;

-- Insert data into the tables.