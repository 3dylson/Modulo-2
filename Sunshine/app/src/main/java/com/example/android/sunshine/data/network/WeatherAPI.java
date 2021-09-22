package com.example.android.sunshine.data.network;

import static com.example.android.sunshine.data.network.ServerValues.DAYS_PARAM;
import static com.example.android.sunshine.data.network.ServerValues.FORMAT_PARAM;
import static com.example.android.sunshine.data.network.ServerValues.LAT_PARAM;
import static com.example.android.sunshine.data.network.ServerValues.LON_PARAM;
import static com.example.android.sunshine.data.network.ServerValues.QUERY_PARAM;
import static com.example.android.sunshine.data.network.ServerValues.UNITS_PARAM;
import static com.example.android.sunshine.data.network.ServerValues.WEATHER;

import com.example.android.sunshine.data.network.responsemodels.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET(WEATHER)
    Call<WeatherResponse> getCurrentWeatherWithLatitudeLongitude(@Query(LAT_PARAM) String latitude,
                                                                 @Query(LON_PARAM) String longitude,
                                                                 @Query(FORMAT_PARAM) String format,
                                                                 @Query(UNITS_PARAM) String units,
                                                                 @Query(DAYS_PARAM) String numDays);


    @GET(WEATHER)
    Call<WeatherResponse> getCurrentWeatherWithLocation(@Query(QUERY_PARAM) String location,
                                                        @Query(FORMAT_PARAM) String format,
                                                        @Query(UNITS_PARAM) String units,
                                                        @Query(DAYS_PARAM) String numDays);

}
