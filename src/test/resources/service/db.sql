create table if not exists test(
	id int not null auto_increment, 
	text varchar(20), 
	primary key(id)
);
insert into test(text) values('test1');
insert into test(text) values('test2');
insert into test(text) values('test3');
insert into test(text) values('test4');
insert into test(text) values('test5');