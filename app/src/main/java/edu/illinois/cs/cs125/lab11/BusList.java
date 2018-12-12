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
import android.view.View;
import android.widget.TextView;

/**
 * Main class for our UI design lab.
 */
public final class BusList extends AppCompatActivity {

    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "BusList";

    /** Request queue for our API requests. */
    private static RequestQueue requestQueue;

    /** General count variable. */
    public int count = 0;

    /** Evening bus */
    public String routeIdEvening = "";

    /** LateNight bus */
    public String routeIdLateNight = "";

    /** Idinput */
    public String routeId = "";

    // Arrays to store arrival times and vehicle ids
    /** Array of bus arrival times at start stop. */
    private String[] startStopTimes = new String[10];

    /** Array of bus arrival times at end stop. */
    private String[] endStopTimes = new String[10];

    private String[] startStopVehicleIds = new String[100];

    private String[] endStopVehicleIds = new String[100];

    /** Api Key value holder. */
    private static final String apiKey = "a007306f70264930870da537901333e3";

    /** Array of strings containing all supported bus route:key pairs on weekdays. */
    // Supports 220, 22, 13, 130
    private static final String[][] routeIdsWeekday = {
            {"22", "ILLINI", "ILLINI LIMITED", "ILLINI LIMITED"}
            , {"220", "ILLINI", "ILLINI EVENING", "ILLINI EVENING"}
            , {"12", "TEAL", "TEAL EVENING", "TEAL LATE NIGHT"}
            , {"120", "TEAL EVENING", "TEAL LATE NIGHT", "TEAL LATE NIGHT"}
            , {"13", "SILVER", "SILVER EVENING", "SILVER LATE NIGHT"}
            , {"130", "SILVER EVENING", "SILVER LATE NIGHT", "SILVER LATE NIGHT"}};

    /** Array of strings containing all supported bus route:key pairs on Saturday. */
    // Supports 220, 120, 130
    private static final String[][] routeIdsSaturday = {
            {"220", "ILLINI LIMITED SATURDAY", "ILLINI EVENING SATURDAY", "ILLINI LIMITED EVENING SATURDAY"}
            , {"120" , "TEAL SATURDAY", "TEAL EVENING SATURDAY", "TEAL LATE NIGHT SATURDAY"}
            , {"130", "SILVER SATURDAY", "SILVER EVENING SATURDAY", "SILVER LATE NIGHT"}};

    /** Array of strings containing all supported bus route:key pairs on Sunday. */
    // Supports 220, 120, 130
    private static final String[][] routeIdsSunday = {
            {"220", "ILLINI LIMITED SUNDAY", "ILLINI EVENING SUNDAY", "ILLINI EVENING SUNDAY"}
            ,{"120", "TEAL SUNDAY", "TEAL LATE NIGHT SUNDAY", "TEAL LATE NIGHT SUNDAY"}
            , {"130", "SILVER SUNDAY", "SILVER EVENING SUNDAY", "SILVER LATE NIGHT"}};

    /** Array of strings containing all supported bus stops. */
    private static final String[][] stopIds = {{"ISRW", "ISR:2"}, {"ISRE", "ISR:1"}, {"KRANNERTCENTERN", "KRANNERT:2"}
    , {"CHEMICALANDLIFESCIENCES", "CHEMLS"}, {"LARN", "LAR:2"}, {"LARS", "LAR:1"}
    , {"KRANNERTCENTERS", ""}, {"PARW", "PAR:2"}, {"GREGORYANDDORNERN", "GRGDNR:2"}, {"GREGORYANDDORNERS", "GRGDNR:3"}
    , {"GOODWINANDNEVADAN", "GWNNV:1"}, {"ILLINIUNIONE", "IU:1"}, {"ILLINIUNIONW", "IU:9"}, {"GREGORYATLIBRARYE, GRGLIB:1"}
    , {"GREGORYATLIBRARYW, GRGLIB:2"}};


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

        // Sets view to bus list layout
        setContentView(R.layout.bus_list);

        String name = getIntent().getStringExtra("name");
        String day = getIntent().getStringExtra("day");
        String route = getIntent().getStringExtra("route");
        String startStop = getIntent().getStringExtra("startStop");
        String endStop = getIntent().getStringExtra("endStop");

        // Declaring storage variables for correct ids
        String startStopId = "";
        String endStopId = "";

        // Transfers user input to display personalized greeting
        TextView textViewBus;
        textViewBus  = findViewById(R.id.textViewBus);
        String displayGreeting = "Hi, " + name + ". " + route + " departing from " + startStop + ", arriving at " + endStop + "." + "\n" + "    Depart:                                Arrive:";
        textViewBus.setText(displayGreeting);

        // Removes all spaces and moves to upper case in the inputs and then compares them with
        for (int i = 0; i < stopIds.length; i++) {
            if (startStop.replaceAll("\\s+", "").toUpperCase().equals(stopIds[i][0])) {
                startStopId = stopIds[i][1];
            }
            if (endStop.replaceAll("\\s+", "").toUpperCase().equals(stopIds[i][0])) {
                endStopId = stopIds[i][1];
            }
        }
        if (startStopId.length() == 0 || endStopId.length() == 0) {
            finish();
            Intent intent = new Intent(BusList.this, MainActivity.class);
            startActivity(intent);
        }

        // Using the same manipulations, this determines if it is a weekday, Saturday, or Sunday and then
        // finds the correct daytime, evening, and late night route ids from our predetermined set of input:key pairs

        String dayCompare = day.replaceAll("\\s+", "").toUpperCase();
        if (dayCompare.equals("WEEKDAY")) {
            for (int i = 0; i < routeIdsWeekday.length; i++) {
                if (route.replaceAll("\\s+", "").toUpperCase().equals(routeIdsWeekday[i][0])) {
                    routeId = routeIdsWeekday[i][1];
                    routeIdEvening = routeIdsWeekday[i][2];
                    routeIdLateNight = routeIdsWeekday[i][3];
                }
            }
        } else if (dayCompare.equals("SATURDAY")) {
            for (int i = 0; i < routeIdsSaturday.length; i++) {
                if (route.replaceAll("\\s+", "").toUpperCase().equals(routeIdsSaturday[i][0])) {
                    routeId = routeIdsSaturday[i][1];
                    routeIdEvening = routeIdsSaturday[i][2];
                    routeIdLateNight = routeIdsSaturday[i][3];
                }
            }
        } else if (dayCompare.equals("SUNDAY")) {
            for (int i = 0; i < routeIdsSunday.length; i++) {
                if (route.replaceAll("\\s+", "").toUpperCase().equals(routeIdsSunday[i][0])) {
                    routeId = routeIdsSunday[i][1];
                    routeIdEvening = routeIdsSunday[i][2];
                    routeIdLateNight = routeIdsSunday[i][3];
                }
            }
        } else {
            finish();
            Intent intent = new Intent(BusList.this, MainActivity.class);
            startActivity(intent);
        }
        if (routeId.length() == 0) {
            finish();
            Intent intent = new Intent(BusList.this, MainActivity.class);
            startActivity(intent);
        }

        // Starts api call

        startAPICall(startStopId, endStopId, routeId);
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
     * @param startStopIdInput id for the starting stop.
     * @param endStopIdInput id for the ending stop
     * @param routeIdInput is for the input route.
     */
    void startAPICall(final String startStopIdInput, final String endStopIdInput, final String routeIdInput) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://developer.cumtd.com/api/v2.2/json/getdeparturesbystop?key=" + apiKey + "&stop_id=" + startStopIdInput + "&route_id=" + routeIdInput,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                apiCallDone(response);

                                // Since every response from the api call is formatted the same way, we are able to extract data through manipulation.
                                // First, we ust getString with the key "departures" which returns a string of all info for every upcoming bus returned.
                                // Next, we split that string every time "expected" appears because the expected arrival time is located after the first
                                // instance of "expected" for every upcoming bus.

                                String responseApi = response.getString("departures");
                                System.out.println("first api departures is called");
                                if (responseApi == null) {
                                }
                                String[] split = responseApi.split("expected");
                               // System.out.println(responseApi);

                                // Since each upcoming bus has two instances of "expected" in the data returned, we skip every second instance and
                                // grab the time from the correct indices of the string using substring, then add them to the startStopTimes array.
                                // To maintain correct walking of startStopTimes, we used a class variable count that is initialized to zero.

                                count = 0;
                                for (int i = 1; i < split.length; i += 2) {
                                    startStopTimes[count] = split[i].substring(14, 19);
                                    count++;
                                }

                                // Gets all of the vehicle ids for the buses arriving at the start stop

                                String[] splitId = responseApi.split("vehicle_id");
                                for (int i = 1; i < splitId.length; i++) {
                                    startStopVehicleIds[i - 1] = splitId[i].substring(3, 7);
                                }


                                // We grab the first two digits of each time in the array and test if they greater than 12, and if so subtracting 12
                                // and reintroducing them back into a string in the array.
                                // This maintains a 12 hour clock rather than a 24 hour clock.

                                for (int i = 0; i < 4; i++) {
                                    if (startStopTimes[i] != null) {
                                        int time = Integer.parseInt(startStopTimes[i].substring(0, 2));
                                        if (time > 12) {
                                            time = time - 12;
                                        }
                                        if (time == 0) {
                                            time = 12;
                                        }
                                        startStopTimes[i] = Integer.toString(time) + startStopTimes[i].substring(2, 5);
                                    }
                                }

                                // The process above repeats for the end stop.

                                try {
                                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                            Request.Method.GET,
                                            "https://developer.cumtd.com/api/v2.2/json/getdeparturesbystop?key=" + apiKey + "&stop_id=" + endStopIdInput + "&route_id=" + routeIdInput,
                                            null,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(final JSONObject response) {
                                                    try {
                                                        apiCallDone(response);
                                                        String newResponseApi = response.getString("departures");
                                                        String[] splitTwo = newResponseApi.split("expected");
                                                        //System.out.println(newResponseApi);
                                                        String[] splitIdTwo = newResponseApi.split("vehicle_id");

                                                        // Gets vehicle ids for all buses arriving at end stop.
                                                        // idCount maintains the correct index to start at when pulling arrival times for the
                                                        // end stop by comparing vehicle ids from the start stop with ids from the end stop

                                                        int idCount = 1;
                                                        for (int i = 1; i < splitIdTwo.length; i++) {
                                                            endStopVehicleIds[i - 1] = splitIdTwo[i].substring(3, 7);
                                                            if (endStopVehicleIds[i - 1].equals(startStopVehicleIds[0])) {
                                                                break;
                                                            }
                                                            idCount += 2;
                                                        }

                                                        // Pulls arrival times at end stop for up to the first four buses that will be arriving at start stop

                                                        count = 0;
                                                        for (int i = idCount; i < splitTwo.length; i += 2) {
                                                            endStopTimes[count] = splitTwo[i].substring(14, 19);
                                                            count++;
                                                        }

                                                        // Continues the same as previous try statement.

                                                        for (int i = 0; i < 4; i++) {
                                                            if (endStopTimes[i] != null) {
                                                                int time = Integer.parseInt(endStopTimes[i].substring(0, 2));
                                                                if (time > 12) {
                                                                    time = time - 12;
                                                                }
                                                                if (time == 0) {
                                                                    time = 12;
                                                                }
                                                                endStopTimes[i] = Integer.toString(time) + endStopTimes[i].substring(2, 5);
                                                            }
                                                        }

                                                        if (startStopTimes[0] == null && endStopTimes[0] == null) {
                                                            if (routeId.equals(routeIdInput)) {
                                                                routeId = routeIdEvening;
                                                                startAPICall(startStopIdInput, endStopIdInput, routeIdEvening);
                                                            } else if (routeId.equals(routeIdEvening)) {
                                                                routeId = routeIdLateNight;
                                                                startAPICall(startStopIdInput, endStopIdInput, routeIdLateNight);
                                                            }
                                                        } else {

                                                            // Changes any null values to "" so that "null" is not displayed.

                                                            for (int i = 0; i < 4; i++) {
                                                                if (startStopTimes[i] == null) {
                                                                    startStopTimes[i] = "";
                                                                }
                                                                if (endStopTimes[i] == null) {
                                                                    endStopTimes[i] = "";
                                                                }
                                                            }

                                                            // Displays times in respective text windows.

                                                            TextView textViewOne;
                                                            textViewOne = findViewById(R.id.textViewOne);
                                                            String displayTimeOne = startStopTimes[0] + "       " + endStopTimes[0];
                                                            textViewOne.setText(displayTimeOne);

                                                            TextView textViewTwo;
                                                            textViewTwo = findViewById(R.id.textViewTwo);
                                                            String displayTimeTwo = startStopTimes[1] + "       " + endStopTimes[1];
                                                            textViewTwo.setText(displayTimeTwo);

                                                            TextView textViewThree;
                                                            textViewThree = findViewById(R.id.textViewThree);
                                                            String displayTimeThree = startStopTimes[2] + "       " + endStopTimes[2];
                                                            textViewThree.setText(displayTimeThree);

                                                            TextView textViewFour;
                                                            textViewFour = findViewById(R.id.textViewFour);
                                                            String displayTimeFour = startStopTimes[3] + "       " + endStopTimes[3];
                                                            textViewFour.setText(displayTimeFour);
                                                        }

                                                    } catch(JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(final VolleyError error) {
                                            Log.e(TAG, error.toString());
                                        }
                                    });
                                    jsonObjectRequest.setShouldCache(false);
                                    requestQueue.add(jsonObjectRequest);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } catch(JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.e(TAG, error.toString());
                }
            });
            jsonObjectRequest.setShouldCache(false);
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void refreshPage(View v) {
        finish();
        startActivity(getIntent());
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