package com.mha.jokes.service;

/**
 * business logic over informations to make available in REST services
 * 
 * @author michel
 * @version 0.0.1
 * 
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mha.jokes.model.CategoryAvg;
import com.mha.jokes.model.Joke;
import com.mha.jokes.model.dto.CategoryDTO;
import com.mha.jokes.model.dto.JokeDTO;
import com.mha.jokes.model.enumerated.Category;

import lombok.Getter;

@Service
public class JokesService {

	@Autowired
	JokesAPIService jokesAPIService;

	
	@Getter
	private List<CategoryAvg> categories = new ArrayList<>();
	@Getter
	private HashMap<Joke, Boolean> jokes = new HashMap<>(); // boolean param = joke rated

	JokesService() {

		//populate array w/ initial parameters
		categories.add(new CategoryAvg(Category.Christmas, 0, 0, 0));
		categories.add(new CategoryAvg(Category.Dark, 0, 0, 0));
		categories.add(new CategoryAvg(Category.Misc, 0, 0, 0));
		categories.add(new CategoryAvg(Category.Programming, 0, 0, 0));
		categories.add(new CategoryAvg(Category.Pun, 0, 0, 0));
		categories.add(new CategoryAvg(Category.Spooky, 0, 0, 0));

	}

	/**
     * 
     * category by any category
     * 
     * @return Optional<JokeDTO> 
     */
	public Optional<JokeDTO> getAnyJoke() {

		return jokesAPIService.consumingJoke("/Any");
	}

	/**
     * joke by category
     * 
     * @param Optional<String> category to filter
     * @return Optional<JokeDTO> 
     */
	public Optional<JokeDTO> getCategorizedJoke(Optional<String> category) {

		return jokesAPIService.consumingJoke(category.get());
	}

	
	/**
     * received rate from users
     * 
     * @param Optional<Integer> joke id
     * @param Optional<Integer> grade to register (0-10)
     * @return ResponseEntity<?> 
     */
	public void registerRate(Optional<Integer> id, Optional<Integer> grade) {

		//retrieve joke registered by id
		Optional<Entry<Joke, Boolean>> jokeAndParameters = getJokeById(id.get());

		//retrieve category by description
		Optional<CategoryAvg> categoryAvg = getCategoryAvgByCategory(
				jokeAndParameters.get().getKey().getCategory());

		//perform calculations to obtain the average
		Joke joke = jokeAndParameters.get().getKey();
		int nrOfRates = categoryAvg.get().getNrOfRates() + 1;
		int lastTotal = categoryAvg.get().getTotal() + grade.get();
		int lastAvg = lastTotal / nrOfRates;

		//update new infos in list
		updateCategoriesParameters(joke.getCategory(), nrOfRates, lastTotal, lastAvg);
		
		//update joke register to rated
		if (!jokeAndParameters.get().getValue())
			updateJokeRated(joke);

	}

	/**
     * list of categories sorted by rate avg
     * 
     * @return List<CategoryDTO>
     */
	public List<CategoryDTO> categoriesList() {

		return this.getCategories().parallelStream().sorted(Comparator.comparingInt(CategoryAvg::getAvg).reversed())
				.map(element -> new CategoryDTO(element.getDescription(), element.getAvg()))
				.collect(Collectors.toList());

		}

	/**
     * list jokes register without rate
     * 
     * @return Stream<Object>
     */
	public Stream<Object> getUnratedJokes() {

		return this.getJokes().entrySet().parallelStream().filter(element -> element.getValue() == false)
				.map(element -> new JokeDTO(element.getKey().getId(), element.getKey().getJoke()));

	}

	/**
     * return joke register by id
     * 
     * @param int id
     * @return Optional<Entry<Joke, Boolean>>
     */
	public Optional<Entry<Joke, Boolean>> getJokeById(int id) {

		return this.getJokes().entrySet().parallelStream().filter(element -> id == element.getKey().getId())
				.findFirst();

	}

	/**
     * return category register by description
     * 
     * @param Category
     * @return Optional<CategoryAvg>
     */
	public Optional<CategoryAvg> getCategoryAvgByCategory(Category category) {

		return this.getCategories().parallelStream().filter(element -> element.getDescription().equals(category))
				.findFirst();

	}

	/**
     * update avg values of category register
     * 
     * @param Category
     * @param int - qty of rates realized
     * @param int - last total sum of votes
     * @param int - last avg
     * @return
     */
	public void updateCategoriesParameters(Category category, int nrOfRates, int total, int avg) {

		this.categories.stream().filter(e -> e.getDescription().equals(category))
				.forEach(e -> e.setParams(nrOfRates, total, avg));
	}

	/**
     * update joke register to rated (true)
     * 
     * @param Joke
     * @return
     */
	public void updateJokeRated(Joke joke) {

		this.jokes.entrySet().stream().filter(element -> element.getKey().getId() == joke.getId())
				.map(element -> element.setValue(true)).collect(Collectors.toList());

	}

	/**
     * add a new joke register
     * 
     * @param Joke
     * @return
     */
	public void addToJoke(Joke joke) {
		this.jokes.put(joke, false);
	}

}
