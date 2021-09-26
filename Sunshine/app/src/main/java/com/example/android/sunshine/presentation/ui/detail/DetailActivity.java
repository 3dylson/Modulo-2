package com.example.android.sunshine.presentation.ui.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.sunshine.R;
import com.example.android.sunshine.data.database.entity.Weather;
import com.example.android.sunshine.databinding.ActivityDetailBinding;
import com.example.android.sunshine.presentation.ui.SettingsActivity;
import com.example.android.sunshine.presentation.viewmodels.DetailActivityViewModel;
import com.example.android.sunshine.presentation.viewmodels.DetailViewModelFactory;
import com.example.android.sunshine.utils.SunshineDateUtils;
import com.example.android.sunshine.utils.SunshineWeatherUtils;

import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private ActivityDetailBinding binding;
    private DetailActivityViewModel mViewModel;

    private String mForecast;
    public static final String WEATHER_ID_EXTRA = "WEATHER_ID_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        long timestamp = getIntent().getLongExtra(WEATHER_ID_EXTRA, -1);
        Date date = new Date(timestamp);

        DetailViewModelFactory factory = new DetailViewModelFactory(this.getApplication(),date);
        mViewModel = new ViewModelProvider(this,factory).get(DetailActivityViewModel.class);

        // Observers changes in the WeatherEntry with the id mId
        mViewModel.getWeather().observe(this, weather -> {
            // If the weather forecast details change, update the UI
            if (weather != null) bindWeatherToUI(weather);
        });

    }

    private void bindWeatherToUI(Weather weather) {
        /****************
         * Weather Icon *
         ****************/

        int weatherId = weather.getWeatherIconId();
        int weatherImageId = SunshineWeatherUtils.getLargeArtResourceIdForWeatherCondition(weatherId);

        /* Set the resource ID on the icon to display the art */
        binding.primaryInfo.weatherIcon.setImageResource(weatherImageId);

        /****************
         * Weather Date *
         ****************/
        /*
         * The date that is stored is a GMT representation at midnight of the date when the weather
         * information was loaded for.
         *
         * When displaying this date, one must add the GMT offset (in milliseconds) to acquire
         * the date representation for the local date in local time.
         * SunshineDateUtils#getFriendlyDateString takes care of this for us.
         */
        long localDateMidnightGmt = weather.getDate().getTime();
        String dateText = SunshineDateUtils.getFriendlyDateString(DetailActivity.this, localDateMidnightGmt, true);
        binding.primaryInfo.date.setText(dateText);

        /***********************
         * Weather Description *
         ***********************/
        /* Use the weatherId to obtain the proper description */
        String description = SunshineWeatherUtils.getStringForWeatherCondition(DetailActivity.this, weatherId);

        mForecast = dateText+" | "+description;

        /* Create the accessibility (a11y) String from the weather description */
        String descriptionA11y = getString(R.string.a11y_forecast, description);

        /* Set the text and content description (for accessibility purposes) */
        binding.primaryInfo.weatherDescription.setText(description);
        binding.primaryInfo.weatherDescription.setContentDescription(descriptionA11y);

        /* Set the content description on the weather image (for accessibility purposes) */
        binding.primaryInfo.weatherIcon.setContentDescription(descriptionA11y);

        /**************************
         * High (max) temperature *
         **************************/

        double maxInCelsius = weather.getMax();

        /*
         * If the user's preference for weather is fahrenheit, formatTemperature will convert
         * the temperature. This method will also append either °C or °F to the temperature
         * String.
         */
        String highString = SunshineWeatherUtils.formatTemperature(DetailActivity.this, maxInCelsius);

        /* Create the accessibility (a11y) String from the weather description */
        String highA11y = getString(R.string.a11y_high_temp, highString);

        /* Set the text and content description (for accessibility purposes) */
        binding.primaryInfo.highTemperature.setText(highString);
        binding.primaryInfo.highTemperature.setContentDescription(highA11y);

        /*************************
         * Low (min) temperature *
         *************************/

        double minInCelsius = weather.getMin();
        /*
         * If the user's preference for weather is fahrenheit, formatTemperature will convert
         * the temperature. This method will also append either °C or °F to the temperature
         * String.
         */
        String lowString = SunshineWeatherUtils.formatTemperature(DetailActivity.this, minInCelsius);

        String lowA11y = getString(R.string.a11y_low_temp, lowString);

        /* Set the text and content description (for accessibility purposes) */
        binding.primaryInfo.lowTemperature.setText(lowString);
        binding.primaryInfo.lowTemperature.setContentDescription(lowA11y);

        /************
         * Humidity *
         ************/

        double humidity = weather.getHumidity();
        String humidityString = getString(R.string.format_humidity, humidity);
        String humidityA11y = getString(R.string.a11y_humidity, humidityString);

        /* Set the text and content description (for accessibility purposes) */
        binding.extraDetails.humidity.setText(humidityString);
        binding.extraDetails.humidity.setContentDescription(humidityA11y);

        binding.extraDetails.humidityLabel.setContentDescription(humidityA11y);

        /****************************
         * Wind speed and direction *
         ****************************/
        /* Read wind speed (in MPH) and direction (in compass degrees)*/
        double windSpeed = weather.getWind();
        double windDirection = weather.getDegrees();
        String windString = SunshineWeatherUtils.getFormattedWind(DetailActivity.this, windSpeed, windDirection);
        String windA11y = getString(R.string.a11y_wind, windString);

        /* Set the text and content description (for accessibility purposes) */
        binding.extraDetails.windMeasurement.setText(windString);
        binding.extraDetails.windMeasurement.setContentDescription(windA11y);
        binding.extraDetails.windLabel.setContentDescription(windA11y);

        /************
         * Pressure *
         ************/
        double pressure = weather.getPressure();

        /*
         * Format the pressure text using string resources. The reason we directly access
         * resources using getString rather than using a method from SunshineWeatherUtils as
         * we have for other data displayed in this Activity is because there is no
         * additional logic that needs to be considered in order to properly display the
         * pressure.
         */
        String pressureString = getString(R.string.format_pressure, pressure);

        String pressureA11y = getString(R.string.a11y_pressure, pressureString);

        /* Set the text and content description (for accessibility purposes) */
        binding.extraDetails.pressure.setText(pressureString);
        binding.extraDetails.pressure.setContentDescription(pressureA11y);
        binding.extraDetails.pressureLabel.setContentDescription(pressureA11y);
    }

    /**
     * Uses the ShareCompat Intent builder to create our Forecast intent for sharing. We set the
     * type of content that we are sharing (just regular text), the text itself, and we return the
     * newly created Intent.
     *
     * @return The Intent to use to start our share.
     */
    private Intent createShareForecastIntent() {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mForecast + FORECAST_SHARE_HASHTAG);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Testing");


        return shareIntent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createShareForecastIntent());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}