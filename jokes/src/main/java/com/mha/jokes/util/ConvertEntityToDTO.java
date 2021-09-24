package com.mha.jokes.util;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ConvertEntityToDTO {

	// converte objetos simples para o DTO
	public <E> E mappingObjects(Object entity, Class<E> dto) {
		
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(entity, dto);
	}

}
