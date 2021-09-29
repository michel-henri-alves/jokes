package com.mha.jokes.controller;

/**
 * unit test JokeController
 * 
 * @author michel
 * @version 0.0.1
 * 
 */

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

import java.util.AbstractMap;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import com.mha.jokes.model.Joke;
import com.mha.jokes.model.dto.JokeDTO;
import com.mha.jokes.model.enumerated.Category;
import com.mha.jokes.service.JokesService;
import com.mha.jokes.util.ConvertEntityToDTO;

import io.restassured.http.ContentType;

@WebMvcTest
public class JokesControllerTest {

	@Autowired
	private JokesController jokesController;
	@MockBean
	private JokesService jokesService;
	@MockBean
	private RestTemplate template;
	@MockBean
	ConvertEntityToDTO entityToDTO;

	@BeforeEach
	public void setup() {
		standaloneSetup(this.jokesController);
	}
	
	@Test
	public void shouldReturnOK_getAnyJoke() {

		Optional<JokeDTO> dto = Optional.of(new JokeDTO(1, "Papai noel tem o saco de brinquedo!"));
		
		// mock da classe service
		when(this.jokesService.getAnyJoke())
				.thenReturn(dto);

		given().accept(ContentType.JSON).when().get("/service/joke").then()
				.statusCode(HttpStatus.OK.value())
		        .body(is(equalTo("{\"id\":1,\"joke\":\"Papai noel tem o saco de brinquedo!\"}")));
	}
	


	@Test
	public void shouldReturnOK_WhenGetCategorizedJoke() {

		// mock da classe service
		when(this.jokesService.getCategorizedJoke(Optional.of(Category.Christmas.name())))
				.thenReturn(Optional.of(new JokeDTO(1, "Papai noel tem o saco de brinquedo!")));

		given().accept(ContentType.JSON).when().get("/service/joke/{category}", Category.Christmas.name()).then()
				.statusCode(HttpStatus.OK.value());
	}

	@Test
	public void shouldReturnBADREQUEST_WhenGetCategorizedJoke() {

		given().accept(ContentType.JSON).when().get("/service/joke/{category}", "Sports").then()
				.statusCode(HttpStatus.BAD_REQUEST.value());

	}
	
	@Test
	public void shouldReturnEmpty_WhenGetCategorizedJoke() {

		// mock da classe service
		when(this.jokesService.getCategorizedJoke(Optional.of(Category.Christmas.name())))
				.thenReturn(null);

		given().accept(ContentType.JSON).when().get("/service/joke").then()
				.statusCode(HttpStatus.OK.value())
		        .body(is(equalTo("{\"message\":\"You're out of jokes\"}")));
	}

	@Test
	public void shouldReturnOK_WhenPostRegisterRate() {

		when(this.jokesService.getJokeById(1))
				.thenReturn(Optional.of(new AbstractMap.SimpleEntry<>(new Joke(), false)));

		given().accept(ContentType.JSON).when().post("/service/rate/{id}/{grade}", 1, 10).then()
				.statusCode(HttpStatus.OK.value());

	}

	@Test
	public void shouldReturnBADREQUEST_WhenPostRegisterRateWithGradeBiggerThan10() {

		given().accept(ContentType.JSON).when().post("/service/rate/{id}/{grade}", 1, 11).then()
				.statusCode(HttpStatus.BAD_REQUEST.value());

	}

	@Test
	public void shouldReturnBADREQUEST_WhenPostRegisterRateWithGradeLessThan0() {

		given().accept(ContentType.JSON).when().post("/service/rate/{id}/{grade}", 1, -1).then()
				.statusCode(HttpStatus.BAD_REQUEST.value());

	}

	@Test
	public void shouldReturnBADREQUEST_WhenPostRegisterRateWithJokeNotRegistered() {

		when(this.jokesService.getJokeById(1)).thenReturn(Optional.empty());

		given().accept(ContentType.JSON).when().post("/service/rate/{id}/{grade}", 1, 10).then()
				.statusCode(HttpStatus.BAD_REQUEST.value());

	}

}
