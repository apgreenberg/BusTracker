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
import org.json.JSONArray;
//import org.json.simple.parser.*;
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
    private int count = 0;

    /** Api Key value holder. */
    private static final String apiKey = "a007306f70264930870da537901333e3";

    /** Array of strings containing all supported bus routes. */
    private static final String[][] routeIds = {{"22S", "ILLINI"}, {"22N", "ILLINI"}, {"12WTEAL", "TEAL"}
        , {"12ETEAL", "TEAL"}, {"220N", "ILLINI EVENING"}, {"220S", "ILLINI EVENING"}
, {"13N", "SILVER"}, {"13S", "SILVER"}};

    /** Array of strings containing all supported bus stops. */
    //par is not working properly
    private static final String[][] stopIds = {{"ISRN", "ISR:2"}, {"ISRS", "ISR:1"}, {"KRANNERTCENTER", "KRANNERT"}
    , {"CHEMICALANDLIFESCIENCES", "CHEMLS"}, {"LARN", "LAR:2"}, {"LARS", "LAR:1"}
    , {"PAR", "PAPAR:2"}, {"GREGORYANDDORNERN", "GRGDNR:2"}, {"GREGORYANDDORNERS", "GRGDNR:3"}};

    /** Array of upcoming bus arival times. */
    private String[] stopTimes = new String[20];

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

        //transfers four upcoming times to their respective displays on the layout

        for (int i = 0; i < stopTimes.length; i++) {
            System.out.println(stopTimes[i]);
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
                    "https://developer.cumtd.com/api/v2.2/json/getdeparturesbystop?key=" + apiKey + "&stop_id=" + stopId + "&route_id=" + routeId,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                apiCallDone(response);
                                responseAPI = response.getString("departures");
                                //responseAPI.replaceAll("\"", "");
                                String[] split = responseAPI.split("expected");
                                System.out.println(responseAPI);
                                for (int i = 1; i < split.length; i += 2) {
                                    stopTimes[count] = split[i].substring(14, 19);
                                    count++;
                                }
                                for (int i = 0; i < stopTimes.length; i++) {
                                    System.out.println(stopTimes[i]);
                                }
                                for (int i = 0; i < 4; i++) {
                                    if (stopTimes[i] != null) {
                                        int time = Integer.parseInt(stopTimes[i].substring(0, 2));
                                        if (time > 12) {
                                            time = time - 12;
                                        }
                                        if (time == 0) {
                                            time = 12;
                                        }
                                        stopTimes[i] = Integer.toString(time) + stopTimes[i].substring(2, 5);
                                    }
                                }

                                TextView textViewOne;
                                textViewOne = findViewById(R.id.textViewOne);
                                String displayTimeOne = stopTimes[0];
                                textViewOne.setText(displayTimeOne);

                                TextView textViewTwo;
                                textViewTwo = findViewById(R.id.textViewTwo);
                                String displayTimeTwo = stopTimes[1];
                                textViewTwo.setText(displayTimeTwo);

                                TextView textViewThree;
                                textViewThree = findViewById(R.id.textViewThree);
                                String displayTimeThree = stopTimes[2];
                                textViewThree.setText(displayTimeThree);

                                TextView textViewFour;
                                textViewFour = findViewById(R.id.textViewFour);
                                String displayTimeFour = stopTimes[3];
                                textViewFour.setText(displayTimeFour);

                                //for (int i = 0; i < split.length; i++) {
                                 //   System.out.println(split[i]);
                                //}
                            } catch(JSONException e) {
                                e.printStackTrace();
                            }
                           // JSONObject jsonObject = response;
                           // JSONArray jsonArray = jsonObject.get("stop_times");
                            //System.out.println(responseAPI);
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


