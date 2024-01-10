package com.authorization;

import com.entities.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthHttpResponse {
	
	private String token;
	private Usuario usuario;
}
