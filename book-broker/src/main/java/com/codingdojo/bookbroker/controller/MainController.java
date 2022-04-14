package com.codingdojo.bookbroker.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codingdojo.bookbroker.models.Book;
import com.codingdojo.bookbroker.models.LoginUser;
import com.codingdojo.bookbroker.models.User;
import com.codingdojo.bookbroker.services.BookService;
import com.codingdojo.bookbroker.services.UserService;

@Controller
public class MainController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BookService bookService;
	
	//******* Register post route ********//
	//login page
	@GetMapping("/")
	public String index(
			@ModelAttribute("newUser") User newUser,
			@ModelAttribute("newLogin") LoginUser loginUser) {
		
		return "index.jsp";
	}
	
	@PostMapping("/register")
	public String register(
			@Valid @ModelAttribute("newUser") User newUser,
			BindingResult result, 
			HttpSession session,
			@ModelAttribute("newLogin") LoginUser loginUser) {
		// Validate a user
		userService.validate(newUser, result);
		if(result.hasErrors()) {
			
			return "index.jsp";
		}
		// Register a user
		userService.registerUser(newUser);
		// Put user in Session
		session.setAttribute("loggedInUser", newUser);
		
		return "redirect:/bookmarket";
	}
	
	@PostMapping("/login")
	public String login(
			@Valid @ModelAttribute("newLogin") LoginUser loginUser,
			BindingResult result,
			HttpSession session,
			@ModelAttribute("newUser") User newUser) {
		// Authenticate a user
		userService.authenticateUser(loginUser, result);
		if(result.hasErrors()) {
			
				return "index.jsp";
			}
		
		User loggedInUser = userService.findUserByEmai(loginUser.getEmail());
		
		// put user in session
		session.setAttribute("loggedInUser", loggedInUser);
		return "redirect:/bookmarket";
	}
	
	//******* Logout get route ********//
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		
		return "redirect:/";
	}
	
	//******* DASHBOARD get route ********//
	@GetMapping("/books")
	public String dashboard(
			Model bookModel,
			HttpSession session) {
		
		if(session.getAttribute("loggedInUser") != null) {
			bookModel.addAttribute("books", bookService.findAllBooks()); 
			return "dashboard.jsp";
		} else {
			return "redirect:/";
		}
	}
	
	//******* New Book get route ********//
	@GetMapping("books/new")
	public String newBook(@ModelAttribute("newBook") Book newBook,
			HttpSession session) {
		if(session.getAttribute("loggedInUser") != null) {
			return "new.jsp";
		}
		
		return "redirect:/";
	}
	
	@PostMapping("books/create")
	public String createBook(
			@Valid @ModelAttribute("newBook") Book newBook,
			BindingResult result, 
			HttpSession session) {
		if(result.hasErrors()) {
			return "new.jsp";
		}
		
		bookService.createBook(newBook);
		
		return "redirect:/bookmarket";
	}
	
	//******* Book get route ********//
	@GetMapping("/books/{id}")
	public String showBook(
			@PathVariable("id") Long id,
			Model bookModel,
			HttpSession session) {
		if(session.getAttribute("loggedInUser") != null) {
			
			Book book = bookService.findBook(id);
			bookModel.addAttribute("book", book);
			bookModel.addAttribute("user", userService.findUserByEmai("loggedInUser"));
			
			return "details.jsp";
		}
		
		return "redirect:/";
	}
	
	//******* Edit Book get route ********//
	@GetMapping("/books/{id}/edit")
	public String editBook(
			@PathVariable("id") Long id,
			Model editModel, 
			HttpSession session) {
		
		if(session.getAttribute("loggedInUser") != null) {
			
			Book book = bookService.findBook(id);
			editModel.addAttribute("book", book);
			
			return "edit.jsp";
		}
		
		return "redirect:/";
	}
	
	@PutMapping("/books/{id}")
	public String updateBook(
			@Valid @ModelAttribute("book") Book book,
			BindingResult result) {
		
		if(result.hasErrors()) {
			return "edit.jsp";
		}
		
		bookService.updateBook(book);
		
		return "redirect:/bookmarket";
	}
	
	@DeleteMapping("/books/{id}/delete")
	public String deleteBook(@PathVariable("id") Long id) {
		bookService.findBook(id);
		return "redirect:/bookmarket";
	}
	
	//******* BookMarket get route ********//
	@GetMapping("/bookmarket")
	public String bookmarket(
			Model marketModel,
			HttpSession session) {
		if(session.getAttribute("loggedInUser") != null) {
			User user = (User) session.getAttribute("loggedInUser");
			User userLoggedIn = userService.findUserById(user.getId());
			
			marketModel.addAttribute("books", bookService.findAllBooks()); 
			marketModel.addAttribute("userLoggedIn", userLoggedIn);
			
			return "bookmarket.jsp";
		}
		
		return "redirect:/";
	}
	
	//******* Borrow Book get route ********//
	@RequestMapping("/bookmarket/{id}/borrow")
	public String borrowerBook(
			@PathVariable Long id,
			HttpSession session,
			Model borrowModel) {
		Book book = bookService.findOneBook(id);
		User user = (User)session.getAttribute("loggedInUser");
		User userLoggedIn = userService.findUserById(user.getId());
		
		bookService.borrowBook(book, userLoggedIn);
		
		return "redirect:/bookmarket";
	}
	
	//******* Return Book get route ********//
	@RequestMapping("/bookmarket/{id}/return")
	public String returnBook(
			@PathVariable Long id,
			HttpSession session,
			Model borrowModel) {
		Book book = bookService.findOneBook(id);
		User user = (User)session.getAttribute("loggedInUser");
		User userLoggedIn = userService.findUserById(user.getId());
		
		bookService.returnBook(book, userLoggedIn);
		
		return "redirect:/bookmarket";
	}
}