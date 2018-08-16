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
insert into companies(name,tickerSymbol,industryName) values ('DB', 'DB.HK', 'IT Services');
insert into companies(name,tickerSymbol,industryName) values ('MS', 'MS.HK', 'IT Services');
insert into companies(name,tickerSymbol,industryName) values ('RBS', 'RBS.SG', 'IT Services');

insert into transactions(orderId,operation,price,quantity,txnTimeStamp) values (1,'OPEN',10.0,5,'2008-11-11 13:23:44');
insert into transactions(orderId,operation,price,quantity,txnTimeStamp) values (2,'FILL',10.1,6,'2010-11-11 13:23:44');
insert into transactions(orderId,operation,price,quantity,txnTimeStamp) values (3,'CANCEL',10.2,7,'2012-11-11 13:23:44');

insert into orders values (1,1,'ABC.HK','B','LIMIT',10.0,5,'OPENED','2018-08-16 10:17:23');
insert into orders values (2,2,'DEF.HK','B','MARKET',10.0,10,'FILLED','2018-08-16 10:21:23');
insert into orders values (3,2,'HIJ.HK','S','LIMIT',10.0,15,'CANCELLED','2018-08-16 10:17:23');
insert into orders values (4,3,'HIJ.HK','S','LIMIT',10.0,20,'OPENED','2018-08-16 10:17:28');
insert into orders values (5,8,'HIJ.HK','S','MARKET',10.0,25,'FILLED','2018-08-16 10:27:23');
insert into orders values (6,3,'HIJ.HK','B','LIMIT',10.0,45,'CANCELLED','2018-08-16 10:37:23');
insert into orders values (7,3,'HIJ.HK','S','LIMIT',10.0,555,'FILLED','2018-08-16 10:47:23');
insert into orders values (8,2,'HIJ.HK','B','LIMIT',10.0,21,'FILLED','2018-08-16 11:07:23');
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
insert into orders values (21,3,'HIJ.HK','B','LIMIT',10.0, 0,'FILLED','2018-08-16 10:57:23');
insert into orders values (22,4,'HIJ.HK','S','LIMIT',10.0, 0,'FILLED','2018-08-16 10:57:23');
insert into orders values (23,3,'HIJ.HK','B','LIMIT',10.0, 0,'FILLED','2018-08-16 10:57:23');
insert into orders values (24,4,'HIJ.HK','S','LIMIT',10.0, 0,'FILLED','2018-08-16 10:57:23');
insert into orders values (25,5,'HIJ.HK','B','LIMIT',10.0, 0,'FILLED','2018-08-16 10:57:23');
insert into orders values (26,6,'HIJ.HK','S','LIMIT',10.0, 0,'FILLED','2018-08-16 10:57:23');
insert into orders values (27,7,'HIJ.HK','B','LIMIT',10.0, 0,'FILLED','2018-08-16 10:57:23');
insert into orders values (28,8,'HIJ.HK','S','LIMIT',10.0, 0,'FILLED','2018-08-16 10:57:23');
insert into orders values (29,5,'HIJ.HK','B','LIMIT',10.0, 0,'FILLED','2018-08-16 10:57:23');
insert into orders values (30,6,'HIJ.HK','S','LIMIT',10.0, 0,'FILLED','2018-08-16 10:57:23');
insert into orders values (31,3,'HIJ.HK','B','LIMIT',10.0, 0,'FILLED','2018-08-16 10:57:23');
insert into orders values (32,4,'HIJ.HK','S','LIMIT',10.0, 0,'FILLED','2018-08-16 10:57:23');

insert into quotes values (21,22,10,'2018-08-16 10:57:23');
insert into quotes values (23,24,20,'2018-08-16 10:17:23');
insert into quotes values (25,26,30,'2018-08-16 10:17:28');
insert into quotes values (27,28,40,'2018-08-16 10:27:23');
insert into quotes values (29,30,50,'2018-08-16 10:37:23');
insert into quotes values (31,32,60,'2018-08-16 10:47:23');


