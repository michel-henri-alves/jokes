package com.mha.jokes.controller;

import java.util.Optional;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mha.jokes.model.dto.JokeDTO;
import com.mha.jokes.model.dto.Message;
import com.mha.jokes.model.enumerated.Category;
import com.mha.jokes.service.JokesService;

@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8080" })
@RestController
@RequestMapping("/service")
public class JokesController {

	@Autowired
	JokesService jokesService;

	@GetMapping("/joke")
	public ResponseEntity<?> getAnyJoke() {

		Optional<JokeDTO> response = Optional.ofNullable(jokesService.getAnyJoke());
		return response.isPresent() ? new ResponseEntity<>(response, HttpStatus.OK)
				: new ResponseEntity<>(new Message("You're out of jokes"), HttpStatus.OK);
	}

	@GetMapping("/joke/{category}")
	public ResponseEntity<?> getCategorizedJoke(@PathVariable("category") Optional<String> category) {

		ResponseEntity<?> response = null;

		if (EnumUtils.isValidEnum(Category.class, category.get())) {

			Optional<JokeDTO> dto = Optional.ofNullable(jokesService.getCategorizedJoke(category));
			response = dto.isPresent() ? new ResponseEntity<>(jokesService.getCategorizedJoke(category), HttpStatus.OK)
					: new ResponseEntity<>(new Message("You're out of jokes"), HttpStatus.OK);

		}

		else {
			response = new ResponseEntity<>(new Message("Category not supported"), HttpStatus.BAD_REQUEST);
		}

		return response;

	}

	@PostMapping("/rate/{id}/{grade}")
	public ResponseEntity<?> registerRate(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> grade) {

		ResponseEntity<?> response = null;

		if (grade.get() < 0 || grade.get() > 10) {
			response = new ResponseEntity<>(new Message("This grade is not possible:\n Vote valid: 1 to 10"),
					HttpStatus.BAD_REQUEST);
		}

		else if (jokesService.getJokeById(id).isEmpty()) {
			response = new ResponseEntity<>(new Message("This joke is not presents"), HttpStatus.BAD_REQUEST);
		}

		else {
			jokesService.registerRate(id, grade);
			response = new ResponseEntity<>(new Message("Joke " + id.get() + " was rated"), HttpStatus.OK);
		}

		return response;
	}

	@GetMapping("/categories")
	public ResponseEntity<?> categoriesList() {

		return new ResponseEntity<>(jokesService.categoriesList(), HttpStatus.OK);
	}

	@GetMapping("/unrated")
	public ResponseEntity<?> getUnrated() {

		return new ResponseEntity<>(jokesService.getUnratedJokes(), HttpStatus.OK);
	}

}
