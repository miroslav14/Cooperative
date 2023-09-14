package springBoot.core.a_frontEnd.restApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springBoot.core.b_backEnd.a_facade.AuthenticationFacade;
import springBoot.core.b_backEnd.a_facade.UserFacade;
import springBoot.core.b_backEnd.b_dto.DtoAuthenticationRequest;
import springBoot.core.b_backEnd.b_dto.DtoAuthenticationResponse;
import springBoot.core.b_backEnd.b_dto.DtoUser;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/public/auth/api")
@Tag(name = "Authentication of Users")
public class PublicRestController {

    private final Logger JAVA_LOGGER = Logger.getLogger(PublicRestController.class.getName());
    @Autowired
    private UserFacade userFacade;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @PostMapping(value="/register",consumes = "application/json",produces="application/json")
    @Operation(summary = "Register user as json object with 4 attributes")
    public ResponseEntity<Object> registerUserDetails(@RequestBody DtoUser dtoUser){

        try {

            if(dtoUser == null)
                return new ResponseEntity<>("Details of the user were missing", HttpStatus.NO_CONTENT);

            DtoAuthenticationResponse dtoAuthenticationResponse = userFacade.saveUserFromDTO_produceToken(dtoUser);

            if(dtoAuthenticationResponse == null)
                return new ResponseEntity<>("User was not saved", HttpStatus.NOT_MODIFIED);

            return new ResponseEntity<>(dtoAuthenticationResponse.toString(),HttpStatus.CREATED);
        } catch (Exception e) {
            JAVA_LOGGER.warning("------------- " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }

    }


    @PostMapping(value = "/authentication",consumes = "application/json",produces="application/json")
    @Operation(summary = "Authenticate user with the user's credentials consisted of 2 parameters - username and password")
    public ResponseEntity<Object> authenticateUserDetails(@RequestBody Optional<DtoAuthenticationRequest> dtoAuthenticationRequest){
        try{

            if(dtoAuthenticationRequest == null || dtoAuthenticationRequest.get() == null)
                    throw new Exception("required parameters were missing");


            DtoAuthenticationResponse dtoAuthenticationResponse = authenticationFacade.authenticate(dtoAuthenticationRequest.get());

            if(dtoAuthenticationResponse == null)
                throw new Exception("Authentication failed");

            JSONObject jsonAuthResponse = new JSONObject();
            jsonAuthResponse.put("authResponse",dtoAuthenticationResponse);

            return new ResponseEntity<>(jsonAuthResponse.get("authResponse"),HttpStatus.OK);

        }catch (Exception e){
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("error"," " + e.getMessage());
            return new ResponseEntity<>(errorResponse.get("error"),HttpStatus.UNAUTHORIZED);
        }

    }


//    @PostMapping(value = "/authentication",consumes = "application/json",produces="application/json")
//    public ResponseEntity<Object> authenticateUserDetails(@RequestBody Optional<DtoAuthenticationRequest> dtoAuthenticationRequest, @RequestParam("name") Optional<String> nameID){
//
////        {
////            "username": "shakazulu",
////                "password": "1234567"
////        }
//        JAVA_LOGGER.info("------------------ request parameter had value " + nameID);
//
//        try{
//
//
//            if(dtoAuthenticationRequest == null || dtoAuthenticationRequest.get() == null) {
//                if(nameID == null || nameID.get().isEmpty() ||nameID.get().equals("")) {
//                    JAVA_LOGGER.warning("+++++++++++++++++++++ request parameter was not specified");
//                    throw new Exception("required parameters were missing");
//                }
//            }
//
//
//            DtoAuthenticationResponse dtoAuthenticationResponse = facadeAuthentication.authenticate(dtoAuthenticationRequest.get());
//
//            if(dtoAuthenticationResponse == null)
//                throw new Exception("dto response is null");
//            //return new ResponseEntity<>("DtoAuthenticationResponse was null so FacadeAuthentication did not transfer the data correctly", HttpStatus.NO_CONTENT);
//
//            JSONObject errorResponse = new JSONObject(dtoAuthenticationResponse);
//            return new ResponseEntity<>(errorResponse,HttpStatus.OK);
//
//        }catch (Exception e){
//            JSONObject errorResponse = new JSONObject();
//            errorResponse.put("error",new DtoAuthenticationResponse("prooooooooooooblme"));
//            return new ResponseEntity<>(errorResponse.get("error"),HttpStatus.UNAUTHORIZED);
//        }
//
//    }
}
