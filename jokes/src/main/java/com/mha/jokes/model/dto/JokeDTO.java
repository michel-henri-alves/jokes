package com.mha.jokes.model.dto;

/**
 * data transfer object to joke
 * 
 * @author michel
 * @version 0.0.1
 * 
 */


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class JokeDTO {
	
	@Getter
	@Setter
	private int id;
	@Getter
	@Setter
	private String joke;

}
