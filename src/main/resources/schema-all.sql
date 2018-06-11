CREATE SCHEMA library;

CREATE TABLE IF NOT EXISTS library.authors (
 authorId serial primary key not null,
 authorFirstName varchar(255) not null,
 authorLastName varchar(255) not null
);

CREATE TABLE IF NOT EXISTS library.books (
 bookId serial primary key not null,
 bookTitle varchar(255) not null,
 bookDescription text not null
);

create table if not exists library.authors_books (
	authorId int references library.authors (authorId),
	bookId int references library.books (bookId),
	primary key(authorId, bookId)
);
