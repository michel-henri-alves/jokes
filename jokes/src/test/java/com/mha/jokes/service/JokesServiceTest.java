package com.mha.jokes.service;

/**
 * unit test JokeService
 * 
 * @author michel
 * @version 0.0.1
 * 
 */

import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.mha.jokes.controller.JokeAPIController;
import com.mha.jokes.model.CategoryAvg;
import com.mha.jokes.model.Flags;
import com.mha.jokes.model.Joke;
import com.mha.jokes.model.dto.JokeDTO;
import com.mha.jokes.model.enumerated.Category;
import com.mha.jokes.model.enumerated.JokeType;
import com.mha.jokes.model.enumerated.Language;

@SpringBootTest
public class JokesServiceTest {

	@Autowired
	private JokesService jokesService;
	@MockBean
	private JokeAPIController jokeAPIController;

	@BeforeEach
	public void setup() {
		standaloneSetup(this.jokesService);
	}

	@Test
	public void shouldSetValueToTrue_updateJokeRated() {

		Joke _joke = new Joke(1, true, Language.en, Category.Christmas, JokeType.single,
				"Papai Noel tem o saco de brinquedo", null, null, new Flags(), false);

		jokesService.addToJoke(_joke);
		jokesService.updateJokeRated(_joke);

		assertEquals(jokesService.getJokeById(1).get().getValue(), true);
	}
	
	@Test
	public void shouldSetCategoryAvg_updateCategoriesParameters() {

		jokesService.getCategories().add(new CategoryAvg(Category.Christmas, 1, 10, 10)); 
		jokesService.updateCategoriesParameters(Category.Christmas, 2, 8, 9);
		
		assertEquals(jokesService.getCategoryAvgByCategory(Category.Christmas).get().getAvg(), 9);
	}
	

	
	@Test
	public void shouldRegisterRate_registerRate() {
		
		Joke _joke1 = new Joke(1, true, Language.en, Category.Christmas, JokeType.single,
				"Papai Noel tem o saco de brinquedo", null, null, new Flags(), true);
		
		CategoryAvg _category1 = new CategoryAvg(Category.Christmas, 1,10,10);
		CategoryAvg expected = new CategoryAvg(Category.Christmas, 2,18,9);
		
		jokesService.addToJoke(_joke1);
		jokesService.getCategories().add(_category1);
		
		jokesService.registerRate(Optional.of(1), Optional.of(8));
		jokesService.registerRate(Optional.of(1), Optional.of(10));
		
		assertThat(jokesService.getCategoryAvgByCategory(Category.Christmas).get()).usingRecursiveComparison().isEqualTo(expected);

	}
}
