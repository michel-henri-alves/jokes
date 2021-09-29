package com.mha.jokes.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mha.jokes.controller.JokesController;
import com.mha.jokes.model.Flags;
import com.mha.jokes.model.Joke;
import com.mha.jokes.model.dto.CategoryDTO;
import com.mha.jokes.model.dto.JokeDTO;
import com.mha.jokes.model.enumerated.Category;
import com.mha.jokes.model.enumerated.JokeType;
import com.mha.jokes.model.enumerated.Language;
import com.mha.jokes.service.JokesService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JokesControllerIntegrationTest {

	@LocalServerPort
	private int port;
	TestRestTemplate testRestTemplate = new TestRestTemplate();
	HttpHeaders headers = new HttpHeaders();
	@Autowired
	private JokesService jokesService;
	@Autowired
	private JokesController jokesController;

	private static final Logger log = LoggerFactory.getLogger(JokesController.class);

	public String getUrl(String uri) {
		return "http://localhost:" + port + uri;
	}

	@Test
	public void shouldReturnOK_getAnyJoke() throws Exception {

		HttpEntity<Object> entity = new HttpEntity<>(null, headers);
		ResponseEntity<String> responseEntity = testRestTemplate.exchange(getUrl("/service/joke"), HttpMethod.GET,
				entity, String.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void shouldReturnEmpty_getAnyJoke() {

		HttpEntity<Object> entity = new HttpEntity<>(null, headers);
		ResponseEntity<String> responseEntity = null;

//		int counter = 344;
		int counter = 100; // so pra deixar mais rapido

		while (counter != 0) {
			counter--;
			log.info("Remains " + counter);
			responseEntity = testRestTemplate.exchange(getUrl("/service/joke"), HttpMethod.GET, entity, String.class);
			log.info("new joke arrived: " + responseEntity.getBody());
		}

		assertEquals("{\"message\":\"You're out of jokes\"}", responseEntity.getBody());
	}

	@Test
	public void shouldReturnBADREQUEST_WhenGetCategorizedJoke() {

		HttpEntity<Object> entity = new HttpEntity<>(null, headers);
		ResponseEntity<String> responseEntity = testRestTemplate.exchange(getUrl("/service/joke/Sports"),
				HttpMethod.GET, entity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

	}

	@Test
	public void shouldSuccess_WhenPostRegisterRate() {

		jokesService.addToJoke(new Joke(1, true, Language.en, Category.Christmas, JokeType.single,
				"Papai Noel tem o saco de brinquedo", null, null, new Flags(), false));

		HttpEntity<Object> entity = new HttpEntity<>(null, headers);
		ResponseEntity<String> responseEntity = testRestTemplate.exchange(getUrl("/service/rate/" + 1 + "/10"),
				HttpMethod.POST, entity, String.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void shouldReturnBADREQUEST_WhenPostRegisterRateWithGradeBiggerThan10() {

		jokesService.addToJoke(new Joke(1, true, Language.en, Category.Christmas, JokeType.single,
				"Papai Noel tem o saco de brinquedo", null, null, new Flags(), false));

		HttpEntity<Object> entity = new HttpEntity<>(null, headers);
		ResponseEntity<String> responseEntity = testRestTemplate.exchange(getUrl("/service/rate/" + 1 + "/11"),
				HttpMethod.POST, entity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

	}

	@Test
	public void shouldReturnBADREQUEST_WhenPostRegisterRateWithGradeLessThan0() {

		jokesService.addToJoke(new Joke(1, true, Language.en, Category.Christmas, JokeType.single,
				"Papai Noel tem o saco de brinquedo", null, null, new Flags(), false));

		HttpEntity<Object> entity = new HttpEntity<>(null, headers);
		ResponseEntity<String> responseEntity = testRestTemplate.exchange(getUrl("/service/rate/" + 1 + "/-1"),
				HttpMethod.POST, entity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

	}

	@Test
	public void shouldReturnBADREQUEST_WhenPostRegisterRateWithJokeNotRegistered() {

		HttpEntity<Object> entity = new HttpEntity<>(null, headers);
		ResponseEntity<String> responseEntity = testRestTemplate.exchange(getUrl("/service/rate/2/10"), HttpMethod.POST,
				entity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

	}

	@Test
	public void shouldListCategoriesSortedByAvg_WhenGetCategoriesList() {

		// limpar a lista
		jokesService.getJokes().clear();

		jokesService.addToJoke(new Joke(1, true, Language.en, Category.Christmas, JokeType.single,
				"Papai Noel tem o saco de brinquedo", null, null, new Flags(), false));
		jokesService.addToJoke(new Joke(2, true, Language.en, Category.Spooky, JokeType.single, "boo", null, null,
				new Flags(), false));

		jokesController.registerRate(Optional.of(1), Optional.of(10));
		jokesController.registerRate(Optional.of(2), Optional.of(9));

		List<String> expected = List.of(Category.Christmas.name(), Category.Spooky.name());

		ResponseEntity<CategoryDTO[]> responseEntity = testRestTemplate.getForEntity(getUrl("/service/categories"),
				CategoryDTO[].class);

		List<String> in = List.of(responseEntity.getBody()[0].getCategory().name(),
				responseEntity.getBody()[1].getCategory().name());

		assertEquals(expected, in);

	}

	@Test
	public void shouldListJustUnratedJokes_WhenGetUnratedList() {

//		 limpar a lista
		jokesService.getJokes().clear();

		jokesService.addToJoke(new Joke(1, true, Language.en, Category.Christmas, JokeType.single,
				"Papai Noel tem o saco de brinquedo", null, null, new Flags(), false));
		jokesService.addToJoke(
				new Joke(2, true, Language.en, Category.Spooky, JokeType.single, "boo", null, null, new Flags(), false));
		
		HttpEntity<Object> entity = new HttpEntity<>(null, headers);
		ResponseEntity<String> _responseEntity = testRestTemplate.exchange(getUrl("/service/rate/1/10"),
				HttpMethod.POST, entity, String.class);
		
		
		ResponseEntity<JokeDTO[]> responseEntity = testRestTemplate.getForEntity(getUrl("/service/unrated"),
				JokeDTO[].class);

		assertEquals("boo", responseEntity.getBody()[0].getJoke());

	}

}
