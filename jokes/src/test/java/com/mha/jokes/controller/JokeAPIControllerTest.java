package com.mha.jokes.controller;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import com.mha.jokes.model.Flags;
import com.mha.jokes.model.Joke;
import com.mha.jokes.model.dto.JokeDTO;
import com.mha.jokes.model.enumerated.Category;
import com.mha.jokes.model.enumerated.JokeType;
import com.mha.jokes.model.enumerated.Language;
import com.mha.jokes.service.JokesService;
import com.mha.jokes.util.ConvertEntityToDTO;

@WebMvcTest
public class JokeAPIControllerTest {

	@Autowired
	private JokeAPIController jokeAPIController;
	@MockBean
	private JokesService jokesService;
	@MockBean
	private RestTemplate template;
	@MockBean
	ConvertEntityToDTO entityToDTO;

	@BeforeEach
	public void setup() {
		standaloneSetup(this.jokeAPIController);
	}

	@Test	
	public void shouldReturnExpectedJokeDTO_WhenConsumingJoke() {

		// objetos mockados
		JokeDTO _expected = new JokeDTO(1, "Papai Noel tem o saco de brinquedo");
		Joke _joke = new Joke(1, true, Language.en, Category.Christmas, JokeType.single,
				"Papai Noel tem o saco de brinquedo", null, null, new Flags(), false);

		// mock da classe service
		when(this.jokesService.getJokeById(Optional.ofNullable(1))).thenReturn(Optional.empty());

		// mock da resposta da API
		when(this.template.getForObject("https://v2.jokeapi.dev/joke/Any?safe-mode", Joke.class)).thenReturn(_joke);

		// mock do conversor para DTO
		when(this.entityToDTO.mappingObjects(_joke, JokeDTO.class))
				.thenReturn(new JokeDTO(1, "Papai Noel tem o saco de brinquedo"));

		JokeDTO in = jokeAPIController.consumingJoke("Any");

		assertThat(in).usingRecursiveComparison().isEqualTo(_expected);

	}

	@Test
	public void shouldReturnVoidJokeDTO_WhenConsumingJokeButAllJokeAlreadyExistsConsumed() {

		// objetos mockados
		JokeDTO expected = null;
		Joke alreadyExist = new Joke(1, true, Language.en, Category.Christmas, JokeType.single,
				"Papai Noel tem o saco de brinquedo", null, null, new Flags(), false);

		HashMap<Joke, Boolean> jokeAndParameters = new HashMap<Joke, Boolean>();
		jokeAndParameters.put(alreadyExist, false);// para este teste um Joke ja foi adicionado

		// mock da classe service
		when(this.jokesService.getJokeById(Optional.ofNullable(1)))
				.thenReturn(Optional.of(new AbstractMap.SimpleEntry<>(alreadyExist, false)));

		// mock da resposta da API

			when(this.template.getForObject("https://v2.jokeapi.dev/joke/Any?safe-mode", Joke.class))
					.thenReturn(alreadyExist);

		// mock do conversor para DTO
		when(this.entityToDTO.mappingObjects(expected, JokeDTO.class))
				.thenReturn(null);

		JokeDTO in = jokeAPIController.consumingJoke("Any");

		assertThat(in).usingRecursiveComparison().isEqualTo(expected);

	}

}
