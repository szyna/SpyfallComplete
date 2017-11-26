package az.spyfallcomplete;

import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.LinkedList;

public class GameRoundActivity extends AppCompatActivity {

    private CountDownTimer countDownTimer;
    private TextView timer;
    private GameConfiguration config;
    private boolean roundPause = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_round);
        config = getIntent().getExtras().getParcelable("gameConfig");
        config.selectNextGameConfiguration();

        timer = (TextView) findViewById(R.id.gameTimer);
        timer.setText(String.valueOf(config.timeLeft / 1000));
        //TODO fix first and last second issues
        final Button startGameBtn = (Button) findViewById(R.id.startGameBtn);
        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (roundPause) {
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                    countDownTimer = new CountDownTimer(config.timeLeft, 1000) {

                        public void onTick(long millisUntilFinished) {
                            config.timeLeft = millisUntilFinished;
                            timer.setText(String.valueOf(millisUntilFinished / 1000));
                        }

                        public void onFinish() {
                            timer.setText("0");
                        }
                    };
                    countDownTimer.start();
                    startGameBtn.setText(getResources().getString(R.string.pause_game));
                    roundPause = false;
                    //countDownTimer.onTick((time-1) * 1000);
                }else{
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                    startGameBtn.setText(getResources().getString(R.string.unpase_game));
                    roundPause = true;
                }
            }
        });

        Button endGameBtn = (Button) findViewById(R.id.endGameBtn);
        endGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                // TODO add code resposible for ending game
            }
        });

        ListView roundPlayerListView = (ListView) findViewById(R.id.roundPlayersListView);
        roundPlayerListView.setAdapter(new RoundListViewAdapter(this, config));
        roundPlayerListView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        setListViewHeightBasedOnChildren(roundPlayerListView);

        EditText locationsText = (EditText) findViewById(R.id.locationsText);
        LinkedList<String> locations = new LinkedList<>(config.locations.keySet());
        Collections.sort(locations);
        String text = "";
        for(int i=0; i<locations.size(); i++){
            text += locations.get(i) + " | ";
        }
        locationsText.setText(text);
        locationsText.setTextSize(14); // TODO
        locationsText.setFocusable(false);
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ActionBar.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
