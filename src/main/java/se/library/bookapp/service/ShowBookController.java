package se.library.bookapp.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import se.library.bookapp.entities.Author;
import se.library.bookapp.entities.Book;
import se.library.bookapp.entities.BookWithAuthors;
import se.library.bookapp.repository.AuthorDAO;
import se.library.bookapp.repository.BookAuthorsDAO;
import se.library.bookapp.repository.BookDAO;
import se.library.bookapp.repository.LibraryDAO;

@Controller
@RequestMapping("show_books")
public class ShowBookController {

	private BookDAO bookDAO;
	private AuthorDAO authorDAO;
	private BookAuthorsDAO bookAuthorsDAO;
	private LibraryDAO libraryDAO;


	@Autowired
	public ShowBookController(BookDAO bookDAO, AuthorDAO authorDAO, BookAuthorsDAO bookAuthorsDAO, LibraryDAO libraryDAO ) {
		this.bookDAO = bookDAO;
		this.authorDAO = authorDAO;
		this.bookAuthorsDAO = bookAuthorsDAO;
		this.libraryDAO = libraryDAO;
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String showBooks(@RequestParam(value = "search_string", required = false) String searchString, Model model) {
		if(searchString == null) {
			return "show_book/show_books";
		}
		List<Book> books = bookDAO.search(searchString);
		if(books == null) {
			return "show_book/show_books";
		}

		List<BookWithAuthors> booksAndAuthors = libraryDAO.fetchBooksWithAuthorsForBooks(books);
		model.addAttribute("booksAndAuthors", booksAndAuthors);
		List<Author> authors = libraryDAO.searchAuthor(searchString);
		if(authors == null) {
			return "show_book/show_books";
		}

		booksAndAuthors.addAll(libraryDAO.fetchBooksWithAuthorsForAuthors(libraryDAO.searchAuthor(searchString)));

		model.addAttribute("booksAndAuthors", new HashSet<>(booksAndAuthors));
		return "show_book/show_book_search";
	}

	@RequestMapping(value = "/show_all", method = RequestMethod.GET)
	public String showAllBooks(Model model) {

		List<BookWithAuthors> booksAndAuthors = libraryDAO.fetchBooksWithAuthorsForBooks(libraryDAO.fetchAllBooks());

		model.addAttribute("booksAndAuthors",booksAndAuthors);
		return "show_book/show_all_books";
	}
}
