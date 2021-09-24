package com.mha.jokes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
	
	@Getter 
	@Setter
	private int id;
	@Getter 
	@Setter
	private boolean safe;
	@Getter 
	@Setter
	private Language lang;
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
	
	public String getJoke() {
		return this.type == JokeType.single ? this.joke : "- "+this.setup +"\n - "+this.delivery; 
	}

}
