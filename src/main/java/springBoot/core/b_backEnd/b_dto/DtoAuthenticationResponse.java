package springBoot.core.b_backEnd.b_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DtoAuthenticationResponse {
    private String token;
}
