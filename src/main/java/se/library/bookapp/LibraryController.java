package se.library.bookapp;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LibraryController {
	
	@Autowired
	private LibraryDAO libraryDAO;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/show_books", method = RequestMethod.GET)
	public String showBooks() {
		return "show_book/show_books";
	}
	
	@RequestMapping(value = "/show_books/show_all", method = RequestMethod.GET)
	public String showBooksWithHeader(Model model) {
		System.out.println("worked");
		List<BookWithAuthors> booksAndAuthors = new ArrayList<>();
		List<Book> books = libraryDAO.fetchAllBooks();
		List<Author> authors;
		BookWithAuthors bookWithAuthors;
		for(Book book : books) {
			authors = libraryDAO.fetchAuthorsForBook(book.getId());
			bookWithAuthors = new BookWithAuthors(book, authors);
			booksAndAuthors.add(bookWithAuthors);
		}
		System.out.println(booksAndAuthors.get(0).getAuthors().get(0).getFirstName());
		model.addAttribute("booksAndAuthors",booksAndAuthors);
		return "show_book/show_all_books";
	}

	@RequestMapping(value = "/add_book", method = RequestMethod.GET)
	public String getAddBook() {
		return "add_book/add_book_GET";
	}

	@RequestMapping(value = "/add_book", method = RequestMethod.POST)
	public String postAddBook(@RequestParam("title") String title, @RequestParam("description") String description,
			@RequestParam("author_firstname") String authorFirstName, @RequestParam("author_lastname") String authorLastName, Model model) {
		model.addAttribute("title", title);
		model.addAttribute("description", description);
		model.addAttribute("author_firstname", authorFirstName);
		model.addAttribute("author_lastname", authorLastName);
		
		Book book = new Book();
		book.setTitle(title);
		book.setDescription(description);
		Author author = new Author();
		author.setFirstName(authorFirstName);
		author.setLastName(authorLastName);
		
		int ret = libraryDAO.addBook(book, author);
		model.addAttribute("response_code", ret);
		return "add_book/add_book_POST";
	}

	@RequestMapping(value = "/edit_book", method = RequestMethod.GET)
	public String editBook() {
		return "edit_book/edit_book";
	}

}
