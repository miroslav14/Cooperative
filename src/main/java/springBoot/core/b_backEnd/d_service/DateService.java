package springBoot.core.b_backEnd.d_service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import springBoot.core.b_backEnd.b_dto.DtoDateRequest;
import springBoot.core.b_backEnd.b_dto.DtoDateResponse;
import springBoot.core.c_config.a_security.WorkingDays;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

@Service
public class DateService {

    private final Logger JAVA_LOGGER = Logger.getLogger(DateService.class.getName());


    public DtoDateResponse processValidation(Optional<DtoDateRequest> dtoDateRequest, Optional<String> name) {
       return processing_Of_dateValue(dtoDateRequest,name);
    }

    private DtoDateResponse processing_Of_dateValue(Optional<DtoDateRequest> dtoDateRequest, Optional<String> name){
        try{

            DtoDateResponse response = new DtoDateResponse();

            if((dtoDateRequest == null && name == null)
                    || ((dtoDateRequest.get().getDate() == null || dtoDateRequest.get().getDate().equals(""))
                    && (name.get() == null || name.get().equals("")) ) ){
               throw new Exception("All properties were empty");
            }


            if((dtoDateRequest != null && name != null) && (dtoDateRequest.get().getDate() != null && !dtoDateRequest.get().getDate().equals(""))){
                 String newDate = calculateDate(dtoDateRequest.get().getDate(), name.get());

                 if(newDate == null || newDate.equals(""))
                     throw new Exception("Date format was wrong");


                 response.setDate(newDate);
                 return response;
            }



            return null;

        }catch (Exception e){
            JAVA_LOGGER.warning("------------  " + e.getMessage());
            return null;
        }
    }

    private String calculateDate(String date , String name){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, MMM dd yyyy");

            if(name.equals("after")) {
                return nextDate(date, formatter);
            }else {
                LocalDate localDate = LocalDate.now();
                String newDate = localDate.format(formatter);

                return nextDate(newDate, formatter);
            }

        }catch (Exception e){
            return null;
        }

    }

    private String nextDate(String date, DateTimeFormatter formatter){
        try {
            LocalDate today = LocalDate.parse(date, formatter);

            boolean isNextDay = true;

            int counter = 1;


            while (isNextDay) {
                String[] workingDays = {"Mon", "Tue", "Wed", "Thu", "Fri"};

                LocalDate followingDay = today.plusDays(0 + counter);
                String newDate = followingDay.format(formatter);

                for (String day : workingDays) {
                    if (newDate.startsWith(day)) {
                        isNextDay = false;
                        return newDate;
                    }
                }

                counter++;
            }
            return null;

        }catch (Exception e){
            return null;
        }
    }

}
