package com.mha.jokes.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;

import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.mha.jokes.controller.JokesController;
import com.mha.jokes.model.Flags;
import com.mha.jokes.model.Joke;
import com.mha.jokes.model.dto.CategoryDTO;
import com.mha.jokes.model.enumerated.Category;
import com.mha.jokes.model.enumerated.JokeType;
import com.mha.jokes.model.enumerated.Language;
import com.mha.jokes.service.JokesService;

@SpringBootTest//(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JokesControllerIntegrationTest {

	@Autowired
	private JokesService jokesService;
	@Autowired
	private JokesController jokesController;
	@Autowired
	private RestTemplate template;

	@Test
	public void shouldReturnOK_getAnyJoke() {

		ResponseEntity<?> response = jokesController.getAnyJoke();
		assertEquals(response.getStatusCode().value(), HttpStatus.OK.value());

	}
//
//	@Test
//	public void shouldReturnEmpty_getAnyJoke() {
//
//		ResponseEntity<?> response = null;
//		int counter = 101;
//
//		while (counter != 0) {
//			counter--;
//			System.out.println(counter);
//			response = jokesController.getAnyJoke();
//		}
//
//		assertEquals(response.getBody(), "{\"message\":\"You're out of jokes\"}");
//	}
//
//	@Test
//	public void shouldReturnBADREQUEST_WhenGetCategorizedJoke() {
//
//		ResponseEntity<?> response = jokesController.getCategorizedJoke(Optional.of("Sports"));
//		assertEquals(response.getStatusCode().value(), HttpStatus.BAD_REQUEST.value());
//
//	}
//
//	@Test
//	public void shouldSuccess_WhenPostRegisterRate() {
//
//		jokesService.addToJoke(new Joke(1, true, Language.en, Category.Christmas, JokeType.single,
//				"Papai Noel tem o saco de brinquedo", null, null, new Flags(), false));
//
//		ResponseEntity<?> response = jokesController.registerRate(Optional.of(1), Optional.of(10));
//		assertEquals(response.getStatusCode().value(), HttpStatus.OK.value());
//
//	}
//
//	@Test
//	public void shouldReturnBADREQUEST_WhenPostRegisterRateWithGradeBiggerThan10() {
//
//		ResponseEntity<?> response = jokesController.registerRate(Optional.of(1), Optional.of(11));
//		assertEquals(response.getStatusCode().value(), HttpStatus.BAD_REQUEST.value());
//
//	}
//
//	@Test
//	public void shouldReturnBADREQUEST_WhenPostRegisterRateWithGradeLessThan0() {
//
//		ResponseEntity<?> response = jokesController.registerRate(Optional.of(1), Optional.of(-1));
//		assertEquals(response.getStatusCode().value(), HttpStatus.BAD_REQUEST.value());
//
//	}
//
//	@Test
//	public void shouldReturnBADREQUEST_WhenPostRegisterRateWithJokeNotRegistered() {
//
//		ResponseEntity<?> response = jokesController.registerRate(Optional.of(500), Optional.of(10));
//		assertEquals(response.getStatusCode().value(), HttpStatus.BAD_REQUEST.value());
//
//	}

//	@Test
//	public void shouldListCategoriesSortedByAvg_WhenGetCategoriesList() {
//
//		jokesService.addToJoke(new Joke(1, true, Language.en, Category.Christmas, JokeType.single,
//				"Papai Noel tem o saco de brinquedo", null, null, new Flags(), false));
//		jokesService.addToJoke(new Joke(2, true, Language.en, Category.Spooky, JokeType.single, "boo", null, null,
//				new Flags(), false));
//
//		jokesController.registerRate(Optional.of(1), Optional.of(10));
//		jokesController.registerRate(Optional.of(2), Optional.of(9));
//
//		List<String> expected = List.of(Category.Christmas.name(),
//				Category.Spooky.name());
//
//		CategoryDTO list = template.getForObject(
//				  "http://localhost:8080/service/categories",
//				  CategoryDTO.class);
//		List<String> result = List.of(list.get(0).getCategory().name(),
//				list.get(0).getCategory().name());
//		
//		// primeiro deve estart Christmas e em 2nd spooky
//		assertLinesMatch(result, expected);
//
//	}

//	@Test
//	public void shouldListJustUnratedJokes_WhenGetUnratedList() {
//
//		jokesService.addToJoke(new Joke(1, true, Language.en, Category.Christmas, JokeType.single,
//				"Papai Noel tem o saco de brinquedo", null, null, new Flags(), false));
//		jokesService.addToJoke(new Joke(2, true, Language.en, Category.Spooky, JokeType.single, "boo", null, null,
//				new Flags(), false));
//
//		jokesController.registerRate(Optional.of(1), Optional.of(10));
//
//		ResponseEntity<?> response = jokesController.getUnrated();
//
//		// deve ter apenas o 2nd elemento
////		assertEquals(response.getBody(), HttpStatus.BAD_REQUEST.value());
//
//	}

}
