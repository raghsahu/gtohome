package com.grocery.gtohome.model;

import android.content.Context;

import com.grocery.gtohome.R;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Raghvendra Sahu on 10-Apr-20.
 */
public class ExpandableListDataSource {

    public static Map<String, List<String>> getData(Context context) {
        Map<String, List<String>> expandableListData = new TreeMap<>();

        List<String> filmGenres = Arrays.asList(context.getResources().getStringArray(R.array.film_genre));

        List<String> actionFilms = Arrays.asList(context.getResources().getStringArray(R.array.actionFilms));
        List<String> musicalFilms = Arrays.asList(context.getResources().getStringArray(R.array.musicals));
        List<String> dramaFilms = Arrays.asList(context.getResources().getStringArray(R.array.dramas));
        List<String> thrillerFilms = Arrays.asList(context.getResources().getStringArray(R.array.thrillers));
        List<String> comedyFilms = Arrays.asList(context.getResources().getStringArray(R.array.comedies));
        List<String> fishes_meats = Arrays.asList(context.getResources().getStringArray(R.array.fishes_meats));

        expandableListData.put(filmGenres.get(0), actionFilms);
        expandableListData.put(filmGenres.get(1), musicalFilms);
        expandableListData.put(filmGenres.get(2), dramaFilms);
        expandableListData.put(filmGenres.get(3), thrillerFilms);
        expandableListData.put(filmGenres.get(4), comedyFilms);
        expandableListData.put(filmGenres.get(5), fishes_meats);

        return expandableListData;
    }
}
