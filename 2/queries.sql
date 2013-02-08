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
name char(20) NOT NULL UNIQUE
);

create table Theaters(
id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
name char(20) NOT NULL UNIQUE,
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
  REFERENCES Theaters(id),
CONSTRAINT oncePerDay UNIQUE (movieId, theDate)
);

create table Reservations (
reservationNumber int PRIMARY KEY NOT NULL AUTO_INCREMENT,
performanceId int NOT NULL,
userId int NOT NULL,
FOREIGN KEY(performanceId)
  REFERENCES Performances(id),
FOREIGN KEY(userId)
  REFERENCES Users(id)
CONSTRAINT oncePerPerformance UNIQUE (performanceId,userId)
);

SET foreign_key_checks = 1;

-- Insert data into the tables.
insert into Users(username, name, phoneNumber, address) values('Sane', 'Christofer Heimonen', '0707283358', 'Spolegatan 15 22220 Lund');
insert into Users(username, name, phoneNumber, address) values('Rauban', 'Robert Borg', '0703213355', 'Trollgatan 14 34323 Lund');
insert into Users(username, name, phoneNumber, address) values('Troll', 'Andorz Trollheim', '0734384234', 'Sturevagen 123 34320 Lund');
insert into Users(username, name, phoneNumber, address) values('Apa', 'Thomas Svensson', '0703828399', 'Gatan 23 23222 Göteborg');
insert into Users(username, name, phoneNumber, address) values('Ond', 'Onde Man', '0482030222', 'HejaVagen 34 34322 Stockholm');
insert into Users(username, name, phoneNumber, address) values('Haha', 'Anna Gardhagen', '0731232890', 'Ankvägen 23 23212 Ankeborg');

insert into Movies(name) values('King Kong');
insert into Movies(name) values('Enter the Matrix');
insert into Movies(name) values('Lord of the Rings');
insert into Movies(name) values('The Expendables');
insert into Movies(name) values('Inception');
insert into Movies(name) values('Alice in the Underworld');
insert into Movies(name) values('Mad World');

insert into Theaters(name, numberOfSeats) values('SF Lund', 100);
insert into Theaters(name, numberOfSeats) values('SF Orebro', 150);
insert into Theaters(name, numberOfSeats) values('SF Helsingborg', 80);
insert into Theaters(name, numberOfSeats) values('SF Malmo', 120);
insert into Theaters(name, numberOfSeats) values('SF Stockholm', 200);

insert into Performances(movieId, theaterId, theDate) 
select Movies.id, Theaters.id, '2012-02-08' as theDate 
from Movies, Theaters 
where Movies.name = 'King Kong' and Theaters.name='SF LUND';

-- Create a reservation
insert into Reservations(performanceId, userId)
select Performances.id, Users.id
from Performances, Users, Movies
where Performances.theDate = '2012-02-08' and Performances.movieId = Movies.id and Movies.name = 'King Kong' and Users.username = 'Sane';

-- List all movies that are shown
select Movies.name, Performances.theDate as date
from Movies, Performances
where Movies.id = Performances.movieId;

-- list dates when a movie is shown
select Performances.theDate as date, Movies.name
from Performances, Movies
where Performances.movieId = Movies.id and Movies.name = 'King Kong';

-- list all data concerning a movie performance
select Movies.name as movieName, Theaters.name as theaterName, Performances.theDate as date
from Movies, Theaters, Performances
where Performances.movieId = Movies.id and Performances.theaterId = Theaters.id and Performances.id = 1;

-- insert two movie theaters with the same name,
insert into Theaters(name, numberOfSeats) values('SF Lund', 100);

-- insert two performances of the same movie on the same date
insert into Performances(movieId, theaterId, theDate) 
select Movies.id, Theaters.id, '2012-02-08' as theDate 
from Movies, Theaters 
where Movies.name = 'King Kong' and Theaters.name='SF LUND';

-- insert a performance where the theater doesn’t exist in the database
insert into Performances(movieId, theaterId, theDate) 
select Movies.id, 9 as theaterId, '2012-01-08' as theDate 
from Movies
where Movies.name = 'Inception';

-- insert a ticket reservation where either the user or the performance doesn’t exist
insert into Reservations(performanceId, userId)
select Performances.id,100 as userId
from Performances, Movies
where Performances.theDate = '2012-02-08' and Performances.movieId = Movies.id and Movies.name = 'King Kong';






