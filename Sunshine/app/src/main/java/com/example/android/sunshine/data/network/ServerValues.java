package com.example.android.sunshine.data.network;

import com.example.android.sunshine.BuildConfig;

final class ServerValues {
    static final String BASE_URL = "https://andfun-weather.udacity.com/";
    static final String API_KEY = BuildConfig.API_KEY;

    static final String WEATHER = "weather";

    /**
     * NOTE: These values only effect responses from OpenWeatherMap, NOT from the fake weather
     * server.
     */

    /* The format we want our API to return */
    static final String FORMAT = "json";
    /* The units we want our API to return */
    static final String UNITS = "metric";
    /* The number of days we want our API to return */
    static final int NUM_DAYS = 14;

    /* The query parameter allows us to provide a location string to the API */
    static final String QUERY_PARAM = "q";

    static final String LAT_PARAM = "lat";
    static final String LON_PARAM = "lon";

    /* The format parameter allows us to designate whether we want JSON or XML from our API */
    static final String FORMAT_PARAM = "mode";
    /* The units parameter allows us to designate whether we want metric units or imperial units */
    static final String UNITS_PARAM = "units";
    /* The days parameter allows us to designate how many days of weather data we want */
    static final String DAYS_PARAM = "cnt";

    // Ensures this class is never instantiated
    private ServerValues() {}

}
