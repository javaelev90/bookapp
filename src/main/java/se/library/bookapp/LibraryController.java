package se.library.bookapp;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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

	@GetMapping("/edit_book/{id}")
	public String updateBook(@PathVariable("id") int id, Model model) {
		BookWithAuthors bookWithAuthors = libraryDAO.fetchBookWithAuthors(id);
		if(bookWithAuthors != null) {
			model.addAttribute("bookAndAuthors",libraryDAO.fetchBookWithAuthors(id));
			model.addAttribute("gotContent", true);
			System.out.println("added authors");
		}
		System.out.println("/edit_book/{id}");
		return "edit_book/edit_book";
	}
	
	@PutMapping("/edit_book")
	public String editBook(@RequestParam("id") int id, @RequestParam("title") String title, @RequestParam("description") String description,
			@RequestParam("author_firstname") List<String> authorFirstName, @RequestParam("author_lastname") List<String> authorLastName, Model model) {
		
		return "";
	}
	
	@GetMapping("/edit_book")
	public String editBook(@RequestParam(value = "id", required = false) Integer id, Model model) {
		if(id != null) {
			BookWithAuthors bookWithAuthors = libraryDAO.fetchBookWithAuthors(id);
			if(bookWithAuthors != null) {
				model.addAttribute("bookAndAuthors",libraryDAO.fetchBookWithAuthors(id));
				model.addAttribute("gotContent", true);
				System.out.println("added authors");
			}
		}
		
		System.out.println("/edit_book/"+" with model");
		return "edit_book/edit_book";
	}

	
	@DeleteMapping("/show_books/show_all/{id}")
	public String deleteBook(@PathVariable(value = "id", required = true) int id)  { 
		libraryDAO.removeBook(id); 
		return "redirect:/show_books/show_all";
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
			@RequestParam("author_firstname") List<String> authorFirstName, @RequestParam("author_lastname") List<String> authorLastName, Model model) {
		
		model.addAttribute("title", title); 
		model.addAttribute("description", description);
		model.addAttribute("author_firstname", authorFirstName);
		model.addAttribute("author_lastname", authorLastName);
		
		Book book = new Book();
		book.setTitle(title);
		book.setDescription(description);
		List<Author> authors = new ArrayList<>();
		Author author;
		int ret = -1;
		for(int i = 0; i < authorFirstName.size(); i++) {
			author = new Author();
			author.setFirstName(authorFirstName.get(i));
			author.setLastName(authorLastName.get(i));
			authors.add(author);
		}
		ret = libraryDAO.addBook(book, authors);
		model.addAttribute("response_code", ret);
		return "add_book/add_book_POST";
	}

	

}
