package az.spyfallcomplete;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static Locale myLocale;
    private static final String Locale_Preference = "Locale Preference";
    private static final String Locale_KeyValue = "Saved Locale";
    private static SharedPreferences sharedPreferences;
    private Spinner languageSpinner;
    private Button plBtn, enBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plBtn = (Button) findViewById(R.id.plBtn);
        plBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLocale("pl");
            }
        });
        enBtn = (Button) findViewById(R.id.engBtn);
        enBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLocale("en");
            }
        });

        changeLocale("pl");
    }

    public void startGame(View view){
        Intent intent = new Intent(this, StartGameActivity.class);
        startActivity(intent);
    }

    public void changeLocale(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);//Set Selected Locale
        Locale.setDefault(myLocale);//set new locale as default
        Configuration config = new Configuration();//get Configuration
        config.locale = myLocale;//set config locale as selected locale
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());//Update the config
        Button b = (Button) findViewById(R.id.new_game);
        b.setText(R.string.start_game);
        plBtn.setText(getResources().getStringArray(R.array.languages)[0]);
        enBtn.setText(getResources().getStringArray(R.array.languages)[1]);
    }


}
