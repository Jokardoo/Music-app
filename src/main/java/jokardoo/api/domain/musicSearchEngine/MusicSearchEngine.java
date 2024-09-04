package jokardoo.api.domain.musicSearchEngine;

import jokardoo.api.domain.music.Track;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.*;
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
    public static Set<Track> getMusicList(String songName) {
        String resultString = songName.toLowerCase().replace(" ", "+");

        Set<Track> trackList = new HashSet<>();
        boolean hasNextPage = true;
        int curPage = 1;

        while (hasNextPage) {
            trackList.addAll(getAllTracksOnPage(songName, curPage));
            curPage++;
            hasNextPage = hasNextPage(songName, curPage);
        }


//        Document page = Jsoup.connect(URL_FORMAT + "search?q=" + resultString).get();
//
//
//
//        // выборка элементов - треков с нашей страницы
//        Elements tracks = page.select(PATH_TO_TRACK_LIST).select("li");
//
//
//        //добавляем с 1-го листа
//        for (int i = 0; i < tracks.size(); i++) {
//            // Название группы у данного трека
//            String artistName = page.select(TRACK_ARTIST_NAME).get(i).text();
//
//            // Название самой песни данного трека
//            String trackName = page.select(TRACK_NAME).get(i).text();
//
//            int trackTime = parseTrackTime(page.select(TRACK_FULL_TIME).get(i).text());
//            // Ссылка на скачивание данного трека
//            String downloadLink = page.select(TRACK_DOWNLOAD_LINK).get(i).attr("href");
//
//            Track currentTrack = new Track();
//
//            //TODO добавить определение жанра
//            currentTrack.setName(trackName);
//            currentTrack.setArtist(artistName);
//            currentTrack.setFullTime(trackTime);
//            currentTrack.setTrackGenre("Unknown");
//            currentTrack.setDownloadLink(downloadLink);
//
//            trackList.add(currentTrack);
//        }
//

        return trackList;
    }


    @SneakyThrows
    private static boolean hasNextPage(String songName, int pageNum) {
//        String resultString = songName.toLowerCase().replace(" ", "+");
        Document page;

        // тут находятся все отображаемые страницы
        String PAGES = "body > main.content > div.container > div#pjax-container > div > div > section > ul > li.pagination__item";


        if (pageNum == 1) {
            page = Jsoup.connect(URL_FORMAT + "search/start/" + pageNum + " ?q=" + songName).get();
        } else {
            int temp = ((pageNum - 1) * 48);
            page = Jsoup.connect(URL_FORMAT + "search/start/" + temp + " ?q=" + songName).get();
        }
        Elements elements = page.select(PAGES);

        for (int i = 0; i < elements.size(); i++) {

            if (elements.get(i).text().matches("\\d+") && Integer.parseInt(elements.get(i).text()) >= pageNum) {
                return true;
            }
        }
        return false;

    }

    @SneakyThrows
    private static List<Track> getAllTracksOnPage(String songName, int pageNum) {
        String resultString = songName.toLowerCase().replace(" ", "+");
        Document page;

        if (pageNum == 1) {
            page = Jsoup.connect(URL_FORMAT + "search/start/" + pageNum + " ?q=" + resultString).get();
        } else {
            int temp = ((pageNum - 1) * 48);
            page = Jsoup.connect(URL_FORMAT + "search/start/" + temp + " ?q=" + resultString).get();
        }


        List<Track> trackList = new ArrayList<>();

        // выборка элементов - треков с нашей страницы
        Elements tracks = page.select(PATH_TO_TRACK_LIST).select("li");


        //добавляем с 1-го листа
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
        Set<Track> tracks = getMusicList(artist);

        Track response = null;
        for (Track track : tracks) {
            System.out.println("result = " + track.getName());
            if (trackName.trim().equalsIgnoreCase(track.getName().trim())) {
                response = track;
                break;
            }
        }
        return Optional.ofNullable(response);
//        return tracks.stream()
//                .filter(track -> track.getName().trim().equalsIgnoreCase(trackName.trim()))
//                .findFirst();
    }

    public static List<Track> getAllByArtistAndTrackName(String artist, String trackName) {
        // Сначала ищем все треки по исполнителю
        Set<Track> tracks = getMusicList(artist);

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
        } else {
            curMinutes = minutes;
        }

        int minutesInt = Integer.parseInt(curMinutes);
        int secondsInt = Integer.parseInt(seconds);

        int resultSecondsTime = (minutesInt * 60) + secondsInt;

        return resultSecondsTime;

    }

    public static void main(String[] args) {
        MusicSearchEngine searchEngine = new MusicSearchEngine();
        Set<Track> tracks = searchEngine.getMusicList("Foo fighters");

        int i = 1;

        for (Track t : tracks) {
            System.out.println(i++ + " - " + t.getName());
        }
    }
}
