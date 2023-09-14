package springBoot.core.a_frontEnd.restApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springBoot.core.b_backEnd.a_facade.DateFacade;
import springBoot.core.b_backEnd.b_dto.DtoDateRequest;
import springBoot.core.b_backEnd.b_dto.DtoDateResponse;

import java.util.Optional;
import java.util.logging.Logger;

@RestController
@Tag(name = "Private controller to get next working day")
public class PrivateRestController {

    private final Logger JAVA_LOGGER = Logger.getLogger(PrivateRestController.class.getName());

    @Autowired
    private DateFacade dateFacade;

    @GetMapping(value="/next-working-day",produces="application/json")
    @Operation(summary = "Get request to receive the next working day after we specify the date either in param or in ")
    public ResponseEntity<Object> getHomePage(@RequestBody Optional<DtoDateRequest> dtoDate, @RequestParam(name="name") Optional<String> name){


        try{

            DtoDateResponse dtoDateResponse = dateFacade.validateUserInputDate(dtoDate,name);

            if (dtoDateResponse == null)
                 throw new Exception("The after date is not a valid date");

            JSONObject correctDate = new JSONObject();
            correctDate.put("description","Next working day");
            correctDate.put("date",dtoDateResponse.getDate());

            return new ResponseEntity<>(correctDate, HttpStatus.OK);

        }catch (Exception e){
            DtoDateResponse errorResponse = new DtoDateResponse(HttpStatus.BAD_REQUEST.name(),e.getMessage(),"400");
            JSONObject jsonObject = new JSONObject("ErrorResponse");
            jsonObject.put("description",errorResponse.getError());
            return new ResponseEntity<>(jsonObject, HttpStatus.BAD_REQUEST);

        }
    }
}
