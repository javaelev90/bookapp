package se.library.bookapp;


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

	@RequestMapping(value = "/add_book", method = RequestMethod.GET)
	public String getAddBook() {
		return "add_book/add_book";
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
		return "add_book/add_book_show_result";
	}

	@RequestMapping(value = "/edit_book", method = RequestMethod.GET)
	public String editBook() {
		return "edit_book/edit_book";
	}

}
