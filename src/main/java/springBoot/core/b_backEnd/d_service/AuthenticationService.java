package springBoot.core.b_backEnd.d_service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import springBoot.core.b_backEnd.c_model.User;
import springBoot.core.c_config.b_authentication.CustomAuthProvider;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final Logger JAVA_LOGGER = Logger.getLogger(AuthenticationService.class.getName());
    @Autowired
    private CustomAuthProvider customAuthProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomJwt_token_Service customJwtTokenService;


    public String authenticateToken(String username, String password) {
        try {

            if ((username == null || username.equals("")) || (password == null || password.equals("")))
                throw new Exception("Parameters required for authentication were missing");

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            User user = userService.findByUsername(username);
            if (user == null)
                throw new Exception("User does not exist in DB");

            String token = customJwtTokenService.generateToken_withUserDetailsOnly(user);

            if (token == null)
                throw new Exception("AuthenticationService received null from generation of the token from user " + user.toString());

            return token;

        } catch (Exception e) {
            JAVA_LOGGER.warning("-------------- " + e.getMessage());
            return null;
        }
    }
}
