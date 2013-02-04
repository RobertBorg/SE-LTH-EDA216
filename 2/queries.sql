-- Delete the tables if they exist. Set foreign_key_checks = 0 to
-- disable foreign key checks, so the tables may be dropped in
-- arbitrary order.

SET foreign_key_checks = 0;


DROP TABLE IF EXISTS Users;


DROP TABLE IF EXISTS Theaters;


DROP TABLE IF EXISTS Movies;


DROP TABLE IF EXISTS Reservations;


DROP TABLE IF EXISTS Performances;


SET foreign_key_checks = 1;


-- Create the tables.
create table Users (
id PRIMARY KEY NOT NULL,
username char(20) NOT NULL UNIQUE,
name char(20) NOT NULL,
phoneNumber char(20) NOT NULL
address varchar(256)
);
...
-- Insert data into the tables.
insert into Users values(...);

create table Movies (
id PRIMARY KEY NOT NULL, 
name char(20) NOT NULL);

create table Theaters(
id PRIMARY KEY NOT NULL,
name char(20) NOT NULL,
numberOfSeats int NOT NULL);

create table Performances (
id PRIMARY KEY NOT NULL,
movieId int NOT NULL,
theaterId int NOT NULL,
date DATE NOT NULL);

create table Reservations (
id PRIMARY KEY NOT NULL,
reservationNumber int NOT NULL,
performanceId int NOT NULL,
userId int NOT NULL);