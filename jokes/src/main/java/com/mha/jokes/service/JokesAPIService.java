package com.mha.jokes.service;

/**
 * business logic over informations received by JokeAPI
 * 
 * @author michel
 * @version 0.0.1
 * 
 */

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mha.jokes.controller.JokeAPIController;
import com.mha.jokes.model.Joke;
import com.mha.jokes.model.dto.JokeDTO;
import com.mha.jokes.util.ConvertEntityToDTO;

@Service
public class JokesAPIService {

	@Autowired
	private JokeAPIController jokeAPIController;
	@Autowired
	private ConvertEntityToDTO entityToDTO;
	@Autowired
	private JokesService jokesService;
//	private int TOTAL_JOKES = 343;
	private int TOTAL_JOKES = 100;

	/**
     * 
     * @param category filter by category
     * @return Optional<JokeDTO> 
     */
	public Optional<JokeDTO> consumingJoke(String category) {

		Optional<Joke> joke = Optional.ofNullable(null);
		Optional<JokeDTO> dto = Optional.ofNullable(null);
		
		//checks if the joke has already been consumed, otherwise it searches for a new record
		do {

			joke = jokeAPIController.consumingJoke(category).or(() -> Optional.of(new Joke()));

		} while (jokesService.getJokeById(joke.get().getId()).isPresent()); 

		
		//if limit provided in the API doc is 343 jokes not reached and convert to DTO
		if (joke.isPresent() && jokesService.getJokes().size() < TOTAL_JOKES) {

			jokesService.addToJoke(joke.get());
			dto = Optional.of(entityToDTO.mappingObjects(joke.get(), JokeDTO.class));
		} 
		
		//API limit case reached
		else {

			dto = Optional.empty();
		}

		return dto;
	}

}
