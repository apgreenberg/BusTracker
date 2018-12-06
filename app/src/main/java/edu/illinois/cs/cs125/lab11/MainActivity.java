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
    private String timeOfClass;
    private String locationOfClass;
    private String busNumber;
    public String name;
    public String time;
    public String location;
    public String bus;
    public String stop;

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

        startAPICall("192.17.96.8");
       // https://developer.cumtd.com/api/v2.2/json/getstoptimesbystop?key=a007306f70264930870da537901333e3&stop_id=it:1
    }
    public void switchScreens(View v){
        EditText timeId = findViewById(R.id.time_input);
        EditText locId = findViewById(R.id.loc_input);
        EditText busId = findViewById(R.id.bus_input);
        EditText stopId = findViewById(R.id.stop_input);
        EditText nameId = findViewById(R.id.name_input);
        name = nameId.getText().toString();
        time = timeId.getText().toString();
        location = locId.getText().toString();
        bus = busId.getText().toString();
        stop = stopId.getText().toString();
        Intent intent = new Intent(MainActivity.this, BusList.class);
        intent.putExtra("time", time);
        intent.putExtra("location", location);
        intent.putExtra("bus", bus);
        intent.putExtra("stop", stop);
        intent.putExtra("name", name);
        startActivity(intent);
    }
    /**
     * what to so onclick.
     */
    public void onClick() {
        startAPICall("192.17.96.8");
        System.out.println(requestQueue);
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
     * @param ipAddress IP address to look up
     */
    void startAPICall(final String ipAddress) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://ipinfo.io/" + ipAddress + "/json",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            apiCallDone(response);
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
