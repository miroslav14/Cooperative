package springBoot.core.b_backEnd.a_facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springBoot.core.b_backEnd.b_dto.DtoAuthenticationResponse;
import springBoot.core.b_backEnd.b_dto.DtoUser;
import springBoot.core.b_backEnd.c_model.User;
import springBoot.core.b_backEnd.d_service.UserService;
import springBoot.core.c_config.a_security.Role;

import java.util.logging.Logger;

@Component
public class UserFacade {

    private final Logger JAVA_LOGGER = Logger.getLogger(UserFacade.class.getName());
    @Autowired
    private UserService userService;

    public DtoAuthenticationResponse saveUserFromDTO_produceToken(DtoUser dtoUser) {

        try {
            if (dtoUser == null)
                throw new Exception("Dto object in Facade layer was null");

            User user = new User();
            user.setFirstname(dtoUser.getFirstname());
            user.setUsername(dtoUser.getUsername());
            user.setLastname(dtoUser.getLastname());
            user.setPassword(dtoUser.getPassword());
            boolean isAddedAuthority = user.add_GrantedAuthority(Role.REST_USER);
            user.setAccountNonLocked(true);
            user.setEnabled(true);
            user.setCredentialsNonExpired(true);
            user.setAccountNonExpired(true);

            String tokenFromSavedUser = userService.createUser(user);

            if (tokenFromSavedUser == null)
                throw new Exception("User vas not saved in DB");

            return new DtoAuthenticationResponse(tokenFromSavedUser);

        } catch (Exception e) {
            JAVA_LOGGER.warning("--------- " + e.getMessage());
            return null;
        }

    }


}
