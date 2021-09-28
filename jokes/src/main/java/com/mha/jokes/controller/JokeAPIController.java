package com.mha.jokes.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import com.mha.jokes.model.Joke;
import com.mha.jokes.model.dto.JokeDTO;
import com.mha.jokes.service.JokesService;
import com.mha.jokes.util.ConvertEntityToDTO;

@Controller
public class JokeAPIController {

	@Autowired
	private RestTemplate template;
	@Autowired
	ConvertEntityToDTO entityToDTO;
	@Autowired
	JokesService jokesService;

	private static final Logger log = LoggerFactory.getLogger(JokesController.class);
	private static String CONSUMING_URL = "https://v2.jokeapi.dev/joke/";

	public JokeDTO consumingJoke(String category) {

		Joke joke;
		int counter = 0;

		do {

			counter++;
			try {
			
				joke = this.template.getForObject(CONSUMING_URL + category + "?safe-mode", Joke.class);
	
			} catch (Exception e) {
				joke = null;
				log.error(e.getMessage());
			}

		} while (jokesService.getJokeById(Optional.ofNullable(joke.getId())).isPresent() && counter < 100);

		if (counter == 100) {
			joke = null;
		} else {
			jokesService.addToJoke(joke);
		}

		return entityToDTO.mappingObjects(joke, JokeDTO.class);

	}

	@Bean
	private RestTemplate template(RestTemplateBuilder builder) {
		return builder.build();
	}

}
