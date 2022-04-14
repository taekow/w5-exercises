package com.codingdojo.bookbroker.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codingdojo.bookbroker.models.Book;
import com.codingdojo.bookbroker.models.User;
import com.codingdojo.bookbroker.repositories.BookRepository;
import com.codingdojo.bookbroker.repositories.UserRepository;

@Service
public class BookService {
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	// get all book
	public List<Book> findAllBooks() {
		return bookRepository.findAll();
	}
	
	// Retrieve one Book
	public Book findOneBook(Long id) {
		return bookRepository.findById(id).orElse(null);
	}
	
	// Retrieve book by Id
	public Book findBook(Long id) {
		Optional<Book> optionalBook = bookRepository.findById(id);
		
		if (optionalBook.isPresent()) {
			return optionalBook.get();
		} else {
			return null;
		}
	}
	
	// Create a new book
	public Book createBook(Book newBook) {
		return bookRepository.save(newBook);
	}

	// Update existing book
	public Book updateBook(Book book) {
		return bookRepository.save(book);
	}

	// Delete existing book
	public void deleteBook(Long id) {
		Optional<Book> optionalBook = bookRepository.findById(id);
		
		if (optionalBook.isPresent()) {
			bookRepository.deleteById(id);
		}
	}
	
	// borrow a book
	public void borrowBook(Book book, User user) {
		book.setBorrower(user);
		bookRepository.save(book);
	}
	
	// Return a book
	public void returnBook(Book book, User user) {
		book.setBorrower(null);
		bookRepository.save(book);
	}
}
