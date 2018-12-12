package edu.illinois.cs.cs125.lab11;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

/**
 * Main class for our UI design lab.
 */
public final class MainActivity extends AppCompatActivity {

    public String name;
    public String day;
    public String route;
    public String startStop;
    public String endStop;

    /**
     * Run when this activity comes to the foreground.
     *
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void switchScreens(View v){

        //Storing inputs as EditText variables to allow use of getText() for user inputs

        EditText nameInput = findViewById(R.id.name_input);
        EditText dayInput = findViewById(R.id.day_input);
        EditText routeInput = findViewById(R.id.route_input);
        EditText startStopInput = findViewById(R.id.startStop_input);
        EditText endStopInput = findViewById(R.id.endStop_input);

        //Storing inputs from above as strings in class variables

        name = nameInput.getText().toString();
        day = dayInput.getText().toString();
        route = routeInput.getText().toString();
        startStop = startStopInput.getText().toString();
        endStop = endStopInput.getText().toString();

        //Passing user inputs to the BusList class

        Intent intent = new Intent(MainActivity.this, BusList.class);
        intent.putExtra("name", name);
        intent.putExtra("day", day);
        intent.putExtra("route", route);
        intent.putExtra("startStop", startStop);
        intent.putExtra("endStop", endStop);

        //Starts BusList activity

        System.out.println("bout to start activity");
        startActivity(intent);
    }
    /**
     * what to so onclick.
     */
    public void infoPageStart(View v) {

        // Starts help page when button is clicked to call this method

        Intent intent = new Intent(MainActivity.this, Help.class);
        startActivity(intent);
    }

}
