package com.mha.jokes.controller;

/**
 * unit test JokesAPIService
 * 
 * @author michel
 * @version 0.0.1
 * 
 */

import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import com.mha.jokes.model.Flags;
import com.mha.jokes.model.Joke;
import com.mha.jokes.model.dto.JokeDTO;
import com.mha.jokes.model.enumerated.Category;
import com.mha.jokes.model.enumerated.JokeType;
import com.mha.jokes.model.enumerated.Language;
import com.mha.jokes.service.JokesAPIService;
import com.mha.jokes.service.JokesService;
import com.mha.jokes.util.ConvertEntityToDTO;

@SpringBootTest
public class JokeAPIControllerTest {

	@Autowired
	private JokesAPIService jokeAPIService;
	@MockBean
	private JokeAPIController jokeAPIController;
	@MockBean
	private JokesService jokesService;
	@MockBean
	private RestTemplate template;
	@MockBean
	ConvertEntityToDTO entityToDTO;

	@BeforeEach
	public void setup() {
		standaloneSetup(this.jokeAPIService);
	}

	@Test	
	public void shouldReturnExpectedJokeDTO_WhenConsumingJoke() {

		// objetos mockados
		Optional<JokeDTO> _expected = Optional.of (new JokeDTO(1, "Papai Noel tem o saco de brinquedo"));
		Optional<Joke> _joke = Optional.of (new Joke(1, true, Language.en, Category.Christmas, JokeType.single,
				"Papai Noel tem o saco de brinquedo", null, null, new Flags(), false));

		// mock da classe service
		HashMap<Joke, Boolean> jokes = new HashMap<>();
		jokes.put(_joke.get(), false);
		when(this.jokesService.getJokeById(1)).thenReturn(Optional.empty());
		when(this.jokesService.getJokes()).thenReturn(jokes);

		// mock da resposta da API
		when(this.jokeAPIController.consumingJoke(Category.Christmas.name())).thenReturn(_joke);
			
		// mock do conversor para DTO
		when(this.entityToDTO.mappingObjects(_joke.get(), JokeDTO.class))
				.thenReturn(new JokeDTO(1, "Papai Noel tem o saco de brinquedo"));

		Optional<JokeDTO> in = jokeAPIService.consumingJoke("Christmas");

		assertThat(in).usingRecursiveComparison().isEqualTo(_expected);

	}


}
