package cl.randstad.common.security;

import static cl.randstad.common.helper.Constants.AUTHORIZATION;
import static cl.randstad.common.helper.Constants.BEARER_PREFIX;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import cl.randstad.common.exception.JwtValidationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
	private final JwtTokenService jwtTokenService;

    public JwtAuthorizationFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
    	
    	String path = request.getRequestURI();
        String method = request.getMethod();
        
    	if (path.equals("/users") && method.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        Optional<String> bearerToken = Optional.ofNullable(request.getHeader(AUTHORIZATION));

        if (bearerToken.isPresent() && bearerToken.get().startsWith(BEARER_PREFIX)) {
            String token = bearerToken.get().substring(BEARER_PREFIX.length());
            System.out.println("TOKEN: " + token);
            
            try {
	            if (jwtTokenService.validateToken(token)) {
	                UUID userId = jwtTokenService.getUserIdFromToken(token);
	
	                UsernamePasswordAuthenticationToken authentication =
	                        new UsernamePasswordAuthenticationToken(userId, null, null);
	
	                SecurityContextHolder.getContext().setAuthentication(authentication);
	            }
            }catch(JwtValidationException ex) {
            	throw ex;
            }
            
        } else {
            System.out.println("No hay token en header o no comienza con Bearer");
        }

        filterChain.doFilter(request, response);
    }
}
