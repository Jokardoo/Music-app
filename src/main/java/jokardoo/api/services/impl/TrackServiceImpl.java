package jokardoo.api.services.impl;

import jokardoo.api.domain.exceptions.TrackNotFoundException;
import jokardoo.api.domain.music.Artist;
import jokardoo.api.domain.music.Genre;
import jokardoo.api.domain.music.Track;
import jokardoo.api.repositories.TrackRepository;
import jokardoo.api.repositories.impl.TrackRepositoryImpl;
import jokardoo.api.services.ArtistService;
import jokardoo.api.services.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrackServiceImpl implements TrackService {

    private final TrackRepository trackRepository;
    private final ArtistService artistService;

    @Transactional(readOnly = true)
    @Override
    @Cacheable(value = "TrackService::getByName", key = "#name")
    public List<Track> getByName(String name) {
        return trackRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    @Override
    @Cacheable(value = "TrackService::getById", key = "#id")
    public Track getById(long id) {
        return trackRepository.findById(id);
    }

    @Transactional
    @Override
    @Caching(put = {
            @CachePut(value = "TrackService::getById", key = "#track.id"),
            @CachePut(value = "TrackService::getByName", key = "#track.name"),
            @CachePut(value = "TrackService::getByTrackNameAndArtistName", key = "#trackName + '.' + #artistName")
    })
    public Track update(Track track) {

        if (trackRepository.findById(track.getId()) != null) {
            trackRepository.update(track);
        }

        return track;
    }

    @Transactional(readOnly = true)
    @Override
    @Cacheable(value = "TrackService::getByTrackNameAndArtistName", key = "#trackName + '.' + #artistName")
    public List<Track> getByTrackNameAndArtistName(String trackName, String artistName) {
        return trackRepository.findByTrackNameAndArtistName(trackName, artistName);
    }

    @Transactional
    @Override
    @CacheEvict(value = "TrackService::getById", key = "#id")
    public void delete(Long id) {
        trackRepository.delete(id);
    }

    @Transactional
    @Override
    @Caching(cacheable = {
            @Cacheable(value = "TrackService::getById", key = "#track.id"),
            @Cacheable(value = "TrackService::getByName", key = "#track.name"),
            @Cacheable(value = "TrackService::getByTrackNameAndArtistName", key = "#trackName + '.' + #artistName")
    })
    public Track create(Track track, Genre genre) {
        Track createdTrack = trackRepository.create(track, genre);
        return createdTrack;

    }

    @Transactional
    @Override
    public void assignToArtistById(Long trackId, Long artistId) {
        trackRepository.assignToArtistById(trackId, artistId);
    }

    @Transactional
    @Override
    public void assignTrackToUserStorage(Long userId, Long trackId) {
        trackRepository.assignTrackToUserStorage(userId, trackId);
    }

    @Override
    @Cacheable(value = "TrackService::getOneByTrackNameAndArtistName", key = "#trackName + '.' + #artistName")
    public Track getOneByTrackNameAndArtistName(String trackName, String artistName) {
        Optional<Track> trackOptional = trackRepository.findOneByTrackNameAndArtistName(trackName, artistName);

        return trackOptional.orElseThrow(() -> new TrackNotFoundException("Track with this artist and track name not found!"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Track> getByGenre(String genre) {
        return trackRepository.findByGenre(genre);
    }



}
