package com.mha.jokes.model;

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
	private int nrOfRates;
	@Getter 
	@Setter
	private int total;
	@Getter
	@Setter
	private int avg;
	
	public void setParams(int nrOfRates, int total, int avg) {
		this.setAvg(avg);
		this.setNrOfRates(nrOfRates);
		this.setTotal(total);
	}
}
