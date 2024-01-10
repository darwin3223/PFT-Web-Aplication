package com.utils;

import java.util.Date;
import java.util.UUID;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.primefaces.PrimeFaces;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.enums.TipoUsuario;

@Stateless
@LocalBean
public class JwtUtil {
	
	private static final String jwtKey = "This is an ultra secret key";
	private static Algorithm algorithm = Algorithm.HMAC256(jwtKey);
	
	public static String createJwt(String username, TipoUsuario tipoUsuario, long idUsuario) {
		Long currentTime = System.currentTimeMillis();
		String jwt = JWT.create()
				.withIssuer("GEDDAF")
				.withSubject(username)
				.withClaim("idUsuario", idUsuario)
				.withClaim("tipoUsuario", tipoUsuario.name())
				.withIssuedAt(new Date(currentTime))
				.withExpiresAt(new Date(currentTime + 1800000))//1800000 60000
				.withJWTId(UUID.randomUUID().toString())
				.sign(algorithm);
		return jwt;
	}

	public static boolean isValidJwt(String jwt) throws TokenExpiredException, JWTVerificationException{
		JWTVerifier verifier = JWT.require(algorithm)
				.withIssuer("GEDDAF")
				.build();	
		
		verifier.verify(jwt);
	    return true;
    }
	
	public static String getNombreUsuarioFromJwt(String jwt) {
		try {
			DecodedJWT decodedJWT = getDecodedFromJwt(jwt);
			return decodedJWT.getSubject().toString();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Long getIdFromJwt(String jwt) {
		try {
			DecodedJWT decodedJWT = getDecodedFromJwt(jwt);
			return decodedJWT.getClaim("idUsuario").asLong();			
		} catch (Exception e) {
			return null;
		}
    }
	
	public static TipoUsuario getTipoUsuarioFromJwt(String jwt) {
		try {
			DecodedJWT decodedJWT = getDecodedFromJwt(jwt);
			return TipoUsuario.valueOf(decodedJWT.getClaim("tipoUsuario").asString()); 
		} catch (Exception e) {
			return null;
		}
    }
	
	private static DecodedJWT getDecodedFromJwt(String jwt) throws JWTVerificationException {
		JWTVerifier verifier = JWT.require(algorithm).withIssuer("GEDDAF").build();		
		DecodedJWT decodedJWT = verifier.verify(jwt);
	    return decodedJWT;
	}
	
	public static long getTimeUntilTokenExpiration(String jwt) {
        try {
            DecodedJWT decodedJWT = getDecodedFromJwt(jwt);
            Date expirationDate = decodedJWT.getExpiresAt();
            long currentTime = System.currentTimeMillis();
            return expirationDate.getTime() - currentTime;
        } catch (Exception e) {
            return 0;
        }
    }
	
}