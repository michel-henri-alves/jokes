package com.mha.jokes.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mha.jokes.controller.JokeAPIController;
import com.mha.jokes.controller.JokesController;
import com.mha.jokes.model.CategoryAvg;
import com.mha.jokes.model.Joke;
import com.mha.jokes.model.dto.CategoryDTO;
import com.mha.jokes.model.dto.JokeDTO;
//import com.mha.jokes.model.enumerated.Category;
import com.mha.jokes.model.enumerated.Category;

import lombok.Getter;

@Service
public class JokesService {

	@Autowired
	JokeAPIController jokeAPIController;

	// triplet values 1st- numbers of rates, 2nd- last sum 3rd- last avg
	@Getter
	private List<CategoryAvg> categories = new ArrayList<>();
	@Getter
	private HashMap<Joke, Boolean> jokes = new HashMap<>(); // boolean param = joke rated
	private static final Logger log = LoggerFactory.getLogger(JokesController.class);

	JokesService() {

		categories.add(new CategoryAvg(Category.Christmas, 0, 0, 0));
		categories.add(new CategoryAvg(Category.Dark, 0, 0, 0));
		categories.add(new CategoryAvg(Category.Misc, 0, 0, 0));
		categories.add(new CategoryAvg(Category.Programming, 0, 0, 0));
		categories.add(new CategoryAvg(Category.Pun, 0, 0, 0));
		categories.add(new CategoryAvg(Category.Spooky, 0, 0, 0));

	}

	public JokeDTO getAnyJoke() {

		return jokeAPIController.consumingJoke("/Any");
	}

	public JokeDTO getCategorizedJoke(Optional<String> category) {

		return jokeAPIController.consumingJoke(category.get());
	}

	public void registerRate(Optional<Integer> id, Optional<Integer> grade) {

		Optional<Entry<Joke, Boolean>> jokeAndParameters = getJokeById(id);

		Optional<CategoryAvg> categoryAvg = getCategoryAvgByCategory(
				jokeAndParameters.get().getKey().getCategory());

		Joke joke = jokeAndParameters.get().getKey();
		int nrOfRates = categoryAvg.get().getNrOfRates() + 1;
		int lastTotal = categoryAvg.get().getTotal() + grade.get();
		int lastAvg = lastTotal / nrOfRates;

		updateCategoriesParameters(joke.getCategory(), nrOfRates, lastTotal, lastAvg);

		if (!jokeAndParameters.get().getValue())
			updateJokeRated(joke);

	}

	/*
	 * 
	 */
	public List<CategoryDTO> categoriesList() {

		return this.getCategories().parallelStream().sorted(Comparator.comparingInt(CategoryAvg::getAvg).reversed())
				.map(element -> new CategoryDTO(element.getDescription(), element.getAvg()))
				.collect(Collectors.toList());

		}

	/*
	 * 
	 */
	public Stream<Object> getUnratedJokes() {

		return this.getJokes().entrySet().parallelStream().filter(element -> element.getValue() == false)
				.map(element -> new JokeDTO(element.getKey().getId(), element.getKey().getJoke()));

	}

	public Optional<Entry<Joke, Boolean>> getJokeById(Optional<Integer> id) {

		return this.getJokes().entrySet().parallelStream().filter(element -> id.get() == element.getKey().getId())
				.findFirst();

	}


	public Optional<CategoryAvg> getCategoryAvgByCategory(Category category) {

		return this.getCategories().parallelStream().filter(element -> element.getDescription().equals(category))
				.findFirst();

	}

	/*
	 * 
	 */
	public void updateCategoriesParameters(Category category, int nrOfRates, int total, int avg) {

		this.categories.stream().filter(e -> e.getDescription().equals(category))
				.forEach(e -> e.setParams(nrOfRates, total, avg));
	}

	/*
	 * 
	 */
	public void updateJokeRated(Joke joke) {

		this.jokes.entrySet().stream().filter(element -> element.getKey().getId() == joke.getId())
				.map(element -> element.setValue(true)).collect(Collectors.toList());

	}

	/*
	 * 
	 */
	public void addToJoke(Joke joke) {
		this.jokes.put(joke, false);
	}

}
