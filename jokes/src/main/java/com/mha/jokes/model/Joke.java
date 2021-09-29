package com.mha.jokes.model;

/**
 * pure joke object from API
 * 
 * @author michel
 * @version 0.0.1
 * 
 */


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mha.jokes.model.enumerated.Category;
import com.mha.jokes.model.enumerated.JokeType;
import com.mha.jokes.model.enumerated.Language;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Joke {
	
	@ApiModelProperty(notes = "Joke ID")
	@Getter 
	@Setter
	private int id;
	@Getter 
	@Setter
	private boolean safe;
	@Getter 
	@Setter
	private Language lang;
	@ApiModelProperty(notes = "Joke category")
	@Getter
	@Setter
	private Category category;
	@Getter 
	@Setter
	private JokeType type;
	@Setter
	private String joke;
	@Getter 
	@Setter
	private String setup;
	@Getter 
	@Setter
	private String delivery;
	@Getter 
	@Setter
	private Flags flags;
	@Getter 
	@Setter
	private boolean error;	
	
	/**
     * case joke-object is single-type set value only using joke and case twopart concatenate setup+delivery
     * 
     * @return String 
     */
	@ApiModelProperty(notes = "A Joke")
	public String getJoke() {
		return this.type == JokeType.single ? this.joke : "- "+this.setup +"\n - "+this.delivery; 
	}

}
