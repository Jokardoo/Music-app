package jokardoo.api.domain.musicSearchEngine;

import jokardoo.api.domain.music.Track;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MusicSearchEngine {
    private static final String URL_FORMAT = "https://rus.hitmotop.com/";
    private static final String PATH_TO_TRACK_LIST = "body > main.content > div.container > div.content-center.muslist "
            + "> div.content-inner > div.p-info.p-inner > ul.tracks__list";

    private static final String PATH_TO_TRACK_INFO = PATH_TO_TRACK_LIST + " > li.tracks__item.track.mustoggler > div.track__info";

    private static final String TRACK_FULL_TIME = PATH_TO_TRACK_INFO + " > div.track__info-r > div.track__time > div.track__fulltime";

    private static final String TRACK_DOWNLOAD_LINK = PATH_TO_TRACK_INFO + " > div.track__info-r > a.track__download-btn";

    private static final String TRACK_ARTIST_NAME = PATH_TO_TRACK_INFO + " > a.track__info-l > div.track__desc";

    private static final String TRACK_NAME = PATH_TO_TRACK_INFO + " > a.track__info-l > div.track__title";

    @SneakyThrows
    public static List<Track> getMusicList(String songName) {
        Document page = Jsoup.connect(URL_FORMAT + "search?q=" + songName).get();

        List<Track> trackList = new ArrayList<>();

        // выборка элементов - треков с нашей страницы
        Elements tracks = page.select(PATH_TO_TRACK_LIST).select("li");

        for (int i = 0; i < tracks.size(); i++) {
            // Название группы у данного трека
            String artistName = page.select(TRACK_ARTIST_NAME).get(i).text();

            // Название самой песни данного трека
            String trackName = page.select(TRACK_NAME).get(i).text();

            int trackTime = parseTrackTime(page.select(TRACK_FULL_TIME).get(i).text());
            // Ссылка на скачивание данного трека
            String downloadLink = page.select(TRACK_DOWNLOAD_LINK).get(i).attr("href");

            Track currentTrack = new Track();

            //TODO добавить определение жанра
            currentTrack.setName(trackName);
            currentTrack.setArtist(artistName);
            currentTrack.setFullTime(trackTime);
            currentTrack.setTrackGenre("Unknown");
            currentTrack.setDownloadLink(downloadLink);

            trackList.add(currentTrack);
        }
        return trackList;
    }


    public static Optional<Track> getOneByArtistAndTrackName(String artist, String trackName) {
        // Сначала ищем все треки по исполнителю
        List<Track> tracks = getMusicList(artist);

        return tracks.stream()
                .filter(track -> track.getName().trim().equalsIgnoreCase(trackName.trim()))
                .findFirst();
    }

    public static List<Track> getAllByArtistAndTrackName(String artist, String trackName) {
        // Сначала ищем все треки по исполнителю
        List<Track> tracks = getMusicList(artist);

        return tracks.stream()
                .filter(track -> track.getName().trim().equalsIgnoreCase(trackName.trim()))
                .collect(Collectors.toList());


    }

    private static int parseTrackTime(String time) {
        String[] splitTime = time.split(":");

        String minutes = splitTime[0];
        String seconds = splitTime[1];

        String curMinutes;
        if (minutes.startsWith("0")) {
            curMinutes = minutes.substring(1);
        }
        else {
            curMinutes = minutes;
        }

        int minutesInt = Integer.parseInt(curMinutes);
        int secondsInt = Integer.parseInt(seconds);

        int resultSecondsTime = (minutesInt * 60) + secondsInt;

        return resultSecondsTime;

    }

    public static void main(String[] args) {
        MusicSearchEngine searchEngine = new MusicSearchEngine();
        List<Track> tracks = searchEngine.getMusicList("Synthwave");

        for (Track t : tracks) {
            System.out.println(t);
        }
    }
}
