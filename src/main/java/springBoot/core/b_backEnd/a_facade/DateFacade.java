package springBoot.core.b_backEnd.a_facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springBoot.core.b_backEnd.b_dto.DtoDateRequest;
import springBoot.core.b_backEnd.b_dto.DtoDateResponse;
import springBoot.core.b_backEnd.d_service.DateService;

import java.util.Optional;
import java.util.logging.Logger;

@Component
public class DateFacade {

    private final Logger JAVA_LOGGER = Logger.getLogger(DateFacade.class.getName());

    @Autowired
    private DateService dateService;

    public DtoDateResponse validateUserInputDate(Optional<DtoDateRequest> dtoDate, Optional<String> name) {
        try{
            return dateService.processValidation(dtoDate,name);
        }catch (Exception e){
            JAVA_LOGGER.warning("---------  " + e.getMessage());
            return null;
        }
    }
}
