package com.skybonds;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Ildar Gafarov (ildar@skybonds.com)
 */
public class Filters {

    private Map<String, Set<String>> filtersValues = new HashMap<>();
    private Map<String, Set<String>> filterResults = new HashMap<>();

    private DataProvider dataProvider;

    public Filters(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public void init() {
        filtersValues = dataProvider.getFiltersValues();
        filtersValues.remove("issuer");
        for(Map.Entry<String, Set<String>> entry : filtersValues.entrySet()) {
            for(String value : entry.getValue()) {
                filterResults.put(key(entry.getKey(), value), dataProvider.getFilterResults(entry.getKey(), value));
            }
        }
    }


    public Map<String, Set<String>> getFiltersValues() {
        return filtersValues;
    }

    public Set<String> getFilterResults(String name, String value) {
        return filterResults.get(key(name, value));
    }

    private static String key(String name, String value) {
        return name+value;
    }

}
