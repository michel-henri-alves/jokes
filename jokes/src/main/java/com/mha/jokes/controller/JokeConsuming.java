package com.mha.jokes.controller;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mha.jokes.model.Joke;
import com.mha.jokes.model.JokeDTO;
import com.mha.jokes.util.ConvertEntityToDTO;

@Service
public class JokeConsuming {

	@Autowired
	private RestTemplate template;
	@Autowired
	ConvertEntityToDTO entityToDTO;

	private HashMap<Joke, SimpleEntry<Integer,Integer>> jokes = new HashMap<Joke, AbstractMap.SimpleEntry<Integer, Integer>>();

	private static final Logger log = LoggerFactory.getLogger(JokesController.class);
	private static String CONSUMING_URL = "https://v2.jokeapi.dev/joke/";

	public JokeDTO consumingJoke(String category) {

		Joke joke;

		do
			joke = this.template.getForObject(CONSUMING_URL + category + "?safe-mode", Joke.class);
		while (jokes.get(joke) != null);
		
		jokes.put(joke, new AbstractMap.SimpleEntry(0,0));
		
		return entityToDTO.mappingObjects(joke, JokeDTO.class);

	}

	@Bean
	public RestTemplate template(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	public HashMap<Joke, Integer> getConsumedJokes(){
		return this.jokes;
	}
	
}
