package az.spyfallcomplete;

import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TextView;

public class GameRoundActivity extends AppCompatActivity {

    private CountDownTimer countDownTimer;
    private TextView timer;
    private long time;
    private GameConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_round);
        config = getIntent().getExtras().getParcelable("gameConfig");

        timer = (TextView) findViewById(R.id.gameTimer);
        timer.setText(String.valueOf(config.timeLeft / 1000));
        //TODO fix first and last second issues
        Button startGameBtn = (Button) findViewById(R.id.startGameBtn);
        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                //countDownTimer.onTick((time-1) * 1000);
            }
        });

        Button pauseGameBtn = (Button) findViewById(R.id.pauseGameBtn);
        pauseGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.cancel();
            }
        });

        ListView roundPlayerListView = (ListView) findViewById(R.id.roundPlayersListView);
        roundPlayerListView.setAdapter(new RoundListViewAdapter(this, config.playersList));
    }
}
