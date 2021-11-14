create table roles(
    id serial primary key,
    name varchar(100)
);

insert into roles(name) values ('ROLE_ADMIN'), ('ROLE_USER');

create table persons(
    id serial primary key,
    name varchar(200),
    username varchar(200) unique ,
    password varchar(200),
    role_id int not null references roles(id)
);

insert into persons(name, username, password, role_id) VALUES
('admin', 'admin', 'admin', 1), ('user', 'user', 'user', 2);

create table rooms(
    id serial primary key ,
    name varchar(200),
    created timestamp default now(),
    person_id int not null references persons(id)
);

create table messages(
    id serial primary key,
    name varchar(500),
    created timestamp default now(),
    person_id int not null references persons(id),
    room_id int not null references rooms(id)
);