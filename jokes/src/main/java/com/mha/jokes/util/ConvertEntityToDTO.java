package com.mha.jokes.util;

/**
 * Convert object to DTO
 * 
 * @author michel
 * @version 0.0.1
 * 
 */

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ConvertEntityToDTO {

	/**
	 * object to DTO
	 * 
	 * @param Object   - object to be converted
	 * @param Class<E> - dto for conversion
	 * @return <E> E
	 */
	public <E> E mappingObjects(Object entity, Class<E> dto) {

		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(entity, dto);
	}

}
