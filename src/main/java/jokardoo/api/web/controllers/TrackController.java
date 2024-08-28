package jokardoo.api.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jokardoo.api.domain.exceptions.TrackCannotBeCreatedException;
import jokardoo.api.domain.exceptions.TrackNotFoundException;
import jokardoo.api.domain.music.Artist;
import jokardoo.api.domain.music.Genre;
import jokardoo.api.domain.music.Track;
import jokardoo.api.domain.musicSearchEngine.MusicSearchEngine;
import jokardoo.api.repositories.UserRepository;
import jokardoo.api.services.ArtistService;
import jokardoo.api.services.TrackService;
import jokardoo.api.services.impl.ArtistServiceImpl;
import jokardoo.api.services.impl.TrackServiceImpl;
import jokardoo.api.web.dto.track.TrackDto;
import jokardoo.api.web.dto.validation.OnCreate;
import jokardoo.api.web.dto.validation.OnUpdate;
import jokardoo.api.web.mappers.TrackMapper;
import jokardoo.api.web.security.JwtEntity;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tracks")
@Validated
// используется для Swagger'a,
@Tag(name = "Track Controller", description = "Track API")
public class TrackController {
    private final TrackMapper trackMapper;

    private final UserRepository userRepository;

    private final ArtistService artistService;

    private final TrackService trackService;

    @PreAuthorize("@customSecurityExpression.isItMuse(#name)")
    @GetMapping("/find/{name}")
    @Operation(summary = "Find track by name")  // Используется для swagger'a, поясняет работу метода
    public List<TrackDto> findByName(@PathVariable(name = "name") String name) {
        List<Track> tracks = MusicSearchEngine.getMusicList(name);

        for (Track t : tracks) {
            System.out.println(t);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtEntity userDetails = (JwtEntity) authentication.getPrincipal();

        System.out.println(userDetails.getUsername());
        return trackMapper.toDto(tracks);
    }

    @GetMapping("/find/{artist}/{track}")
    public Track findOneByArtistAndTrackName(@PathVariable("artist") String artist, @PathVariable("track") String trackName) {

        Optional<Track> trackOptional = MusicSearchEngine.getOneByArtistAndTrackName(artist, trackName);

        return trackOptional.orElseThrow(() -> new TrackNotFoundException("Track with this artist and track name not found!"));
    }

    @PostMapping("/find/add-to-storage")
    public void addTrackToUserStorage(@RequestBody Map<String, String> requestData) {

        String artistName = requestData.get("artistName");
        String trackName = requestData.get("trackName");

        System.out.println(artistName);
        System.out.println(trackName);
        Optional<Track> trackOptional = MusicSearchEngine.getOneByArtistAndTrackName(artistName, trackName);

        if (trackOptional.isPresent()) {
            createTrack(trackMapper.toDto(trackOptional.get()));
        }
        else {
            throw new TrackNotFoundException("Track with this artist not found!");
        }
    }

    @Transactional
    @PostMapping("/create-track")
    public TrackDto createTrack(@RequestBody TrackDto trackDto) {
//        Track track = trackMapper.toEntity(trackDto);
//
//        try {
//            // Если введенный жанр существует в нашем списке жанров, то используем заданное название жанра
//            Genre containsGenre = Genre.valueOf(track.getTrackGenre());
//            Track createdTrack;
////            trackService.createRelationshipBetweenTrackAndGenre(createdTrack.getId(), containsGenre);
//
//            if (artistService.isArtistContains(track.getArtist())) {
//                Artist foundArtist = artistService.getByName(track.getArtist());
//
//                List<Track> tracks = artistService.getTracksByArtistName(foundArtist.getName());
//                // Если у этого артиста уже есть трэк с таким названием в бд
//                if (tracks.stream().filter(t -> t.getDownloadLink().equals(track.getDownloadLink())).findFirst().isPresent()) {
//                    System.out.println("Track contains!");
//                    List<Track> artistTracks = trackService.getByTrackNameAndArtistName(track.getName(), foundArtist.getName());
//                    System.out.println("Track with this name already contains in artist list");
//
//                    return trackMapper.toDto(artistTracks.stream().filter(t -> t.getDownloadLink().equalsIgnoreCase(track.getDownloadLink())).findFirst().get());
//
//                }
//                else {
//                    System.out.println("track not contains!");
//                    createdTrack = trackService.create(track, containsGenre);
//                    trackService.assignToArtistById(createdTrack.getId(), foundArtist.getId());
//                }
//
//            }
//            else {
//                System.out.println("Artist and track not contains!");
//                Artist foundArtist = artistService.create(track.getArtist(), "No information.");
//                createdTrack = trackService.create(track, containsGenre);
//                trackService.assignToArtistById(createdTrack.getId(), foundArtist.getId());
//            }
//
//
//            return trackMapper.toDto(createdTrack);
//        }
//        // Если на вход поступил жанр, которого нет в базе - устанавливаем название жанра OTHER
//        catch (IllegalArgumentException e) {
//            Track createdTrack = trackService.create(track, Genre.OTHER);
////            trackService.createRelationshipBetweenTrackAndGenre(createdTrack.getId(), Genre.OTHER);
//
//            if (artistService.isArtistContains(createdTrack.getArtist())) {
//                Artist foundArtist = artistService.getByName(createdTrack.getArtist());
//                trackService.assignToArtistById(createdTrack.getId(), foundArtist.getId());
//            }
//            else {
//                Artist foundArtist = artistService.create(createdTrack.getArtist(), "No information.");
//                trackService.assignToArtistById(createdTrack.getId(), foundArtist.getId());
//            }
//
//            track.setTrackGenre("Other");
//            System.out.println(createdTrack);
//            return trackMapper.toDto(createdTrack);
//        }
    Track track = getOrCreateTrackByTrackDto(trackDto);

    return trackMapper.toDto(track);
    }

    @GetMapping("/find/get-all/{artist}/{track}")
    public List<Track> findByArtistAndTrackName(@PathVariable("artist") String artist, @PathVariable("track") String trackName) {


        List<Track> tracks = MusicSearchEngine.getAllByArtistAndTrackName(artist, trackName);

        return tracks;
    }



    @PutMapping
    @Operation(summary = "Update track in database")
    public TrackDto update(@Validated(OnUpdate.class) @RequestBody TrackDto dto) {
        Track track = trackMapper.toEntity(dto);
        Track updatedTrack = trackService.update(track);

        return trackMapper.toDto(updatedTrack);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get track by id")
    public TrackDto getById(@PathVariable long id) {
        Track track = trackService.getById(id);
        return  trackMapper.toDto(track);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete track by id")
    public void deleteById(@PathVariable Long id) {
        trackService.delete(id);
    }


    private Track getOrCreateTrackByTrackDto(TrackDto trackDto) {
        Track track = trackMapper.toEntity(trackDto);

        Track createdTrack;

        if (artistService.isArtistContains(track.getArtist())) {
            Artist foundArtist = artistService.getByName(track.getArtist());

            List<Track> tracks = artistService.getTracksByArtistName(foundArtist.getName());
            // Если у этого артиста уже есть трэк с таким названием в бд
            if (tracks.stream().filter(t -> t.getDownloadLink().equals(track.getDownloadLink())).findFirst().isPresent()) {
                System.out.println("Track contains!");
                List<Track> artistTracks = trackService.getByTrackNameAndArtistName(track.getName(), foundArtist.getName());
                System.out.println("Track with this name already contains in artist list");
                System.out.println("return track from database");
                return artistTracks.stream().filter(t -> t.getDownloadLink().equalsIgnoreCase(track.getDownloadLink())).findFirst().get();

            }
            // Если у данного артиста нет такого трека
            else {
                System.out.println("track not contains!");
                try {
                    Genre containsGenre = Genre.valueOf(track.getTrackGenre());
                    createdTrack = trackService.create(track, containsGenre);
                     trackService.assignToArtistById(createdTrack.getId(), foundArtist.getId());
                } catch (IllegalArgumentException e) {
                    createdTrack = trackService.create(track, Genre.OTHER);
                    trackService.assignToArtistById(createdTrack.getId(), foundArtist.getId());
                }

                return createdTrack;
            }

        }
        else {
            System.out.println("Artist and track not contains!");
            Artist foundArtist = artistService.create(track.getArtist(), "No information.");
            try {
                Genre containsGenre = Genre.valueOf(track.getTrackGenre());
                createdTrack = trackService.create(track, containsGenre);
                trackService.assignToArtistById(createdTrack.getId(), foundArtist.getId());
            } catch (IllegalArgumentException e) {
                createdTrack = trackService.create(track, Genre.OTHER);
                trackService.assignToArtistById(createdTrack.getId(), foundArtist.getId());
            }
            return createdTrack;
        }

    }
}
