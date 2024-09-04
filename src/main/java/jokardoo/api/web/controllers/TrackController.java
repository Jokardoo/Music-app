package jokardoo.api.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jokardoo.api.domain.exceptions.ArtistNotFoundException;
import jokardoo.api.domain.exceptions.TrackNotFoundException;
import jokardoo.api.domain.music.Artist;
import jokardoo.api.domain.music.GenreEnum;
import jokardoo.api.domain.music.Track;
import jokardoo.api.domain.music.TrackFile;
import jokardoo.api.domain.musicSearchEngine.MusicSearchEngine;
import jokardoo.api.domain.musicSearchEngine.MusicTextSearchEngine;
import jokardoo.api.repositories.UserRepository;
import jokardoo.api.services.ArtistService;
import jokardoo.api.services.TrackService;
import jokardoo.api.web.dto.track.TrackDto;
import jokardoo.api.web.dto.track.TrackFileDto;
import jokardoo.api.web.dto.validation.OnUpdate;
import jokardoo.api.web.mappers.TrackFileMapper;
import jokardoo.api.web.mappers.TrackMapper;
import jokardoo.api.web.security.JwtEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final TrackFileMapper trackFileMapper;

    private final UserRepository userRepository;

    private final ArtistService artistService;

    private final TrackService trackService;

    @PreAuthorize("@customSecurityExpression.isItMuse(#name)")
    @GetMapping("/find/{name}")
    @Operation(summary = "Find track by name")  // Используется для swagger'a, поясняет работу метода
    public List<TrackDto> findByName(@PathVariable(name = "name") String name) {
        List<Track> tracks = List.copyOf(MusicSearchEngine.getMusicList(name));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtEntity userDetails = (JwtEntity) authentication.getPrincipal();

        System.out.println(userDetails.getUsername());
        return trackMapper.toDto(tracks);
    }

    @GetMapping("/find/{artist}/{track}")
    public Track findOneByArtistAndTrackName(@PathVariable("artist") String artist, @PathVariable("track") String trackName) {

        Optional<Track> trackOptional = MusicSearchEngine.getOneByArtistAndTrackName(artist, trackName);

        if (trackOptional.isPresent()) {
            Track t = trackOptional.get();
            t.setOriginalTextMap(MusicTextSearchEngine.findSongLyricsOriginal(t.getArtist(), t.getName()));
            t.setTranslateTextMap(MusicTextSearchEngine.findSongLyricsTranslate(t.getArtist(), t.getName()));

            return t;
        }

        return trackOptional.orElseThrow(() -> new TrackNotFoundException("Track with this artist and track name not found!"));
    }

    @PostMapping("/find/add-to-storage")
    public void addTrackToStorage(@RequestBody Map<String, String> requestData) {

        String artistName = requestData.get("artistName");
        String trackName = requestData.get("trackName");

        System.out.println(artistName);
        System.out.println(trackName);
        Optional<Track> trackOptional = MusicSearchEngine.getOneByArtistAndTrackName(artistName, trackName);

        if (trackOptional.isPresent()) {

            createTrack(trackMapper.toDto(trackOptional.get()));
        } else {
            throw new TrackNotFoundException("Track with this artist not found!");
        }
    }

    @Transactional
    @PostMapping("/create-track")
    public TrackDto createTrack(@RequestBody TrackDto trackDto) {

        System.out.println("track creating process ... " + trackDto);
        Track track = trackMapper.toEntity(trackDto);

        List<Track> tracks = trackService.getByTrackNameAndArtistName(track.getName(), track.getArtist());

        // Проверяем, есть ли в БД по исполнителю и названию трека такие записи.
        // Если нашли полностью идентичные - процесс создания трека останавливаем
        Optional<Track> trackOptional = tracks.stream().filter(current -> current.equals(track)).findFirst();

        if (trackOptional.isPresent()) {
            System.out.println("This track is already exists in database.");
            return trackMapper.toDto(trackOptional.get());
        }

        trackService.save(track);
        System.out.println("Track was created!");

        try {
            System.out.println("Trying to find artist");
            artistService.getByName(track.getArtist());
            System.out.println("Artist is already exists in database!");
        } catch (ArtistNotFoundException e) {
            // Трек не был найден, создаем его
            System.out.println("Artist not found in database!");
            artistService.create(track.getArtist(), "Unknown");
            System.out.println("Artist was created!");
        }

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
        Track updatedTrack = trackService.save(track);

        return trackMapper.toDto(updatedTrack);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get track by id")
    public TrackDto getById(@PathVariable long id) {
        Track track = trackService.getById(id);
        return trackMapper.toDto(track);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete track by id")
    public void deleteById(@PathVariable Long id) {
        trackService.delete(id);
    }


    //TODO перенести логику в сервис
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
                    createdTrack = trackService.save(track);
                    trackService.assignToArtistById(createdTrack.getId(), foundArtist.getId());
                } catch (IllegalArgumentException e) {
                    track.setTrackGenre(GenreEnum.OTHER.name());
                    createdTrack = trackService.save(track);
                    trackService.assignToArtistById(createdTrack.getId(), foundArtist.getId());
                }

                return createdTrack;
            }

        } else {
            System.out.println("Artist and track not contains!");
            Artist foundArtist = artistService.create(track.getArtist(), "No information.");
            try {
                createdTrack = trackService.save(track);
                trackService.assignToArtistById(createdTrack.getId(), foundArtist.getId());
            } catch (IllegalArgumentException e) {
                track.setTrackGenre(GenreEnum.OTHER.name());
                createdTrack = trackService.save(track);
                trackService.assignToArtistById(createdTrack.getId(), foundArtist.getId());
            }
            return createdTrack;
        }

    }

    @PostMapping("/{id}/file")
    @Operation(summary = "Upload file to track")

    public void uploadFile(@PathVariable(name = "id") Long id,
                           @Validated @ModelAttribute TrackFileDto trackFileDto) {
        TrackFile trackFile = trackFileMapper.toEntity(trackFileDto);
        trackService.uploadFile(id, trackFile);
    }

}
