package edu.illinois.cs.cs125.lab11;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;

public class Help extends AppCompatActivity {
    protected void onCreate(final Bundle savedInstanceState) {
        System.out.println("ONCREATE IS CALLED IN HELP");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

        // Displays helpful information

        TextView textViewHelp;
        textViewHelp  = findViewById(R.id.helpView);
        String displayHelpInfo = "GENERAL: \n\n" + "Bus routes should be entered by their number. Eg. 120 W and 120 E would both be entered as 120." +
                " Bus stops are identified by the names listed below paired with a directional abbreviation. The direction represents the orientation of the bus at a stop." +
                " Eg. The 220 S is oriented eastward at the ISR stops, thus when searching for 220 S routes, ISR E should be your first input. \n\n" +
                "Supported Stops: \n\n" +
                "ISR E, ISR W, Krannert Center N, LAR N, LAR S \n\n" +
                "Supported Routes: \n\n" +
                "22, 220";
        textViewHelp.setText(displayHelpInfo);
    }
}
