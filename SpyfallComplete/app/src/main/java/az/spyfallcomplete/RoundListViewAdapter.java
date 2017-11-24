package az.spyfallcomplete;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

class RoundListViewAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<String> players;

    RoundListViewAdapter(Activity context, List<String> players){
        this.players = players;
        mInflater = context.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return players.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = mInflater.inflate(R.layout.round_player_list_view_item, null);
            Button btn = view.findViewById(R.id.playerNameBtn);
            btn.setText(players.get(i));
            TextView tv = view.findViewById(R.id.playerPoints);
            tv.setText("0");
        }
        return view;
    }
}
