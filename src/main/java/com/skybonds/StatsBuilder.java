package com.skybonds;

import java.util.*;

/**
 * @author Ildar Gafarov (ildar@skybonds.com)
 */
public class StatsBuilder {

    private Filters filters;

    public StatsBuilder(Filters filters) {
        this.filters = filters;
    }

    public List<Stat> build(Set<String> isins) {
        List<Stat> stats = new ArrayList<>();
        long begin = Calendar.getInstance().getTimeInMillis();
        for(Map.Entry<String, Set<String>> filter : filters.getFiltersValues().entrySet()) {
            for(String value : filter.getValue()) {
                Set<String> result = filters.getFilterResults(filter.getKey(), value);
                int amount = intersect(isins, result);
//                stats.add(new Stat(filter.getKey(), value, amount));
            }
        }
        long end = Calendar.getInstance().getTimeInMillis();
        System.out.println("Stats calc time: " + (end - begin));
        return stats;
    }

    private int intersect(Set<String> s1, Set<String> s2) {
        Set<String> intersection = new HashSet<>(s1);
        intersection.retainAll(s2);
        return intersection.size();
    }

    public class Stat {
        String name;
        String value;
        int amount;

        public Stat(String name, String value, int amount) {
            this.name = name;
            this.value = value;
            this.amount = amount;
        }
    }

}
