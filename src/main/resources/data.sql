insert into users(id,firstName,lastName,contact,email,role,address) values (1,'Jon','Doe','1234','jondoe@gmail.com','TRADER','smu');
insert into users(id,firstName,lastName,contact,email,role,address) values (2,'Brandon','Tan','1234','jondoe@gmail.com','QA','smu');
insert into users(id,firstName,lastName,contact,email,role,address) values (3,'Ys','Ngo','1234','jondoe@gmail.com','TRADER', 'smu');
insert into users(id,firstName,lastName,contact,email,role,address) values (4,'aa','Na','123','jondoe1@gmail.com','TRADER', 'smu');
insert into users(id,firstName,lastName,contact,email,role,address) values (5,'bb','Nb','1234','jondoe2@gmail.com','TRADER', 'smu');
insert into users(id,firstName,lastName,contact,email,role,address) values (6,'cc','Nc','1235','jondoe3@gmail.com','TRADER', 'smu');
insert into users(id,firstName,lastName,contact,email,role,address) values (7,'dd','Nd','1236','jondoe4@gmail.com','TRADER', 'smu');
insert into users(id,firstName,lastName,contact,email,role,address) values (8,'ee','Ne','1237','jondoe5@gmail.com','TRADER', 'smu');

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
<<<<<<< HEAD
insert into orders values (8,4,'HIJ.HK','B','LIMIT',10.0,21,'FILLED','2018-08-16 11:07:23');
insert into orders values (9,4,'HIJ.HK','S','LIMIT',10.0,555,'FILLED','2018-08-16 11:17:23');
insert into orders values (10,4,'HIJ.HK','B','MARKET',10.0,21,'FILLED','2018-08-16 11:57:23');
insert into orders values (11,4,'HIJ.HK','S','LIMIT',10.0,555,'FILLED','2018-08-16 12:47:23');
insert into orders values (12,4,'HIJ.HK','B','LIMIT',10.0,21,'FILLED','2018-08-16 13:57:23');
insert into orders values (13,5,'HIJ.HK','S','LIMIT',10.0,555,'FILLED','2018-08-16 14:47:23');
insert into orders values (14,5,'HIJ.HK','B','LIMIT',10.0,21,'FILLED','2018-08-16 13:57:23');
insert into orders values (15,5,'HIJ.HK','S','LIMIT',10.0,555,'FILLED','2018-08-16 14:47:23');
insert into orders values (16,6,'HIJ.HK','B','LIMIT',10.0,21,'FILLED','2018-08-16 13:57:23');
insert into orders values (17,6,'HIJ.HK','S','LIMIT',10.0,555,'FILLED','2018-08-16 14:47:23');
insert into orders values (18,6,'HIJ.HK','B','LIMIT',10.0,21,'FILLED','2018-08-16 13:57:23');
insert into orders values (19,2,'HIJ.HK','S','LIMIT',10.0,555,'FILLED','2018-08-16 14:47:23');
insert into orders values (20,6,'HIJ.HK','B','LIMIT',10.0,21,'FILLED','2018-08-16 13:57:23');
=======
insert into orders values (8,3,'HIJ.HK','B','LIMIT',10.0,21,'CANCELLED','2018-08-16 10:57:23');

insert into quotes values (2,3,10,'2018-08-16 10:21:23');
insert into quotes values (3,4,20,'2018-08-16 10:17:23');
insert into quotes values (4,5,5,'2018-08-16 10:17:28');
insert into quotes values (6,7,10,'2018-08-16 10:27:23');
insert into quotes values (7,8,15,'2018-08-16 10:37:23');
insert into quotes values (1,2,10,'2018-08-16 10:47:23');
>>>>>>> 4bfbe4b1a979663dd9cdc3c33ccfdf28065fe240
