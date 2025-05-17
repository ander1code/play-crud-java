# --- !Ups

CREATE TABLE author (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(100),
  artistic_name VARCHAR(100),
  email VARCHAR(100),
  birthday TIMESTAMP,
  gender VARCHAR(1),
  biography VARCHAR(1000),
  picture BLOB,
  CONSTRAINT uq_author_email UNIQUE (email)
);

CREATE TABLE book (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  author_id INTEGER,
  isbn VARCHAR(13),
  title VARCHAR(100),
  price REAL,
  picture BLOB,
  CONSTRAINT uq_book_isbn UNIQUE (isbn),
  FOREIGN KEY(author_id) REFERENCES author(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE usersys (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  username VARCHAR(128),
  userpass VARCHAR(128)
);

CREATE INDEX ix_book_author_id ON book(author_id);


# --- !Downs

DROP INDEX IF EXISTS ix_book_author_id;
DROP TABLE IF EXISTS usersys;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS author;
