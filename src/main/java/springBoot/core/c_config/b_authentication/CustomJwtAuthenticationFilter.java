package springBoot.core.c_config.b_authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import springBoot.core.b_backEnd.d_service.CustomJwt_token_Service;

import java.io.IOException;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class CustomJwtAuthenticationFilter extends OncePerRequestFilter {

    private final Logger JAVA_LOGGER = Logger.getLogger(CustomJwtAuthenticationFilter.class.getName());
    @Autowired
    private CustomJwt_token_Service customJwtTokenService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt_token;
        final String username;


        //here we check the header
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        //initialize jwt_token
        jwt_token = authHeader.substring(7);
        //extract username from the token
        username = customJwtTokenService.extractUsername(jwt_token);
        //if we have username and user is not authenticated
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            //we get the userdetails from the database with unique username passed as parameter
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            //here we check if the user and token are valid
            if(customJwtTokenService.isTokenValid(jwt_token, userDetails)){
                //we create new object of type of UsernamePasswordAuthenticationToken with three parameters (userdetails, credentials, authorities)
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                //here we enforce the authToken object with the details of our request
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }

        //here we have to call the filterChain method
        filterChain.doFilter(request,response);
    }
}