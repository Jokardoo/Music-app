package jokardoo.api.web.dto.track;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jokardoo.api.web.dto.validation.OnCreate;
import jokardoo.api.web.dto.validation.OnUpdate;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "TrackDTO")
public class TrackDto {

    @Schema(description = "Track id", example = "17")
    @NotNull(message = "Track id cannot be null!", groups = OnUpdate.class)
    private Long id;

    @Schema(description = "Track name", example = "Synth pop")
    private String trackGenre;

    @Schema(description = "Name", example = "Blinding lights")
    @NotNull(message = "Track name cannot be null! If you dont know track name, input 'unknown' in this area", groups = {OnUpdate.class, OnCreate.class})
    @Length(max = 255, message = "Track name should be smaller than 255 character")
    private String name;


    @Schema(description = "Artist name", example = "The Weeknd")
    @NotNull(message = "Artist name cannot be null! If you dont know track name, input 'unknown' in this area")
    @Length(max = 255, message = "Artist name should be smaller than 255 character")
    private String artist;

    @Schema(description = "Download link", example = "rus.hitmotop.com/song/123456789")
    private String downloadLink;

    @Schema(description = "Full time in seconds", example = "211")
    private int fullTime;
}
