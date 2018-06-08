package se.library.bookapp;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@DeleteMapping("/show_books/{id}")
	public String deleteBook(@PathVariable("id") int id) {
		System.out.println("went in");
		
		return "redirect:/show_book/show_all_books";
	}
	
	@RequestMapping(value = "/show_books", method = RequestMethod.GET)
	public String showBooks(@RequestParam(value = "search_string", required = false) String searchString, Model model) {
		if(searchString == null) {
			return "show_book/show_books";
		} 
		List<Book> books = libraryDAO.searchBook(searchString);
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
		model.addAttribute("booksAndAuthors", booksAndAuthors);
		return "show_book/show_book_search";
	}
	
	@RequestMapping(value = "/show_books/show_all", method = RequestMethod.GET)
	public String showAllBooks(Model model) {
		
		List<BookWithAuthors> booksAndAuthors = libraryDAO.fetchBooksWithAuthorsForBooks(libraryDAO.fetchAllBooks());

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
