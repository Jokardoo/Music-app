package jokardoo.api.web.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jokardoo.api.domain.music.Artist;
import jokardoo.api.domain.music.Track;
import jokardoo.api.web.dto.validation.OnCreate;
import jokardoo.api.web.dto.validation.OnUpdate;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.Set;

@Data   //lombok
@Schema(description = "User DTO")   // Используется для swagger'a
public class UserDto {

    @Schema(description = "User id", example = "27")   // Название поля класса для Swagger'a и его пример
    @NotNull(message = "User id cannot be null!", groups = OnUpdate.class)
    private Long id;

    @Schema(description = "Username", example = "JohnBoy")   // Название поля класса для Swagger'a и его пример
    @NotNull(message = "Username cannot be null!")
    @Length(max = 255, message = "Username characters cannot be bigger than 255", groups = {OnCreate.class, OnUpdate.class})
    private String username;

    @Schema(description = "Name", example = "John")    // Название поля класса для Swagger'a и его пример
    @NotNull(message = "Name cannot be null!")
    @Length(max = 255, message = "Name characters cannot be bigger than 255", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @Email
    @Schema(description = "Email", example = "john@gmail.com")  // Название поля класса для Swagger'a и его пример
    @NotNull(message = "Email cannot be null!")
    @Length(max = 255, message = "Email characters cannot be bigger than 255", groups = {OnCreate.class, OnUpdate.class})
    private String email;

    @Schema(description = "Password", example = "1234567")    // Название поля класса для Swagger'a и его пример
    @NotNull(message = "Password cannot be null!")
    @Length(min = 6, max = 255, message = "Password should be bigger than 6 and smaller than 255", groups = {OnCreate.class, OnUpdate.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    // Все Dto, которые к нам будут приходить, не будут иметь поля пароля
    private String password;

    @Schema(description = "Password confirmation", example = "1234567")
    @NotNull(message = "Password confirmation cannot be null!")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    // Все Dto, которые к нам будут приходить, не будут иметь поля пароля
    @Length(min = 6, message = "Password should be bigger than 6", groups = OnCreate.class)
    private String passwordConfirmation;

    private List<Track> tracks;

    private Set<Artist> artists;

}
