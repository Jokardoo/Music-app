package jokardoo.api.services.impl;

import jokardoo.api.domain.exceptions.TrackNotFoundException;
import jokardoo.api.domain.music.Track;
import jokardoo.api.domain.music.TrackFile;
import jokardoo.api.repositories.TrackRepository;
import jokardoo.api.services.ArtistService;
import jokardoo.api.services.FileService;
import jokardoo.api.services.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrackServiceImpl implements TrackService {

    private final TrackRepository trackRepository;
    private final ArtistService artistService;
    private final FileService fileService;

    @Transactional(readOnly = true)
    @Override
//    @Cacheable(value = "TrackService::getByName", key = "#name")
    public List<Track> getAllByName(String name) {
        return trackRepository.findAllByName(name);
    }

    @Transactional(readOnly = true)
    @Override
//    @Cacheable(value = "TrackService::getById", key = "#id")
    public Track getById(long id) {
        return trackRepository.findById(id).orElseThrow();
    }

    @Transactional
    @Override
//    @Caching(put = {
//            @CachePut(value = "TrackService::getById", key = "#track.id"),
//            @CachePut(value = "TrackService::getByName", key = "#track.name"),
//            @CachePut(value = "TrackService::getByTrackNameAndArtistName", key = "#trackName + '.' + #artistName")
//    })
    public Track save(Track track) {
        trackRepository.save(track);
        return track;
    }

    @Transactional(readOnly = true)
    @Override
//    @Cacheable(value = "TrackService::getByTrackNameAndArtistName", key = "#trackName + '.' + #artistName")
    public List<Track> getByTrackNameAndArtistName(String trackName, String artistName) {
        return trackRepository.findAllByNameAndArtist(trackName, artistName);
    }

    @Transactional
    @Override
//    @CacheEvict(value = "TrackService::getById", key = "#id")
    public void delete(Long id) {
        trackRepository.deleteById(id);
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
//    @Cacheable(value = "TrackService::getOneByTrackNameAndArtistName", key = "#trackName + '.' + #artistName")
    public Track getOneByTrackNameAndArtistName(String trackName, String artistName) {
        Optional<Track> trackOptional = trackRepository.findByNameAndArtist(trackName, artistName);

        return trackOptional.orElseThrow(() -> new TrackNotFoundException("Track with this artist and track name not found!"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Track> getByGenre(String genre) {
        return trackRepository.findByGenre(genre);
    }

    @Override
    @Transactional
//    @CacheEvict(value = "TrackService::getById", key = "#id")
    public void uploadFile(Long id, TrackFile file) {
        Track track = getById(id);
        String fileName = fileService.upload(file);
        track.setFile(fileName);
        trackRepository.save(track);
    }
}
