package net.dickode.naima.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.dickode.naima.service.UserServiceImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Predicate;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j @Component
public class JwtAutorisationFilter extends OncePerRequestFilter {


    private final UserServiceImpl userService;

    public JwtAutorisationFilter(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)  throws ServletException, IOException {

        final List<String> apiEndpoints = List.of("/api/login","/playground", "/report","/uploads","/graphql");

        Predicate<HttpServletRequest> isApiSecured = r -> apiEndpoints.stream()
                .noneMatch(uri -> r.getRequestURI().contains(uri));

        if (!isApiSecured.test(request)){
            chain.doFilter(request,response);
            return;
        }

        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null  && authorizationHeader.startsWith("Bearer ")) {

            try {

                String token = authorizationHeader.substring(7);
                Algorithm algorithm = Algorithm.HMAC256(this.userService.getJwtSecrete().getBytes(StandardCharsets.UTF_8));
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                String username = decodedJWT.getSubject();

                String [] roles = decodedJWT.getClaim("roles").asArray(String.class);
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

                if (roles != null){
                    for (String role : roles) {
                        authorities.add(new SimpleGrantedAuthority(role));
                    }
                }

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username,null,authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                chain.doFilter(request,response);


            } catch (JWTVerificationException exception) {

                exception.printStackTrace();
                response.setHeader("Error",exception.getMessage());

                response.setStatus(FORBIDDEN.value());
                Map<String,String> error = new HashMap<>();
                error.put("message","Token d'authentification invalide !");
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);

            }

        }else {

            log.error("FORBIDDEN ! : " + authorizationHeader);
            log.error("FORBIDDEN ! : " + authorizationHeader);
            response.setStatus(FORBIDDEN.value());
            Map<String,String> error = new HashMap<>();
            error.put("message","Non autorisé !");
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(),error);

        }

    }


}
