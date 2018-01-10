begin transaction;

create table author (
id                            serial not null,
name                          varchar(100),
artistic_name                 varchar(100),
email                         varchar(100),
birthday                      timestamp,
gender                        varchar(1),
biography                     varchar(1000),
picture                       bytea,
constraint uq_author_email unique (email),
constraint pk_author primary key (id)
);

create table book (
id                            serial not null,
author_id                     integer,
isbn                          varchar(13),
title                         varchar(100),
price                         float(12,2),
picture                       bytea,
constraint uq_book_isbn unique (isbn),
constraint pk_book primary key (id)
);

create table usersys (
id                            serial not null,
username                      varchar(128),
userpass                      varchar(128),
constraint pk_usersys primary key (id)
);

alter table book add constraint fk_book_author_id foreign key (author_id) references author (id) on delete restrict on update restrict;
create index ix_book_author_id on book (author_id);

rollback;