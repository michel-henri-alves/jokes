package com.mha.jokes.controller;

import java.util.List;

/**
 * Provide endpoints with API information
 * 
 * @author michel
 * @version 0.0.1
 * 
 */

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

import com.mha.jokes.model.dto.CategoryDTO;
import com.mha.jokes.model.dto.JokeDTO;
import com.mha.jokes.model.dto.Message;
import com.mha.jokes.model.enumerated.Category;
import com.mha.jokes.service.JokesService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8080" })
@RestController
@RequestMapping("/service")
@Api(tags = "Jokes")
public class JokesController {

	@Autowired
	JokesService jokesService;

	
	/**
     * return a joke from any category
     * @return Optional<JokeDTO> 
     */
	@GetMapping("/joke")
	@ApiOperation(value = "take a joke")
	public ResponseEntity<?> getAnyJoke() {

		Optional<JokeDTO> response = jokesService.getAnyJoke();
		return response.isPresent() ? new ResponseEntity<>(response, HttpStatus.OK)
				: new ResponseEntity<>(new Message("You're out of jokes"), HttpStatus.OK);
	}

	/**
     * return a joke filtered by category
     * 
     * @param Optional<String> - filtered by category
     * @return Optional<JokeDTO> 
     */
	@GetMapping("/joke/{category}")
	@ApiOperation(value = "take a joke by category")
	public ResponseEntity<?> getCategorizedJoke(@PathVariable("category") Optional<String> category) {

		ResponseEntity<?> response = null;

		if (EnumUtils.isValidEnum(Category.class, category.get())) {

			Optional<JokeDTO> dto = jokesService.getCategorizedJoke(category);
			response = dto.isPresent() ? new ResponseEntity<>(jokesService.getCategorizedJoke(category), HttpStatus.OK)
					: new ResponseEntity<>(new Message("You're out of jokes"), HttpStatus.OK);

		}

		else {
			response = new ResponseEntity<>(new Message("Category not supported"), HttpStatus.BAD_REQUEST);
		}

		return response;

	}

	
	/**
     * received rate from users
     * 
     * @param Optional<Integer> joke id
     * @param Optional<Integer> grade to register (0-10)
     * @return ResponseEntity<?> 
     */
	@PostMapping("/rate/{id}/{grade}")
	@ApiOperation(value = "rate a joke. Please grades from 0 to 10 ")
	public ResponseEntity<?> registerRate(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> grade) {

		ResponseEntity<?> response = null;

		//ensure grade inside range (0 to 10)
		if (grade.get() < 0 || grade.get() > 10) {
			response = new ResponseEntity<>(new Message("This grade is not possible:\n Vote valid: 1 to 10"),
					HttpStatus.BAD_REQUEST);
		}

		//ensures that id belongs to a consumed register 
		else if (jokesService.getJokeById(id.get()).isEmpty()) {
			response = new ResponseEntity<>(new Message("This joke is not presents"), HttpStatus.BAD_REQUEST);
		}

		//register rate
		else {
			jokesService.registerRate(id, grade);
			response = new ResponseEntity<>(new Message("Joke " + id.get() + " was rated"), HttpStatus.OK);
		}

		return response;
	}

	/**
     * list all categories sorted by rate average
     * 
     * @return ResponseEntity<?> 
     */
	@GetMapping("/categories")
	@ApiOperation(value = "category list sorted by average")
	public ResponseEntity<List<CategoryDTO>> categoriesList() {

		return new ResponseEntity<>(jokesService.categoriesList(), HttpStatus.OK);
	}

	
	/**
     * list all jokes stored and not rated yet
     * 
     * @return ResponseEntity<?> 
     */
	@GetMapping("/unrated")
	@ApiOperation(value = "joke unrated list")
	public ResponseEntity<?> getUnrated() {

		return new ResponseEntity<>(jokesService.getUnratedJokes(), HttpStatus.OK);
	}

}
