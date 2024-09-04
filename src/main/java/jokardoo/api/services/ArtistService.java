package jokardoo.api.services;

import jokardoo.api.domain.music.Artist;
import jokardoo.api.domain.music.Track;

import java.util.List;
import java.util.Set;

public interface ArtistService {
    Artist getByName(String name);

    Artist getById(Long id);

    void delete(Long id);

    Set<Artist> getByGenre(String genre);

    List<Track> getTracksByArtistName(String artistName);

    Artist create(String name, String description);

    public void update(Artist artist);

    boolean isArtistContains(String artistName);
}
