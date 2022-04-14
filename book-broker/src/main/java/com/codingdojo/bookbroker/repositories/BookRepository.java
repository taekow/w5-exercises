package com.codingdojo.bookbroker.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.codingdojo.bookbroker.models.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Long>{

	List<Book> findAll();
	
	Book findByTitle(String title);
}
