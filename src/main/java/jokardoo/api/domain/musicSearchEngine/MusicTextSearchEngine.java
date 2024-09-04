package jokardoo.api.domain.musicSearchEngine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MusicTextSearchEngine {
    private static final String URL = "https://www.amalgama-lab.com/songs";

    private static final String TEXT_PATH = "body > div.row > div.main_content.col > div.row > div.texts.col > div#click_area > div.string_container";

    public static Map<Integer, String> findSongLyricsOriginal(String artistName, String songName) {

        Character firstArtistNameLetter = artistName.trim().charAt(0);
        String modifiedArtistName = artistName.trim().replace(" ", "_");
        String modifiedSongName = songName.trim().replace(" ", "_");

        try {
            Document page = Jsoup.connect(URL + "/" +
                            firstArtistNameLetter.toString().toLowerCase() +
                            "/" +
                            modifiedArtistName.toLowerCase()
                            + "/" +
                            modifiedSongName.toLowerCase() +
                            ".html")
                    .get();

            Map<Integer, String> engText = parseElements(page
                    .select(TEXT_PATH)
                    .select(" > div.original"));

            return engText;

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Track with this artist and song name was not found on https://www.amalgama-lab.com/songs");
            return new HashMap<>();
        }

    }

    public static Map<Integer, String> findSongLyricsTranslate(String artistName, String songName) {

        Character firstArtistNameLetter = artistName.trim().charAt(0);
        String modifiedArtistName = artistName.replace(" ", "_");
        String modifiedSongName = songName.trim().replace(" ", "_");

        try {
            Document page = Jsoup.connect(URL + "/" +
                            firstArtistNameLetter.toString().toLowerCase() +
                            "/" +
                            modifiedArtistName.toLowerCase()
                            + "/" +
                            modifiedSongName.toLowerCase() +
                            ".html")
                    .get();


            Map<Integer, String> ruText = parseElements(page
                    .select(TEXT_PATH)
                    .select(" > div.translate"));

            return ruText;

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Track with this artist and song name was not found on https://www.amalgama-lab.com/songs");
            return new HashMap<>();
        }

    }

    private static Map<Integer, String> parseElements(Elements elements) {

        Map<Integer, String> map = new HashMap<>();

        for (int i = 0; i < elements.size(); i++) {
            map.put(i, elements.get(i).text());
        }

        return map;
    }

    public static void main(String[] args) {
        Map<Integer, String> map = findSongLyricsOriginal("muse", "madness");

        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            System.out.println(entry.getValue());
        }
    }
}
