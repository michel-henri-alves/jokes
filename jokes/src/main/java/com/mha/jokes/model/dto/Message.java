package com.mha.jokes.model.dto;

/**
 * data transfer object to message
 * 
 * @author michel
 * @version 0.0.1
 * 
 */

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
public class Message {
	
	@Getter
	@Setter
	private String message;

}
