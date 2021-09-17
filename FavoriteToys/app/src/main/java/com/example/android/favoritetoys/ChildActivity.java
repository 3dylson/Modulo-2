package com.example.android.favoritetoys;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ChildActivity extends AppCompatActivity {

    private TextView mDisplayText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDisplayText = findViewById(R.id.tv_display);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            /*
             * Now that we've checked to make sure the extra we are looking for is contained within
             * the Intent, we can extract the extra. To do that, we simply call the getStringExtra
             * method on the Intent. There are various other get*Extra methods you can call for
             * different types of data. Please feel free to explore those yourself.
             */
            String textEntered = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);

            /*
             * Finally, we can set the text of our TextView (using setText) to the text that was
             * passed to this Activity.
             */
            mDisplayText.setText(textEntered);
        }
    }
}
