package com.example.android.favoritetoys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mToysListTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToysListTextView = findViewById(R.id.tv_toy_names);

        String[] toyNames = ToyBox.getToyNames();

        for (String toyName : toyNames) {
            mToysListTextView.append(toyName + "\n\n\n");
        }
    }
}