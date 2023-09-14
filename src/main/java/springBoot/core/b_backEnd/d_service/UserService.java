package springBoot.core.b_backEnd.d_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import springBoot.core.b_backEnd.a_facade.UserFacade;
import springBoot.core.b_backEnd.c_model.User;
import springBoot.core.b_backEnd.e_dao.UserDaoRepository;
import springBoot.core.c_config.b_authentication.CustomAuthProvider;

import java.util.List;
import java.util.logging.Logger;

@Service
public class UserService {

    private final Logger JAVA_LOGGER = Logger.getLogger(UserService.class.getName());

    @Autowired
    private CustomJwt_token_Service customJwtTokenService;

    @Autowired
    private CustomAuthProvider authProvider;
    @Autowired
    private UserDaoRepository userDaoRepository;

    public String createUser(User user) {

        try{

            User foundUser = findByUsername(user.getUsername());

            if(foundUser != null)
                throw new Exception("User with this email already exists");


            user.setPassword(authProvider.getPasswordEncoder().encode(user.getPassword()));

            User user1 = userDaoRepository.save(user);
            if(user1 == null)
                throw new Exception("User failed to be saved in DB");

            String tokentFromSavedUser = customJwtTokenService.generateToken_withUserDetailsOnly(user1);

            if(tokentFromSavedUser == null)
                throw new Exception("Generating of the token has failed in the userService by passing newly saved user");

            return tokentFromSavedUser;

        }catch (Exception e){
            JAVA_LOGGER.warning("------------ " + e.getMessage());
            return null;
        }


    }

    public User findByUsername(String username) {
        try{
            if(username == null || username.isEmpty())
                throw new Exception("Field of the username must be specified");

            return userDaoRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username - " + username + " not found!"));

        }catch (Exception e){
            JAVA_LOGGER.warning("--------------  " + e.getMessage());
            return null;
        }

    }


}
