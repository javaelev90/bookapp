package se.library.bookapp;

import java.util.List;

public class BookWithAuthors {

	private final Book book;
	private final List<Author> authors;
	
	public BookWithAuthors(Book book, List<Author> authors) {
		this.book = book;
		this.authors = authors;
	}

	public Book getBook() {
		return book;
	}

	public List<Author> getAuthors() {
		return authors;
	}
	
	
}
