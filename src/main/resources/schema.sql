drop table users if exists;

create table users(
  	id int(11) NOT NULL AUTO_INCREMENT,
  	firstName varchar(100),
  	lastName varchar(100),
  	contact varchar(20),
  	email varchar(100),
  	role varchar(20),
  	PRIMARY KEY(id)
);

drop table orders if exists;

create table orders(
	id int(11) NOT NULL AUTO_INCREMENT,
	userId int NOT NULL,
	tickerSymbol varchar(10),
	side varchar(10),
	orderType varchar(10),
	price double,
	noOfShares int,
	status varchar(20),
	orderTimeStamp varchar(50),
	PRIMARY KEY(id)
);

drop table quotes if exists;

create table quotes(
	buyOrderId int(11) NOT NULL,
	sellOrderId int(11) NOT NULL,
	noOfShares int,
	quoteTimeStamp varchar(50)
);

drop table industries if exists;

create table industries(
	name varchar(100),
	description varchar(200),
	PRIMARY KEY(name)
);

drop table companies if exists;

create table companies(
	name varchar(100),
	tickerSymbol varchar(10),
	industryName varchar(100),
	PRIMARY KEY(tickerSymbol)
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
