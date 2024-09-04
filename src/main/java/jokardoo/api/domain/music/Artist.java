package jokardoo.api.domain.music;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

// the class represents the artist, the genres in which he performs and the list of his tracks
@Data
@Entity
@Table(name = "artists")
public class Artist implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @JoinTable(inverseJoinColumns = @JoinColumn(name = "track_id"))
    @OneToMany
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
