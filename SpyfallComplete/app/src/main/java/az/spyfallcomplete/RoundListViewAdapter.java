package az.spyfallcomplete;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.Format;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

class RoundListViewAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<String> players;
    private GameConfiguration config;
    private RelativeLayout rl;
    private Resources resources;

    RoundListViewAdapter(Activity activity, GameConfiguration config){
        this.players = config.playerList;
        this.config = config;
        this.mInflater = activity.getLayoutInflater();
        this.rl = activity.findViewById(R.id.gameRoundRelativeLayout);
        this.resources = activity.getResources();
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
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        if (view == null){
            view = mInflater.inflate(R.layout.round_player_list_view_item, null);
            final Button btn = view.findViewById(R.id.playerNameBtn);
            btn.setText(players.get(i));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View popupView = mInflater.inflate(R.layout.popup_window, null);
                    final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    final Button acceptBtn = popupView.findViewById(R.id.ok_btn);
                    acceptBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                        }
                    });
                    TextView tv = popupView.findViewById(R.id.location_text);
                    String location = "???";
                    String role = config.playerRoles.get(players.get(i));
                    if (!config.locations.get(config.currentLocation).get(0).equals(role)){
                        location = config.currentLocation;
                    }
                    tv.setText(MessageFormat.format("{0}: {1}", resources.getString(R.string.location), location));

                    tv = popupView.findViewById(R.id.role_text);
                    tv.setText(MessageFormat.format("{0}: {1}", resources.getString(R.string.role), role));
                    popupWindow.showAsDropDown(view, 50, -30);
                    btn.setEnabled(false);
                }
            });
            TextView tv = view.findViewById(R.id.playerPoints);
            tv.setText(String.valueOf(config.playerPoints.get(players.get(i))));
        }
        return view;
    }
}
