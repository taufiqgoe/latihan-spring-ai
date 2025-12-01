create table if not exists users (
    id serial primary key,
    username varchar(255) not null unique,
    password varchar(255) not null,
    roles varchar(255),
    enabled boolean default true not null
);

create table if not exists tokens (
    token varchar(255) primary key,
    username varchar(255) not null,
    created_at timestamp default current_timestamp
);
