package jokardoo.api.domain.music;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tracks")
public class Track implements Serializable {

    // Вынужденная мера, т.к. данный класс изменялся в то время, как был Serializable
    private static final long serialVersionUID = 5177273350523880333L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trackgenre")
    private String trackGenre;
    private String name;

    private String artist;

    @CollectionTable(name = "artists_tracks")
    @JoinColumn(name = "artist_id")
    @ManyToOne
    private Artist artistObj;

    @Column(name = "download_link")
    private String downloadLink;

    @Transient
    private transient Map<Integer, String> originalTextMap;
    @Transient
    private transient Map<Integer, String> translateTextMap;

    @Column(name = "full_time")
    private int fullTime;

    @Column(name = "file")
    private String file;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return fullTime == track.fullTime && name.equals(track.name) && artist.equals(track.artist) && downloadLink.equals(track.downloadLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, artist, downloadLink, fullTime);
    }
}
