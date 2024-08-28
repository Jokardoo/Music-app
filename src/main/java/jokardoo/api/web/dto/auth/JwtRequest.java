package jokardoo.api.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "JWT Request")
public class JwtRequest {

    @Schema(description = "username", example = "JohnBoy")
    @NotNull(message = "username must be not null")
    private String username;

    @Schema(description = "password", example = "12345")
    @NotNull(message = "Password must be not null")
    private String password;
}
