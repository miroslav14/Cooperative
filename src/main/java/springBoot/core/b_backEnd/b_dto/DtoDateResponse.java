package springBoot.core.b_backEnd.b_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DtoDateResponse {
    private String date;
    private JSONObject error;

    public DtoDateResponse(String status, String errorMessage,String errorCode){

        JSONObject errorMessg = new JSONObject(status);

        JSONObject properties = new JSONObject("properties");

        JSONObject code = new JSONObject();
        code.put("description", errorCode);
        properties.put("code",code);

        JSONObject message = new JSONObject();
        message.put("description", errorMessage);
        properties.put("message",message);

        errorMessg.put("properties", properties);

    }
}
