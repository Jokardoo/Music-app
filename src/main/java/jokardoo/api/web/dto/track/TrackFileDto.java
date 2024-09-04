package jokardoo.api.web.dto.track;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TrackFileDto {

    @NotNull(message = "File must be not null.")
    private MultipartFile file;

}
