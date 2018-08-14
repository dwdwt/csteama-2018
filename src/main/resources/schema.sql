drop table users if exists;

create table users(
  	userId int(11) NOT NULL AUTO_INCREMENT,
  	firstName varchar(100),
  	lastName varchar(100),
  	contact varchar(20),
  	email varchar(100),
  	role varchar(20),
  	PRIMARY KEY(userId)
  );

drop table orders if exists;

create table orders(
	orderId int(11) NOT NULL AUTO_INCREMENT,
	userId int NOT NULL,
	tickerSymbol varchar(10),
	side varchar(10),
	orderType varchar(10),
	price double,
	noOfShares int,
	status varchar(20),
	orderTimeStamp varchar(50),
	PRIMARY KEY(orderId)
);

drop table sectors if exists;

create table sectors(
	name varchar(100),
	description varchar(200),
	PRIMARY KEY(name)
);

drop table transactions if exists;

create table transactions (
    id int(11) NOT NULL AUTO_INCREMENT,
	orderId int(11),
    operation varchar(200),
    price double,
    quantity int,
    txnTimeStamp varchar(50),
	PRIMARY KEY(id)
  );