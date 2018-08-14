insert into users(id,firstName,lastName,contact,email,role) values (1,'Jon','Doe','1234','jondoe@gmail.com','trader');
insert into users(id,firstName,lastName,contact,email,role) values (2,'Brandon','Tan','1234','jondoe@gmail.com','trader');
insert into users(id,firstName,lastName,contact,email,role) values (3,'Ys','Ngo','1234','jondoe@gmail.com','trader');

insert into industries(name,description) values ('IT Services', 'Services');
insert into industries(name,description) values ('Software & Services', 'Services');
insert into industries(name,description) values ('Telecommunication Services', 'Services');


insert into companies(name,tickerSymbol,industryName) values ('CS', 'ABC.HK', 'IT Services');
insert into companies(name,tickerSymbol,industryName) values ('JP', 'DEF.HK', 'IT Services');
insert into companies(name,tickerSymbol,industryName) values ('DBS', 'HIJ.HK', 'IT Services');

insert into transactions values (1,1,'OPEN',10.0,5,'2008-11-11 13:23:44');
insert into transactions values (2,2,'FILL',10.1,6,'2008-11-11 13:23:44');
insert into transactions values (3,3,'CANCEL',10.2,7,'2008-11-11 13:23:44');

insert into orders values (1,1,'ABC.HK','B','LIMIT',10.0,5,'OPENED','2018-08-16 10:17:23');
insert into orders values (2,2,'DEF.HK','B','MARKET',10.0,5,'FILLED','2018-08-16 10:21:23');
insert into orders values (3,3,'HIJ.HK','S','LIMIT',10.0,5,'CANCELLED','2018-08-16 10:17:23');
