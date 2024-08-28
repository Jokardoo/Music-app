package jokardoo.api.web.dto.artist;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jokardoo.api.domain.music.Track;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.Set;

@Data
@Schema(description = "Artist DTO")     // Используется для swagger'a
public class ArtistDto {

    @Schema(description = "Artist id", example = "7")
    @NotNull(message = "Id cannot be null!")
    private Long id;

    @Schema(description = "Name", example = "Muse")
    @NotNull(message = "Artist name cannot be null! If you dont know track name, input 'unknown' in this area")
    @Length(max = 255, message = "Artist name should be smaller than 255!")
    private String name;


    @Length(max = 255, message = "Description length should be smaller than 255!")
    private String description;
    private Set<String> genres;
    private List<Track> tracks;
}
