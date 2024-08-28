package jokardoo.api.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Jwt Response")
public class JwtResponse {

    private long id;
    private String username;
    private String accessToken;
    private String refreshToken;
}
