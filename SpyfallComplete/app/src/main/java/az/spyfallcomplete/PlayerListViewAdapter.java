package az.spyfallcomplete;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class PlayerListViewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<String> players;
    private GameConfiguration config;

    PlayerListViewAdapter(Activity context, GameConfiguration config){
        this.players = config.playerList;
        this.mInflater = context.getLayoutInflater();
        this.config = config;
    }

    class ViewHolder {
        EditText caption;
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
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.player_list_view_item, null);
            holder.caption = view.findViewById(R.id.ItemCaption);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //Fill EditText with the value you have in data source
        holder.caption.setText(players.get(i));
        holder.caption.setId(i);

        //we need to update adapter once we finish with editing
        holder.caption.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    final int position = v.getId();
                    final EditText Caption = (EditText) v;
                    players.set(position, Caption.getText().toString());
                }
            }
        });

        return view;
    }

    void changeDataSize(int newSize){
        int prevSize = players.size();
        if (newSize > prevSize){
            for(int i=prevSize; i<newSize; i++){
                players.add("");
            }
        }else if (newSize < prevSize){
            for(int i=prevSize-1; i>=newSize; i--){
                players.remove(i);
            }
        }
    }

}
