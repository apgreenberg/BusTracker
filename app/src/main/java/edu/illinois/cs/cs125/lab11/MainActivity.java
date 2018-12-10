package edu.illinois.cs.cs125.lab11;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Main class for our UI design lab.
 */
public final class MainActivity extends AppCompatActivity {
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "Lab11:Main";

    public String name;
    public String day;
    public String route;
    public String startStop;
    public String endStop;

    /** Request queue for our API requests. */
    private static RequestQueue requestQueue;

    /**
     * Run when this activity comes to the foreground.
     *
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the queue for our API requests
        requestQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_main);

    }
    public void switchScreens(View v){
        System.out.println("the button click works");
        //Storing inputs as EditText variables to allow use of getText()
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
        System.out.println("INFOPAGESTART IS CALLED");
        Intent intent = new Intent(MainActivity.this, Help.class);
        startActivity(intent);
    }

}
