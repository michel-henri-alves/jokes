package com.mha.jokes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class Flags {
	
	@Getter 
	@Setter
	private boolean nsfw;
	@Getter 
	@Setter
	private boolean religious;
	@Getter 
	@Setter
	private boolean political;
	@Getter 
	@Setter
	private boolean racist;
	@Getter 
	@Setter
	private boolean sexist;
	@Getter 
	@Setter
	private boolean explicit;

}
