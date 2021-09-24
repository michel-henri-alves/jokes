package com.mha.jokes.controller;

import java.util.Optional;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mha.jokes.model.Category;
import com.mha.jokes.model.Message;

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200", "http://localhost:8080" })
@RestController
@RequestMapping("/service/")
public class JokesController {

	private static final Logger log = LoggerFactory.getLogger(JokesController.class);
	@Autowired
	JokeConsuming jokeConsuming;

	@GetMapping("/joke")
	public ResponseEntity<?> getNewJoke() {

		log.info("Requested new joke without category specified");
		return new ResponseEntity<>(jokeConsuming.consumingJoke("/Any"), HttpStatus.OK);
	}

	@GetMapping("/joke/{category}")
	public ResponseEntity<?> getNewJoke(@PathVariable("category") Optional<String> category) {

		log.info("Requested new joke with category " + category.get());
		return EnumUtils.isValidEnum(Category.class, category.get())
				? new ResponseEntity<>(jokeConsuming.consumingJoke(category.get()), HttpStatus.OK)
				: new ResponseEntity<>(new Message("Category not supported"),
						HttpStatus.BAD_REQUEST);
	}
	

	@PostMapping("/rate/{id}/{grade}")
    public void rating(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> grade){
		
		log.info("ID da joke: "+id.get());
		log.info("Nota da joke: "+grade.get());
		
		jokeConsuming.getConsumedJokes().entrySet().stream()
		.filter(null)
		
		//return new ResponseEntity<>(dto, HttpStatus.OK);
    }

}
