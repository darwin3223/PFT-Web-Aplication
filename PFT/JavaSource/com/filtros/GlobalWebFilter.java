package com.filtros;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.enums.TipoUsuario;
import com.utils.CookiesUtil;
import com.utils.JwtUtil;

@WebFilter("/app/*")
public class GlobalWebFilter implements Filter {
    @Inject private PermissionChecker permissionChecker;
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        Cookie cookie = null;
        try {
        cookie = CookiesUtil.getCookie("jwt", httpRequest);
        } catch (TokenExpiredException e) {
			e.printStackTrace();
		} catch (JWTVerificationException e) {
			e.printStackTrace();
		}
        
        if (cookie == null) {
        	httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
        }
        try {
        	JwtUtil.isValidJwt(cookie.getValue());
		} catch (TokenExpiredException e) {
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
			
		} catch (JWTVerificationException e) {
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
		}
        
    	
    	TipoUsuario tipo = JwtUtil.getTipoUsuarioFromJwt(cookie.getValue());
		String uri = httpRequest.getRequestURI().toString().replace("/app", "");
		try {
			if (permissionChecker.check(uri, tipo)) {
				chain.doFilter(request, response);
			} else {
				httpResponse.sendRedirect(httpRequest.getContextPath() + "/access-denied");
			}
		} catch (Exception e) {
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/app/welcome");
		}
    }

}
