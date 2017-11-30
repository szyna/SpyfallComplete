package az.spyfallcomplete;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GameRoundActivity extends AppCompatActivity {

    private CountDownTimer countDownTimer;
    private TextView timer;
    private GameConfiguration config;
    private ListView roundPlayerListView;
    private ListView locationsListView;
    private boolean roundPause = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_round);
        config = getIntent().getExtras().getParcelable("gameConfig");
        timer = (TextView) findViewById(R.id.gameTimer);

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
                    startGameBtn.setText(getResources().getString(R.string.unpause_game));
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
                showEndRoundDialog();
            }
        });

        roundPlayerListView = (ListView) findViewById(R.id.roundPlayersListView);
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

        locationsListView = (ListView) findViewById(R.id.locationsListView);
        locationsListView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        prepareNextRound();
    }

    void prepareNextRound(){
        config.selectNextGameConfiguration();

        TextView t = (TextView) findViewById(R.id.roundCounter);
        t.setText(MessageFormat.format(getResources().getString(R.string.rounds_left), config.roundsLeft));
        LinkedList<String> locations = new LinkedList<>(config.locations.keySet());
        Collections.sort(locations);

        LocationsListViewAdapter llva = new LocationsListViewAdapter(this, locations);
        locationsListView.setAdapter(llva);
        setListViewHeightBasedOnChildren(locationsListView);

        timer.setText(String.valueOf(config.timeLeft / 1000));
        // TODO run super on roundlistview addapter rather than creating new object
        RoundListViewAdapter rva = new RoundListViewAdapter(this, config);
        roundPlayerListView.setAdapter(rva);
        Button startGameBtn = (Button) findViewById(R.id.startGameBtn);
        startGameBtn.setText(getResources().getString(R.string.start_game));
        if (config.roundsLeft == 0) {
            startGameBtn.setEnabled(false);
            Button endGameBtn = (Button) findViewById(R.id.endGameBtn);
            endGameBtn.setEnabled(false);
        }
    }

    void showEndRoundDialog(){
        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.end_round_dialog, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);

        final Spinner playersSpn = promptsView.findViewById(R.id.end_round_player_spn);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, config.playerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playersSpn.setAdapter(adapter);

        final Button userInput = promptsView.findViewById(R.id.reveal_btn);
        userInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInput.setText(MessageFormat.format("{0} : {1}\n{2} : {3}",
                        getResources().getString(R.string.location), config.currentLocation,
                        getResources().getString(R.string.spy), config.currentSpy));
            }
        });

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.next_round),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                TextView timer = (TextView) findViewById(R.id.gameTimer);
                                Spinner resultSpn = (Spinner) promptsView.findViewById(R.id.end_round_result);
                                List<String> results = Arrays.asList(getResources().getStringArray(R.array.game_results));
                                List<String> pointsTable = Arrays.asList(getResources().getStringArray(R.array.game_results_points));
                                int result_id = results.indexOf((String) resultSpn.getSelectedItem());
                                switch (result_id){
                                    case 0:
                                        config.playerPoints.put(config.currentSpy, config.playerPoints.get(config.currentSpy) + Integer.valueOf(pointsTable.get(result_id)));
                                        break;
                                    case 1:
                                        for(String player : config.playerList){
                                            if(!player.equals(config.currentSpy)){
                                                config.playerPoints.put(player, config.playerPoints.get(player) + Integer.valueOf(pointsTable.get(result_id)));
                                            }
                                        }
                                        break;
                                    case 2:
                                        for(String player : config.playerList){
                                            if(!player.equals(config.currentSpy)){
                                                config.playerPoints.put(player, config.playerPoints.get(player) + Integer.valueOf(pointsTable.get(result_id)));
                                            }
                                        }
                                        if (!timer.getText().equals("0")){
                                            config.playerPoints.put((String) playersSpn.getSelectedItem(), config.playerPoints.get(playersSpn.getSelectedItem()) + Integer.valueOf(pointsTable.get(result_id)));
                                        }
                                        break;
                                    case 3:
                                        config.playerPoints.put(config.currentSpy, config.playerPoints.get(config.currentSpy) + Integer.valueOf(pointsTable.get(result_id)));
                                        break;
                                }
                                prepareNextRound();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
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
