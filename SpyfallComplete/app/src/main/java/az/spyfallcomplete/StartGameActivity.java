package az.spyfallcomplete;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class StartGameActivity extends AppCompatActivity {

    private ListView playerLv;
    private PlayerListViewAdapter playerListAdapter;
    GameConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
    }

    public void onStart(){
        super.onStart();

        LinkedList<String> players = new LinkedList<String>(){
                {
                        add("A"); add("B"); add("C");
                }
        };

        config = new GameConfiguration(this, players, 480*1000, "locations.json");

        playerLv = (ListView) findViewById(R.id.playersListView);
        playerLv.setItemsCanFocus(true);
        playerListAdapter = new PlayerListViewAdapter(this, config);
        playerLv.setAdapter(playerListAdapter);

        Spinner s = (Spinner) findViewById(R.id.playerNumberSpn);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String playerNr = adapterView.getItemAtPosition(i).toString();
                ListView lv = (ListView) findViewById(R.id.playersListView);
                PlayerListViewAdapter adapter = (PlayerListViewAdapter) lv.getAdapter();
                adapter.changeDataSize(Integer.parseInt(playerNr));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });


        Button startGameBtn = (Button) findViewById(R.id.startGameBtn);
        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                config.updatePlayerPoints();
                Intent intent = new Intent(StartGameActivity.this, GameRoundActivity.class);
                intent.putExtra("gameConfig", config);
                startActivity(intent);
            }
        });

    }


}
