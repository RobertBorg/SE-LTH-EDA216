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
name char(40) PRIMARY KEY,
quantity int NOT NULL
);

create table Recipes (
name char(100) PRIMARY KEY
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
id int PRIMARY KEY AUTO_INCREMENT,
orderId int,
creationDateAndTime datetime,
recipeName char(100) NOT NULL,
shipmentId int,
isBlocked boolean,
FOREIGN KEY (orderId)
  REFERENCES Orders(id),
FOREIGN KEY (recipeName)
  REFERENCES Recipes(name),
FOREIGN KEY (shipmentId)
  REFERENCES Shipments(id)
);

create table Orders (
id int PRIMARY KEY AUTO_INCREMENT,
requestedDeliveryDate date NOT NULL,
customerName char(100) NOT NULL,
FOREIGN KEY (customerName)
  REFERENCES Customers(name)
);

create table Customers (
name char(100) PRIMARY KEY UNIQUE,
address char(100) NOT NULL
);

create table Shipments (
id int PRIMARY KEY AUTO_INCREMENT
);

SET foreign_key_checks = 1;

-- Insert data into the tables.

-- Recipes
Insert INTO Recipes VALUES ('Nut ring');
Insert INTO Recipes VALUES ('Nut cookie');
Insert INTO Recipes VALUES ('Amneris');
Insert INTO Recipes VALUES ('Tango');
Insert INTO Recipes VALUES ('Almond delight');
Insert INTO Recipes VALUES ('Berliner');

-- Raw materials
INSERT INTO RawMaterials VALUES ('Butter', 10000);
INSERT INTO RawMaterials VALUES ('Icing sugar', 10000);
INSERT INTO RawMaterials VALUES ('Roasted, chopped nuts', 10000);
INSERT INTO RawMaterials VALUES ('Fine-ground nuts', 10000);
INSERT INTO RawMaterials VALUES ('Bread crumbs', 10000);
INSERT INTO RawMaterials VALUES ('Sugar', 10000);
INSERT INTO RawMaterials VALUES ('Egg whites', 10000);
INSERT INTO RawMaterials VALUES ('Chocolate', 10000);
INSERT INTO RawMaterials VALUES ('Marzipan', 10000);
INSERT INTO RawMaterials VALUES ('Eggs', 10000);
INSERT INTO RawMaterials VALUES ('Potato starch', 10000);
INSERT INTO RawMaterials VALUES ('Wheat flour', 10000);
INSERT INTO RawMaterials VALUES ('Flour', 10000);
INSERT INTO RawMaterials VALUES ('Sodium bicarbonate', 10000);
INSERT INTO RawMaterials VALUES ('Vanilla', 10000);
INSERT INTO RawMaterials VALUES ('Chopped almonds', 10000);
INSERT INTO RawMaterials VALUES ('Cinnamon', 10000);
INSERT INTO RawMaterials VALUES ('Vanilla sugar', 10000);
INSERT INTO RawMaterials VALUES ('Ground, roasted nuts', 10000);

-- Nut ring
INSERT INTO Ingredients VALUES (450,'Flour','Nut ring');
INSERT INTO Ingredients VALUES (450,'Butter','Nut ring');
INSERT INTO Ingredients VALUES (190,'Icing sugar','Nut ring');
INSERT INTO Ingredients VALUES (225,'Roasted, chopped nuts','Nut ring');

-- Nut cookie
INSERT INTO Ingredients VALUES (750,'Fine-ground nuts','Nut cookie');
INSERT INTO Ingredients VALUES (625,'Ground, roasted nuts','Nut cookie');
INSERT INTO Ingredients VALUES (125,'Bread crumbs','Nut cookie');
INSERT INTO Ingredients VALUES (375,'Sugar','Nut cookie');
INSERT INTO Ingredients VALUES (350,'Egg whites','Nut cookie');
INSERT INTO Ingredients VALUES (50,'Chocolate','Nut cookie');

-- Amneris
INSERT INTO Ingredients VALUES (750,'Marzipan','Amneris');
INSERT INTO Ingredients VALUES (250,'Butter','Amneris');
INSERT INTO Ingredients VALUES (250,'Eggs','Amneris');
INSERT INTO Ingredients VALUES (25,'Potato starch','Amneris');
INSERT INTO Ingredients VALUES (25,'Wheat flour','Amneris');

-- Tango
INSERT INTO Ingredients VALUES (200,'Butter','Tango');
INSERT INTO Ingredients VALUES (250,'Sugar','Tango');
INSERT INTO Ingredients VALUES (300,'Flour','Tango');
INSERT INTO Ingredients VALUES (4,'Sodium bicarbonate','Tango');
INSERT INTO Ingredients VALUES (2,'Vanilla','Tango');

-- Almond delight
INSERT INTO Ingredients VALUES (400,'Butter','Almond delight');
INSERT INTO Ingredients VALUES (270,'Sugar','Almond delight');
INSERT INTO Ingredients VALUES (279,'Chopped almonds','Almond delight');
INSERT INTO Ingredients VALUES (400,'Flour','Almond delight');
INSERT INTO Ingredients VALUES (10,'Cinnamon','Almond delight');

-- Berliner
INSERT INTO Ingredients VALUES (350,'Flour','Berliner');
INSERT INTO Ingredients VALUES (250,'Butter','Berliner');
INSERT INTO Ingredients VALUES (100,'Icing sugar','Berliner');
INSERT INTO Ingredients VALUES (50,'Eggs','Berliner');
INSERT INTO Ingredients VALUES (5,'Vanilla sugar','Berliner');
INSERT INTO Ingredients VALUES (50,'Chocolate','Berliner');

-- Customers
INSERT into customers VALUES ('Finkakor AB', 'Helsingborg');
INSERT into customers VALUES ('Småbröd AB', 'Malmö');
INSERT into customers VALUES ('Kaffebröd AB', 'Landskrona');
INSERT into customers VALUES ('Bjudkakor AB', 'Ystad');
INSERT into customers VALUES ('Kalaskakor AB', 'Trelleborg');
INSERT into customers VALUES ('Partykakor AB', 'Kristianstad');
INSERT into customers VALUES ('Gästkakor AB', 'Hässleholm');
INSERT into customers VALUES ('Skånekakor AB', 'Perstorp');

-- Orders
INSERT INTO Orders VALUES (1, '2013-04-15', 'Finkakor AB' );
Insert INTO Pallets VALUES (1, 1, NULL, 'Nut ring', NULL, FALSE);

INSERT INTO Orders VALUES (2, '2013-04-16', 'Småbröd AB' );
Insert INTO Pallets VALUES (2, 2, NULL, 'Amneris', NULL, FALSE);

INSERT INTO Orders VALUES (3, '2013-05-06', 'Kaffebröd AB' );
Insert INTO Pallets VALUES (3, 3, NULL, 'Nut cookie', NULL, FALSE);

INSERT INTO Orders VALUES (4, '2013-05-07', 'Bjudkakor AB' );
Insert INTO Pallets VALUES (4, 4, NULL, 'Tango', NULL, FALSE);

INSERT INTO Orders VALUES (5, '2013-05-01', 'Kalaskakor AB' );
Insert INTO Pallets VALUES (5, 5, NULL, 'Berliner', NULL, FALSE);