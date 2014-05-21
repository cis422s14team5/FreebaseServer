package com.thedragons.freebase.server;

import info.movito.themoviedbapi.*;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.tv.Network;
import info.movito.themoviedbapi.model.tv.TvSeason;
import info.movito.themoviedbapi.model.tv.TvSeries;

import java.util.Arrays;
import java.util.List;

public class TMDb {

    private static String API_KEY = "a38e8eae6ca74512988a3d6ff6bd1d9b";

    public TMDb() {
        TmdbSearch search = new TmdbApi(API_KEY).getSearch();
        List<TvSeries> tempList = search.searchTv("Breaking Bad", "en", 0);
        String tvString = tempList.get(0).toString();
        List<String> list = Arrays.asList(tvString.split(","));

        int id = Integer.parseInt(list.get(list.size() - 1));

        TmdbTV tv = new TmdbApi(API_KEY).getTvSeries();
        TvSeries tvSeries = tv.getSeries(id ,"en");

        String title = tvSeries.getOriginalName();
        List<Genre> genre = tvSeries.getGenres();
        List<Person> creator = tvSeries.getCreatedBy();
        List<Network> network = tvSeries.getNetworks();
        List<Integer> runtime = tvSeries.getEpisodeRuntime();
        int seasons = tvSeries.getNumberOfSeasons();
        int episodes = tvSeries.getNumberOfEpisodes();
        String description = tvSeries.getOverview();


        for (Genre genre1 : genre) {
            System.out.println(genre1.getName());
        }

        System.out.println("Title: " + title);
        System.out.println("Genre: " + genre.get(0));
        System.out.println("Creator: " + creator);
        System.out.println("Network: " + network.get(0));
        System.out.println("Runtime: " + runtime.get(0));
        System.out.println("Seasons: " + seasons);
        System.out.println("Episodes: " + episodes);
        System.out.println("Description:\n" + description);
    }

    public static void main(String[] args) {
        new TMDb();
    }
}
