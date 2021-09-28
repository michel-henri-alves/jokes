package com.mha.jokes.util;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mha.jokes.model.Flags;
import com.mha.jokes.model.Joke;
import com.mha.jokes.model.dto.JokeDTO;
import com.mha.jokes.model.enumerated.Category;
import com.mha.jokes.model.enumerated.JokeType;
import com.mha.jokes.model.enumerated.Language;

@SpringBootTest
public class ConvertEntityToDTOTest {

	@Autowired
	private ConvertEntityToDTO convertEntityToDTO;

	@BeforeEach
	public void setup() {
		standaloneSetup(this.convertEntityToDTO);
	}

	@Test
	public void shouldReturnDTO_mappingObjects() {
		
		JokeDTO expected = new JokeDTO(1, "Papai Noel tem o saco de brinquedo");		
		Joke _joke = new Joke(1, true, Language.en, Category.Christmas, JokeType.single,
				"Papai Noel tem o saco de brinquedo", null, null, new Flags(), false);
		
		assertThat(convertEntityToDTO.mappingObjects(_joke, JokeDTO.class)).usingRecursiveComparison().isEqualTo(expected);
		
	}

}
