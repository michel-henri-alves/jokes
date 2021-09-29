package com.mha.jokes.model.dto;


/**
 * data transfer object to category
 * 
 * @author michel
 * @version 0.0.1
 * 
 */

import com.mha.jokes.model.enumerated.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
	
	@Getter
	@Setter
	private Category category;
	@Getter
	@Setter
	private int avg;

}
