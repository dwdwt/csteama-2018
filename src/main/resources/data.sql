insert into users(id,firstName,lastName,contact,email,role) values (1,'Jon','Doe','1234','jondoe@gmail.com','trader');
insert into users(id,firstName,lastName,contact,email,role) values (2,'Brandon','Tan','1234','jondoe@gmail.com','trader');
insert into users(id,firstName,lastName,contact,email,role) values (3,'Ys','Ngo','1234','jondoe@gmail.com','trader');

insert into industries(name,description) values ('IT Services', 'Services');
insert into industries(name,description) values ('Software & Services', 'Services');
insert into industries(name,description) values ('Telecommunication Services', 'Services');


insert into companies(name,tickerSymbol,industryName) values ('CS', 'ABC.HK', 'IT Services');
insert into companies(name,tickerSymbol,industryName) values ('JP', 'DEF.HK', 'IT Services');
insert into companies(name,tickerSymbol,industryName) values ('DBS', 'HIJ.HK', 'IT Services');

insert into transactions(orderId,operation,price,quantity,txnTimeStamp) values (1,'OPEN',10.0,5,'2008-11-11 13:23:44');
insert into transactions(orderId,operation,price,quantity,txnTimeStamp) values (2,'FILL',10.1,6,'2010-11-11 13:23:44');
insert into transactions(orderId,operation,price,quantity,txnTimeStamp) values (3,'CANCEL',10.2,7,'2012-11-11 13:23:44');


insert into orders values (1,1,'ABC.HK','B','LIMIT',10.0,5,'OPENED','2018-08-16 10:17:23');
insert into orders values (2,2,'DEF.HK','B','MARKET',10.0,10,'FILLED','2018-08-16 10:21:23');
insert into orders values (3,2,'HIJ.HK','S','LIMIT',10.0,15,'CANCELLED','2018-08-16 10:17:23');
insert into orders values (4,3,'HIJ.HK','S','LIMIT',10.0,20,'OPENED','2018-08-16 10:17:28');
insert into orders values (5,3,'HIJ.HK','S','MARKET',10.0,25,'FILLED','2018-08-16 10:27:23');
insert into orders values (6,3,'HIJ.HK','B','LIMIT',10.0,45,'CANCELLED','2018-08-16 10:37:23');
insert into orders values (7,3,'HIJ.HK','S','LIMIT',10.0,555,'FILLED','2018-08-16 10:47:23');
insert into orders values (8,3,'HIJ.HK','B','LIMIT',10.0,21,'CANCELLED','2018-08-16 10:57:23');

