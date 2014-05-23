package com.thedragons.database.server;

import info.movito.themoviedbapi.*;
import info.movito.themoviedbapi.model.*;
import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.tv.Network;
import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeason;
import info.movito.themoviedbapi.model.tv.TvSeries;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class TMDb {

    private static String API_KEY = "a38e8eae6ca74512988a3d6ff6bd1d9b";

    private TmdbSearch search;

    public TMDb() {
        search = new TmdbApi(API_KEY).getSearch();
    }

    public JSONObject searchTV(String query) {
        List<TvSeries> seriesList = search.searchTv(query, "en", 0);

        List<Integer> idList = new ArrayList<>();
        for (TvSeries series : seriesList) {
            idList.add(series.getId());
        }

        int id = idList.get(0);

        TmdbTV tv = new TmdbApi(API_KEY).getTvSeries();
        TvSeries tvSeries = tv.getSeries(id ,"en");

        String title = tvSeries.getOriginalName();
        List<Genre> genres = tvSeries.getGenres();
        String genre = "";
        for (Genre g : genres) {
            genre = g.getName();
        }
        List<Person> creators = tvSeries.getCreatedBy();
        String creator = "";
        for (Person p : creators) {
            creator = p.getName();
        }
        List<Network> networks = tvSeries.getNetworks();
        String network = "";
        for (Network n : networks) {
            network = n.getName();
        }
        List<Integer> runtimes = tvSeries.getEpisodeRuntime();
        String runtime = "";
        for (Integer i : runtimes) {
            runtime = i.toString();
        }
        String seasons = Integer.toString(tvSeries.getNumberOfSeasons());
        String episodes = Integer.toString(tvSeries.getNumberOfEpisodes());
        String description = tvSeries.getOverview();

        JSONObject output = new JSONObject();
        output.put("title", title);
        output.put("genre", genre);
        output.put("creator", creator);
        output.put("network", network);
        output.put("runtime", runtime);
        output.put("seasons", seasons);
        output.put("episodes", episodes);
        output.put("description", description);

//        List<String> output = new ArrayList<>();
//        output.add(title);
//        output.add(genre);
//        output.add(creator);
//        output.add(network);
//        output.add(runtime);
//        output.add(seasons);
//        output.add(episodes);
//        output.add(description);

        TmdbTvSeasons tvSeasons = new TmdbApi(API_KEY).getTvSeasons();
        for (int i = 0; i <= tvSeries.getNumberOfSeasons(); i++) {
            TvSeason tvSeason = tvSeasons.getSeason(id, i, "en");
            JSONArray episodeList = new JSONArray();
            for (TvEpisode episode : tvSeason.getEpisodes()) {
                episodeList.add(episode.getName());
            }
            output.put(i, episodeList);
        }

        System.out.println(output.toJSONString());
        return output;
    }

//    public void searchFilm(String query) {
//        MovieResults movieResults = search.searchMovie(query, 0, "en", false, 0);
//
//        List<MovieDb> results = movieResults.getResults();
//
//        List<Integer> idList = new ArrayList<>();
//        for (MovieDb movie : results) {
//            idList.add(movie.getId());
//        }
//
//        int id = idList.get(0);
//
//        TmdbMovies movies = new TmdbApi("<apikey>").g;
//        MovieDb movie = movies.getMovie(id, "en");
//
//        String title = movie.getOriginalTitle();
//        System.out.println(title);
//
//        TmdbGenre genre = movieDb.;
//        System.out.println(genre);
//        List<PersonCrew> crew = movieDb.getCredits().getCrew();
//        System.out.println(crew);
//        director.
//       ReleaseInfo releaseInfoList = movieDb.
//        System.out.println(releaseInfoList);
//
//        int runtime = movieDb.getRuntime();
//        String producer
//        String writer
//        String description
//        String description = movieDb.getOverview();
//        System.out.println(description);
//
//
//
//        List<Integer> idList = new ArrayList<>();
//        for (TvSeries series : seriesList) {
//            idList.add(series.getId());
//        }
//
//        int id = idList.get(0);
//
//        TmdbTV tv = new TmdbApi(API_KEY).getTvSeries();
//        TvSeries tvSeries = tv.getSeries(id ,"en");
//
//        String title = tvSeries.getOriginalName();
//        List<Genre> genres = tvSeries.getGenres();
//        String genre = "";
//        for (Genre g : genres) {
//            genre = g.getName();
//        }
//        List<Person> creators = tvSeries.getCreatedBy();
//        String creator = "";
//        for (Person p : creators) {
//            creator = p.getName();
//        }
//        List<Network> networks = tvSeries.getNetworks();
//        String network = "";
//        for (Network n : networks) {
//            network = n.getName();
//        }
//        List<Integer> runtimes = tvSeries.getEpisodeRuntime();
//        String runtime = "";
//        for (Integer i : runtimes) {
//            runtime = i.toString();
//        }
//        String seasons = Integer.toString(tvSeries.getNumberOfSeasons());
//        String episodes = Integer.toString(tvSeries.getNumberOfEpisodes());
//        String description = tvSeries.getOverview();
//
//        JSONObject output = new JSONObject();
//        output.put("title", title);
//        output.put("genre", genre);
//        output.put("creator", creator);
//        output.put("network", network);
//        output.put("runtime", runtime);
//        output.put("seasons", seasons);
//        output.put("episodes", episodes);
//        output.put("description", description);
//
//        List<String> output = new ArrayList<>();
//        output.add(title);
//        output.add(genre);
//        output.add(creator);
//        output.add(network);
//        output.add(runtime);
//        output.add(seasons);
//        output.add(episodes);
//        output.add(description);
//
//        TmdbTvSeasons tvSeasons = new TmdbApi(API_KEY).getTvSeasons();
//        for (int i = 0; i <= tvSeries.getNumberOfSeasons(); i++) {
//            TvSeason tvSeason = tvSeasons.getSeason(id, i, "en");
//            JSONArray episodeList = new JSONArray();
//            for (TvEpisode episode : tvSeason.getEpisodes()) {
//                episodeList.add(episode.getName());
//            }
//            output.put(i, episodeList);
//        }
//
//        return output;
//    }

//    public static void main(String[] args) {
//        TMDb tmDb = new TMDb();
//        tmDb.searchTV("korra");
//        //tmDb.searchFilm("avatar");
//    }
}
