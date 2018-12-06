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
public final class BusList extends AppCompatActivity {

    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "BusList";

    /** Request queue for our API requests. */
    private static RequestQueue requestQueue;

    /** Api Key value holder. */
    private static final String apiKey = "a007306f70264930870da537901333e3";

    /** Array of strings containing all supported bus routes. */
    private static final String[][] routeIds = {{"22S", "ILLINI"}, {"22N", "ILLINI"}, {"12WTEAL", "TEAL"}
        , {"12ETEAL", "TEAL"}, {"220N", "ILLINI EVENING"}, {"220S", "ILLINI EVENING"}};

    /** Array of strings containing all supported bus stops. */
    //par is not working properly
    private static final String[][] stopIds = {{"ISRN", "ISR:2"}, {"ISRS", "ISR:1"}, {"KRANNERTCENTER", "KRANNERT"}
    , {"CHEMICALANDLIFESCIENCES", "CHEMLS"}, {"LARN", "LAR:2"}, {"LARS", "LAR:1"}, {"PAR", "PAPAR:2"}};

    /** Array of strings consisting of upcoming times that input bus and stop have returned. */
    /** what the API returns */
    private static String responseAPI = "";
    /**
     * Run when this activity comes to the foreground.
     *
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(routeIds[0]);
        // Set up the queue for our API requests
        requestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.bus_list);
        String name = getIntent().getStringExtra("name");
        String time = getIntent().getStringExtra("time");
        String location = getIntent().getStringExtra("location");
        String bus = getIntent().getStringExtra("bus");
        String stop = getIntent().getStringExtra("stop");
        //Storage variables for correct ids
        String routeId = "";
        String stopId = "";
        //Determines the correct bus route id from the user input
        for (int i = 0; i < routeIds.length; i++) {
            if (bus.replaceAll("\\s+", "").toUpperCase().equals(routeIds[i][0])) {
                routeId = routeIds[i][1];
            }
        }
        for (int i = 0; i < stopIds.length; i++) {
            if (stop.replaceAll("\\s+", "").toUpperCase().equals(stopIds[i][0])) {
                stopId = stopIds[i][1];
            }
        }
        //Transfers user input to display personalized greeting
        TextView textViewBus;
        textViewBus  = findViewById(R.id.textViewBus);
        String displayGreeting = "Hi, " + name + ". " + bus + " will be arriving at " + stop + " at:";
        textViewBus.setText(displayGreeting);

        //starts api call
        startAPICall(stopId, routeId);

        // helper function to break down the API response
        getTimes(responseAPI);
        //transfers four upcoming times to their respective displays on the layout
        TextView textViewOne;
        textViewOne = findViewById(R.id.textViewOne);
        String displayTimeOne = "";
        //textViewOne.setText(displayTimeOne);

        TextView textViewTwo;
        textViewOne = findViewById(R.id.textViewTwo);
        String displayTimeTwo = "";
        //textViewOne.setText(displayTimeTwo);

        TextView textViewThree;
        textViewOne = findViewById(R.id.textViewThree);
        String displayTimeThree = "";
        //textViewOne.setText(displayTimeThree);

        TextView textViewFour;
        textViewOne = findViewById(R.id.textViewFour);
        String displayTimeFour = "";
        //textViewOne.setText(displayTimeFour);
    }
    private void getTimes(String input) {
        String responseTrimmed = responseAPI.replaceAll("\"", "");
        System.out.println("this is where getTimes is called and this is response from api call variable once trimmed up: " + responseTrimmed);
        String[] temp = input.split("arrival_time");
        for (int i = 0; i < temp.length; i++) {
            System.out.println("temp has a length of: " + temp.length);
            System.out.println("iterating through temp: " + temp[i]);
        }
    }
    /**
     * Run when this activity is no longer visible.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Make a call to the IP geolocation API.
     *
     * @param stopId id for the input stop.
     * @param routeId is for the input route.
     */
    void startAPICall(final String stopId, final String routeId) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://developer.cumtd.com/api/v2.2/json/getstoptimesbystop?key=" + apiKey + "&stop_id=" + stopId + "&route_id=" + routeId,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            apiCallDone(response);
                            responseAPI = response.toString();
                            System.out.println(responseAPI);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.e(TAG, error.toString());
                }
            });
            jsonObjectRequest.setShouldCache(false);
            requestQueue.add(jsonObjectRequest);
            responseAPI = jsonObjectRequest.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle the response from our IP geolocation API.
     *
     * @param response response from our IP geolocation API.
     */
    void apiCallDone(final JSONObject response) {
        try {
            Log.d(TAG, response.toString(2));
            // Example of how to pull a field off the returned JSON object
            Log.i(TAG, response.get("hostname").toString());
        } catch (JSONException ignored) { }
    }
}


