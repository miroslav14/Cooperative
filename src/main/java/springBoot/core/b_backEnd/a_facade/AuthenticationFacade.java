package springBoot.core.b_backEnd.a_facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springBoot.core.b_backEnd.b_dto.DtoAuthenticationRequest;
import springBoot.core.b_backEnd.b_dto.DtoAuthenticationResponse;
import springBoot.core.b_backEnd.d_service.AuthenticationService;

import java.util.logging.Logger;

@Component
public class AuthenticationFacade {
    private final Logger JAVA_LOGGER = Logger.getLogger(AuthenticationFacade.class.getName());
    @Autowired
    private AuthenticationService authenticationService;


    public DtoAuthenticationResponse authenticate(DtoAuthenticationRequest dtoAuthentication)  {
        try{
            String token = authenticationService.authenticateToken(dtoAuthentication.getUsername(),dtoAuthentication.getPassword());
            if(token == null)
                throw new NullPointerException("Token value received from authenticationService was null");

            return new DtoAuthenticationResponse(token);
        }catch (Exception e){
            JAVA_LOGGER.warning("----------- " + e.getMessage());
            return null;
        }
    }

}
