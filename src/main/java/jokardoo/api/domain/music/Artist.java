package jokardoo.api.domain.music;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

// the class represents the artist, the genres in which he performs and the list of his tracks
@Data
public class Artist implements Serializable {
    private Long id;
    private String name;
    private String description;
    private Set<String> genres;
    private List<Track> tracks;

    public Artist(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public Artist() {

    }
}
