package com.mha.jokes.controller;

/**
 * Consuming by JokeAPI (https://v2.jokeapi.dev/) in safe-mode
 * 
 * @author michel
 * @version 0.0.1
 * 
 */

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import com.mha.jokes.model.Joke;

@Controller
public class JokeAPIController {

	@Autowired
	private RestTemplate template;

	private static final Logger log = LoggerFactory.getLogger(JokesController.class);
	private static String CONSUMING_URL = "https://v2.jokeapi.dev/joke/";

	/**
     * consuming a joke
     * 
     * @param category filter by category
     * @return Optional<Joke> 
     */
	public Optional<Joke> consumingJoke(String category) {

		Optional<Joke> response = Optional.ofNullable(null);
		
		try {
			response = Optional.ofNullable(this.template.getForObject(CONSUMING_URL + category + "?safe-mode", Joke.class));

		} catch (Exception e) {
			log.error("Falha ao recuperar dados do API: "+e.getMessage());

		}
		return response;

	}

	/**
     * 
     * bean to build RestTemplate
     *
     */
	@Bean
	private RestTemplate template(RestTemplateBuilder builder) {
		return builder.build();
	}

}
