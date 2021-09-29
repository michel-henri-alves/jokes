package com.mha.jokes.model;

/**
 * object to store informations about jokes rate by category
 * 
 * @author michel
 * @version 0.0.1
 * 
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mha.jokes.model.enumerated.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryAvg {
	
	@Getter 
	@Setter
	private Category description;
	@Getter 
	@Setter
	private int nrOfRates;//qty of rates realized
	@Getter 
	@Setter
	private int total;//last total sum of votes
	@Getter
	@Setter
	private int avg;//last avg
	
	
	public void setParams(int nrOfRates, int total, int avg) {
		this.setAvg(avg);
		this.setNrOfRates(nrOfRates);
		this.setTotal(total);
	}
}
