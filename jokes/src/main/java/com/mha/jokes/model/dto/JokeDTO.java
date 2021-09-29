package com.mha.jokes.model.dto;

import io.swagger.annotations.ApiModelProperty;

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
	
    @ApiModelProperty(notes = "Joke ID")
	@Getter
	@Setter
	private int id;
    @ApiModelProperty(notes = "A joke")
	@Getter
	@Setter
	private String joke;

}
