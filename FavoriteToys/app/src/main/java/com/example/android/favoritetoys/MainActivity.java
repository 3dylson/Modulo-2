package com.example.android.favoritetoys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private EditText mNameEntry;
    private Button mDoSomethingCoolButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDoSomethingCoolButton = findViewById(R.id.b_do_something_cool);
        mNameEntry = findViewById(R.id.et_text_entry);

        mDoSomethingCoolButton.setOnClickListener(new View.OnClickListener() {

            /**
             * The onClick method is triggered when this button (mDoSomethingCoolButton) is clicked.
             *
             * @param v The view that is clicked. In this case, it's mDoSomethingCoolButton.
             */
            @Override
            public void onClick(View v) {

                String textEntered = mNameEntry.getText().toString();
                /*
                 * Storing the Context in a variable in this case is redundant since we could have
                 * just used "this" or "MainActivity.this" in the method call below. However, we
                 * wanted to demonstrate what parameter we were using "MainActivity.this" for as
                 * clear as possible.
                 */
                Context context = MainActivity.this;

                /* This is the class that we want to start (and open) when the button is clicked. */
                Class destinationActivity = ChildActivity.class;


                Intent startChildActivityIntent = new Intent(context, destinationActivity);
                startChildActivityIntent.putExtra(Intent.EXTRA_TEXT, textEntered);
                startActivity(startChildActivityIntent);
            }
        });

    }
}