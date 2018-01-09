# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table author (
  id                            integer auto_increment not null,
  name                          varchar(100),
  artistic_name                 varchar(100),
  email                         varchar(100),
  birthday                      datetime(6),
  gender                        varchar(1),
  biography                     varchar(1000),
  picture                       longblob,
  constraint uq_author_email unique (email),
  constraint pk_author primary key (id)
);

create table book (
  id                            integer auto_increment not null,
  author_id                     integer,
  isbn                          varchar(13),
  title                         varchar(100),
  price                         float(12,2),
  picture                       longblob,
  constraint uq_book_isbn unique (isbn),
  constraint pk_book primary key (id)
);

create table user (
  id                            integer auto_increment not null,
  username                      varchar(128),
  password                      varchar(128),
  constraint pk_user primary key (id)
);

alter table book add constraint fk_book_author_id foreign key (author_id) references author (id) on delete restrict on update restrict;
create index ix_book_author_id on book (author_id);


# --- !Downs

alter table book drop foreign key fk_book_author_id;
drop index ix_book_author_id on book;

drop table if exists author;

drop table if exists book;

drop table if exists user;

