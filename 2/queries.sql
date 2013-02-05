-- Delete the tables if they exist. Set foreign_key_checks = 0 to
-- disable foreign key checks, so the tables may be dropped in
-- arbitrary order.

SET foreign_key_checks = 0;


DROP TABLE IF EXISTS Users;


DROP TABLE IF EXISTS Theaters;


DROP TABLE IF EXISTS Movies;


DROP TABLE IF EXISTS Reservations;


DROP TABLE IF EXISTS Performances;

-- Create the tables.
create table Users (
id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
username char(20) NOT NULL UNIQUE,
name char(20) NOT NULL,
phoneNumber char(20) NOT NULL,
address varchar(256)
);

create table Movies (
id int PRIMARY KEY NOT NULL AUTO_INCREMENT, 
name char(20) NOT NULL);

create table Theaters(
id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
name char(20) NOT NULL,
numberOfSeats int NOT NULL
);

create table Performances (
id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
movieId int NOT NULL,
theaterId int NOT NULL,
theDate DATE NOT NULL,
FOREIGN KEY(movieId)
  REFERENCES Movies(id),
FOREIGN KEY(theaterId)
  REFERENCES Theaters(id)
);

create table Reservations (
reservationNumber int PRIMARY KEY NOT NULL AUTO_INCREMENT,
performanceId int NOT NULL,
userId int NOT NULL,
FOREIGN KEY(performanceId)
  REFERENCES Performances(id),
FOREIGN KEY(userId)
  REFERENCES Users(id)
);

SET foreign_key_checks = 1;

-- Insert data into the tables.
insert into Users(username, name, phoneNumber, address) values('Sane', 'Christofer Heimonen', '0707283358', 'Spolegatan 15 22220 Lund');
insert into Users values('Rauban', 'Robert Borg', '0703213355', 'Trollgatan 14 34323 Lund');
insert into Users values('Troll', 'Andorz Trollheim', '0734384234', 'Sturevagen 123 34320 Lund');
insert into Users values('Apa', 'Thomas Svensson', '0703828399', 'Gatan 23 23222 Göteborg');
insert into Users values('Ond', 'Onde Man', '0482030222', 'HejaVagen 34 34322 Stockholm');
insert into Users values('Haha', 'Anna Gardhagen', '0731232890', 'Ankvägen 23 23212 Ankeborg');

insert into Movies values('King Kong');
insert into Movies values('Enter the Matrix');
insert into Movies values('Lord of the Rings the Return of the King');
insert into Movies values('The Expendables');
insert into Movies values('Inception');
insert into Movies values('Alice in the Underworld');
insert into Movies values('Mad World');

insert into Theaters values('SF Lund', 100);
insert into Theaters values('SF Orebro', 150);
insert into Theaters values('SF Helsingborg', 80);
insert into Theaters values('SF Malmo', 120);
insert into Theaters values('SF Stockholm', 200);

insert into Performances (movieId, theaterId, '2012-02-08')
	select id as movieId from Movies where name='King Kong' union select id as theaterId from Theaters where name='SF Lund';

--insert into Reservations (performanceId, userId)
--	select id as performanceId from Performances (where (movieId = select id from Movies where name = 'King Kong') and (theaterId = select id from Theaters where name = 'SF Lund') and (date = '2012-02-08')) union select id from Users where username = 'Sane';


