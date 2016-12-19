package com.skybonds;

/**
 * @author Ildar Gafarov (ildar@skybonds.com)
 */
public class Main {

    public static void main(String[] args) {
        DataProvider dataProvider = new DataProvider();
        Filters filters = new Filters(dataProvider);
        filters.init();
        StatsBuilder builder = new StatsBuilder(filters);
        builder.build(filters.getFilterResults("country", "USA"));
    }

}
