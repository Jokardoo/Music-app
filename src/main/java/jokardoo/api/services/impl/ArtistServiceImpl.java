package jokardoo.api.services.impl;

import jokardoo.api.domain.exceptions.ArtistNotFoundException;
import jokardoo.api.domain.music.Artist;
import jokardoo.api.domain.music.Track;
import jokardoo.api.repositories.ArtistRepository;
import jokardoo.api.services.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;

    @Override
    @Transactional(readOnly = true)
//    @Cacheable(value = "ArtistService::getByName", key = "#name")
    public Artist getByName(String name) {
        return artistRepository.findByName(name).orElseThrow(() -> new ArtistNotFoundException("Artist with this name not found!"));
    }

    @Override
    @Transactional(readOnly = true)
//    @Cacheable(value = "ArtistService::getById", key = "#id")
    public Artist getById(Long id) {
        return artistRepository.findById(id).orElseThrow(() -> new ArtistNotFoundException("Artist with this id not found!"));
    }

    @Override
    @Transactional
//    @CacheEvict(value = "ArtistService::findById", key = "#id")
    public void delete(Long id) {
        artistRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Artist> getByGenre(String genre) {
        return artistRepository.findByGenre(genre);
    }

    @Override
    @Transactional
//    @Caching(cacheable = {
//            @Cacheable(value = "ArtistService::getByName", key = "#name")
//    })
    public Artist create(String name, String description) {
        Artist artist = new Artist();
        artist.setName(name);
        artist.setDescription(description);

        artistRepository.save(artist);

        return artist;
    }

    @Override
    @Transactional
//    @Caching(put = {
//            @CachePut(value = "ArtistService::getByName", key = "#artist.name"),
//            @CachePut(value = "ArtistService::")
//    })
    public void update(Artist artist) {
        artistRepository.save(artist);
    }

    @Override
    @Transactional(readOnly = true)
//    @Cacheable(value = "ArtistService::isArtistContains", key = "#artistName")
    public boolean isArtistContains(String artistName) {
        return artistRepository.isArtistContains(artistName);
    }

    @Override
//    @Cacheable(value = "ArtistService::getTracksByArtistName", key = "#artistName")
    public List<Track> getTracksByArtistName(String artistName) {
        return artistRepository.findTracksByArtistName(artistName);
    }

}
