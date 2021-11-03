insert into roles(name) values ('admin');
insert into roles(name) values ('user');
insert into roles(name) values ('guest');

insert into users (access, address, avatar, country, email, name, password, phone, title)
values (?, ?, ?, ?, ?, ?, ?, ?, ?);
